package com.alweimine.supportticketsystemclientswing.mappper;


import com.alweimine.supportticketsystemclientswing.entities.User;
import com.alweimine.supportticketsystemclientswing.mappper.dto.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
public class UserMapper extends AbstractMapper<User, UserDto> {
    ;

    @Override
    public User dtoToEntity(UserDto dto) {
        // Initialize a new User entity
        User user = new User();
        // Check if UserId is not null
        if (dto.getUserId() != null) {
            user.setUserId(dto.getUserId());
        }
        user.setPassword(dto.getPassword());
        user.setUsername(dto.getUsername());
        user.setRole(dto.getRole());
        return user;
    }

    @Override
    public UserDto entityToDto(User entity) {
        UserDto userDto = null;
        // Check if the entity is not null
        if (entity != null) {
            // Initialize a new UserDto object
            userDto = new UserDto();
            userDto.setUserId(entity.getUserId());
            userDto.setRole(entity.getRole());
            userDto.setUsername(entity.getUsername());
            userDto.setPassword(entity.getPassword());
        }
        return userDto;
    }
}