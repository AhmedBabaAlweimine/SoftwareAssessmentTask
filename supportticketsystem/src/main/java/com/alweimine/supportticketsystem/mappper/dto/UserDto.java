package com.alweimine.supportticketsystem.mappper.dto;

import com.alweimine.supportticketsystem.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long userId;
    private String username;
    private String password;
    private User.Role role;

    private List<TicketDto> ticketDtos;
    private List<CommentDto> commentDtos;

   }