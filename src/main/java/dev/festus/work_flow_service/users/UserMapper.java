package dev.festus.work_flow_service.users;

import org.springframework.stereotype.Component;

import java.util.function.Function;
@Component
public class UserMapper implements Function<AppUser,UserDto> {

    @Override
    public UserDto apply(AppUser appUser) {
        return UserDto.builder()
                .email(appUser.getEmail())
                .firstName(appUser.getFirstName())
                .lastName(appUser.getLastName())
                .enabled(appUser.isEnabled())
                .roles(appUser.getRole().toString())
                .build();
    }
}
