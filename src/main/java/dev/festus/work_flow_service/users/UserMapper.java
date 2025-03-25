package dev.festus.work_flow_service.users;

import org.springframework.stereotype.Component;

import java.util.function.Function;
@Component
public class UserMapper implements Function<AppUser, UserResponseDto> {

    @Override
    public UserResponseDto apply(AppUser appUser) {
        return UserResponseDto.builder()
                .id(appUser.getId())
                .email(appUser.getEmail())
                .firstName(appUser.getFirstName())
                .lastName(appUser.getLastName())
                .enabled(appUser.isEnabled())
                .roles(appUser.getRole().toString())
                .build();
    }
}
