package com.alweimine.supportticketsystem.repositories;

import com.alweimine.supportticketsystem.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByStatus(Ticket.Status status);

    List<Ticket> findByTicketIdAndStatus(Long ticketId, Ticket.Status status);

    List<Ticket> getByUser_Username(String userName);

    @Modifying(clearAutomatically = true)
    // Indicates a modifying query that automatically clears the persistence context
    @Query(value = "UPDATE Ticket t SET t.status =:newStatus WHERE t.ticketId =:ticketId")
    int updateStatus(@Param("ticketId") Long ticketId, @Param("newStatus") Ticket.Status newStatus);

}
