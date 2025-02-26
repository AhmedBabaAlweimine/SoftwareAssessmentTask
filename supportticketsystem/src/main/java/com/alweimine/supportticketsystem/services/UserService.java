package com.alweimine.supportticketsystem.services;

import com.alweimine.supportticketsystem.entities.User;
import com.alweimine.supportticketsystem.exception.BusinessException;
import com.alweimine.supportticketsystem.exception.DuplicateUserNameException;
import com.alweimine.supportticketsystem.mappper.UserMapper;
import com.alweimine.supportticketsystem.mappper.dto.UserDto;
import com.alweimine.supportticketsystem.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserMapper userMapper;

    public List<UserDto> findAllUsers() {
        logger.info("start---- findAllUsers in UserService class.");
        List<User> users = userRepository.findAll();
        logger.info("End---- findAllUsers in UserService class.");

        return users.stream().map(userMapper::entityToDto).collect(Collectors.toList());
    }

    @Transactional // Marks this method as transactional to ensure data consistency
    public int deleteUserById(Long userId) {
        logger.info("start---- deleteUserById : id {} in UserService class.", userId);
        return userRepository.deleteByUserId(userId);
    }

    public UserDto findUserById(Long userID) {
        logger.info("start---- findUserById : id {} in UserService class.", userID);
        return userRepository.findById(userID).isPresent() ? userMapper.entityToDto(userRepository.findById(userID).get()) : null;
    }

    public UserDto save(UserDto userDto) {
        logger.info("start save new User with username: {}  in UserService class.", userDto.getUsername());
        // Check if all required fields are provided
        if (isValidUser(userDto)) {
            // Check if username already exists
            if (isAcountExist(userDto.getUsername())) {
                logger.error("Save User  operation Failed  {}.", "This username already exists. Please try with a different username.");
                throw new DuplicateUserNameException(605,
                        "Registration Failed!!: " + userDto.getUsername()
                                + " " + "This username already exists. Please try with a different username.");
            } else {
                // Save the new user and return as DTO
                User entity = userMapper.dtoToEntity(userDto);
                User user = userRepository.save(entity);
                logger.info("User with username: {} saved successfully.", userDto.getUsername());
                return userMapper.entityToDto(user);

            }
        } else {
            // Throw an error if required fields are missing
            logger.error("Save User operation Failed  {}  ,All field required .", userDto.getUsername());
            throw new BusinessException(606,
                    "save Failed!!:  All field required");
        }
    }

    /**
     * Helper method to check if the user DTO has valid values for all required fields.
     *
     * @param userDto The user DTO to check.
     * @return True if all required fields are valid, otherwise false.
     */
    private boolean isValidUser(UserDto userDto) {
        return userDto.getUsername() != null && !userDto.getUsername().isEmpty()
                && userDto.getPassword() != null && !userDto.getPassword().isEmpty();
    }

    /**
     * Checks if an account already exists by username.
     *
     * @param username The username to check.
     * @return True if the account exists, otherwise false.
     */
    public boolean isAcountExist(String username) {
        return this.findUserByUsername(username) != null;
    }

    /**
     * Finds a user by their username.
     *
     * @param username The username to search for.
     * @return The UserDto if found, otherwise null.
     */
    public UserDto findUserByUsername(String username) {
        logger.info("start findUserByUsername with username: {}  in UserService class.", username);
        return userMapper.entityToDto(userRepository.findByUsername(username));
    }



    public UserDto findAccountByUsernameAndPassword(String username, String password,User.Role role) {
        // Validate input fields
        logger.info("start---- findAccountByUsernameAndPassword with username : {}  in UserService class.", username);

        if (username != null && !username.isEmpty() && password != null && !password.isEmpty()) {
            User accountFound = userRepository.findByUsernameAndPasswordAndRole(username, password,role);
            if (accountFound != null) {
                logger.info("account found successfully with username and password: {}  .", username);
                return userMapper.entityToDto(accountFound);
            } else {
                logger.error("End --- No account found  with username: {} and password:.", username);
                throw new BusinessException(607,
                        "Login Failed!!: Bad credentials. Please try again.");
            }
        } else {
            logger.error("End ----- findAccountByUsernameAndPassword with username : {}  in UserService class Failed ed .",
                    "Login Failed!!: username and password required");
            throw new BusinessException(608,
                    "Login Failed!!: username and password required");
        }
    }
}
