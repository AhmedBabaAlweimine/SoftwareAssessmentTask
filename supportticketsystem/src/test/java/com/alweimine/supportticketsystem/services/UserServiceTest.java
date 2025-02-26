package com.alweimine.supportticketsystem.services;

import com.alweimine.supportticketsystem.entities.User;
import com.alweimine.supportticketsystem.exception.BusinessException;
import com.alweimine.supportticketsystem.exception.DuplicateUserNameException;
import com.alweimine.supportticketsystem.mappper.UserMapper;
import com.alweimine.supportticketsystem.mappper.dto.UserDto;
import com.alweimine.supportticketsystem.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        user = new User(1L, "testUser", "password", User.Role.EMPLOYEE, null, null);
        userDto = new UserDto(1L, "testUser", "password", User.Role.EMPLOYEE, null,null);
    }

    @Test
    void testFindAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(userMapper.entityToDto(user)).thenReturn(userDto);

        List<UserDto> userDtos = userService.findAllUsers();

        assertNotNull(userDtos);
        assertEquals(1, userDtos.size());
        assertEquals("testUser", userDtos.get(0).getUsername());
    }

    @Test
    void testSaveUser() {
        Mockito.when(userMapper.dtoToEntity(userDto)).thenReturn(user);
        Mockito.when(userMapper.entityToDto(user)).thenReturn(userDto);
        Mockito.when(userMapper.entityToDto(userRepository.findByUsername(userDto.getUsername()))).thenReturn(null);// No duplicate user
        Mockito.when(userRepository.save(user)).thenReturn(user); // Save user

        // Act: Call the method to save user
        UserDto savedUser = userService.save(userDto);

        // Assert: Check that the returned user is not null and matches the expected userDto
        Assertions.assertNotNull(savedUser);
        Assertions.assertEquals(userDto, savedUser);
        Mockito.verify(userRepository, Mockito.times(1)).save(user); // Verify repository save method was called

    }

    @Test
    void testSaveUser_throwsDuplicateUsernameException() {
        Mockito.when(userMapper.entityToDto(userRepository.findByUsername(userDto.getUsername()))).thenReturn(userDto);

        DuplicateUserNameException exception = assertThrows(DuplicateUserNameException.class, () -> userService.save(userDto));
        assertEquals("Registration Failed!!: testUser This username already exists. Please try with a different username.", exception.getMessage());
    }

    @Test
    void testFindUserById() {
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
        when(userMapper.entityToDto(user)).thenReturn(userDto);

        UserDto foundUser = userService.findUserById(user.getUserId());

        assertNotNull(foundUser);
        assertEquals("testUser", foundUser.getUsername());
    }

    @Test
    void testFindUserById_UserNotFound() {
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.empty());

        UserDto foundUser = userService.findUserById(user.getUserId());

        assertNull(foundUser);
    }

    @Test
    void testDeleteUserById() {
        when(userRepository.deleteByUserId(user.getUserId())).thenReturn(1);

        int result = userService.deleteUserById(user.getUserId());

        assertEquals(1, result);
        verify(userRepository, times(1)).deleteByUserId(user.getUserId());
    }

    @Test
    void testIsAccountExist_UserNotFound() {
        when(userRepository.findByUsername("nonExistentUser")).thenReturn(null);

        boolean result = userService.isAcountExist("nonExistentUser");

        assertFalse(result);
    }

    @Test
    void testFindAccountByUsernameAndPassword() {
        when(userRepository.findByUsernameAndPasswordAndRole("testUser", "password", User.Role.EMPLOYEE)).thenReturn(user);
        when(userMapper.entityToDto(user)).thenReturn(userDto);

        UserDto result = userService.findAccountByUsernameAndPassword("testUser", "password", User.Role.EMPLOYEE);

        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
    }

    @Test
    void testFindAccountByUsernameAndPassword_InvalidCredentials() {
        when(userRepository.findByUsernameAndPasswordAndRole("testUser", "wrongPassword", User.Role.EMPLOYEE)).thenReturn(null);

        BusinessException exception = assertThrows(BusinessException.class, () -> userService.findAccountByUsernameAndPassword("testUser", "wrongPassword", User.Role.EMPLOYEE));
        assertEquals("Login Failed!!: Bad credentials. Please try again.", exception.getMessage());
    }
}
