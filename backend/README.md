# Backend - Spring Boot URL Shortener

This is the backend service for the URL Shortener SaaS application.

## Tech Stack

- **Spring Boot 3.2.0**
- **PostgreSQL**
- **JWT Authentication**
- **QR Code Payment Integration**
- **Swagger/OpenAPI Documentation**

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- PostgreSQL 14+

## Quick Start

1. **Configure Database**:
   Update `src/main/resources/application.yml`:
   ```yaml
   spring:
     datasource:
       url: jdbc:postgresql://localhost:5432/urlshortener
       username: YOUR_USERNAME
       password: YOUR_PASSWORD
   ```

2. **Set Environment Variables**:
   ```yaml
   app:
     jwt:
       secret: YOUR_256_BIT_SECRET
     payment:
       upi-id: YOUR_UPI_ID
       merchant-name: YOUR_BUSINESS_NAME
   ```

3. **Build & Run**:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

Server starts at: `http://localhost:8080`

## API Documentation

Swagger UI: `http://localhost:8080/swagger-ui.html`

## Project Structure

```
src/main/java/com/urlshortener/
├── config/          # Configuration classes
├── controller/      # REST API controllers
├── dto/            # Data Transfer Objects
├── entity/         # JPA entities
├── exception/      # Exception handling
├── repository/     # Data access layer
├── security/       # JWT & security
└── service/        # Business logic
```

## Key Features

- JWT-based authentication with BCrypt
- QR code payment with UPI
- Unique short code generation
- Click tracking with analytics
- Global exception handling
- CORS configuration for frontend

## Testing

Access Swagger UI to test all endpoints interactively.

## Deployment

See `../docs/DEPLOYMENT.md` for deployment instructions.
