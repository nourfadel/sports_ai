# ADAII – AI-Powered Sports Monitoring, Performance Analysis & Scouting Platform

> **ADAII** is an intelligent sports technology platform designed to monitor football players in real time, analyze their physical and performance data using Artificial Intelligence, detect potential injury and fatigue risks, and support coaches and scouts with data-driven decisions.

ADAII combines **wearable hardware, MQTT communication, Spring Boot, MySQL, FastAPI, Machine Learning, and a mobile application** into one integrated sports intelligence ecosystem.

The platform is designed to move from traditional subjective player evaluation toward **continuous monitoring, real-time analytics, AI-assisted decision making, and intelligent scouting**.

---

# Table of Contents

* [Project Overview](#project-overview)
* [The Problem](#the-problem)
* [The Solution](#the-solution)
* [Main Features](#main-features)
* [System Architecture](#system-architecture)
* [Data Flow](#data-flow)
* [User Roles](#user-roles)
* [Player Features](#player-features)
* [Coach Features](#coach-features)
* [Scout Features](#scout-features)
* [AI & Machine Learning](#ai--machine-learning)
* [Real-Time Hardware Integration](#real-time-hardware-integration)
* [MQTT Communication](#mqtt-communication)
* [Backend Architecture](#backend-architecture)
* [Authentication & Security](#authentication--security)
* [Player Scoring](#player-scoring)
* [Database](#database)
* [API Documentation](#api-documentation)
* [Example End-to-End Flow](#example-end-to-end-flow)
* [Example MQTT Payload](#example-mqtt-payload)
* [Example API Requests](#example-api-requests)
* [Project Structure](#project-structure)
* [Technology Stack](#technology-stack)
* [Hardware & Integration Challenges](#hardware--integration-challenges)
* [Deployment](#deployment)
* [Running the Project Locally](#running-the-project-locally)
* [Testing](#testing)
* [Future Improvements](#future-improvements)
* [Project Team](#project-team)

---

# Project Overview

Modern football is increasingly becoming data-driven.

Traditional player evaluation often depends heavily on:

* Manual observation
* Coach experience
* Subjective judgment
* Limited performance statistics
* Delayed detection of physical problems
* Time-consuming scouting processes

ADAII was developed to address these limitations by creating an integrated platform capable of collecting player data continuously, processing it in real time, analyzing it using AI, and presenting meaningful insights to players, coaches, and scouts.

The platform combines:

```text
Wearable Hardware
        ↓
     MQTT / HiveMQ
        ↓
Spring Boot Backend
        ↓
     MySQL
        ↓
   FastAPI / AI Layer
        ↓
Mobile Application
        ↓
Players / Coaches / Scouts
```

---

# The Problem

ADAII was designed around three major problems in football.

## 1. Injury Prevention

Many physical problems do not appear suddenly. They can be preceded by changes in physiological and movement indicators such as:

* Heart rate
* Body temperature
* Player load
* Acceleration
* Speed
* Fatigue indicators

Without continuous monitoring, identifying these warning signs early can be difficult.

ADAII continuously collects player data and generates alerts when abnormal or dangerous conditions are detected.

---

## 2. Objective Performance Evaluation

Player performance is often evaluated using human observation and traditional statistics.

ADAII introduces data-driven performance analysis using measurements such as:

* Speed
* Distance
* Sprint count
* Heart rate
* Acceleration
* Player load
* Session statistics

This enables objective performance tracking across training sessions.

---

## 3. Intelligent Scouting

Scouts often need to manually observe many players before deciding which players are suitable for a team.

ADAII helps scouts by providing:

* Player search
* Filtering
* Sorting
* Player profiles
* Player comparison
* Watchlists
* Performance scores
* Potential scores
* AI-generated insights

This reduces the time required to identify suitable players.

---

# The Solution

ADAII provides a unified platform where:

1. Wearable devices collect player data.
2. MQTT transfers the data in real time.
3. Spring Boot receives and processes the data.
4. MySQL stores historical information.
5. FastAPI provides AI-based analysis.
6. The backend calculates performance and potential scores.
7. Alerts are generated when abnormal conditions are detected.
8. Mobile dashboards present the results to the appropriate users.

---

# Main Features

## Authentication & Security

* JWT authentication
* Role-based authorization
* Spring Security
* BCrypt password hashing
* Protected REST APIs
* JWT blacklist-based logout
* Global validation and exception handling
* Swagger/OpenAPI security integration

### Supported Roles

* `PLAYER`
* `COACH`
* `SCOUT`
* `ADMIN`

---

# Player Features

Players can:

* Register and authenticate
* Create their player profile
* Update their profile
* Create training sessions
* Start training sessions
* End training sessions
* Receive live sensor data
* View session history
* View session summaries
* Receive alerts
* Request AI analysis
* View AI-generated session results
* Monitor performance over time

---

# Coach Features

Coaches can:

* Create and manage coach profiles
* View team players
* Monitor player sessions
* Access player session history
* View live session data
* View session sensor history
* View alerts
* View AI analysis
* Access coach dashboards
* Monitor player performance and risk indicators

---

# Scout Features

Scouts can:

* Create scout profiles
* Browse players
* Search players
* Filter players
* Sort players
* View detailed player profiles
* Compare players
* Add players to watchlists
* Remove players from watchlists
* View overall scores
* View potential scores
* Access scout dashboards

---

# AI & Machine Learning

ADAII uses a separate **FastAPI-based AI service** rather than placing the machine learning logic directly inside the Java backend.

This separation provides a clean architecture where:

```text
Spring Boot
   ↓
AI Request
   ↓
FastAPI
   ↓
Machine Learning Model
   ↓
Analysis Result
   ↓
Spring Boot
```

## AI Responsibilities

The AI layer is designed to support:

* Performance analysis
* Fatigue analysis
* Injury risk prediction
* Risk classification
* Player recommendations
* Performance insights

The separation between backend and AI service also makes it easier to replace or improve the machine learning model without rewriting the main Java backend.

---

# Real-Time Hardware Integration

One of the main components of ADAII is the wearable sports device.

The hardware is responsible for collecting real-time player data such as:

* Heart rate
* Body temperature
* GPS latitude
* GPS longitude
* Speed
* Distance
* Acceleration on X/Y/Z axes
* Sprint count
* Player load
* HRV
* Respiratory rate
* Satellite information

The device sends the collected measurements as structured JSON messages.

---

# MQTT Communication

ADAII uses **MQTT** as the communication protocol between wearable devices and the backend.

The MQTT broker is hosted using **HiveMQ Cloud**.

### Why MQTT?

MQTT is suitable for the project because it is:

* Lightweight
* Efficient for IoT devices
* Designed for low-bandwidth communication
* Suitable for continuous sensor streams
* Event-driven
* Well suited for real-time device communication

### Topic Pattern

The backend subscribes using a wildcard topic:

```text
devices/+/data
```

A device can publish to:

```text
devices/ESP32-001/data
```

This allows the backend to dynamically receive data from multiple devices.

---

# Example MQTT Message

```json
{
  "sessionId": 27,
  "playerProfileId": 7,
  "timestamp": "2026-06-09T04:30:00",
  "lat": 30.0444,
  "lng": 31.2357,
  "speed_mps": 5.4,
  "distance_m": 120.5,
  "satellites": 8,
  "accel_x": 0.22,
  "accel_y": 0.33,
  "accel_z": 0.11,
  "sprints": 2,
  "player_load": 14.6,
  "body_temp": 36.7,
  "heart_rate": 92,
  "hrv": 48,
  "respiratory_rate": 17
}
```

The device UUID is also extracted from the MQTT topic:

```text
devices/{deviceUuid}/data
```

For example:

```text
devices/ESP32-001/data
```

---

# Backend Architecture

The backend follows a layered architecture:

```text
Controller Layer
       ↓
Service Layer
       ↓
Repository Layer
       ↓
MySQL Database
```

External integrations are separated into dedicated components:

```text
Spring Boot Backend
 ├── REST APIs
 ├── Spring Security / JWT
 ├── MQTT Integration
 ├── Business Services
 ├── JPA / Hibernate
 ├── MySQL
 └── FastAPI AI Integration
```

This separation improves:

* Maintainability
* Scalability
* Testability
* Code organization
* Separation of concerns

---

# Real-Time Sensor Processing Flow

When a sensor message arrives:

```text
1. Wearable Device
        ↓
2. HiveMQ Cloud
        ↓
3. MQTT Subscriber
        ↓
4. JSON Deserialization
        ↓
5. SensorDataRequest
        ↓
6. Session & Player Validation
        ↓
7. SensorData Entity
        ↓
8. MySQL
        ↓
9. Alert Analysis
```

The system stores the reading and then checks whether the values require an alert.

---

# Authentication & Security

ADAII uses **Spring Security + JWT**.

Authentication flow:

```text
User Login
    ↓
Spring Security Authentication
    ↓
JWT Generation
    ↓
Client Stores Token
    ↓
Authorization Header
    ↓
JWT Filter
    ↓
Role Validation
    ↓
Protected Endpoint
```

Example:

```http
Authorization: Bearer <JWT_TOKEN>
```

JWT is used to protect APIs and ensure that each role can only access the resources allowed for it.

---

# Player Scoring

ADAII provides two important player metrics:

## Overall Score

The `overallScore` represents the player's current performance level based on available performance and session data.

It can consider indicators such as:

* Speed
* Distance
* Sprint activity
* Player load
* Heart rate
* Session analysis

---

## Potential Score

The `potentialScore` represents the player's estimated future potential based on the current performance profile and additional player characteristics.

These scores are particularly useful for the scouting module.

They allow scouts to sort and compare players based on measurable performance indicators instead of relying only on subjective observation.

---

# Database

ADAII uses **MySQL** as the primary relational database.

The database stores information related to:

* Users
* Player profiles
* Coach profiles
* Scout profiles
* Teams
* Training sessions
* Sensor data
* Alerts
* AI analysis
* Watchlists
* Blacklisted JWT tokens

### Why MySQL?

MySQL was selected because the project contains strongly related entities and requires:

* Relational data modeling
* Foreign-key relationships
* Reliable transactions
* Efficient CRUD operations
* Indexing
* Structured historical data storage
* Easy integration with Spring Data JPA and Hibernate

For example:

```text
User
  ↓
PlayerProfile
  ↓
TrainingSession
  ↓
SensorData
  ↓
Alerts / AI Analysis
```

---

# API Documentation

ADAII provides interactive API documentation using **Swagger / OpenAPI**.

### Swagger UI

```text
http://adaii-app.servebeer.com/api/docs
```

The documentation includes:

* API endpoints
* Request bodies
* Response models
* Parameters
* HTTP response codes
* Authentication requirements
* Role-based protected endpoints

---

# Example End-to-End Flow

## Player Workflow

```text
1. Register
        ↓
2. Login
        ↓
3. Create Player Profile
        ↓
4. Create Training Session
        ↓
5. Start Session
        ↓
6. Wearable Device Sends MQTT Data
        ↓
7. Backend Receives Sensor Data
        ↓
8. Data Stored in MySQL
        ↓
9. Alerts Generated if Needed
        ↓
10. View Live Data
        ↓
11. End Session
        ↓
12. Generate AI Analysis
        ↓
13. View Session Summary & AI Results
```

---

# Example API Requests

## Register

```http
POST /api/sports/auth/register
```

Example:

```json
{
  "firstName": "Nour",
  "lastName": "Fadel",
  "email": "player@example.com",
  "password": "StrongPassword123",
  "role": "PLAYER"
}
```

---

## Login

```http
POST /api/sports/auth/login
```

```json
{
  "email": "player@example.com",
  "password": "StrongPassword123"
}
```

Use the returned token in Swagger's **Authorize** button.

---

## Create Session

```http
POST /api/sessions/create-session
```

The authenticated player creates a new training session.

---

## Start Session

```http
PUT /api/sessions/{sessionId}/start
```

Example:

```text
PUT /api/sessions/27/start
```

---

## Get Live Data

```http
GET /api/sessions/{sessionId}/live
```

Example:

```text
GET /api/sessions/27/live
```

Possible response:

```json
{
  "status": "SUCCESS",
  "message": "Live sensor data fetched successfully",
  "data": {
    "sessionId": 27,
    "deviceUuid": "ESP32-001",
    "heartRate": 92,
    "speed": 5.4,
    "distance": 120.5,
    "playerLoad": 14.6,
    "bodyTemperature": 36.7
  }
}
```

---

## Get Session Sensor Data

```http
GET /api/sessions/{sessionId}/sensor-data
```

---

## Get Session Summary

```http
GET /api/sessions/{sessionId}/summary
```

The summary can provide values such as:

* Average heart rate
* Maximum heart rate
* Average speed
* Maximum speed
* Total distance
* Total sprints
* Average player load
* Maximum impact force
* Average body temperature
* Number of sensor readings

---

## Analyze Session

```http
POST /api/sessions/{sessionId}/analyze
```

The backend sends session data to the FastAPI AI service and stores the returned analysis.

---

## Get AI Analysis

```http
GET /api/sessions/{sessionId}/analysis
```

---

# Scout API Examples

## Browse Players

```http
GET /api/scouts/players
```

---

## Search Players

```http
GET /api/scouts/players?search=nour
```

---

## Filter Players

```http
GET /api/scouts/players?position=FORWARD
```

---

## Sort by Overall Score

```http
GET /api/scouts/players?sortBy=overallScore&direction=desc
```

---

## Compare Players

```http
GET /api/scouts/compare?player1Id=1&player2Id=2
```

---

## Add Player to Watchlist

```http
POST /api/scouts/watchlist/{playerProfileId}
```

---

## Get Watchlist

```http
GET /api/scouts/watchlist
```

---

# Alert System

ADAII monitors incoming sensor measurements and can generate alerts for abnormal conditions.

Examples of monitored indicators include:

* High heart rate
* High body temperature
* High physical load
* Fatigue-related indicators
* Other risk conditions generated by the analysis layer

Alerts can then be accessed by authorized users such as players and coaches.

---

# Hardware & Integration Challenges

Integrating wearable hardware with a cloud-based backend introduced several practical challenges.

## 1. Real-Time Communication

Sensor data must be continuously transmitted without depending on traditional request/response communication.

**Solution:** MQTT was used as a lightweight publish/subscribe communication protocol.

---

## 2. Secure MQTT Communication

The system communicates with HiveMQ Cloud using TLS-secured MQTT communication.

The backend uses the secure MQTT connection on port:

```text
8883
```

The browser-based MQTT WebSocket client uses the WebSocket endpoint instead.

---

## 3. Dynamic Device Topics

Multiple devices need to publish data without requiring a separate subscription for each device.

**Solution:**

```text
devices/+/data
```

The `+` wildcard allows the backend to receive data from multiple devices dynamically.

---

## 4. Sensor Data Format

Hardware data must be converted into a format that can be understood reliably by the backend.

ADAII uses structured JSON messages with clearly defined fields.

---

## 5. Timestamp Serialization

Sensor data contains timestamps that must be converted correctly between JSON and Java `LocalDateTime`.

This required correct Jackson Java Time support using:

```text
jackson-datatype-jsr310
```

---

## 6. Internet Connectivity

Wearable devices depend on wireless connectivity to communicate with the MQTT broker.

Real-world hardware environments can introduce:

* Connection interruptions
* Network instability
* Delayed packets
* Device reconnection requirements

The MQTT client therefore supports automatic reconnect behavior.

---

## 7. High-Frequency Sensor Data

Wearable devices can continuously generate readings.

The backend must process and store these readings efficiently while still providing real-time access to the latest values.

---

# Deployment

The backend is deployed on **Amazon EC2**.

High-level deployment architecture:

```text
Internet
   ↓
Nginx
   ↓
Amazon EC2
   ↓
Spring Boot Application
   ↓
MySQL
   ↓
HiveMQ Cloud
   ↓
FastAPI AI Service
```

The application is packaged as a Spring Boot executable JAR.

Example:

```bash
java -jar sports-ai-system-0.0.1-SNAPSHOT.jar
```

The deployed backend is accessible through:

```text
http://adaii-app.servebeer.com
```

Swagger:

```text
http://adaii-app.servebeer.com/api/docs
```

---

# Running the Project Locally

## Requirements

Install:

* Java 21
* Maven
* MySQL 8+
* Python 3+
* FastAPI
* MQTT broker access

---

## 1. Clone the Repository

```bash
git clone <YOUR_GITHUB_REPOSITORY>
cd sports-ai-system
```

---

## 2. Configure MySQL

Create a database:

```sql
CREATE DATABASE adaii;
```

Configure:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/adaii
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
```

---

## 3. Configure JWT

Example:

```properties
jwt.secret=YOUR_SECRET
jwt.expiration=3600000
```

Do not commit real production secrets to GitHub.

---

## 4. Configure MQTT

Example:

```properties
mqtt.broker-url=ssl://YOUR_HIVEMQ_HOST:8883
mqtt.client-id=sports-ai-backend
mqtt.username=YOUR_USERNAME
mqtt.password=YOUR_PASSWORD
mqtt.topic=devices/+/data
```

---

## 5. Run the Spring Boot Backend

```bash
mvn spring-boot:run
```

or:

```bash
mvn clean package -DskipTests
java -jar target/sports-ai-system-0.0.1-SNAPSHOT.jar
```

---

## 6. Run the FastAPI AI Service

Example:

```bash
uvicorn main:app --reload
```

The exact command depends on the AI service entry point.

---

# Environment Variables & Security

Sensitive configuration should not be stored directly in the Git repository.

Recommended production configuration includes:

```text
DATABASE_USERNAME
DATABASE_PASSWORD
JWT_SECRET
MQTT_USERNAME
MQTT_PASSWORD
MAIL_USERNAME
MAIL_PASSWORD
AI_SERVICE_URL
```

For production deployments, use environment variables or a secret-management solution.

---

# Testing Strategy

ADAII can be tested through:

## Swagger

Use:

```text
http://adaii-app.servebeer.com/api/docs
```

Swagger can be used to test:

* Authentication
* Player APIs
* Coach APIs
* Scout APIs
* Session APIs
* Sensor APIs
* Alert APIs
* AI analysis APIs

---

## MQTT Testing

HiveMQ's WebSocket Client or a real wearable device can be used to publish sensor data.

Example:

```text
Topic:
devices/test-device/data
```

Then verify the complete pipeline:

```text
MQTT Publish
   ↓
Spring Boot Subscriber
   ↓
SensorData Processing
   ↓
MySQL
   ↓
GET /api/sessions/{id}/live
```

---

# Project Structure

```text
sports-ai-system/
│
├── src/
│   └── main/
│       ├── java/
│       │   └── adaii/
│       │       ├── config/
│       │       ├── controller/
│       │       ├── dto/
│       │       ├── entity/
│       │       ├── exception/
│       │       ├── mapper/
│       │       ├── repository/
│       │       ├── service/
│       │       └── util/
│       │
│       └── resources/
│           └── application.properties
│
├── pom.xml
└── README.md
```

---

# Technology Stack

## Backend

* Java 21
* Spring Boot
* Spring MVC
* Spring Security
* JWT
* Spring Data JPA
* Hibernate
* Maven

## AI

* Python
* FastAPI
* Machine Learning

## Database

* MySQL

## IoT / Communication

* ESP32 / Wearable Sensors
* MQTT
* HiveMQ Cloud

## API Documentation

* Swagger
* OpenAPI

## Deployment

* Amazon EC2
* Nginx

---

# Why These Technologies?

## Why Spring Boot?

Spring Boot was selected because ADAII requires a scalable backend capable of handling:

* REST APIs
* Real-time data processing
* Security
* Database integration
* MQTT communication
* AI service integration

It also provides a clean enterprise architecture through dependency injection, layered services, Spring Security, and Spring Data JPA.

---

## Why MySQL?

MySQL is suitable because ADAII contains strongly related entities such as:

```text
Users
Players
Teams
Sessions
Sensor Data
Alerts
AI Analysis
```

A relational database provides:

* Structured relationships
* Referential integrity
* Reliable transactions
* Efficient querying
* Indexing
* Easy integration with Hibernate/JPA

---

## Why MQTT?

MQTT was selected because wearable devices continuously send small sensor messages.

MQTT provides:

* Low overhead
* Publish/subscribe communication
* Real-time delivery
* Lightweight device communication
* Efficient communication for IoT systems

---

## Why FastAPI?

The AI system is implemented as a separate FastAPI service because machine learning workloads are easier to develop and maintain in Python.

This architecture allows:

* Python-based ML libraries
* Independent AI model development
* Easy model replacement
* Independent deployment
* Clear separation between business logic and AI processing

---

# Example Architecture

```text
                ┌─────────────────────┐
                │   Wearable / ESP32  │
                │ Sensors + GPS       │
                └──────────┬──────────┘
                           │
                           │ MQTT
                           ▼
                ┌─────────────────────┐
                │    HiveMQ Cloud      │
                │     MQTT Broker      │
                └──────────┬──────────┘
                           │
                           ▼
        ┌──────────────────────────────────┐
        │      Spring Boot Backend          │
        │                                  │
        │  Security / REST APIs            │
        │  Session Management              │
        │  Sensor Processing               │
        │  Alerts                          │
        │  Scouting                        │
        └─────────────┬───────────┬────────┘
                      │           │
                      │           │
                      ▼           ▼
             ┌────────────┐   ┌──────────────┐
             │   MySQL    │   │   FastAPI    │
             │  Database  │   │  AI Service  │
             └────────────┘   └──────────────┘
                      │           │
                      └─────┬─────┘
                            ▼
                     Mobile Application
                            │
                ┌───────────┼───────────┐
                ▼           ▼           ▼
             Player      Coach       Scout
```

---

# Future Improvements

Possible future enhancements include:

* WebSocket-based live dashboards
* Advanced real-time visualization
* Docker containerization
* CI/CD pipelines
* Kubernetes deployment
* More advanced machine learning models
* Improved model personalization
* Automated model retraining
* Advanced analytics and reporting
* Expanded hardware support
* Offline device buffering and synchronization

---

# Project Team

## ADAII Team

* **Nour El-Din Fadel Ibrahim El-Metwally**
* **Mazen El-Sayed Shabara**
* **Mostafa Rizk Labib Ghazy**
* **Ahmed Sabry Hegazy**
* **Mohamed Sameh El-Sayed**
* **Soha Kassab Abdel-Ghany**
* **Ahmed Ateya Elagharably**

### Supervisors

* **Dr. Nesma Ibrahim Hassanein**
* **Dr. Wael Awad**

---

# Developed With

```text
Java • Spring Boot • Spring Security • MySQL
FastAPI • Python • MQTT • HiveMQ
ESP32 • REST APIs • JWT • AI/ML
Amazon EC2 • Nginx • Swagger/OpenAPI
```

---

# Project Goal

ADAII aims to transform sports data into actionable intelligence by connecting **wearable devices, real-time communication, backend processing, artificial intelligence, and intelligent scouting** within one unified platform.

> **Monitor smarter. Analyze deeper. Prevent risks. Discover talent.**
