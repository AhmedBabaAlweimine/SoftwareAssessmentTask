package com.alweimine.supportticketsystemclientswing;

import com.alweimine.supportticketsystemclientswing.entities.User;
import com.alweimine.supportticketsystemclientswing.mappper.dto.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SpringBootApplication
public class UserLoginForm extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, registerButton;
    private JLabel statusLabel;
    private CardLayout cardLayout;
    private JPanel panelContainer;
    private JComboBox<String> roleComboBox;
    @Value("${host.ulr}")
    private String host;

    public static void main(String[] args) {

        var ctx = new SpringApplicationBuilder(UserLoginForm.class)
                .headless(false).web(WebApplicationType.NONE).run(args);
        EventQueue.invokeLater(() -> {

            var ex = ctx.getBean(UserLoginForm.class);
            ex.setVisible(true);
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
        String loginUrl = host + "users/login";  // Replace with your backend URL
        UserDto userDto = new UserDto(null, username, password, role.equals("Employee") ? User.Role.EMPLOYEE : User.Role.IT_SUPPORT, null, null);

        try {
            ResponseEntity<UserDto> response = restTemplate.postForEntity(loginUrl, userDto, UserDto.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                TicketListUi ticketListGUI = new TicketListUi(username, role,host);
                ticketListGUI.setVisible(true);
                usernameField.setText("");
                passwordField.setText("");
                statusLabel.setText("Login successful!");
            } else {
                statusLabel.setText("Invalid credentials.");
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            statusLabel.setText("Error connecting to server.");
        }
    }

    private void handleRegister(String username, String password, String role) {
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(registerButton, "Username and password cannot be empty.");
            return;
        }

        RestTemplate restTemplate = new RestTemplate();
        String registerUrl = host + "users";  // Replace with your backend URL
        UserDto userDto = new UserDto(null, username, password, role.equals("Employee") ? User.Role.EMPLOYEE : User.Role.IT_SUPPORT, null, null);

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
}

