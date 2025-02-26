package com.alweimine.supportticketsystem.swingclient;

import com.alweimine.supportticketsystem.mappper.dto.AuditLogDto;
import com.alweimine.supportticketsystem.mappper.dto.CommentDto;
import com.alweimine.supportticketsystem.mappper.dto.TicketDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AuditLogUI extends JFrame {

    private String usename;
    private String rol;
    private JTable auditTable,commentTable;
    private DefaultTableModel auditTableModel,commentTableModel;
    private JButton editButton, deleteButton, addCommentButton, logOutButton,viewAuditlogsButton;
    private RestTemplate restTemplate;
    private static final String BASE_URL = "http://localhost:8080/tickets"; // Replace with your backend URL


    public AuditLogUI (String username, String role,Long ticketId) {
        this.usename = username;
        this.rol = role;
        restTemplate = new RestTemplate();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setTitle("Audit log");
        setBounds(100, 100, 600, 400);
        setLayout(new GridBagLayout()); // Set GridBagLayout

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL; // Set default horizontal filling

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
        gbc.gridy = 0; // Place at the top
        gbc.gridwidth = 3; // Span across 3 columns
        gbc.weightx = 1.0;
        gbc.weighty = 1.0; // Allow table to take up remaining space
        gbc.fill = GridBagConstraints.BOTH;
        add(scrollPane, gbc);
        // Add scroll pane for the table to the grid
        gbc.gridx = 0;
        gbc.gridy = 3; // Place at the top
        gbc.gridwidth = 3; // Span across 3 columns
        gbc.weightx = 1.0;
        //gbc.weighty = 1.0; // Allow table to take up remaining space
        //gbc.fill = GridBagConstraints.BOTH;
        add(scrollPaneComm,gbc);

        // Fetch tickets from backend
        fetchAuditLog(ticketId);
        fetchComment(ticketId);
    }

    private void fetchAuditLog(Long ticketId) {
        String url = BASE_URL; // Replace with your backend URL
        try {
            ResponseEntity<List<AuditLogDto>> response = restTemplate.exchange(url+"/audit/"+ticketId, HttpMethod.GET, null, new ParameterizedTypeReference<List<AuditLogDto>>() {
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
        String url = BASE_URL; // Replace with your backend URL
        try {
            ResponseEntity<List<TicketDto>> response = restTemplate.exchange(url+"/search?ticketId="+ticketId, HttpMethod.GET, null, new ParameterizedTypeReference<List<TicketDto>>() {
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
