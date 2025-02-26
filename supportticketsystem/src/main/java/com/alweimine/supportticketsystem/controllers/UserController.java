package com.alweimine.supportticketsystem.controllers;

import com.alweimine.supportticketsystem.entities.User;
import com.alweimine.supportticketsystem.mappper.dto.UserDto;
import com.alweimine.supportticketsystem.services.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    // Endpoint for user login
    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody UserDto userDto, HttpSession session) {
        // Check user credentials and get the user info
        UserDto foundUser = userService.findAccountByUsernameAndPassword(userDto.getUsername(), userDto.getPassword(),userDto.getRole());

        // Log the login attempt
        logger.info("User " + session.getAttribute("username") + " logged in!");

        // If successful, create a session and store user info
        session.setAttribute("userId", foundUser.getUserId());
        session.setAttribute("username", foundUser.getUsername());
        session.setAttribute("role", foundUser.getRole());

        // Log the successful login
        logger.info("User " + session.getAttribute("username") + " logged in!");

        return ResponseEntity.ok(foundUser);  // Return the found user details as a response
    }

    // Endpoint to create or save a new user
    @PostMapping
    public ResponseEntity<UserDto> saveUser(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.save(userDto));  // Save the user and return the created user
    }

    // Endpoint to find a user by their username
    @GetMapping("/{username}")
    public ResponseEntity<UserDto> findUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.findUserByUsername(username));  // Return the user found by username
    }

    /* Endpoint to find a user by their ID
    @GetMapping("/user/{userID}")
    public ResponseEntity<UserDto> findUserById(@PathVariable Long userID) {
        return ResponseEntity.ok(userService.findUserById(userID));  // Return the user found by ID
    }*/

    // Endpoint to get all users (admin only access)
    @GetMapping
   // @AdminOnly  // Custom annotation ensuring only admins can access this endpoint
    public ResponseEntity<List<UserDto>> findAllUsers() {
        return ResponseEntity.ok(userService.findAllUsers());  // Return a list of all users
    }

    // Endpoint to delete a user by ID (admin only access)
    @DeleteMapping("/{id}")
    //@AdminOnly  // Admin-only access
    public ResponseEntity<Integer> deleteUserById(@PathVariable Long id) {
        int numberDeleted = userService.deleteUserById(id);  // Delete the user and get the number of deleted records
        return numberDeleted != 0 ? new ResponseEntity<>(numberDeleted, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.OK);  // Return the number of users deleted
    }

    // Endpoint to update user details (admin only access)
    /*@AdminOnly  // Admin-only access
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.updateUser(id, userDto));  // Update and return the updated user
    }*/
}
