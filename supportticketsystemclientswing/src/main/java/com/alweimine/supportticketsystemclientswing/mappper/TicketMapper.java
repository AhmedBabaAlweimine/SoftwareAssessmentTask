package com.alweimine.supportticketsystemclientswing.mappper;


import com.alweimine.supportticketsystemclientswing.entities.Ticket;
import com.alweimine.supportticketsystemclientswing.mappper.dto.TicketDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class TicketMapper extends AbstractMapper<Ticket, TicketDto> {

    private final UserMapper userMapper;
    private final CommentMapper commentMapper;

    @Override
    public Ticket dtoToEntity(TicketDto dto) {
        Ticket ticket = new Ticket();
        ticket.setStatus(dto.getStatus());
        ticket.setPriority(dto.getPriority());
        ticket.setCategory(dto.getCategory());
        ticket.setTitle(dto.getTitle());
        ticket.setDescription(dto.getDescription());
        ticket.setCreationDate(LocalDateTime.now());
        if (dto.getUserDto() != null)
            ticket.setUser(userMapper.dtoToEntity(dto.getUserDto()));
        if (dto.getCommentDtos() != null)
            ticket.setComments(dto.getCommentDtos().stream().
                    map(commentMapper::dtoToEntity).collect(Collectors.toList()));
        return ticket;
    }

    @Override
    public TicketDto entityToDto(Ticket entity) {
        TicketDto ticketDto = new TicketDto();
        ticketDto.setTicketId(entity.getTicketId());
        ticketDto.setTitle(entity.getTitle());
        ticketDto.setCategory(entity.getCategory());
        ticketDto.setDescription(entity.getDescription());
        ticketDto.setPriority(entity.getPriority());
        ticketDto.setCreationDate(entity.getCreationDate());
        ticketDto.setStatus(entity.getStatus());
        ticketDto.setUserDto(userMapper.entityToDto(entity.getUser()));
        if (entity.getComments() != null)
            ticketDto.setCommentDtos(entity.getComments().stream().map(commentMapper::entityToDto).collect(Collectors.toList()));

        return ticketDto;
    }
}
