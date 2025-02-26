package com.alweimine.supportticketsystem.repositories;

import com.alweimine.supportticketsystem.entities.Comment;
import com.alweimine.supportticketsystem.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByTicketTicketId(Long ticketId);
}
