package com.alweimine.supportticketsystemclientswing.mappper.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuditLogDto {
    private Long logId;
    private String action;
    private TicketDto ticket;
    private UserDto actionBy;
    private LocalDateTime timestamp;
}
