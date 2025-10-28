# Sab3 Islamic Bank System

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Java 21](https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html)
[![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/License-Unspecified-blue.svg)](https://github.com/MooRaa86/Sab3-Bank/blob/main/LICENSE)

## Overview

The **Sab3 Islamic Bank System** is a comprehensive Back-End project built using **Spring Boot** and **Java 21**. Its primary goal is to provide a robust set of RESTful APIs for managing core banking operations.

This system is designed to serve as the backbone for a banking service, focusing on security, user account management, financial transactions, email services, and statement generation.

---

## Key Features

*   **User and Account Management:** User registration, bank account creation, and querying account information.
*   **Financial Transactions:** Support for deposit, withdrawal, and inter-account transfer operations.
*   **Security and Authentication:** Utilizes Spring Security and JSON Web Tokens (JJWT) to secure the APIs.
*   **Email Services:** Sending email notifications to users (via Spring Mail).
*   **Account Statement Generation:** Ability to generate account statements in PDF format (using iTextPDF).
*   **API Documentation:** Interactive API documentation provided using Springdoc OpenAPI (Swagger UI).

---

## Technologies Used

The project relies on a powerful stack of modern Java technologies and tools:

| Category | Technology | Description |
| :--- | :--- | :--- |
| **Core Framework** | Spring Boot 3.5.6 | Simplifies the creation of stand-alone, production-ready Spring applications. |
| **Programming Language** | Java 21 | The latest Java version supporting modern features and performance. |
| **Database** | MySQL | The relational database management system used to store user and transaction data. |
| **Data Access** | Spring Data JPA | Simplifies data access and persistence using Java objects. |
| **Security** | Spring Security & JJWT | Secures endpoints and manages authentication and authorization. |
| **Email Service** | Spring Boot Starter Mail | Used for sending banking notifications via email. |
| **API Documentation** | Springdoc OpenAPI (Swagger) | Provides a graphical user interface for exploring and testing the APIs. |
| **Reporting** | iTextPDF | A library for creating PDF documents, used for generating account statements. |
| **Build Tool** | Maven | Used for project dependency management and the build process. |

---

## Project Structure

The project follows a typical Spring Boot application structure, with clear separation of concerns:

```
Sab3-Bank/
├── src/main/java/com/bank/System_V1/
│   ├── configuration/          # Security configurations (Spring Security)
│   ├── contrroller/            # REST Endpoints (REST Controllers)
│   ├── dto/                    # Data Transfer Objects
│   ├── entity/                 # Data Models (JPA Entities)
│   ├── repository/             # Data Access Layer interfaces
│   ├── services/               # Business Logic
│   └── utils/                  # Utility classes (e.g., AccountUtils)
├── src/main/resources/
│   └── application.properties  # Application, database, and email settings
└── pom.xml                     # Maven configuration file
```

---

## Prerequisites

To run the project locally, you need to have the following installed:

1.  **Java Development Kit (JDK) 21** or newer.
2.  **Maven** (Build tool).
3.  A running **MySQL Database Server**.

---

## Setup and Run

### 1. Clone the Repository

```bash
git clone https://github.com/MooRaa86/Sab3-Bank.git
cd Sab3-Bank
```

### 2. Database Setup

Create a new MySQL database for the project.

### 3. Configuration

Modify the `src/main/resources/application.properties` file to set your database connection details and email settings:

```properties
# Database Settings
spring.datasource.url=jdbc:mysql://localhost:3306/your_database_name
spring.datasource.username=your_mysql_username
spring.datasource.password=your_mysql_password
spring.jpa.hibernate.ddl-auto=update # Will automatically create/update tables

# Email Settings (to enable notification service)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email@gmail.com
spring.mail.password=your_email_app_password # Or your application-specific password
spring.mail.properties.mail.smtp.auth= true
spring.mail.properties.mail.smtp.starttls.enable= true
```

### 4. Run the Application

You can run the application using Maven:

```bash
mvn spring-boot:run
```

The application will start on the default port (usually `8080`).

---

## API Documentation (Swagger UI)

Once the application is running, you can access the interactive API documentation via Swagger UI at the following link:

`http://localhost:8080/swagger-ui.html`

This documentation allows you to explore all available endpoints, data models (DTOs), and test the functionality directly.

---

## Contribution

If you wish to contribute to the project, please follow these steps:

1.  Fork the repository.
2.  Create your feature branch (`git checkout -b feature/AmazingFeature`).
3.  Commit your changes (`git commit -m 'Add some AmazingFeature'`).
4.  Push to the branch (`git push origin feature/AmazingFeature`).
5.  Open a Pull Request.

---

## License

This project does not have an explicit license specified in the current repository. Please contact the owner for licensing information.

---

## Contact

This project was developed by:

**Omar Medhat**
*   **Email:** mr.omarmedhat@gmail.com
*   **GitHub:** [MooRaa86](https://github.com/MooRaa86)

---
