Task: 
IT Support Ticket SystemContext: 
Develop a simple ticket management application that allows employees to report and track IT issues.

Requirements:
Ticket Creation:
Employees can create tickets with the following details: 
Title
Description
Priority (Low, Medium, High)
Category (Network, Hardware, Software, Other)
Creation Date (set automatically)
Status Tracking:
Tickets have the following statuses: 
New (default when created)
In Progress (changed by IT support)
Resolved (changed by IT support)
User Roles:
Employees: Can create and view their own tickets.
IT Support: Can view all tickets, change statuses, and add comments.
Audit Log:
Track changes to ticket status and added comments.
Search & Filter:
Search by ticket ID and status.



Technology Stack & Guidelines:
Backend: Java 17, Spring Boot, RESTful API with Swagger/OpenAPI
Database: Oracle SQL (provide schema as an SQL script)
UI: Java Swing (use MigLayout or GridBagLayout)
Testing: JUnit, Mockito
Documentation:
README with setup instructions
Markdown file for API documentation
Deployment:
Docker container for backend and Oracle DB
Swing client as an executable JAR file



Run the Application
1. Backend
To start the backend service:

 a) docker pull  aalwei/supportticketsystemoracle:latest    
    
 b) run the docker compose file : docker-compose up
   
This will automatically download the necessary Docker images and run the backend in a container.
The backend will be accessible at http://localhost:8080.

 c) show running containers: docker ps ,should show the containers runing in healthy state as show th below attached picture:
 ![image](https://github.com/user-attachments/assets/aa5bfe0f-0e44-4214-b7d7-772bfbd382b4)

the documentation swagger is available at : [http://localhost:8080:swagger-ui/index.html#/]
2. Frontend
After ensuring the backend is running, you can launch the frontend by running:

java -jar supportticketsystemswingclient.jar  
or 
enter supportticketsystemclientswing folder and execute: mvn spring-boot:run

The frontend will communicate with the backend for data and display it within the Swing GUI.
