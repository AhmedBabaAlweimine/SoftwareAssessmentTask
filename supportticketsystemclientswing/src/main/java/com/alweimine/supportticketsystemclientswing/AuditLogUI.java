package com.alweimine.supportticketsystemclientswing;


import com.alweimine.supportticketsystemclientswing.mappper.dto.AuditLogDto;
import com.alweimine.supportticketsystemclientswing.mappper.dto.CommentDto;
import com.alweimine.supportticketsystemclientswing.mappper.dto.TicketDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;


public class AuditLogUI extends JFrame {

    private String usename;
    private String rol;
    private JTable auditTable, commentTable;
    private DefaultTableModel auditTableModel, commentTableModel;
    private JButton editButton, deleteButton, addCommentButton, logOutButton, viewAuditlogsButton;
    private RestTemplate restTemplate;
    private String BASE_URL;

    public AuditLogUI(String username, String role, Long ticketId, String BASE_URL) {
        this.usename = username;
        this.rol = role;
        this.BASE_URL = BASE_URL;
        restTemplate = new RestTemplate();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Audit log");
        setBounds(100, 100, 600, 400);
        setLayout(new GridBagLayout()); // Set GridBagLayout

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Table model
        auditTableModel = new DefaultTableModel(new Object[]{"Audit ID", "Action", "Date Modification", "User"}, 0);
        auditTable = new JTable(auditTableModel);

        commentTableModel = new DefaultTableModel(new Object[]{"Comment ID", "Text ", "Date Modification", "User"}, 0);
        commentTable = new JTable(commentTableModel);

        // Adding table to scroll pane
        JScrollPane scrollPane = new JScrollPane(auditTable);
        JScrollPane scrollPaneComm = new JScrollPane(commentTable);


        // Add scroll pane for the table to the grid
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        add(scrollPane, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        add(scrollPaneComm, gbc);

        // Fetch tickets from backend
        fetchAuditLog(ticketId);
        fetchComment(ticketId);
    }

    private void fetchAuditLog(Long ticketId) {
        String url = BASE_URL;
        try {
            ResponseEntity<List<AuditLogDto>> response = restTemplate.exchange(url + "/audit/" + ticketId, HttpMethod.GET, null, new ParameterizedTypeReference<List<AuditLogDto>>() {
            });
            List<AuditLogDto> auditLogDtos = response.getBody();

            if (auditLogDtos != null) {
                for (AuditLogDto auditLogDto : auditLogDtos) {

                    auditTableModel.addRow(new Object[]{auditLogDto.getLogId(), auditLogDto.getAction(), auditLogDto.getTimestamp(), auditLogDto.getActionBy().getUsername()});
                }
            } else {
                JOptionPane.showMessageDialog(this, "No audit log available.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error fetching audit: " + e.getMessage());
        }
    }


    private void fetchComment(Long ticketId) {
        String url = BASE_URL;
        try {
            ResponseEntity<List<TicketDto>> response = restTemplate.exchange(url + "/search?ticketId=" + ticketId, HttpMethod.GET, null, new ParameterizedTypeReference<List<TicketDto>>() {
            });
            List<TicketDto> ticketDtos = response.getBody();

            if (ticketDtos != null) {
                for (CommentDto commentDto : ticketDtos.get(0).getCommentDtos()) {

                    commentTableModel.addRow(new Object[]{commentDto.getCommentId(), commentDto.getText(), commentDto.getCreationDate(), commentDto.getUserDto().getUsername()});
                }
            } else {
                JOptionPane.showMessageDialog(this, "No audit log available.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error fetching audit: " + e.getMessage());
        }
    }

}
