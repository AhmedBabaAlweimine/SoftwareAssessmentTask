# IT Support Ticket System with spring boot,Oracle,Docker and Swing UI

## this project aim to develop a simple ticket management application that allows employees to report and track IT issues.

### Requirements:
#### Ticket Creation:
Employees can create tickets with the following details:
* Title
* Description
* Priority (Low, Medium, High)
* Category (Network, Hardware, Software, Other)
* Creation Date (set automatically)

#### Status Tracking: Tickets have the following statuses:
* New (default when created)
* In Progress (changed by IT support)
* Resolved (changed by IT support)

#### User Roles:
* Employees: Can create and view their own tickets.
* IT Support: Can view all tickets, change statuses, and add comments.

#### Audit Log:
Track changes to ticket status and added comments.

#### Search & Filter:
Search by ticket ID and status.



#### Technology Stack & Guidelines:
* Java 17, Spring Boot, RESTful API with Swagger/OpenAPI
*  Database: Oracle SQL (provide schema as an SQL script)
*  UI: Java Swing (use MigLayout or GridBagLayout)
*  Testing: JUnit, Mockito


* Documentation:
  README with setup instructions Markdown file for API documentation


* Deployment:
  - Docker container for backend and Oracle DB
   - Swing client as an executable JAR file



### User instruction to run this application
1. Backend Spring boot RESTAPI
   To start the backend service:
   * <code style="color : Aqua">git clone this repository </code> 
   * <code style="color : Aqua">cd /supportticketsystem </code>
   * <code style="color : Aqua">docker pull  aalwei/supportticketsystemoracle:latest </code>
   * <code style="color : Aqua">docker-compose up </code>

This will automatically download the necessary Docker images and run the backend (Rest Api and OracleDB) in a container.
The backend will be accessible at http://localhost:8080 or the port you’ve specified in docker-compose.yaml. 

to see the running containers: 
   * <code style="color : Aqua">docker ps </code> 
   * you should now see the containers running in healthy state as show th below attached picture:
   ![image](https://github.com/user-attachments/assets/aa5bfe0f-0e44-4214-b7d7-772bfbd382b4)

the documentation swagger is available at : 
   * http://localhost:8080/swagger-ui/index.html#/
2. Frontend
   After ensuring the backend is running, you can launch the frontend by running:

   * <code style="color : Gold">java -jar supportticketsystemswingclient.jar </code>   
                     or
   * <code style="color : Aqua">cd /supportticketsystemclientswing folder </code> and execute: <code style="color : Gold">mvn spring-boot:run </code>

🏆 The previous sections are the bare minimum, The front-End will communicate with the Backend for data and display it within the Swing GUI.
