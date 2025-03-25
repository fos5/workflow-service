package dev.festus.work_flow_service.users;

import lombok.Builder;

@Builder
public record UserDto(String email, String firstName, String lastName, boolean enabled, String roles) {
}
