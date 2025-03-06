package com.alweimine.supportticketsystem.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "user_ticket")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_sequence")
    @SequenceGenerator(name = "user_id_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1)
    private Long userId;

    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private Role role;

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    private List<Ticket> tickets;

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    private List<Comment> comments;

    // Getters and Setters

    public enum Role {
        EMPLOYEE, IT_SUPPORT
    }
}