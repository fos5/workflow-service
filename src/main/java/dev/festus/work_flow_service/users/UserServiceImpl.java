package dev.festus.work_flow_service.users;

import dev.festus.work_flow_service.exception.customExceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserDto getUser(String email) {
       return userRepository.findByEmail(email).map(userMapper).orElseThrow(()-> new ResourceNotFoundException("User not found"));
    }

    @Override
    public UserDto getUserById(long id) {
        return userRepository.findById(id).map(userMapper).orElseThrow(()-> new ResourceNotFoundException("User not found"));
    }

    @Override
    public List<UserDto> getUsers() {
        return userRepository.findAll().stream().map(userMapper).collect(Collectors.toList());
    }

    @Override
    public void deleteUser(String email) {

    }
}
