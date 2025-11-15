# 🚀 Server Management TD

A robust backend REST API for efficient server lifecycle management.

## 🧠 Overview

This project provides a comprehensive backend REST API designed for managing server instances. It enables users to perform fundamental CRUD (Create, Read, Update, Delete) operations on servers, control their operational status (start/stop), and retrieve their current state. Built with **Spring Boot**, it leverages an **in-memory H2 database** for ease of development and data persistence, and features **Swagger UI** for intuitive API exploration and testing.

## ⚙️ Features

This application offers the following core functionalities:

*   **List Servers**: Retrieve a comprehensive list of all registered servers.
*   **Create Server**: Add new server instances, initialized with a `STOPPED` status.
*   **Rename Server**: Update the name of an existing server.
*   **Get Server Status**: Query the current operational status (`RUNNING` or `STOPPED`) of a specific server.
*   **Start Server**: Change a server's status to `RUNNING`.
*   **Stop Server**: Change a server's status to `STOPPED`.
*   **Delete Server**: Remove a server from the system, permissible only if the server is in a `STOPPED` state.
*   **Data Initialization**: Automatically populates the database with sample server data upon application startup for quick testing.
*   **Global Exception Handling**: Provides centralized and consistent error responses for various exceptions (e.g., resource not found, validation errors, illegal state).
*   **API Documentation**: Integrated **Swagger UI** via SpringDoc OpenAPI for interactive API documentation and testing.
*   **In-Memory Database**: Utilizes H2 Database for lightweight, embedded data storage.
*   **Maven Wrapper**: Ensures a consistent build environment across different machines without requiring a pre-installed Maven.

## 🛠 Tech Stack

The project is built using the following technologies:

*   **Language**: Java 17
*   **Framework**: Spring Boot (3.4.11)
    *   Spring Boot Starter Data JPA
    *   Spring Boot Starter Web
    *   Spring Boot Starter Validation
*   **Database**: H2 Database (in-memory)
*   **ORM**: Hibernate (via Spring Data JPA)
*   **API Documentation**: SpringDoc OpenAPI 2.7.0 (for Swagger UI)
*   **Utility**: Project Lombok
*   **Build Tool**: Apache Maven (via Maven Wrapper)
*   **Testing**: JUnit 5, Mockito, Spring Boot Starter Test

## 📂 Project Structure

```
├── .mvn/                               # Maven Wrapper configuration and JAR files
├── src/                                # Main application source code
│   ├── main/
│   │   ├── java/
│   │   │   └── com/td/server_management_td/
│   │   │       ├── config/             # Application-level configurations
│   │   │       │   ├── DataInit.java     # Initializes sample data upon startup
│   │   │       │   └── OpenApiConfig.java# Configures SpringDoc OpenAPI (Swagger UI)
│   │   │       ├── controller/         # REST API controllers and exception handling
│   │   │       │   ├── GlobalExceptionHandler.java # Centralized error handling for API
│   │   │       │   └── ServerController.java # Defines server-related API endpoints
│   │   │       ├── model/              # JPA Entities and Enums representing data structure
│   │   │       │   ├── Server.java       # Server entity definition with validation
│   │   │       │   └── ServerStatus.java # Enum for server operational status (RUNNING, STOPPED)
│   │   │       ├── repository/         # Spring Data JPA interfaces for database operations
│   │   │       │   └── ServerRepository.java # Repository for Server entity
│   │   │       ├── service/            # Business logic interfaces and their implementations
│   │   │       │   ├── ServerService.java      # Interface for server business operations
│   │   │       │   └── ServerServiceImpl.java  # Implementation of ServerService
│   │   │       └── ServerManagementTdApplication.java # Main Spring Boot application entry point
│   │   └── resources/                  # Application resources and properties
│   │       └── application.properties  # Configuration for H2, JPA, and other app settings
│   └── test/                           # Test code for the application
│       └── java/
│           └── com/td/server_management_td/
│               ├── controller/         # Unit tests for API controllers
│               │   └── ServerControllerTest.java
│               ├── repository/         # Integration tests for JPA repositories
│               │   └── ServerRepositoryTest.java
│               ├── service/            # Unit tests for business logic services
│               │   └── ServerServiceImplTest.java
│               └── ServerManagementTdApplicationTests.java # Main Spring Boot context tests
├── .gitattributes                      # Git attribute definitions (e.g., line ending normalization)
├── .gitignore                          # Specifies files and directories to be ignored by Git
├── mvnw                                # Maven Wrapper script for Linux/macOS
├── mvnw.cmd                            # Maven Wrapper script for Windows
└── pom.xml                             # Project Object Model, Maven's build configuration file
```

