package com.alweimine.supportticketsystemclientswing.mappper.dto;

import com.alweimine.supportticketsystemclientswing.entities.Ticket;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketDto {
    private Long ticketId;
    private String title;
    private String description;
    private Ticket.Priority priority;
    private Ticket.Category category;
    private Ticket.Status status = Ticket.Status.NEW;
    private LocalDateTime creationDate;
    private UserDto userDto;
    private List<CommentDto> commentDtos;

}

