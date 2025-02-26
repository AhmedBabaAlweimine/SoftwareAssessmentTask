package com.alweimine.supportticketsystem.swingclient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import com.alweimine.supportticketsystem.entities.User;
import com.alweimine.supportticketsystem.mappper.dto.UserDto;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

public class UserLoginForm extends JFrame{
    //private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, registerButton;
    private JLabel statusLabel;
    private CardLayout cardLayout;
    private JPanel panelContainer;
    private JComboBox<String> roleComboBox;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UserLoginForm window = new UserLoginForm();
                window.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public UserLoginForm() {
        setTitle("User Login and Registration");
        setBounds(100, 100, 400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        cardLayout = new CardLayout();
        panelContainer = new JPanel(cardLayout);
        add(panelContainer, BorderLayout.CENTER);

        // Panel for Login
        JPanel loginPanel = createLoginPanel();
        // Panel for Register
        JPanel registerPanel = createRegisterPanel();

        panelContainer.add(loginPanel, "Login");
        panelContainer.add(registerPanel, "Register");


        // Show the login panel by default
        cardLayout.show(panelContainer, "Login");
    }

    private JPanel createLoginPanel() {
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel usernameLabel = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 5, 10);
        loginPanel.add(usernameLabel, gbc);

        usernameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 5, 10);
        loginPanel.add(usernameField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 10, 5, 10);
        loginPanel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 10, 5, 10);
        loginPanel.add(passwordField, gbc);

        JLabel roleLabel = new JLabel("Role:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.insets = new Insets(5, 10, 5, 10);
        loginPanel.add(roleLabel, gbc);

        roleComboBox = new JComboBox<>(new String[]{"Employee", "IT Support"});
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.insets = new Insets(5, 10, 5, 10);
        loginPanel.add(roleComboBox, gbc);

        loginButton = new JButton("Login");
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.insets = new Insets(10, 10, 5, 10);
        loginPanel.add(loginButton, gbc);

        statusLabel = new JLabel("");
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.insets = new Insets(5, 10, 10, 10);
        loginPanel.add(statusLabel, gbc);

        JButton switchToRegisterButton = new JButton("Register");
        gbc.gridx = 1;
        gbc.gridy = 5;
        loginPanel.add(switchToRegisterButton, gbc);

        loginButton.addActionListener(e -> handleLogin());
        switchToRegisterButton.addActionListener(e -> cardLayout.show(panelContainer, "Register"));

        return loginPanel;
    }

    private JPanel createRegisterPanel() {
        JPanel registerPanel = new JPanel();
        registerPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel usernameLabel = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 5, 10);
        registerPanel.add(usernameLabel, gbc);

        JTextField registerUsernameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 5, 10);
        registerPanel.add(registerUsernameField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 10, 5, 10);
        registerPanel.add(passwordLabel, gbc);

        JPasswordField registerPasswordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 10, 5, 10);
        registerPanel.add(registerPasswordField, gbc);

        JLabel roleLabel = new JLabel("Role:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.insets = new Insets(5, 10, 5, 10);
        registerPanel.add(roleLabel, gbc);

        JComboBox<String> registerRoleComboBox = new JComboBox<>(new String[]{"Employee", "IT Support"});
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.insets = new Insets(5, 10, 5, 10);
        registerPanel.add(registerRoleComboBox, gbc);

        JButton registerSubmitButton = new JButton("Register");
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.insets = new Insets(10, 10, 5, 10);
        registerPanel.add(registerSubmitButton, gbc);

        JButton switchToLoginButton = new JButton("Back to Login");
        gbc.gridx = 1;
        gbc.gridy = 4;
        registerPanel.add(switchToLoginButton, gbc);

        registerSubmitButton.addActionListener(e -> handleRegister(registerUsernameField.getText(), new String(registerPasswordField.getPassword()), registerRoleComboBox.getSelectedItem().toString()));
        switchToLoginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelContainer, "Login");
            }
        });
        return registerPanel;
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String role = roleComboBox.getSelectedItem().toString();

        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Username or password cannot be empty.");
            return;
        }

        RestTemplate restTemplate = new RestTemplate();
        String loginUrl = "http://localhost:8080/users/login";  // Replace with your backend URL
        UserDto userDto = new UserDto(null,username, password, role.equals("Employee")? User.Role.EMPLOYEE: User.Role.IT_SUPPORT,null,null);

        try {
            ResponseEntity<UserDto> response = restTemplate.postForEntity(loginUrl, userDto, UserDto.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                //dispose();
                TicketListUi ticketListGUI=new TicketListUi(username,role);
                ticketListGUI.setVisible(true);
                usernameField.setText("");
                passwordField.setText("");
                statusLabel.setText("Login successful!");

                /* If the user is IT Support, show ticket list
                if ("IT Support".equals(role)) {
                    showTicketsList();
                }*/
            } else {
                statusLabel.setText("Invalid credentials.");
            }
        } catch (Exception ex) {
            statusLabel.setText("Error connecting to server.");
        }
    }

    private void handleRegister(String username, String password, String role) {
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(registerButton, "Username and password cannot be empty.");
            return;
        }

        RestTemplate restTemplate = new RestTemplate();
        String registerUrl = "http://localhost:8080/users";  // Replace with your backend URL
        UserDto userDto = new UserDto(null,username, password, User.Role.IT_SUPPORT,null,null);

        try {
            ResponseEntity<UserDto> response = restTemplate.postForEntity(registerUrl, userDto, UserDto.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                JOptionPane.showMessageDialog(registerButton, "Registration successful!");
                cardLayout.show(panelContainer, "Login");
            } else {
                JOptionPane.showMessageDialog(registerButton, "Registration failed. Try again.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(registerButton, "Error connecting to server.");
        }
    }

   /* private void showTicketsList() {
        // Fetch the tickets and display them for IT Support
        RestTemplate restTemplate = new RestTemplate();
        String ticketUrl = "http://localhost:8080/tickets";  // Replace with your backend URL

        try {
            ResponseEntity<List<TicketDto>> response = restTemplate.exchange(ticketUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<TicketDto>>() {});
            List<TicketDto> tickets = response.getBody();

            if (tickets != null && !tickets.isEmpty()) {
                StringBuilder ticketList = new StringBuilder("Tickets:\n");
                for (TicketDto ticket : tickets) {
                    ticketList.append(ticket.getTicketId()).append(" - ").append(ticket.getTitle()).append("\n");
                }

                JOptionPane.showMessageDialog(frame, ticketList.toString(), "Tickets List", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "No tickets available.", "Tickets List", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error fetching tickets.");
        }
    }*/
}

