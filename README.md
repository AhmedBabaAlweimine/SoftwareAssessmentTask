# IT Support Ticket System with spring boot and swing UI:

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
##### Backend:
* Java 17, Spring Boot, RESTful API with Swagger/OpenAPI
  Database: Oracle SQL (provide schema as an SQL script)
  UI: Java Swing (use MigLayout or GridBagLayout)
  Testing: JUnit, Mockito

* Documentation:
  README with setup instructions
  Markdown file for API documentation

* Deployment:
  Docker container for backend and Oracle DB
  Swing client as an executable JAR file



### User instruction to run this application
1. Backend
   To start the backend service:
   * clone this repo
   * cd /supportticketsystem
   * docker pull  aalwei/supportticketsystemoracle:latest
   * docker-compose up

This will automatically download the necessary Docker images and run the backend in a container.
The backend will be accessible at http://localhost:8080 or the port you’ve specified in docker-compose.yaml.

the documentation swagger is available at : swagger-ui/index.html#/
2. Frontend
   After ensuring the backend is running, you can launch the frontend by running:

   * java -jar supportticketsystemswingclient.jar  
     or
   * cd /supportticketsystemclientswing folder and execute: mvn spring-boot:run

The frontend will communicate with the backend for data and display it within the Swing GUI.
