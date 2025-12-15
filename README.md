# 🚗 Car Reservation System

A modern, distributed car rental management system showcasing multiple integration technologies in a Service-Oriented Architecture (SOA).

## ✨ Features
- **Car Management** – Full CRUD operations via REST API
- **Booking System** – Reservation management with validation
- **SOAP Service** – Insurance premium calculation
- **RMI Service** – Dynamic discount calculations  
- **JMS Messaging** – Real-time notifications via ActiveMQ
- **Python Microservices** – Statistics dashboard (Flask) and tax service (CORBA)

## 🏗️ Tech Stack

| Component | Technology |
|-----------|------------|
| **Backend** | Spring Boot (REST, JPA, SOAP) |
| **Frontend** | JavaFX Desktop Application |
| **Database** | MySQL 8.0 |
| **Messaging** | ActiveMQ (JMS) |
| **Services** | Python Flask, CORBA |

## 📦 Prerequisites

- **Java 17+**
- **Maven 3.8+**
- **MySQL 8.0+**
- **Python 3.8+**
- **ActiveMQ 5.17+**

## 📁 Project Structure

```
car-reservation/
├── backend/           # Spring Boot application
├── frontend/          # JavaFX client
├── microservice-flask/# Statistics service (Python)
├── corba-server/      # Tax service (Python + CORBA)
└── README.md
```
| Database connection error | Verify MySQL credentials |

