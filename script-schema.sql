-- Create table for User
CREATE TABLE user (
    user_id NUMBER(19) PRIMARY KEY,
    username VARCHAR2(255) NOT NULL,
    password VARCHAR2(255) NOT NULL,
    role VARCHAR2(50) CHECK (role IN ('EMPLOYEE', 'IT_SUPPORT')) NOT NULL
);

-- Create table for Ticket
CREATE TABLE ticket (
    ticket_id NUMBER(19) PRIMARY KEY,
    title VARCHAR2(255) NOT NULL,
    description CLOB,
    priority VARCHAR2(20) CHECK (priority IN ('LOW', 'MEDIUM', 'HIGH')) NOT NULL,
    category VARCHAR2(50) CHECK (category IN ('NETWORK', 'HARDWARE', 'SOFTWARE', 'OTHER')) NOT NULL,
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR2(50) CHECK (status IN ('NEW', 'IN_PROGRESS', 'RESOLVED')) NOT NULL,
    user_id NUMBER(19),
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE SET NULL
);

-- Create table for Comment
CREATE TABLE comment (
    comment_id NUMBER(19) PRIMARY KEY,
    text CLOB NOT NULL,
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ticket_id NUMBER(19),
    user_id NUMBER(19),
    FOREIGN KEY (ticket_id) REFERENCES ticket(ticket_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE SET NULL
);

-- Create table for AuditLog
CREATE TABLE audit_log (
    log_id NUMBER(19) PRIMARY KEY,
    action VARCHAR2(255) NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ticket_id NUMBER(19),
    user_id NUMBER(19),
    FOREIGN KEY (ticket_id) REFERENCES ticket(ticket_id) ON DELETE SET NULL,
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE SET NULL
);


