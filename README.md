# ADAII – Sports AI Monitoring & Scouting System

A smart sports performance analysis platform built using **Spring Boot**, **FastAPI**, **MQTT**, and **MySQL**.  
The system helps players, coaches, and scouts monitor performance, analyze training sessions using AI, detect risks, and discover talented players.

---

# Features

## Authentication & Security
- JWT Authentication
- Role-Based Authorization
- Secure APIs with Spring Security
- Logout with JWT blacklist

### Supported Roles
- PLAYER
- COACH
- SCOUT
- ADMIN

---

# Player Features
- Create and manage player profile
- Start and end training sessions
- Receive live sensor data
- View session summaries
- Analyze sessions using AI
- View alerts and risk notifications

---

# Coach Features
- Coach dashboard
- Monitor active sessions
- View team players
- Access player session history
- View AI analysis results
- Monitor alerts and injury risks

---

# Scout Features
- Create scout profile
- Browse players
- Search players
- Filter by position/team
- Sort by overall/potential score
- Add/remove players from watchlist
- Scout dashboard

---

# AI Features
The system integrates with a FastAPI AI service to:
- Analyze player performance
- Detect fatigue
- Detect injury risks
- Generate recommendations
- Calculate player overall scores
- Calculate player potential scores

---

# MQTT Integration
Real-time sensor data is received using MQTT.

### Example Metrics
- Heart Rate
- Body Temperature
- Speed
- Acceleration
- Distance
- Player Load
- Sprint Count

---

# Tech Stack

## Backend
- Java 21
- Spring Boot
- Spring Security
- Spring Data JPA
- Hibernate
- Maven
- Python
- FastAPI

## Database
- MySQL

## AI Service
- FastAPI (Python)

## Communication
- MQTT (HiveMQ)

## Documentation
- Swagger / OpenAPI

---

# System Architecture

```text
Player Device/Sensors
        ↓
      MQTT Broker
        ↓
Spring Boot Backend
        ↓
     MySQL Database
        ↓
    FastAPI AI Service
        ↓
Dashboard / Mobile App

```
---
## Project Structure
src/main/java/adaii
│
├── config
├── controller
├── dto
├── entity
├── repository
├── service
├── exception
├── mapper
└── util
---
## Authentication
The system uses JWT Bearer Authentication.
Example: Authorization: Bearer your_jwt_token
---
## API Documentation: http://adaii-app.servebeer.com/api/docs
---
## Example API Flow: 
# Player Flow
Register
Login
Create profile
Create session
Start session
Send sensor data
Analyze session
View summary

---
## AI Analysis Output

Example:

Fatigue Score
Injury Risk
Recommendations
Risk Level
Performance Analysis

---
## Future Improvements
WebSocket real-time dashboard
Docker support
Kubernetes deployment
Unit & Integration Testing
CI/CD Pipeline
Mobile application
Advanced AI models

---

## Developed By
## Nour Fadel

## Backend Developer | Java & Spring Boot Developer

# Skills:
Java
Spring Boot
MySQL
FastAPI
MQTT
REST APIs
AI Integration
