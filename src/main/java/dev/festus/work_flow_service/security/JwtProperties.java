package dev.festus.work_flow_service.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "jwt")
@Component
@Getter
@Setter
public class JwtProperties {
    private String publicKey;
    private String privateKey;
    private long tokenExpirationMs;
    private String issuer;
    private String secretKey;

}
