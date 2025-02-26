package com.alweimine.supportticketsystem.repositories;

import com.alweimine.supportticketsystem.entities.AuditLog;
import com.alweimine.supportticketsystem.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findByTicketTicketId(Long ticketId);
}
