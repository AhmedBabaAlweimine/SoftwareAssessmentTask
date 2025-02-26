package com.alweimine.supportticketsystemclientswing;

import com.alweimine.supportticketsystemclientswing.entities.Ticket;
import com.alweimine.supportticketsystemclientswing.entities.User;
import com.alweimine.supportticketsystemclientswing.mappper.dto.CommentDto;
import com.alweimine.supportticketsystemclientswing.mappper.dto.TicketDto;
import com.alweimine.supportticketsystemclientswing.mappper.dto.UserDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;

public class TicketListUi extends JFrame {
    //private JFrame frame;
    private String userName;
    private String role;
    private JTable ticketTable;
    private DefaultTableModel tableModel;
    private JButton editButton, createTicketButton, addCommentButton, logOutButton, viewAuditlogsButton;
    private RestTemplate restTemplate;
    private static final String BASE_URL = "http://localhost:8080/tickets"; // Replace with your backend URL

    public TicketListUi(String username, String role) {
        this.userName = username;
        this.role = role;
        restTemplate = new RestTemplate();
        //setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setTitle("Ticket List");
        setBounds(100, 100, 600, 400);
        setLayout(new GridBagLayout()); // Set GridBagLayout

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL; // Set default horizontal filling

        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());


        editButton = new JButton("Change status");
        createTicketButton = new JButton("Create Ticket");
        addCommentButton = new JButton("Add comment");
        viewAuditlogsButton = new JButton("View Audit");
        logOutButton = new JButton("Logout");
        editButton.setEnabled(false); // Disable until a row is selected
        addCommentButton.setEnabled(false);
        viewAuditlogsButton.setEnabled(false);

        if (role.equalsIgnoreCase(User.Role.EMPLOYEE.toString())) {
            buttonPanel.add(createTicketButton);

        } else {
            buttonPanel.add(editButton);
            buttonPanel.add(addCommentButton);
            buttonPanel.add(viewAuditlogsButton);
        }


        buttonPanel.add(logOutButton);

        // Add the button panel to the grid
        gbc.gridx = 0;
        gbc.gridy = 1; // Place below the table
        gbc.gridwidth = 3; // Span across 3 columns
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding around the panel
        add(buttonPanel, gbc);

        // Table model
        tableModel = new DefaultTableModel(new Object[]{"Ticket ID", "Title", "Description", "Category", "Priority", "Status", "Creation Date"}, 0);
        ticketTable = new JTable(tableModel);

        // Adding table to scroll pane
        JScrollPane scrollPane = new JScrollPane(ticketTable);

        // Add scroll pane for the table to the grid
        gbc.gridx = 0;
        gbc.gridy = 0; // Place at the top
        gbc.gridwidth = 3; // Span across 3 columns
        gbc.weightx = 1.0;
        gbc.weighty = 1.0; // Allow table to take up remaining space
        gbc.fill = GridBagConstraints.BOTH;
        add(scrollPane, gbc);

        // Fetch tickets from backend
        fetchTickets();

