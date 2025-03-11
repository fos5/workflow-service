package dev.festus.work_flow_service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Logger;

@Service
public class JwtService {

    private static final Logger logger = Logger.getLogger(JwtService.class.getName());
    private final PublicKey publicKey;
    private final PrivateKey privateKey;
    private final JwtProperties jwtProperties;

    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.publicKey = loadKey(jwtProperties.getPublicKey(), KeyType.PUBLIC);
        this.privateKey = loadKey(jwtProperties.getPrivateKey(), KeyType.PRIVATE);
    }

    public String generateToken(UserDetails userDetails, Map<String, Object> extraClaims, long expiration) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("roles", userDetails.getAuthorities())
                .claim("tokenVersion", now.toEpochMilli())
                .claims(extraClaims)
                .signWith(privateKey, Jwts.SIG.RS256)
                .issuedAt(Date.from(now))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .issuer(jwtProperties.getIssuer())
                .compact();
    }

    public String generateToken(UserDetails userDetails, Map<String, Object> extraClaims) {
        return generateToken(userDetails, extraClaims, jwtProperties.getTokenExpirationMs());
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(userDetails, Map.of());
    }


    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(extractAllClaims(token));
    }

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUserName(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Claims extractAllClaims(String token) {
            return Jwts.parser()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

    }

    @SuppressWarnings("unchecked")
    private <T> T loadKey(String filePath, KeyType keyType) {
        try {
            byte[] keyBytes = Files.readAllBytes(Paths.get(filePath));
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyType == KeyType.PUBLIC
                    ? (T) keyFactory.generatePublic(spec)
                    : (T) keyFactory.generatePrivate(spec);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load key from file: " + filePath, e);
        }
    }

    private enum KeyType {
        PUBLIC, PRIVATE
    }
}