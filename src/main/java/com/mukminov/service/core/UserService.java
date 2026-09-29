package com.mukminov.service.core;

import com.mukminov.api.generated.dto.UserCreateDto;
import com.mukminov.api.generated.dto.UserDto;
import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();
    UserDto getUserById(Long id);
    UserDto createUser(UserCreateDto createDto);
    void deleteUser(Long id);
    List<UserDto> getMentees(Long mentorId);
}