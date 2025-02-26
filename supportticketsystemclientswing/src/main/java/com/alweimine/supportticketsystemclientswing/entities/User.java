package com.alweimine.supportticketsystemclientswing.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class User {


    private Long userId;

    private String username;
    private String password;
    private Role role;
    private List<Ticket> tickets;
    private List<Comment> comments;

    // Getters and Setters

    public enum Role {
        EMPLOYEE, IT_SUPPORT
    }
}