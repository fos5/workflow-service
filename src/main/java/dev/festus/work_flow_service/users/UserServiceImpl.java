package dev.festus.work_flow_service.users;

import dev.festus.work_flow_service.exception.customExceptions.ResourceNotFoundException;
import dev.festus.work_flow_service.security.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public UserResponseDto register(CreateUserRequest request) {

        Objects.requireNonNull(request, "Registration request cannot be null");

        var appUser = AppUser.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(request.role() == null ? Role.USER : request.role())
                .build();
        if (userRepository.existsByEmail(appUser.getEmail())) {
            log.error("Attempt to register with duplicate email: {}", appUser.getEmail());
            throw new IllegalArgumentException("Registration failed: User with email " + appUser.getEmail() + " already exists");
        }
        AppUser savedUser = userRepository.save(appUser);
        log.info("Successfully registered user with email: {}", savedUser.getEmail());
        String jwtToken = jwtService.generateToken(savedUser);
        //send email for account verification
        return UserResponseDto.builder()
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .roles(savedUser.getRole().toString())
                .accessToken(jwtToken)
                .build();
    }

    @Override
    public UserResponseDto getUser(String email) {
       return userRepository.findByEmail(email).map(userMapper).orElseThrow(()-> new ResourceNotFoundException("User not found"));
    }

    @Override
    public UserResponseDto getUserById(long id) {
        return userRepository.findById(id).map(userMapper).orElseThrow(()-> new ResourceNotFoundException("User not found"));
    }

    @Override
    public List<UserResponseDto> getUsers() {
        return userRepository.findAll().stream().map(userMapper).collect(Collectors.toList());
    }

    @Override
    public void deleteUser(String email) {

    }
}
