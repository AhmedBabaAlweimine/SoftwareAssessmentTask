package com.alweimine.supportticketsystem.services;

import com.alweimine.supportticketsystem.entities.AuditLog;
import com.alweimine.supportticketsystem.entities.Ticket;
import com.alweimine.supportticketsystem.mappper.AuditLogMapper;
import com.alweimine.supportticketsystem.mappper.dto.AuditLogDto;
import com.alweimine.supportticketsystem.repositories.AuditLogRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AuditService {
    private final AuditLogRepository auditLogRepository;
    private final AuditLogMapper auditLogMapper;

    public List<AuditLogDto> getAuditlogsForTicket(Long ticketId){
      List<AuditLog> auditLog= auditLogRepository.findByTicketTicketId(ticketId);
      return auditLog.stream().map( auditLogMapper::entityToDto).collect(Collectors.toList());
    }
}
