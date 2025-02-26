package com.alweimine.supportticketsystem.services;

import com.alweimine.supportticketsystem.mappper.CommentMapper;
import com.alweimine.supportticketsystem.mappper.TicketMapper;
import com.alweimine.supportticketsystem.mappper.UserMapper;
import com.alweimine.supportticketsystem.mappper.dto.CommentDto;
import com.alweimine.supportticketsystem.mappper.dto.TicketDto;
import com.alweimine.supportticketsystem.entities.AuditLog;
import com.alweimine.supportticketsystem.entities.Comment;
import com.alweimine.supportticketsystem.entities.Ticket;
import com.alweimine.supportticketsystem.entities.User;
import com.alweimine.supportticketsystem.repositories.AuditLogRepository;
import com.alweimine.supportticketsystem.repositories.CommentRepository;
import com.alweimine.supportticketsystem.repositories.TicketRepository;
import com.alweimine.supportticketsystem.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final AuditLogRepository auditLogRepository;
    private final CommentRepository commentRepository;
    private final TicketMapper ticketMapper;
    private final CommentMapper commentMapper;
    private final UserMapper userMapper;


    public TicketDto createTicket(TicketDto ticketDto, String username) {
        User user = userRepository.findByUsername(username);
        Ticket ticket = ticketMapper.dtoToEntity(ticketDto);
        ticket.setUser(user);
        return ticketMapper.entityToDto(ticketRepository.save(ticket));
    }

    @Transactional
    public int updateTicketStatus(Long ticketId, Ticket.Status newStatus, String username) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> new RuntimeException("Ticket not found"));
        User user = userRepository.findByUsername(username);

        if (newStatus != Ticket.Status.NEW) {
            AuditLog log = new AuditLog();
            log.setAction("Status Change from :"+ticket.getStatus() +"->"+newStatus);
            log.setTicket(ticket);
            log.setActionBy(user);
            log.setTimestamp(LocalDateTime.now());
            auditLogRepository.save(log);
        }
        return ticketRepository.updateStatus(ticketId, newStatus);
    }

    @Transactional
    public CommentDto addComment(Long ticketId, CommentDto commentDto) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> new RuntimeException("Ticket not found"));
        User user = userRepository.findByUsername(commentDto.getUserDto().getUsername());

        Comment comment = commentMapper.dtoToEntity(commentDto);
        comment.setTicket(ticket);
        comment.setUser(user);
        CommentDto commentSaved = commentMapper.entityToDto(commentRepository.save(comment));
        commentSaved.setUserDto(userMapper.entityToDto(user));
        commentSaved.setTicketDto(ticketMapper.entityToDto(ticket));
        //Audit
        AuditLog log = new AuditLog();
        log.setAction("comment added");
        log.setTicket(ticket);
        log.setActionBy(user);
        log.setTimestamp(LocalDateTime.now());
        auditLogRepository.save(log);

        return commentSaved;
    }

    public List<TicketDto> searchTickets(Long ticketId, Ticket.Status status) {
        if (ticketId != null && status != null) {
            return ticketRepository.findByTicketIdAndStatus(ticketId, status).stream().map(ticketMapper::entityToDto).collect(Collectors.toList());
        } else if (ticketId != null) {
            return ticketRepository.findById(ticketId).map(List::of).orElse(List.of()).stream().map(ticketMapper::entityToDto).collect(Collectors.toList());
        } else {
            return ticketRepository.findByStatus(status).stream().map(ticketMapper::entityToDto).collect(Collectors.toList());
        }
    }

    public List<TicketDto> searchTickets() {
        return ticketRepository.findAll().stream().map(ticketMapper::entityToDto).collect(Collectors.toList());

    }
    public List<TicketDto> getTicketsByUserName(String userName) {
        List<Ticket> ticketList = ticketRepository.getByUser_Username(userName);
        // Convert the list of entities to a list of DTOs
        return ticketList.stream()
                .map(ticketMapper::entityToDto)
                .collect(Collectors.toList());
    }
}
