package com.alweimine.supportticketsystem.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "auditLog_id_sequence")
    @SequenceGenerator(name = "auditLog_id_sequence",
            sequenceName = "auditLog_sequence",
            allocationSize = 1)
    private Long logId;

    private String action;

    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User actionBy;

    private LocalDateTime timestamp;

    // Getters and Setters
}