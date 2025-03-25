package dev.festus.work_flow_service.users;

import dev.festus.work_flow_service.utils.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record CreateUserRequest(
        @NotBlank(message = "First name cannot be null / empty")
        @Pattern(
                regexp = "^[a-zA-Z\\s]+$",
                message = "First name can only contain alphabetic characters and spaces"
        )
        String firstName,
        @NotBlank(message = "Last name cannot be null / empty")
        @Pattern(
                regexp = "^[a-zA-Z\\s]+$",
                message = "Last name can only contain alphabetic characters and spaces"
        )
        String lastName,
        @NotBlank(message = "Email cannot be null / empty")
        @Email(message = "Please input a valid email")
        String email,
        @ValidPassword
        String password,
        Role role) {
}
