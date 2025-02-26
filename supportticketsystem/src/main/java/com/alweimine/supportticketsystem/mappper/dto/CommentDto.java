package com.alweimine.supportticketsystem.mappper.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class  CommentDto {
    private Long commentId;
    private String text;
    private LocalDateTime creationDate;
    private TicketDto ticketDto;
    private UserDto userDto;
}
