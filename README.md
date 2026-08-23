# ADAII – AI-Powered Sports Monitoring, Performance Analysis & Scouting Platform

> **Swagger API Documentation:** [http://adaii-app.servebeer.com/api/docs](http://adaii-app.servebeer.com/api/docs)

**ADAII** is an intelligent sports technology platform designed to monitor football players in real time, analyze their physical and performance data using Artificial Intelligence, detect potential injury and fatigue risks, and support coaches and scouts with data-driven decisions.

The platform combines **wearable hardware, MQTT communication, Spring Boot, MySQL, FastAPI, Machine Learning, and a mobile application** into one integrated sports intelligence ecosystem.

---

## Table of Contents

- [Project Overview](#project-overview)
- [The Problem](#the-problem)
- [The Solution](#the-solution)
- [Project Objectives](#project-objectives)
- [Main Features](#main-features)
- [System Architecture](#system-architecture)
- [Data Flow](#data-flow)
- [AI & Machine Learning](#ai--machine-learning)
- [Real-Time Hardware Integration](#real-time-hardware-integration)
- [Backend Architecture](#backend-architecture)
- [Database](#database)
- [API Documentation](#api-documentation)
- [Example End-to-End Flow](#example-end-to-end-flow)
- [Hardware & Integration Challenges](#hardware--integration-challenges)
- [Project Structure](#project-structure)
- [Technology Stack](#technology-stack)
- [Deployment & Running Locally](#deployment--running-locally)
- [Testing](#testing)
- [Future Improvements](#future-improvements)
- [Research Gap](#research-gap)
- [Project Team](#project-team)

---

## Project Overview

Modern football is increasingly becoming a data-driven sport. Traditional player evaluation and monitoring often depend heavily on manual observation, subjective judgment, and delayed detection of physical problems.

ADAII was developed to address these limitations by creating an integrated platform capable of collecting player data continuously, processing it in real time, analyzing it using AI, and presenting meaningful insights to players, coaches, and scouts.

```text
 Wearable Hardware
        │
        ▼
 ESP32 + Sensors
        │
        ▼
 MQTT / HiveMQ Cloud
        │
        ▼
 Spring Boot Backend
        │
        ├──────────────► MySQL Database
        │
        ▼
 FastAPI AI Service
        │
        ▼
 AI Analysis & Recommendations
        │
        ▼
 Mobile Application / Dashboards
        │
        ├── Player
        ├── Coach
        ├── Scout
        └── Admin
```

---

## The Problem

### 1. Injury Prevention
Football players are exposed to injuries caused by excessive workload, accumulated fatigue, and inadequate recovery. Traditional systems fail to monitor physiological and movement indicators in real time. ADAII continuously collects data like heart rate, HRV, speed, and player load to identify abnormal patterns and potential risks.

### 2. Player Performance Analysis
Player evaluation is often subjective ("Player A looked better than Player B"). ADAII provides measurable indicators (speed, distance, sprint activity, consistency) to evaluate performance objectively.

### 3. Player Selection
Coaches need data to decide who starts the next match. ADAII provides performance analysis and scoring mechanisms to compare players using historical data and AI-generated insights.

### 4. Scouting
Traditional scouting requires heavy travel and manual observation. ADAII provides a digital scouting environment to search, compare, and analyze players based on real performance data, reducing manual effort.

---

## The Solution

ADAII provides an integrated ecosystem that connects hardware, backend services, databases, AI, and applications. 

**Core Workflow:**
Collect Data → Transmit Data in Real Time → Validate & Process Data → Store Data → Analyze Data Using AI → Generate Insights → Notify / Recommend → Support Decision Making

---

## Project Objectives

* Monitor football players in real time.
* Collect physiological and movement data.
* Analyze player performance, detect fatigue, and estimate injury risk.
* Generate AI-based recommendations for coaches and scouts.
* Connect physical hardware with cloud/backend services in a scalable architecture.

---

## Main Features

### Authentication & Security
* JWT authentication with role-based authorization.
* JWT blacklist/logout mechanism.
* Supported Roles: **PLAYER, COACH, SCOUT, ADMIN**

### Player Features
* Manage profile and view personal information.
* Start/end training sessions and receive real-time sensor data.
* Analyze training sessions using AI and receive risk notifications.

### Coach Features
* Monitor active training sessions and view real-time player performance.
* View historical sessions, sensor data, and AI analysis.
* Monitor potential injury risks and compare players.

### Scout Features
* Browse, search, and filter players by position, team, or performance.
* Sort by overall/potential score and compare players.
* Add players to a watchlist and access scouting insights.

### Admin Features
* User, team, and device management.
* Device assignment and platform configuration.

---

## AI & Machine Learning

The AI layer is critical to ADAII. The main backend uses **Java/Spring Boot**, while AI processing runs on **Python/FastAPI**. 

```text
Spring Boot  ──(REST API)──►  FastAPI AI Service  ──►  Machine Learning Models
```

### AI Capabilities
* **Injury Risk Prediction:** Analyzes data to generate risk assessments (e.g., `MEDIUM` risk with recommendations to reduce intensity).
* **Fatigue Detection:** Uses HRV, player load, and sprint activity to estimate fatigue.
* **Performance Analysis:** Generates insights on physical performance and consistency.

**Example AI Output:**
```json
{
  "performanceScore": 86,
  "fatigueScore": 72,
  "injuryRisk": 0.31,
  "riskLevel": "MEDIUM",
  "recommendation": "Reduce high-intensity workload and allow additional recovery."
}
```

---

## Real-Time Hardware Integration

ADAII uses an ESP32-based device to collect data from a GPS, Heart Rate Sensor, Motion Sensors, and Temperature Sensor. The ESP32 packages readings into JSON and publishes them to an MQTT broker (HiveMQ Cloud).

### MQTT Topic Structure
The project uses topics like `devices/{deviceUuid}/data`. 

**Example Payload (`devices/ESP32-001/data`):**
```json
{
  "sessionId": 27,
  "timestamp": "2026-06-09T03:45:10",
  "lat": 31.0409,
  "lng": 31.3785,
  "speed_mps": 6.8,
  "distance_m": 1240.5,
  "satellites": 10,
  "accel_x": 1.25,
  "accel_y": 0.42,
  "accel_z": 9.81,
  "sprints": 4,
  "player_load": 72.5,
  "body_temp": 37.2,
  "heart_rate": 151,
  "hrv": 48.2,
  "respiratory_rate": 22
}
```

---

## Backend Architecture

A layered Spring Boot architecture separates concerns:
**Controller Layer → Service Layer → Repository Layer → Database**

### Sensor Data Processing Flow
1. Receive MQTT message via Spring Integration.
2. Extract device UUID from the topic.
3. Deserialize JSON to `SensorDataRequest`.
4. Resolve player/device relationship and active session.
5. Create `SensorData` entity and store in MySQL.
6. Run alert checks and make data available for analysis.

---

## Database

ADAII uses **MySQL** to manage structured relationships between Users, Players, Teams, Sessions, Devices, and Sensor Data. 

Continuous monitoring generates massive amounts of data. Readings are associated hierarchically: 
`Device → Player → Training Session → Sensor Data`

**Example Session Summary:**
```json
{
  "sessionId": 27,
  "avgHeartRate": 148.4,
  "maxHeartRate": 181.0,
  "avgSpeed": 5.72,
  "maxSpeed": 8.91,
  "totalDistance": 6420.5,
  "totalSprints": 17,
  "avgPlayerLoad": 71.4
}
```

---

## Hardware & Integration Challenges

1. **Real-Time Data Streams:** Standard HTTP REST is insufficient for continuous streams. **MQTT** (Publish/Subscribe) is used for lightweight, high-frequency IoT communication.
2. **Device Identification:** Dynamic routing uses the `{deviceUuid}` embedded directly in the MQTT topic string.
3. **Connectivity Issues:** Handled via MQTT QoS, Last Will and Testament (LWT), and auto-reconnects.
4. **Data Volume:** Handled via efficient MySQL indexing and structured data layers.
5. **Timestamp Handling:** Required implementing Jackson JSR-310 datatype support to properly deserialize JSON timestamps into Java `LocalDateTime`.

---

## Project Structure

```text
src/main/java/adaii
│
├── config           # Security, MQTT, and App configs
├── controller       # REST API endpoints
├── dto              # Request/Response objects
├── entity           # JPA Entities
├── repository       # Database interfaces
├── service          # Business logic, MQTT subscriber, Alerts
├── exception        # Global error handling
├── mapper           # Object mapping
└── util             # Helpers and constants
```

---

## Technology Stack

* **Backend:** Java 21, Spring Boot, Spring Security, Spring Data JPA, Hibernate, Maven
* **AI Engine:** Python, FastAPI, Machine Learning Models
* **Database:** MySQL
* **IoT / Communication:** ESP32, MQTT, HiveMQ Cloud, Eclipse Paho, Spring Integration
* **Security:** JWT, Role-Based Access Control, Password Encryption
* **Deployment:** Linux/Ubuntu, Amazon EC2

---

## Deployment & Running Locally

### Prerequisites
* Java 21, Maven, MySQL 8+, Python 3.10+, Git
* MQTT broker or HiveMQ Cloud account

### 1. Configure MySQL
```sql
CREATE DATABASE adaii;
```
Update `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/adaii
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
```

### 2. Configure MQTT
Update `application.properties`:
```properties
mqtt.broker-url=ssl://YOUR_HIVEMQ_HOST:8883
mqtt.client-id=Python_Backend_Server
mqtt.username=YOUR_USERNAME
mqtt.password=YOUR_PASSWORD
mqtt.topic=devices/+/data
```

### 3. Build & Run Spring Boot
```bash
mvn clean package
java -jar target/sports-ai-system-0.0.1-SNAPSHOT.jar
```

### 4. Run FastAPI AI Service
```bash
pip install -r requirements.txt
uvicorn main:app --host 0.0.0.0 --port 8000
```

---

## Future Improvements

* **Real-Time WebSocket Dashboard:** Transition from API polling to WebSockets for smoother real-time UI updates.
* **Docker & Kubernetes:** Containerize the full stack for easier deployment and scaling.
* **CI/CD Pipelines:** Automate testing and deployment via GitHub Actions.
* **Advanced AI:** Implement Deep Learning, Time-series models, and advanced tactical mapping.
* **Mobile App Expansion:** Add heatmaps, player similarity charts, and tactical analysis features.

---

---

> **ADAII** - Transforming raw athlete data into intelligent sports decisions.
> Monitor → Analyze → Predict → Recommend → Improve
