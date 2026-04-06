# Finance Dashboard API

> A production-ready RESTful backend service for managing financial transactions within an organisation, featuring JWT authentication, role-based access control, and comprehensive transaction analytics.

**Version:** 1.0.0 | **Status:** Production Ready | **License:** MIT

---

## Table of Contents

1. [Project Overview](#1-project-overview)
2. [Tech Stack](#2-tech-stack)
3. [Project Structure](#3-project-structure)
4. [Prerequisites & Setup](#4-prerequisites--setup)
5. [Building & Running](#5-building--running)
6. [Configuration Guide](#6-configuration-guide)
7. [Database Setup](#7-database-setup)
8. [API Endpoints & Examples](#8-api-endpoints--examples)
9. [Authentication & Authorization](#9-authentication--authorization)
10. [Role Permission Matrix](#10-role-permission-matrix)
11. [Testing](#11-testing)
12. [Deployment](#12-deployment)
13. [Architecture & Design](#13-architecture--design)
14. [Troubleshooting](#14-troubleshooting)
15. [Contributing](#15-contributing)

---

## 1. Project Overview

The **Finance Dashboard API** is a RESTful backend service designed for managing financial transactions within organisations. It provides:

✅ **User Management** — Registration, authentication, role assignment, and account status control  
✅ **Transaction Management** — Create, read, update, delete transactions with filtering and search  
✅ **Role-Based Access Control** — Three-tier permission system (ADMIN, ANALYST, VIEWER)  
✅ **JWT Authentication** — Stateless, token-based security  
✅ **Analytics Dashboard** — Real-time summaries, activity logs, and category/monthly breakdowns  
✅ **Production-Ready** — Error handling, logging, and runtime metrics  

**Use Cases:**
- Internal financial tracking for multi-department organisations
- Transaction auditing and compliance reporting
- Real-time transaction analytics and insights
- Secure, role-based employee access to financial data

---

## 2. Tech Stack

| Layer | Technology | Version |
|---|---|---|
| **Framework** | Spring Boot | 3.5.13 |
| **Language** | Java | 17+ |
| **Database** | PostgreSQL | 14+ |
| **ORM** | Hibernate / JPA | 6.6.45 |
| **Authentication** | JWT (jjwt) | 0.12.6 |
| **Build Tool** | Maven | 3.8+ |
| **Security** | Spring Security | 6.2.17 |
| **API Documentation** | Springdoc OpenAPI | 2.8.6 |
| **Deployment** | Railway.app / Docker | Latest |

---

## 3. Project Structure

```
finance_dashboard/
├── src/
│   ├── main/
│   │   ├── java/zovryn/finance_dashboard/
│   │   │   ├── FinanceDashboardApplication.java      # Application entry point & OpenAPI config
│   │   │   ├── config/
│   │   │   │   └── SecurityConfig.java               # Spring Security & JWT filter configuration
│   │   │   ├── controller/
│   │   │   │   ├── UserController.java               # User registration, login, management endpoints
│   │   │   │   ├── TransactionController.java        # Transaction CRUD & filter endpoints
│   │   │   │   └── DashboardController.java          # Analytics & summary endpoints
│   │   │   ├── service/
│   │   │   │   ├── UserService.java                  # User business logic & validation
│   │   │   │   ├── TransactionService.java           # Transaction operations & filtering
│   │   │   │   ├── DashboardService.java             # Analytics calculations
│   │   │   │   └── JwtService.java                   # Token generation & validation
│   │   │   ├── repo/
│   │   │   │   ├── UserRepo.java                     # User repository (JPA)
│   │   │   │   └── TransactionRepo.java              # Transaction repository with custom queries
│   │   │   ├── model/
│   │   │   │   ├── User.java                         # User entity with role & status fields
│   │   │   │   └── Transaction.java                  # Transaction entity with amount, category, etc.
│   │   │   ├── dto/
│   │   │   │   ├── LoginRequestDTO.java              # User login request payload
│   │   │   │   ├── LoginResponseDTO.java             # JWT token response
│   │   │   │   └── [Other DTOs for requests/responses]
│   │   │   ├── filter/
│   │   │   │   └── JwtAuthFilter.java                # Request filter for JWT token extraction & validation
│   │   │   ├── enums/
│   │   │   │   ├── Role.java                         # Roles: ADMIN, ANALYST, VIEWER
│   │   │   │   ├── Status.java                       # User status: ACTIVE, INACTIVE
│   │   │   │   └── TransactionType.java              # Transaction types: INCOME, EXPENSE
│   │   │   ├── mapper/
│   │   │   │   └── [Entity to DTO mappers]           # DTOs for API responses
│   │   │   ├── exceptions/
│   │   │   │   └── [Custom exception classes]        # Global exception handling
│   │   │   └── util/
│   │   │       └── [Utility classes]                 # Helper functions
│   │   └── resources/
│   │       ├── application.properties                # Default configuration (production)
│   │       ├── application-dev.properties            # Development profile
│   │       ├── application-test.properties           # Test profile
│   │       └── db/migration/                         # Liquibase/Flyway migrations (if used)
│   └── test/
│       ├── java/zovryn/finance_dashboard/
│       │   ├── UserAuthenticationTests.java          # User registration & login tests
│       │   ├── TransactionManagementTests.java       # Transaction CRUD tests
│       │   ├── JwtServiceTests.java                  # JWT token tests
│       │   └── FinanceDashboardApplicationTests.java # Integration tests
│       └── resources/
│           └── application-test.properties           # Test database config
├── pom.xml                                            # Maven configuration & dependencies
├── README.md                                          # This file
├── .gitignore                                         # Git exclusions
└── Procfile                                           # Railway/Heroku deployment config
```

**Key Directories:**
- **`controller/`** — HTTP request handlers for users, transactions, and dashboard
- **`service/`** — Business logic separated from HTTP concerns
- **`repo/`** — Database query layer using Spring Data JPA
- **`model/`** — Entity classes mapped to database tables
- **`config/`** — Spring Security, JWT, and application configuration
- **`filter/`** — JWT authentication filter for request interception

---

## 4. Prerequisites & Setup

### System Requirements

- **Java 17 or higher** — [Download](https://www.oracle.com/java/technologies/downloads/)
- **Maven 3.8+** — [Download](https://maven.apache.org/download.cgi)
- **PostgreSQL 14+** — [Download](https://www.postgresql.org/download/)
- **Git** — [Download](https://git-scm.com/)
- **Postman** (optional, for API testing) — [Download](https://www.postman.com/)

### Initial Setup

#### 1. Clone the Repository
```bash
git clone https://github.com/SiRa111/finance_dashboard.git
cd finance_dashboard
```

#### 2. Verify Java Installation
```bash
java -version
# Output should show Java 17+
# openjdk version "17.0.x"
```

#### 3. Verify Maven Installation
```bash
mvn -version
# Output should show Maven 3.8+
```

#### 4. Verify PostgreSQL Connection
```bash
psql --version
# Output should show PostgreSQL 14+
```

---

## 5. Building & Running

### Build Commands

```bash
# Build the project (runs tests, creates JAR)
mvn clean package

# Skip tests during build (faster)
mvn clean package -DskipTests

# Install dependencies only
mvn install

# Clean build artifacts
mvn clean
```

### Run Commands

#### Option A: Using Maven (Development)
```bash
# Run application with development profile
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"

# Or without profile (uses default)
mvn spring-boot:run
```

#### Option B: Using JAR File (Production)
```bash
# After building with mvn clean package
java -jar target/finance_dashboard-0.0.1-SNAPSHOT.jar

# With custom server port
java -Dserver.port=9090 -jar target/finance_dashboard-0.0.1-SNAPSHOT.jar
```

#### Expected Output
```
2026-04-06T10:30:15.123Z  INFO 1 --- [main] z.f.FinanceDashboardApplication : Starting FinanceDashboardApplication
...
2026-04-06T10:30:20.456Z  INFO 1 --- [main] o.s.b.w.embedded.tomcat.TomcatWebServer : Tomcat initialized with port 8080 (http)
...
2026-04-06T10:30:22.789Z  INFO 1 --- [main] z.f.FinanceDashboardApplication : Started FinanceDashboardApplication in 7.234 seconds
```

The application is ready when you see: **"Started FinanceDashboardApplication in X.XXX seconds"**

---

## 6. Configuration Guide

### Configuration Files

| File | Purpose | Environment |
|---|---|---|
| `application.properties` | Default/production config | Default profile |
| `application-dev.properties` | Development overrides | Dev profile (`dev`) |
| `application-test.properties` | Test-specific config | Test profile (`test`) |

### Default Application Properties

**File:** `src/main/resources/application.properties`

```properties
# Application Name
spring.application.name=finance_dashboard

# Database Configuration
spring.datasource.url=${DATABASE_URL:jdbc:postgresql://localhost:5432/financedb}
spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.username=${DB_USER:postgres}
spring.datasource.password=${DB_PASSWORD:password}

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.open-in-view=false

# JWT Configuration
jwt.secret=${JWT_SECRET:superSecureJWTSecretKeyWith256BitsOfEntropyForHS256AlgorithmToEnsureMaximumSecurityAndCompliance}

# Server Configuration
server.port=${PORT:8080}

# Swagger/OpenAPI Documentation
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.api-docs.path=/v3/api-docs
springdoc.swagger-ui.enabled=true
```

### Environment Variables

Set these environment variables for runtime configuration:

| Variable | Description | Default | Required |
|---|---|---|---|
| `DATABASE_URL` | Full JDBC connection string | `jdbc:postgresql://localhost:5432/financedb` | ✅ (Prod) |
| `DB_USER` | Database username | `postgres` | ❌ |
| `DB_PASSWORD` | Database password | `password` | ❌ |
| `PORT` | Server listening port | `8080` | ❌ |
| `JWT_SECRET` | JWT signing secret (256+ bits recommended) | Default secure key | ❌ |

#### Setting Environment Variables (Linux/Mac)
```bash
export DATABASE_URL="jdbc:postgresql://localhost:5432/financedb"
export DB_USER="postgres"
export DB_PASSWORD="mypassword"
export JWT_SECRET="your-secure-256-bit-secret-key"
export PORT="8080"
```

#### Setting Environment Variables (Windows)
```cmd
set DATABASE_URL=jdbc:postgresql://localhost:5432/financedb
set DB_USER=postgres
set DB_PASSWORD=mypassword
set JWT_SECRET=your-secure-256-bit-secret-key
set PORT=8080
```

### Development Profile Configuration

**File:** `src/main/resources/application-dev.properties`

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/financedb_dev
spring.datasource.username=postgres
spring.datasource.password=password
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update
jwt.secret=dev-secret-key-not-for-production
```

---

## 7. Database Setup

### Step 1: Create PostgreSQL Database

```bash
# Connect to PostgreSQL
psql -U postgres

# Inside psql shell, create database
CREATE DATABASE financedb;

# Optionally, create dedicated user
CREATE USER finance_user WITH PASSWORD 'secure_password';
GRANT ALL PRIVILEGES ON DATABASE financedb TO finance_user;
\q
```

### Step 2: Verify Database Connection

```bash
psql -U postgres -d financedb -c "SELECT 1;"
# Output: 1 (if successful)
```

### Step 3: Automatic Schema Creation

The application automatically creates tables on first run. When `spring.jpa.hibernate.ddl-auto=update`, Hibernate will:

1. Connect to the database
2. Detect the JDBC dialect (PostgreSQL)
3. Create tables based on entity classes:
   - `users` — User accounts with roles and status
   - `transactions` — Financial transaction records

### Step 4: Verify Tables Were Created

```bash
psql -U postgres -d financedb

# Inside psql
\dt  # List all tables

# You should see:
# - public | transactions
# - public | users

# View table structure
\d users;
\d transactions;

\q
```

### Sample Database Schema

**Users Table:**
```sql
CREATE TABLE users (
    user_id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    role VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);
```

**Transactions Table:**
```sql
CREATE TABLE transactions (
    transaction_id BIGSERIAL PRIMARY KEY,
    amount DECIMAL(10, 2) NOT NULL,
    category VARCHAR(255),
    type VARCHAR(50),
    description TEXT,
    date DATE,
    created_by BIGINT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (created_by) REFERENCES users(user_id)
);
```

---

## 8. API Endpoints & Examples

### Base URL
- **Local:** `http://localhost:8080`
- **Production:** `https://financedashboard-production-xxxx.railway.app`

### Swagger/OpenAPI Documentation
**Access the interactive API docs:**
```
http://localhost:8080/swagger-ui.html
```

---

### Authentication & User Management

#### 1. Register a New User
```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john.smith",
    "email": "john@example.com",
    "password": "SecurePass123!"
  }'
```

**Response:**
```json
{
  "message": "User registered successfully",
  "userId": 1,
  "role": "VIEWER"
}
```

#### 2. Login & Get JWT Token
```bash
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john.smith",
    "password": "SecurePass123!"
  }'
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "john.smith",
  "role": "VIEWER"
}
```

#### 3. Get All Users (Admin Only)
```bash
curl -X GET http://localhost:8080/api/users/allUsers \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

#### 4. Update User Role (Admin Only)
```bash
curl -X PATCH http://localhost:8080/api/users/1/role \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <JWT_TOKEN>" \
  -d '{
    "role": "ANALYST"
  }'
```

---

### Transactions

#### 1. Create Transaction (Admin Only)
```bash
curl -X POST http://localhost:8080/api/transactions/add \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <JWT_TOKEN>" \
  -d '{
    "amount": 5000.00,
    "category": "Operations",
    "type": "EXPENSE",
    "description": "Q1 Infrastructure costs",
    "date": "2026-04-06"
  }'
```

#### 2. Get Transaction Details (Admin/Analyst)
```bash
curl -X GET http://localhost:8080/api/transactions/details/1 \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

#### 3. List Transactions with Filters (Admin/Analyst)
```bash
curl -X GET "http://localhost:8080/api/transactions/applyFilters?category=Operations&type=EXPENSE&minAmount=1000" \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

#### 4. Update Transaction (Admin Only)
```bash
curl -X PATCH http://localhost:8080/api/transactions/update/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <JWT_TOKEN>" \
  -d '{
    "amount": 5500.00,
    "description": "Updated Q1 Infrastructure costs"
  }'
```

---

### Dashboard Analytics

#### 1. Get Summary (All Roles)
```bash
curl -X GET http://localhost:8080/api/dashboard/summary \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

**Response:**
```json
{
  "totalTransactions": 45,
  "totalIncome": 150000.00,
  "totalExpense": 85000.00,
  "netAmount": 65000.00
}
```

#### 2. Get Recent Activity (All Roles)
```bash
curl -X GET http://localhost:8080/api/dashboard/recent_activity \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

#### 3. Get Monthly Breakdown (All Roles)
```bash
curl -X GET http://localhost:8080/api/dashboard/monthly_breakdown \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

#### 4. Get Category Breakdown (All Roles)
```bash
curl -X GET http://localhost:8080/api/dashboard/category_breakdown \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

---

## 9. Authentication & Authorization

### JWT Flow

1. **User Registers/Logs In** → Submits credentials
2. **Server Validates** → Checks username & password
3. **Token Issued** → JWT generated with user role
4. **Client Stores Token** → Browser/App local storage
5. **Token Sent on Requests** → `Authorization: Bearer <TOKEN>`
6. **Server Validates Token** → JwtAuthFilter extracts & validates
7. **Request Processed** → Access granted/denied based on role

### JWT Token Structure

```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb2huLnNtaXRoIiwiaWF0IjoxNjE0NTUyNDAwLCJleHAiOjE2MTQ2Mzg4MDB9.TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ
│                                          │                                                   │
│                       Header (Base64)    │                  Payload (Base64)                 │ Signature
│ {"alg":"HS256","typ":"JWT"}             │ {"username":"john.smith","role":"VIEWER"} │ HMAC-SHA256
```

### How to Use JWT Token

```bash
# In Authorization header
curl -H "Authorization: Bearer <TOKEN>" \
  http://localhost:8080/api/users/allUsers
```

---

## 10. Role Permission Matrix

| Feature | ADMIN | ANALYST | VIEWER |
|---|:---:|:---:|:---:|
| **Authentication** |
| Register / Login | ✅ | ✅ | ✅ |
| View own profile | ✅ | ✅ | ✅ |
| **User Management** |
| View all users | ✅ | ❌ | ❌ |
| Manage roles | ✅ | ❌ | ❌ |
| Deactivate users | ✅ | ❌ | ❌ |
| Delete users | ✅ | ❌ | ❌ |
| **Transactions** |
| Create transactions | ✅ | ❌ | ❌ |
| View transaction details | ✅ | ✅ | ❌ |
| List transactions | ✅ | ✅ | ❌ |
| Edit transactions | ✅ | ❌ | ❌ |
| Delete transactions | ✅ | ❌ | ❌ |
| **Dashboard** |
| View summary | ✅ | ✅ | ✅ |
| View recent activity | ✅ | ✅ | ✅ |
| View monthly breakdown | ✅ | ✅ | ✅ |
| View category breakdown | ✅ | ✅ | ✅ |

---

## 11. Testing

### Run All Tests
```bash
mvn test
```

### Run Specific Test Class
```bash
mvn test -Dtest=UserAuthenticationTests
mvn test -Dtest=TransactionManagementTests
mvn test -Dtest=JwtServiceTests
```

### Run Tests with Coverage
```bash
mvn test jacoco:report
# View report at: target/site/jacoco/index.html
```

### Test Classes Included

| Test Class | Coverage |
|---|---|
| `UserAuthenticationTests.java` | User registration, login, JWT validation |
| `TransactionManagementTests.java` | Transaction CRUD, filtering, role checks |
| `JwtServiceTests.java` | Token generation, validation, expiration |
| `FinanceDashboardApplicationTests.java` | Integration tests, context loading |

---

## 12. Deployment

### Deploy to Railway.app

#### 1. Create Railway Project
```bash
# Link to Railway (if not already done)
railway link
```

#### 2. Configure Environment Variables
In Railway dashboard:
- Go to **finance_dashboard** service → **Variables**
- Add these variables:

```
DATABASE_URL = jdbc:postgresql://postgres:PASSWORD@HOST:PORT/DATABASE
JWT_SECRET = your-256-bit-secure-secret-key
PORT = 8080
```

#### 3. Deploy
```bash
git push origin main
# Railway auto-deploys on GitHub push
```

#### 4. Verify Deployment
```bash
# Check deployment status at:
# https://dashboard.railway.app/

# Test API:
curl https://financedashboard-production-xxxx.railway.app/swagger-ui.html
```

### Deploy to Docker

#### 1. Create Dockerfile
```dockerfile
FROM eclipse-temurin:21-jdk-alpine
COPY target/finance_dashboard-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

#### 2. Build & Run Docker Image
```bash
# Build
docker build -t finance-dashboard:latest .

# Run
docker run -p 8080:8080 \
  -e DATABASE_URL="jdbc:postgresql://postgres:password@db:5432/financedb" \
  -e JWT_SECRET="your-secret" \
  finance-dashboard:latest
```

---

## 13. Architecture & Design

### Layered Architecture

```
┌─────────────────────────────────────────────────────┐
│ API Layer (Controllers)                              │
│ - Routing, request validation, response formatting  │
└──────────────────┬──────────────────────────────────┘
                   │
┌──────────────────▼──────────────────────────────────┐
│ Service Layer (Business Logic)                       │
│ - User auth, transaction operations, analytics     │
└──────────────────┬──────────────────────────────────┘
                   │
┌──────────────────▼──────────────────────────────────┐
│ Repository Layer (Data Access)                       │
│ - JPA queries, database interactions               │
└──────────────────┬──────────────────────────────────┘
                   │
┌──────────────────▼──────────────────────────────────┐
│ Database Layer (PostgreSQL)                          │
│ - Tables, indexes, constraints                     │
└─────────────────────────────────────────────────────┘
```

### Design Patterns Used

| Pattern | Usage | Benefit |
|---|---|---|
| **Repository Pattern** | Data access layer | Loose coupling, testability |
| **DTO Pattern** | Request/response mapping | Security, flexibility |
| **Service Layer** | Business logic encapsulation | Reusability, testability |
| **Filter Pattern** | JWT authentication | Centralized security |
| **Role-Based Access Control** | Authorization | Granular permissions |

### Key Design Decisions

| Decision | Reasoning |
|---|---|
| **Public registration** | Self-service onboarding, reduces admin burden |
| **Stateless JWT** | Scalable, no session storage, distributed-friendly |
| **Role-based over attribute-based** | Simpler implementation for 3 roles, faster authorization |
| **Separate dashboard endpoints** | Independent loading, flexible frontend integration |
| **Hard deletes** | Simpler logic, audit trail managed externally |

---

## 14. Troubleshooting

### Common Issues & Solutions

#### Issue: "Connection refused" on database startup

**Cause:** PostgreSQL not running or wrong credentials

**Solution:**
```bash
# Check PostgreSQL status
sudo systemctl status postgresql

# Start PostgreSQL
sudo systemctl start postgresql

# Verify connection
psql -U postgres -d financedb -c "SELECT 1;"
```

#### Issue: "Unable to build Hibernate SessionFactory"

**Cause:** DATABASE_URL not set or wrong format

**Solution:**
```bash
# Verify DATABASE_URL is in JDBC format
echo $DATABASE_URL
# Should output: jdbc:postgresql://localhost:5432/financedb

# If missing, set it:
export DATABASE_URL="jdbc:postgresql://localhost:5432/financedb"
```

#### Issue: "JWT token expired" or "Invalid token"

**Cause:** Token expired or wrong secret key

**Solution:**
```bash
# Generate new token by logging in again
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{"username":"test","password":"test"}'

# Ensure JWT_SECRET matches in all environments
```

#### Issue: "Port 8080 already in use"

**Cause:** Another process on port 8080

**Solution:**
```bash
# Use different port
java -Dserver.port=9090 -jar target/finance_dashboard-0.0.1-SNAPSHOT.jar

# Or kill process on 8080
lsof -i :8080
kill -9 <PID>
```

#### Issue: "403 Forbidden" on endpoint

**Cause:** Insufficient role permissions

**Solution:**
- Verify user role with `/api/users/{userId}`
- Admin must assign appropriate role
- Check SecurityConfig for endpoint requirements

### Debug Logging

Enable debug logs for troubleshooting:

```properties
# In application.properties
logging.level.root=INFO
logging.level.zovryn.finance_dashboard=DEBUG
logging.level.org.springframework.security=DEBUG
logging.level.org.hibernate.SQL=DEBUG
```

---

## 15. Contributing

### Code Standards

- **Language:** Java 17+, follow [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
- **Naming:** Clear, descriptive names for classes, methods, variables
- **Comments:** Javadoc on public methods, explain _why_, not _what_
- **Testing:** Minimum 80% coverage for new code

### Git Workflow

```bash
# Create feature branch
git checkout -b feature/user-profile-updates

# Commit with meaningful messages
git commit -m "feat: add user profile update endpoint"

# Push and create PR
git push origin feature/user-profile-updates
```

### Pull Request Checklist

- [ ] Feature/fix is complete and tested
- [ ] Tests added for new functionality
- [ ] Code follows style guide
- [ ] Documentation updated
- [ ] No breaking changes (or clearly documented)
- [ ] Passes all CI checks

---

## Additional Resources

- **Spring Boot Docs:** https://spring.io/projects/spring-boot
- **Spring Security:** https://spring.io/projects/spring-security
- **JWT Guide:** https://jwt.io/introduction
- **PostgreSQL Docs:** https://www.postgresql.org/docs/
- **Railway Deployment:** https://railway.app/docs

---

## Support & Contact

**Issues & Bug Reports:** [GitHub Issues](https://github.com/SiRa111/finance_dashboard/issues)  
**Documentation:** See `/docs` folder  
**Security Issues:** Please email security@example.com (do not use issues)

---

**Last Updated:** April 6, 2026  
**Maintainer:** Simran  
**License:** MIT
