package com.mukminov.service.core.impl;

import com.mukminov.api.generated.dto.UserCreateDto;
import com.mukminov.api.generated.dto.UserDto;
import com.mukminov.entity.Role;
import com.mukminov.entity.User;
import com.mukminov.mapper.UserMapper;
import com.mukminov.model.enums.RoleType;
import com.mukminov.repository.RoleRepository;
import com.mukminov.repository.UserRepository;
import com.mukminov.service.core.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public UserDto createUser(UserCreateDto createDto) {
        Role menteeRole = roleRepository.findByName(RoleType.ROLE_MENTEE.name())
                .orElseGet(() -> roleRepository.save(Role.builder().name(RoleType.ROLE_MENTEE.name()).build()));

        User user = User.builder()
                .username(createDto.getUsername())
                .password(passwordEncoder.encode(createDto.getPassword()))
                .email(createDto.getEmail())
                .githubUsername(createDto.getGithubUsername())
                .build();

        user.getRoles().add(menteeRole);

        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<UserDto> getMentees(Long mentorId) {
        return userRepository.findAllMenteesByMentorId(mentorId).stream()
                .map(userMapper::toDto)
                .toList();
    }
}