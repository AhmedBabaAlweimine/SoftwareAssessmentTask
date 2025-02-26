package com.alweimine.supportticketsystemclientswing.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class AuditLog {
    private Long logId;
    private String action;
    private Ticket ticket;
    private User actionBy;

    private LocalDateTime timestamp;

    // Getters and Setters
}