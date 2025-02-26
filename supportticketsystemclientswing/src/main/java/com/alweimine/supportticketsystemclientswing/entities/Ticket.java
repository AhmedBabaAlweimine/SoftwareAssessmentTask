package com.alweimine.supportticketsystemclientswing.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class Ticket {


    private Long ticketId;

    private String title;
    private String description;

    private Priority priority;

    private Category category;

    private LocalDateTime creationDate;

    private Status status;

    private User user;

    private List<Comment> comments;

    // Getters and Setters

    public enum Priority {
        LOW, MEDIUM, HIGH
    }

    public enum Category {
        NETWORK, HARDWARE, SOFTWARE, OTHER
    }

    public enum Status {
        NEW, IN_PROGRESS, RESOLVED
    }
}
