package dev.festus.work_flow_service.users.auth;

import dev.festus.work_flow_service.ApiResponse;
import dev.festus.work_flow_service.users.CreateUserRequest;
import dev.festus.work_flow_service.users.UserResponseDto;
import dev.festus.work_flow_service.users.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {
    private final UserService userService;
    private final AuthenticationService authService;

    public AuthController(UserService userService, AuthenticationService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/create-account")
    public ResponseEntity<ApiResponse> createNewUser(@RequestBody @Valid CreateUserRequest request, UriComponentsBuilder uriComponentsBuilder) {
        UserResponseDto registered = userService.register(request);
        URI location = uriComponentsBuilder.path("/api/v1/users/{id}").buildAndExpand(registered.getId()).toUri();
        return ResponseEntity.created(location).body(ApiResponse.success("Account created successfully", registered));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<ApiResponse> authenticate(@RequestBody @Valid AuthenticationRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Log in successful",authService.authenticate(request)));
    }
}
