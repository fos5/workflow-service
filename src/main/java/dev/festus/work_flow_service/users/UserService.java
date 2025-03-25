package dev.festus.work_flow_service.users;

import java.util.List;

public interface UserService {
    UserResponseDto register(CreateUserRequest request);
    UserResponseDto getUser(String email);
    UserResponseDto getUserById(long id);
    List<UserResponseDto> getUsers();
    void deleteUser(String email);
}
