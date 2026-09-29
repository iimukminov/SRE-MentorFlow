package com.mukminov;

import com.mukminov.api.generated.dto.UserCreateDto;
import com.mukminov.api.generated.dto.UserDto;
import com.mukminov.entity.Role;
import com.mukminov.entity.User;
import com.mukminov.mapper.UserMapper;
import com.mukminov.repository.RoleRepository;
import com.mukminov.repository.UserRepository;
import com.mukminov.service.core.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock private UserRepository userRepository;
    @Mock private RoleRepository roleRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUser_ShouldReturnUserDto_WhenValidDataProvided() {
        UserCreateDto createDto = new UserCreateDto();
        createDto.setUsername("testuser");
        createDto.setPassword("password");
        createDto.setEmail("test@kfu.ru");

        Role menteeRole = new Role("ROLE_MENTEE");
        when(roleRepository.findByName("ROLE_MENTEE")).thenReturn(Optional.of(menteeRole));
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("testuser");

        UserDto expectedDto = new UserDto();
        expectedDto.setId(1L);
        expectedDto.setUsername("testuser");

        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(userMapper.toDto(savedUser)).thenReturn(expectedDto);

        UserDto actualDto = userService.createUser(createDto);

        assertNotNull(actualDto);
        assertEquals("testuser", actualDto.getUsername());

        verify(userRepository, times(1)).save(any(User.class));
    }
}