## 🌐 API Endpoints

The API is exposed at `http://localhost:8080/api/servers` (default port).
Detailed documentation can be found at the **Swagger UI**: `http://localhost:8080/swagger-ui/index.html`.

All error responses follow a consistent JSON format:
```json
{
  "timestamp": "2023-10-27T10:30:00",
  "status": 400,
  "message": "Validation failed: Server name is required"
}
```

Here's a summary of the available endpoints:

| Method | Endpoint                        | Description                                          | Request Body (Example)                                        | Query Params               | Success Response (Status + Body)                                      | Error Responses (Status + Body)                                    |
| :----- | :------------------------------ | :--------------------------------------------------- | :------------------------------------------------------------ | :------------------------- | :-------------------------------------------------------------------- | :----------------------------------------------------------------- |
| GET    | `/api/servers`                  | Retrieve a list of all servers                       | None                                                          | None                       | `200 OK`<br>`List<Server>` (e.g., `[{"id":1, "name":"Web Server 1", ...}]`) | `404 Not Found` (Generic `RuntimeException` catchall)              |
| POST   | `/api/servers`                  | Create a new server with `STOPPED` status            | ```json<br>{ "name": "New Server", "ipAddress": "192.168.1.50" }<br>``` | None                       | `201 CREATED`<br>`Server` object (e.g., `{"id":4, "name":"New Server", ...}`) | `400 Bad Request` (Validation errors from `@NotBlank`)<br>`404 Not Found` (for unique constraint violations via `RuntimeException` catchall) |
| PUT    | `/api/servers/{id}/rename`      | Update the name of an existing server                | None                                                          | `name` (string, required)  | `200 OK`<br>Updated `Server` object                                   | `404 Not Found` (Server not found)                                 |
| GET    | `/api/servers/{id}/status`      | Retrieve the current status of a server              | None                                                          | None                       | `200 OK`<br>`{"status": "RUNNING"}` or `{"status": "STOPPED"}`      | `404 Not Found` (Server not found)                                 |
| PUT    | `/api/servers/{id}/start`       | Change server status to `RUNNING`                    | None                                                          | None                       | `200 OK`<br>Updated `Server` object                                   | `404 Not Found` (Server not found)                                 |
| PUT    | `/api/servers/{id}/stop`        | Change server status to `STOPPED`                    | None                                                          | None                       | `200 OK`<br>Updated `Server` object                                   | `404 Not Found` (Server not found)                                 |
| DELETE | `/api/servers/{id}`             | Delete a server. Only allowed if status is `STOPPED` | None                                                          | None                       | `204 No Content`                                                      | `404 Not Found` (Server not found)<br>`400 Bad Request` (Cannot delete a running server) |

## 🚀 Getting Started

Follow these steps to get the Server Management TD API up and running on your local machine.

### Prerequisites

*   Java Development Kit (JDK) 17 or higher
*   Git

### 1. Clone the repository

```bash
git clone https://github.com/babe051/server-management.git
cd server-management-td
```

### 2. Build the application

This project uses the Maven Wrapper, so you don't need to install Maven globally.

**On Linux/macOS:**
```bash
./mvnw clean install
```

**On Windows:**
```bash
.\mvnw.cmd clean install
```

### 3. Run the application

**On Linux/macOS:**
```bash
./mvnw spring-boot:run
```

