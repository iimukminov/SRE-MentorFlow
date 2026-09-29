package com.mukminov.mapper;

import com.mukminov.api.generated.dto.UserDto;
import com.mukminov.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto toDto(User entity) {
        if (entity == null) {
            return null;
        }

        UserDto dto = new UserDto();
        dto.setId(entity.getId());
        dto.setUsername(entity.getUsername());
        dto.setEmail(entity.getEmail());
        dto.setGithubUsername(entity.getGithubUsername());

        return dto;
    }
}