package dev.festus.work_flow_service.users;

import java.util.List;

public interface UserService {
    UserDto getUser(String email);
    UserDto getUserById(long id);
    List<UserDto> getUsers();
    void deleteUser(String email);
}
