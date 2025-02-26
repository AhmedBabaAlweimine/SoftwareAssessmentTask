package com.alweimine.supportticketsystemclientswing.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class Comment {
    private Long commentId;
    private String text;
    private LocalDateTime creationDate;
    private Ticket ticket;
    private User user;

    // Getters and Setters
}