**On Windows:**
```bash
.\mvnw.cmd spring-boot:run
```

The application will start on port `8080` by default.

### Access Points

Once the application is running, you can access:
*   **API Endpoints**: `http://localhost:8080/api/servers`
*   **Swagger UI (API Documentation)**: `http://localhost:8080/swagger-ui/index.html`
*   **H2 Console**: `http://localhost:8080/h2-console`
    *   **JDBC URL**: `jdbc:h2:mem:serversdb`
    *   **User Name**: `sa`
    *   **Password**: (leave blank)

The database will be pre-populated with sample server data by `DataInit.java`.

## 🔐 Environment Variables

The Maven Wrapper (`mvnw`, `mvnw.cmd`) supports the following optional environment variables:

| Variable          | Description                                                    | Example                                 |
| :---------------- | :------------------------------------------------------------- | :-------------------------------------- |
| `JAVA_HOME`       | Location of a JDK home directory.                              | `/usr/lib/jvm/java-17-openjdk-amd64`    |
| `MVNW_REPOURL`    | Base repository URL for downloading Maven distribution.        | `https://my.private.repo/maven`         |
| `MVNW_USERNAME`   | Username for authenticating with `MVNW_REPOURL`.                | `myuser`                                |
| `MVNW_PASSWORD`   | Password for authenticating with `MVNW_REPOURL`.               | `mypassword`                            |
| `MVNW_VERBOSE`    | Controls Maven Wrapper logging output.                         | `true` (for verbose) or `debug` (for script trace) |

Application-specific configurations (like H2 database settings) are managed within `src/main/resources/application.properties`.

## 🧪 Testing

The project includes comprehensive unit and integration tests for the controllers, services, and repositories.

### Test Coverage

The project has **28 tests** covering all layers of the application:

*   **ServerServiceImplTest** (11 tests): Unit tests for business logic
    *   List servers
    *   Create server (with default STOPPED status)
    *   Rename server (success and not found scenarios)
    *   Get server status (success and not found scenarios)
    *   Start server
    *   Stop server
    *   Delete server (when stopped, when running, and not found scenarios)

*   **ServerControllerTest** (8 tests): Unit tests for REST API endpoints
    *   GET `/api/servers` - List all servers
    *   POST `/api/servers` - Create server (with validation)
    *   PUT `/api/servers/{id}/rename` - Rename server
    *   GET `/api/servers/{id}/status` - Get server status
    *   PUT `/api/servers/{id}/start` - Start server
    *   PUT `/api/servers/{id}/stop` - Stop server
    *   DELETE `/api/servers/{id}` - Delete server
    *   Validation error handling

*   **ServerRepositoryTest** (8 tests): Integration tests for JPA repository
    *   Save server
    *   Find by ID
    *   Find by name
    *   Find by IP address
    *   Find all servers
    *   Delete server
    *   Update server
    *   Not found scenarios

*   **ServerManagementTdApplicationTests** (1 test): Spring Boot context loading test

### Testing Frameworks

*   **JUnit 5**: Primary testing framework
*   **Mockito**: For mocking dependencies in unit tests
*   **Spring Boot Test**: For integration testing with Spring context
*   **Spring Data JPA Test**: For repository testing with embedded database
*   **MockMvc**: For testing REST controllers

### How to run tests

To execute all tests, use the Maven Wrapper:

**On Linux/macOS:**
```bash
./mvnw test
```

**On Windows:**
```bash
.\mvnw.cmd test
```

All 28 tests should pass successfully. The tests use an in-memory H2 database for integration tests, ensuring no external dependencies are required.

## 🚀 Deployment

This section should be filled in by the project maintainer.

## 🔐 Authentication & Security

This section should be filled in by the project maintainer.

## 📬 Author / Contact

This section should be filled in by the project maintainer.

## 🔖 License

This project is licensed under the Apache License, Version 2.0 for the included Maven Wrapper scripts. For the main application, this section should be filled in by the project maintainer.