package com.alweimine.supportticketsystem.controllers;

import com.alweimine.supportticketsystem.mappper.dto.AuditLogDto;
import com.alweimine.supportticketsystem.mappper.dto.CommentDto;
import com.alweimine.supportticketsystem.mappper.dto.TicketDto;
import com.alweimine.supportticketsystem.entities.Ticket;
import com.alweimine.supportticketsystem.services.AuditService;
import com.alweimine.supportticketsystem.services.TicketService;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;
    private final AuditService auditService;

    @PostMapping
    public ResponseEntity<TicketDto> createTicket(@RequestBody TicketDto ticketDto, @RequestParam String username) {
        TicketDto createdTicket = ticketService.createTicket(ticketDto, username);
        return new ResponseEntity<>(createdTicket, HttpStatus.CREATED); // 201 Created
    }


    @PutMapping("/status/{ticketId}")
    public ResponseEntity<Integer> updateTicketStatus(@PathVariable Long ticketId, @RequestBody TicketDto ticketDto) {
        return new ResponseEntity<>(ticketService.updateTicketStatus(ticketId, ticketDto.getStatus(), ticketDto.getUserDto().getUsername()), HttpStatus.OK); // 200 OK
    }

    @GetMapping("/mytickets/{userName}")
    public ResponseEntity<List<TicketDto>> getTicketsByUserName(@PathVariable String userName) {
        List<TicketDto> tickets = ticketService.getTicketsByUserName(userName);
        if (tickets.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        }
        return new ResponseEntity<>(tickets, HttpStatus.OK); // 200 OK
    }

    @PostMapping("/comments/{ticketId}")
    public ResponseEntity<CommentDto> addComment(@PathVariable Long ticketId, @RequestBody CommentDto commentDto) {
        CommentDto comment = ticketService.addComment(ticketId, commentDto);
        return new ResponseEntity<>(comment, HttpStatus.CREATED); // 201 Created
    }

    @GetMapping("/audit/{ticketId}")
    public ResponseEntity<List<AuditLogDto>> ticketAudit(@PathVariable Long ticketId) {
       List<AuditLogDto>  auditLogsDto = auditService.getAuditlogsForTicket(ticketId);
        return new ResponseEntity<>(auditLogsDto, HttpStatus.OK); // 200 OK
    }

    @GetMapping("/search")
    public ResponseEntity<List<TicketDto>> searchTickets( @RequestParam(required = false) Long ticketId, @RequestParam(required = false) Ticket.Status status) {
        List<TicketDto> tickets = ticketService.searchTickets(ticketId, status);
        if (tickets.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        }
        return new ResponseEntity<>(tickets, HttpStatus.OK); // 200 OK
    }

    @GetMapping
    public ResponseEntity<List<TicketDto>> searchTickets() {
        List<TicketDto> tickets = ticketService.searchTickets();
        if (tickets.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        }
        return new ResponseEntity<>(tickets, HttpStatus.OK); // 200 OK
    }



}
