package dev.festus.work_flow_service.auth;

import dev.festus.work_flow_service.exception.customExceptions.BadCredentialsException;
import dev.festus.work_flow_service.security.JwtService;
import dev.festus.work_flow_service.users.AppUser;
import dev.festus.work_flow_service.users.Role;
import dev.festus.work_flow_service.users.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

   @Transactional
    public AuthenticationResponse register(RegistrationRequest request) {

        Objects.requireNonNull(request, "Registration request cannot be null");

        var appUser = AppUser.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(request.role() == null ? Role.USER : request.role())
                .build();
        if (repository.existsByEmail(appUser.getEmail())) {
            log.error("Attempt to register with duplicate email: {}", appUser.getEmail());
            throw new IllegalArgumentException("Registration failed: User with email " + appUser.getEmail() + " already exists");
        }
        AppUser savedUser = repository.save(appUser);
        log.info("Successfully registered user with email: {}", savedUser.getEmail());
        String jwtToken = jwtService.generateToken(savedUser);
        //send email for account verification
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .build();
    }
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
