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
import com.alweimine.supportticketsystem.mappper.dto.UserDto;
import com.alweimine.supportticketsystem.repositories.AuditLogRepository;
import com.alweimine.supportticketsystem.repositories.CommentRepository;
import com.alweimine.supportticketsystem.repositories.TicketRepository;
import com.alweimine.supportticketsystem.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuditLogRepository auditLogRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TicketMapper ticketMapper;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private TicketService ticketService;

    private Ticket ticket;
    private User user;
    private UserDto userDto;
    private TicketDto ticketDto;
    private CommentDto commentDto;
    private Comment comment;

    @BeforeEach
    public void setUp() {
        // Setting up a sample Ticket and User
        user = new User();
        user.setUsername("testuser");

        ticket = new Ticket();
        ticket.setTicketId(1L);
        ticket.setStatus(Ticket.Status.NEW);
        ticket.setUser(user);

        userDto = new UserDto();
        userDto.setUsername("testuser");
        ticketDto = new TicketDto();
        ticketDto.setTicketId(1L);
        ticketDto.setStatus(Ticket.Status.NEW);
        ticketDto.setUserDto(userDto);

        commentDto = new CommentDto();
        commentDto.setText("Sample comment");
        commentDto.setUserDto(userDto);

        comment = new Comment();
        comment.setCommentId(1L);
        comment.setText("Sample comment");
        comment.setTicket(ticket);
        comment.setUser(user);
    }

    @Test
    public void testCreateTicket() {
        when(userRepository.findByUsername("testuser")).thenReturn(user);
        //when(userMapper.dtoToEntity(use)).thenReturn(ticket);
        when(ticketMapper.dtoToEntity(ticketDto)).thenReturn(ticket);
        when(ticketRepository.save(ticket)).thenReturn(ticket);
        when(ticketMapper.entityToDto(ticket)).thenReturn(ticketDto);

        TicketDto createdTicket = ticketService.createTicket(ticketDto, "testuser");

        assertNotNull(createdTicket);
        assertEquals("testuser", createdTicket.getUserDto().getUsername());
        verify(ticketRepository, times(1)).save(ticket);
    }

    @Test
    public void testUpdateTicketStatus() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(userRepository.findByUsername("testuser")).thenReturn(user);
        when(ticketRepository.updateStatus(1L, Ticket.Status.IN_PROGRESS)).thenReturn(1);

        int result = ticketService.updateTicketStatus(1L, Ticket.Status.IN_PROGRESS, "testuser");

        assertEquals(1, result);
        verify(auditLogRepository, times(1)).save(any(AuditLog.class));  // Audit log should be saved
    }

    @Test
    public void testAddComment() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(userRepository.findByUsername("testuser")).thenReturn(user);
        when(commentMapper.dtoToEntity(commentDto)).thenReturn(comment);
        when(commentRepository.save(comment)).thenReturn(comment);
        when(commentMapper.entityToDto(comment)).thenReturn(commentDto);
        when(userMapper.entityToDto(user)).thenReturn(commentDto.getUserDto());

        CommentDto addedComment = ticketService.addComment(1L, commentDto);

        assertNotNull(addedComment);
        assertEquals("Sample comment", addedComment.getText());
        verify(commentRepository, times(1)).save(comment);
        verify(auditLogRepository, times(1)).save(any(AuditLog.class));  // Audit log should be saved
    }

    @Test
    public void testSearchTicketsByTicketIdAndStatus() {
        when(ticketRepository.findByTicketIdAndStatus(1L, Ticket.Status.NEW))
                .thenReturn(List.of(ticket));
        when(ticketMapper.entityToDto(ticket)).thenReturn(ticketDto);

        List<TicketDto> tickets = ticketService.searchTickets(1L, Ticket.Status.NEW);

        assertNotNull(tickets);
        assertFalse(tickets.isEmpty());
        assertEquals(1L, tickets.get(0).getTicketId());
    }

    @Test
    public void testSearchTicketsByTicketId() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(ticketMapper.entityToDto(ticket)).thenReturn(ticketDto);

        List<TicketDto> tickets = ticketService.searchTickets(1L, null);

        assertNotNull(tickets);
        assertEquals(1, tickets.size());
        assertEquals(1L, tickets.get(0).getTicketId());
    }

    @Test
    public void testSearchTicketsByStatus() {
        when(ticketRepository.findByStatus(Ticket.Status.NEW))
                .thenReturn(List.of(ticket));
        when(ticketMapper.entityToDto(ticket)).thenReturn(ticketDto);

        List<TicketDto> tickets = ticketService.searchTickets(null, Ticket.Status.NEW);

        assertNotNull(tickets);
        assertFalse(tickets.isEmpty());
    }

    @Test
    public void testGetTicketsByUserName() {
        when(ticketRepository.getByUser_Username("testuser")).thenReturn(List.of(ticket));
        when(ticketMapper.entityToDto(ticket)).thenReturn(ticketDto);

        List<TicketDto> tickets = ticketService.getTicketsByUserName("testuser");

        assertNotNull(tickets);
        assertFalse(tickets.isEmpty());
        assertEquals("testuser", tickets.get(0).getUserDto().getUsername());
    }
}