        // Button actions
        editButton.addActionListener(e -> editTicket());
        viewAuditlogsButton.addActionListener(e -> viewTicketAuditlogs());
        addCommentButton.addActionListener(e -> commentTicket());
        logOutButton.addActionListener(e -> handleLogout());
        createTicketButton.addActionListener(e -> createTicket());
        // Row selection listener
        ticketTable.getSelectionModel().addListSelectionListener(e -> {
            if (ticketTable.getSelectedRow() != -1) {
                editButton.setEnabled(true);
                viewAuditlogsButton.setEnabled(true);
                addCommentButton.setEnabled(true);
            } else {
                editButton.setEnabled(false);
                viewAuditlogsButton.setEnabled(false);
                addCommentButton.setEnabled(false);
            }
        });
    }

    private void createTicket() {
        // Show edit dialog
        JTextField titleField = new JTextField(20);
        JTextField descriptionField = new JTextField(20);
        JComboBox<String> categoryComboBox = new JComboBox<>(new String[]{"Network", "Hardware", "Software", "Other"});
        JComboBox<String> priorityComboBox = new JComboBox<>(new String[]{"Low", "Medium", "High"});
        JComboBox<String> statusComboBox = new JComboBox<>(new String[]{"New", "In Progress", "Resolved"});
        statusComboBox.setEnabled(false);
        JTextField creationField = new JTextField(LocalDateTime.now().toString(), 20);
        creationField.setEnabled(false);


        // Panel for the title and description fields
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout()); // Set GridBagLayout

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; // First column
        gbc.gridy = 0; // First row
        gbc.anchor = GridBagConstraints.WEST; // Align components to the left

        panel.add(new JLabel("Title:"), gbc); // Add Title label

        gbc.gridx = 1; // Move to the second column
        panel.add(titleField, gbc); // Add Title field

        gbc.gridx = 0; // Reset to the first column
        gbc.gridy = 1; // Move to the second row
        panel.add(new JLabel("Description:"), gbc); // Add Description label

        gbc.gridx = 1; // Move to the second column
        panel.add(descriptionField, gbc); // Add Description field

        gbc.gridx = 1; // Move to the second column
        panel.add(categoryComboBox, gbc);
        gbc.fill = GridBagConstraints.BOTH;

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Status:"), gbc); // Add Description label

        gbc.gridx = 1; // Move to the second column
        panel.add(statusComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Category:"), gbc); // Add Description label
        gbc.fill = GridBagConstraints.BOTH;

        gbc.gridx = 1; // Move to the second column
        panel.add(categoryComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Priority:"), gbc); // Add Description label

        gbc.gridx = 1; // Move to the second column
        panel.add(priorityComboBox, gbc);
        gbc.fill = GridBagConstraints.BOTH;

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(new JLabel("Creation Date:"), gbc); // Add Description label

        gbc.gridx = 1; // Move to the second column
        panel.add(creationField, gbc);

        int option = JOptionPane.showConfirmDialog(createTicketButton, panel, "New Ticket", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            // Update ticket details
            TicketDto newTicket = new TicketDto();
            newTicket.setDescription(descriptionField.getText());
            newTicket.setTitle(titleField.getText());

            if (newTicket.getTitle().isEmpty() || newTicket.getDescription().isEmpty()) {
                JOptionPane.showMessageDialog(createTicketButton, "Fields required");
                return;
            }

            newTicket.setCreationDate(LocalDateTime.parse(creationField.getText()));
            newTicket.setStatus(statusComboBox.getSelectedItem().toString().equals("New") ?
                    Ticket.Status.NEW : statusComboBox.getSelectedItem().toString().equals("In Progress") ?
                    Ticket.Status.IN_PROGRESS : Ticket.Status.RESOLVED);

            newTicket.setPriority(priorityComboBox.getSelectedItem().toString().equals("Low") ?
                    Ticket.Priority.LOW : priorityComboBox.getSelectedItem().toString().equals("Medium") ?
                    Ticket.Priority.MEDIUM : Ticket.Priority.HIGH);

            newTicket.setCategory(categoryComboBox.getSelectedItem().toString().equals("Network") ?
                    Ticket.Category.NETWORK : categoryComboBox.getSelectedItem().toString().equals("Hardware") ?
                    Ticket.Category.HARDWARE : categoryComboBox.getSelectedItem().toString().equals("Software") ?
                    Ticket.Category.SOFTWARE : Ticket.Category.OTHER);

            String url = BASE_URL + "?username=" + userName;
            try {
                ResponseEntity<TicketDto> response = restTemplate.postForEntity(url, newTicket, TicketDto.class);
                if (response.getStatusCode().is2xxSuccessful()) {
                    JOptionPane.showMessageDialog(createTicketButton, "Ticket created successfully.");
                } else {
                    JOptionPane.showMessageDialog(createTicketButton, "creating Ticket failed. Try again.");
                }
                refreshTickets();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(createTicketButton, "Error creating ticket: " + e.getMessage());
            }
        }
    }

    private void fetchTickets() {
        String url = this.role.equalsIgnoreCase(User.Role.EMPLOYEE.toString()) ? BASE_URL + "/mytickets/" + userName : BASE_URL; // Replace with your backend URL
        try {
            ResponseEntity<List<TicketDto>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<TicketDto>>() {
            });
            List<TicketDto> tickets = response.getBody();

            if (tickets != null) {
                for (TicketDto ticket : tickets) {

                    tableModel.addRow(new Object[]{ticket.getTicketId(), ticket.getTitle(), ticket.getDescription(), ticket.getCategory(), ticket.getPriority(), ticket.getStatus(), ticket.getCreationDate()});
                }
            } else {
                JOptionPane.showMessageDialog(this, "No tickets available.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error fetching tickets: " + e.getMessage());
        }
    }

    private void handleLogout() {
        int a = JOptionPane.showConfirmDialog(logOutButton, "Are you sure?");
        // JOptionPane.setRootFrame(null);
        if (a == JOptionPane.YES_OPTION) {
            //dispose();
            this.setVisible(false);
        }
    }

    private void commentTicket() {
        int selectedRow = ticketTable.getSelectedRow();
        if (selectedRow != -1) {
            long ticketId = (long) tableModel.getValueAt(selectedRow, 0);
            String title = (String) tableModel.getValueAt(selectedRow, 1);
            String description = (String) tableModel.getValueAt(selectedRow, 2);
            String category = tableModel.getValueAt(selectedRow, 3).toString();
            String priority = (String) tableModel.getValueAt(selectedRow, 4).toString();
            String status = (String) tableModel.getValueAt(selectedRow, 5).toString();
            LocalDateTime creation = (LocalDateTime) tableModel.getValueAt(selectedRow, 6);


            // Show edit dialog
            JTextField titleField = new JTextField(title, 20);
            titleField.setEnabled(false);
            JTextField descriptionField = new JTextField(description, 20);
            descriptionField.setEnabled(false);
            JTextField categoryField = new JTextField(category, 20);
            categoryField.setEnabled(false);
            JTextField priorityField = new JTextField(priority, 20);
            priorityField.setEnabled(false);
            JTextField currentStatusField = new JTextField(status, 20);
            currentStatusField.setEnabled(false);
            JTextField creationField = new JTextField(creation.toString(), 20);
            creationField.setEnabled(false);
            JTextArea commentField = new JTextArea();
            commentField.setPreferredSize(new Dimension(100, 100));

            // Panel for the title and description fields
            JPanel panel = new JPanel();
            panel.setLayout(new GridBagLayout()); // Set GridBagLayout

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0; // First column
            gbc.gridy = 0; // First row
            gbc.anchor = GridBagConstraints.WEST; // Align components to the left

            panel.add(new JLabel("Title:"), gbc); // Add Title label

            gbc.gridx = 1; // Move to the second column
            panel.add(titleField, gbc); // Add Title field

            gbc.gridx = 0; // Reset to the first column
            gbc.gridy = 1; // Move to the second row
            panel.add(new JLabel("Description:"), gbc); // Add Description label

            gbc.gridx = 1; // Move to the second column
            panel.add(descriptionField, gbc); // Add Description field

            gbc.gridx = 0;
            gbc.gridy = 2;
            panel.add(new JLabel("Current Status:"), gbc); // Add Description label

            gbc.gridx = 1; // Move to the second column
            panel.add(currentStatusField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 4;
            panel.add(new JLabel("Category:"), gbc); // Add Description label

            gbc.gridx = 1; // Move to the second column
            panel.add(categoryField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 5;
            panel.add(new JLabel("Priority:"), gbc); // Add Description label

            gbc.gridx = 1; // Move to the second column
            panel.add(priorityField, gbc);
            gbc.gridx = 0;
            gbc.gridy = 6;
            panel.add(new JLabel("Creation Date:"), gbc); // Add Description label

            gbc.gridx = 1; // Move to the second column
            panel.add(creationField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 7;
            panel.add(new JLabel("Comment:"), gbc); // Add Description label
            gbc.fill = GridBagConstraints.BOTH;
            gbc.gridx = 1; // Move to the second column
            panel.add(commentField, gbc);

            int option = JOptionPane.showConfirmDialog(editButton, panel, "Add Comment", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                // Update ticket details
                UserDto userDto = new UserDto();
                userDto.setUsername(userName);
                CommentDto newComment = new CommentDto(null, commentField.getText(), null, null, userDto);
                String url = BASE_URL + "/comments/" + ticketId; // Replace with your backend URL
                try {
                    ResponseEntity<CommentDto> response = restTemplate.postForEntity(url, newComment, CommentDto.class);
                    if (response.getStatusCode().is2xxSuccessful()) {
                        JOptionPane.showMessageDialog(addCommentButton, "comment added successfully.");
                    } else {
                        JOptionPane.showMessageDialog(addCommentButton, "comment failed. Try again.");
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(addCommentButton, "Error updating ticket: " + e.getMessage());
                }
            }
        }

    }

    private void editTicket() {
        int selectedRow = ticketTable.getSelectedRow();
        if (selectedRow != -1) {
            long ticketId = (long) tableModel.getValueAt(selectedRow, 0);
            String title = (String) tableModel.getValueAt(selectedRow, 1);
            String description = (String) tableModel.getValueAt(selectedRow, 2);
            String category = tableModel.getValueAt(selectedRow, 3).toString();
            String priority = (String) tableModel.getValueAt(selectedRow, 4).toString();
            String status = (String) tableModel.getValueAt(selectedRow, 5).toString();
            LocalDateTime creation = (LocalDateTime) tableModel.getValueAt(selectedRow, 6);


            // Show edit dialog
            JTextField titleField = new JTextField(title, 20);
            titleField.setEnabled(false);
            JTextField descriptionField = new JTextField(description, 20);
            descriptionField.setEnabled(false);
            JTextField categoryField = new JTextField(category, 20);
            categoryField.setEnabled(false);
            JTextField priorityField = new JTextField(priority, 20);
            priorityField.setEnabled(false);
            JTextField currentStatusField = new JTextField(status, 20);
            currentStatusField.setEnabled(false);
            JTextField creationField = new JTextField(creation.toString(), 20);
            creationField.setEnabled(false);
            JComboBox<String> statusComboBox = new JComboBox<>(new String[]{"New", "In Progress", "Resolved"});


            // Panel for the title and description fields
            JPanel panel = new JPanel();
            panel.setLayout(new GridBagLayout()); // Set GridBagLayout

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0; // First column
            gbc.gridy = 0; // First row
            gbc.anchor = GridBagConstraints.WEST; // Align components to the left

            panel.add(new JLabel("Title:"), gbc); // Add Title label

            gbc.gridx = 1; // Move to the second column
            panel.add(titleField, gbc); // Add Title field

            gbc.gridx = 0; // Reset to the first column
            gbc.gridy = 1; // Move to the second row
            panel.add(new JLabel("Description:"), gbc); // Add Description label

            gbc.gridx = 1; // Move to the second column
            panel.add(descriptionField, gbc); // Add Description field

            gbc.gridx = 0;
            gbc.gridy = 2;
            panel.add(new JLabel("Current Status:"), gbc); // Add Description label

            gbc.gridx = 1; // Move to the second column
            panel.add(currentStatusField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 3;
            panel.add(new JLabel("Status:"), gbc); // Add Description label

            gbc.gridx = 1; // Move to the second column
            panel.add(statusComboBox, gbc);

            gbc.gridx = 0;
            gbc.gridy = 4;
            panel.add(new JLabel("Category:"), gbc); // Add Description label

            gbc.gridx = 1; // Move to the second column
            panel.add(categoryField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 5;
            panel.add(new JLabel("Priority:"), gbc); // Add Description label

            gbc.gridx = 1; // Move to the second column
            panel.add(priorityField, gbc);
            gbc.gridx = 0;
            gbc.gridy = 6;
            panel.add(new JLabel("Creation Date:"), gbc); // Add Description label

            gbc.gridx = 1; // Move to the second column
            panel.add(creationField, gbc);

            int option = JOptionPane.showConfirmDialog(editButton, panel, "Edit Ticket", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                // Update ticket details
                TicketDto updatedTicket = new TicketDto(ticketId, null, null, null, null,
                        statusComboBox.getSelectedItem().toString().equals("New") ?
                                Ticket.Status.NEW : statusComboBox.getSelectedItem().toString().equals("In Progress") ?
                                Ticket.Status.IN_PROGRESS : Ticket.Status.RESOLVED, LocalDateTime.now(),
                        new UserDto(null, userName, null, null, null,
                                null), null);
                String url = BASE_URL + "/status/" + ticketId; // Replace with your backend URL
                try {
                    restTemplate.put(url, updatedTicket);
                    JOptionPane.showMessageDialog(editButton, "Ticket updated successfully.");
                    refreshTickets();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(editButton, "Error updating ticket: " + e.getMessage());
                }
            }
        }
    }

    private void viewTicketAuditlogs() {
        int selectedRow = ticketTable.getSelectedRow();
        if (selectedRow != -1) {
            long ticketId = (long) tableModel.getValueAt(selectedRow, 0);
            AuditLogUI auditLogUI = new AuditLogUI(this.userName, this.role, ticketId);
            auditLogUI.setVisible(true);
        }
    }

    private void refreshTickets() {
        // Clear the table and fetch the tickets again
        tableModel.setRowCount(0);
        fetchTickets();
    }
}

