package com.alweimine.supportticketsystem.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data

public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ticket_id_sequence")
    @SequenceGenerator(name = "ticket_id_sequence",
            sequenceName = "ticket_sequence",
            allocationSize = 1)
    private Long ticketId;

    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    private Category category;

    private LocalDateTime creationDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "ticket",fetch = FetchType.LAZY)
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
