package dev.festus.work_flow_service.users.auth;

import dev.festus.work_flow_service.exception.customExceptions.BadCredentialsException;
import dev.festus.work_flow_service.security.JwtService;
import dev.festus.work_flow_service.users.AppUser;
import dev.festus.work_flow_service.users.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        Objects.requireNonNull(request, "Authentication request cannot be null");

        String jwtToken;
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()
                    )
            );
            AppUser appUser = repository.findByEmail(request.email())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            jwtToken = jwtService.generateToken(appUser);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException(e.getMessage());
        }
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .build();
    }
}
