# E-Commerce Microservices Platform

A full-stack e-commerce platform built with a **microservices architecture** using Spring Boot, Spring Cloud, Apache Kafka, and a React + TypeScript frontend.

---

## Table of Contents

- [Overview](#overview)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Microservices Overview](#microservices-overview)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
  - [1. Infrastructure Setup](#1-infrastructure-setup)
  - [2. Backend Services](#2-backend-services)
  - [3. Frontend](#3-frontend)
- [API Endpoints](#api-endpoints)
- [Frontend Features](#frontend-features)
- [Docker Deployment](#docker-deployment)
- [Kubernetes Deployment](#kubernetes-deployment)
- [Architecture](#architecture)

---

## Overview

This platform provides a complete e-commerce experience with three user portals:

- **Customer Portal** — Browse products, manage cart, place orders, write reviews
- **Seller Portal** — Manage product listings, view and fulfill orders
- **Admin Portal** — Platform administration, manage sellers and coupons

The backend is composed of **12 independently deployable microservices** communicating via REST and Apache Kafka, with service discovery through Netflix Eureka and centralized routing through Spring Cloud Gateway.

---

## Tech Stack

### Backend

| Technology               | Purpose                              |
|--------------------------|--------------------------------------|
| Java 17                  | Programming language                 |
| Spring Boot 3            | Microservice framework               |
| Spring Cloud Gateway     | API Gateway (reactive)               |
| Spring Cloud Netflix Eureka | Service discovery                 |
| Spring Data JPA          | ORM / Data access                    |
| Spring Security          | Authentication & authorization       |
| Apache Kafka             | Asynchronous event messaging         |
| MySQL 8.0                | Relational database                  |
| Resilience4j             | Circuit breaker, retry, time limiter |
| Zipkin                   | Distributed tracing                  |
| JWT (jjwt)               | Token-based authentication           |
| Lombok                   | Boilerplate reduction                |
| Maven                    | Build & dependency management        |

### Frontend

| Technology               | Purpose                              |
|--------------------------|--------------------------------------|
| React 18                 | UI framework                         |
| TypeScript               | Type-safe JavaScript                 |
| Vite                     | Build tool & dev server              |
| Material UI (MUI) v6     | Component library                    |
| Redux Toolkit            | State management                     |
| React Router DOM v7      | Client-side routing                  |
| Axios                    | HTTP client                          |
| TailwindCSS              | Utility-first CSS                    |
| Formik + Yup             | Form handling & validation           |
| Styled Components        | CSS-in-JS styling                    |

### Infrastructure

| Technology               | Purpose                              |
|--------------------------|--------------------------------------|
| Docker                   | Containerization                     |
| Kubernetes               | Container orchestration              |
| Nginx                    | Frontend reverse proxy               |
| Apache Zookeeper         | Kafka coordination                   |

---

## Project Structure

```
microservices-Ecommerce/
├── ecommerce-microservice/          # Backend microservices
│   ├── api-gateway/                 # API Gateway (port 9000)
│   ├── service-registry/            # Eureka Server (port 8761)
│   ├── user-service/                # User management (port 8082)
│   ├── product-service/             # Product catalog (port 8081)
│   ├── cart-service/                # Shopping cart (port 8083)
│   ├── seller-service/              # Seller management (port 8084)
│   ├── order-service/               # Order management (port 8085)
│   ├── seller-order-service/        # Seller order mgmt (port 8086)
│   ├── coupon-service/              # Coupons (port 8087)
│   ├── payment-service/             # Payments (port 8088)
│   ├── review-service/              # Reviews (port 8089)
│   ├── email-service/               # Email notifications (port 9001)
│   ├── deal-service/                # Deals (in progress)
│   ├── docker-compose.yml           # Kafka + Zookeeper setup
│   ├── namespace.yml                # K8s namespace
│   ├── kafka.yml                    # K8s Kafka deployment
│   ├── zookeeper.yml                # K8s Zookeeper deployment
│   ├── mysql.yaml                   # K8s MySQL deployment
│   ├── mysql-pv.yml                 # K8s MySQL PersistentVolume
│   └── mysql-pvc.yml                # K8s MySQL PersistentVolumeClaim
│
└── ecommerce-frontend/              # React frontend application
    ├── src/
    │   ├── customer/                # Customer-facing pages & components
    │   ├── seller/                  # Seller dashboard pages & components
    │   ├── admin/                   # Admin dashboard pages & components
    │   ├── State/                   # Redux store, slices, and thunks
    │   ├── component/               # Shared components
    │   ├── Routes/                  # Route definitions
    │   ├── Theme/                   # MUI theme customization
    │   ├── config/                  # App configuration
    │   ├── types/                   # TypeScript type definitions
    │   ├── data/                    # Static data (categories, etc.)
    │   └── Util/                    # Utility functions
    ├── Dockerfile                   # Multi-stage Docker build
    ├── nginx.conf                   # Nginx reverse proxy config
    ├── package.json
    ├── tailwind.config.js
    ├── tsconfig.json
    └── vite.config.ts
```

Each backend microservice follows this internal structure:

```
service-name/
├── src/main/java/com/service_name/
│   ├── ServiceApplication.java      # Spring Boot entry point
│   ├── controller/                  # REST controllers
│   ├── service/                     # Business logic
│   │   └── impl/                    # Service implementations
│   ├── entity/                      # JPA entities
│   ├── repo/                        # Spring Data repositories
│   ├── dto/                         # Data transfer objects
│   ├── request/                     # Request models
│   ├── response/                    # Response models
│   ├── domain/ or enums/            # Enums and domain constants
│   ├── clients/                     # Feign/WebClient inter-service clients
│   ├── config/                      # Configuration classes
│   ├── kafka/                       # Kafka producers/consumers
│   └── exceptions/                  # Custom exceptions
├── src/main/resources/
│   └── application.properties       # Service configuration
├── src/test/java/                   # Unit & integration tests
├── Dockerfile                       # Docker image definition
├── k8s-deployment.yaml              # Kubernetes manifests
└── pom.xml                          # Maven build configuration
```

---

## Microservices Overview

| Service              | Port | Database                  | Description                                  |
|----------------------|------|---------------------------|----------------------------------------------|
| Service Registry     | 8761 | —                         | Eureka server for service discovery          |
| API Gateway          | 9000 | —                         | Central entry point, JWT auth, routing       |
| User Service         | 8082 | `user_service_db`         | User accounts, auth verification, OTP        |
| Product Service      | 8081 | `product_service_db`      | Product catalog management                   |
| Cart Service         | 8083 | `cart-service_db`         | Shopping cart with resilience patterns        |
| Seller Service       | 8084 | `seller-service_db`       | Seller profiles and account management       |
| Order Service        | 8085 | `order-service_db`        | Customer order lifecycle                     |
| Seller Order Service | 8086 | `seller-order-service_db` | Seller-side order and product management     |
| Coupon Service       | 8087 | `coupon-service_db`       | Discount coupon management                   |
| Payment Service      | 8088 | `payment-service_db`      | Payment processing                           |
| Review Service       | 8089 | `review-service_db`       | Product reviews and ratings                  |
| Email Service        | 9001 | —                         | Email notifications (Kafka consumer)         |
| Deal Service         | —    | —                         | Deals & promotions (in progress)             |

---

## Prerequisites

- **Java 17** (JDK)
- **Maven 3.8+**
- **Node.js 18+** and **npm**
- **MySQL 8.0**
- **Apache Kafka** and **Zookeeper** (or use Docker Compose)
- **Docker** (optional, for containerized deployment)
- **kubectl** (optional, for Kubernetes deployment)

---

## Getting Started

### 1. Infrastructure Setup

#### Start Kafka and Zookeeper (via Docker Compose)

```bash
cd ecommerce-microservice
docker-compose up -d
```

This starts:
- **Zookeeper** on port `2181`
- **Kafka** on port `9092`

#### Set Up MySQL Databases

Connect to your MySQL instance and create the required databases:

```sql
CREATE DATABASE user_service_db;
CREATE DATABASE product_service_db;
CREATE DATABASE `cart-service_db`;
CREATE DATABASE `seller-service_db`;
CREATE DATABASE `order-service_db`;
CREATE DATABASE `seller-order-service_db`;
CREATE DATABASE `coupon-service_db`;
CREATE DATABASE `payment-service_db`;
CREATE DATABASE `review-service_db`;
```

> **Note:** The default MySQL credentials are `root` / `root`. Update `application.properties` files if your credentials differ.

#### Optional: Start Zipkin

```bash
docker run -d -p 9411:9411 openzipkin/zipkin
```

### 2. Backend Services

Start services in this order:

#### Step 1: Service Registry (must start first)

```bash
cd ecommerce-microservice/service-registry
mvn spring-boot:run
```

Verify at: http://localhost:8761

#### Step 2: API Gateway

```bash
cd ecommerce-microservice/api-gateway
mvn spring-boot:run
```

#### Step 3: Remaining Microservices (can start in any order)

```bash
# In separate terminals:
cd ecommerce-microservice/user-service && mvn spring-boot:run
cd ecommerce-microservice/product-service && mvn spring-boot:run
cd ecommerce-microservice/cart-service && mvn spring-boot:run
cd ecommerce-microservice/seller-service && mvn spring-boot:run
cd ecommerce-microservice/order-service && mvn spring-boot:run
cd ecommerce-microservice/seller-order-service && mvn spring-boot:run
cd ecommerce-microservice/coupon-service && mvn spring-boot:run
cd ecommerce-microservice/payment-service && mvn spring-boot:run
cd ecommerce-microservice/review-service && mvn spring-boot:run
cd ecommerce-microservice/email-service && mvn spring-boot:run
```

> **Tip:** For local development, you may need to update `application.properties` files to use `localhost` instead of Kubernetes service names (e.g., `mysql-service` → `localhost`, `service-resigtry-service` → `localhost`).

### 3. Frontend

```bash
cd ecommerce-frontend
npm install
npm run dev
```

The frontend will be available at: http://localhost:5173

---

## API Endpoints

All API requests go through the **API Gateway** at `http://localhost:9000`.

### Authentication (Public)

| Method | Endpoint                | Description                |
|--------|-------------------------|----------------------------|
| POST   | `/auth/signing`         | Customer login             |
| POST   | `/auth/signing/seller`  | Seller login               |
| POST   | `/auth/signup`          | User registration          |
| POST   | `/auth/otp/send`        | Send OTP for verification  |

### Users

| Method | Endpoint                | Description                |
|--------|-------------------------|----------------------------|
| GET    | `/user/profile`         | Get user profile           |

### Products (Public)

| Method | Endpoint                | Description                |
|--------|-------------------------|----------------------------|
| GET    | `/products/**`          | Browse/search products     |

### Cart (Authenticated)

| Method | Endpoint                | Description                |
|--------|-------------------------|----------------------------|
| GET    | `/api/cart/**`          | View/manage shopping cart  |

### Orders (Authenticated)

| Method | Endpoint                | Description                |
|--------|-------------------------|----------------------------|
| GET    | `/api/orders/**`        | View/manage orders         |

### Payments (Authenticated)

| Method | Endpoint                | Description                |
|--------|-------------------------|----------------------------|
| POST   | `/api/payment/**`       | Process payments           |

### Reviews (Authenticated)

| Method | Endpoint                | Description                |
|--------|-------------------------|----------------------------|
| GET    | `/api/reviews/**`       | View/create reviews        |

### Sellers (Public/Authenticated)

| Method | Endpoint                | Description                |
|--------|-------------------------|----------------------------|
| GET    | `/sellers/**`           | Seller profiles            |

### Seller Orders (Authenticated)

| Method | Endpoint                | Description                |
|--------|-------------------------|----------------------------|
| GET    | `/api/seller/orders/**` | Seller order management    |

### Coupons (Authenticated)

| Method | Endpoint                | Description                |
|--------|-------------------------|----------------------------|
| GET    | `/api/coupons/**`       | Coupon management          |

---

## Frontend Features

### Customer Features
- 🏠 Home page with product categories and carousels
- 🔍 Product search and category browsing
- 📦 Product detail pages with images and descriptions
- 🛒 Shopping cart with coupon support
- 💳 Checkout and payment flow
- ⭐ Product reviews and ratings
- ❤️ Wishlist management
- 👤 Account management
- 🔐 Authentication (login/signup with OTP)

### Seller Features
- 📊 Seller dashboard
- 📝 Product listing management
- 📋 Order management and fulfillment
- 🏪 Seller registration flow

### Admin Features
- 🛠️ Admin dashboard
- 👥 Seller account management
- 🎫 Coupon management

---

## Docker Deployment

### Build Backend Service Images

```bash
# Build each service (example for user-service)
cd ecommerce-microservice/user-service
mvn clean package -DskipTests
docker build -t user-service .
```

### Build Frontend Image

```bash
cd ecommerce-frontend
docker build -t ecommerce-frontend .
```

---

## Kubernetes Deployment

### 1. Create Namespace

```bash
kubectl apply -f ecommerce-microservice/namespace.yml
```

### 2. Deploy Infrastructure

```bash
kubectl apply -f ecommerce-microservice/mysql-pv.yml
kubectl apply -f ecommerce-microservice/mysql-pvc.yml
kubectl apply -f ecommerce-microservice/mysql.yaml
kubectl apply -f ecommerce-microservice/zookeeper.yml
kubectl apply -f ecommerce-microservice/kafka.yml
```

### 3. Deploy Services

```bash
kubectl apply -f ecommerce-microservice/service-registry/k8s-deployment.yaml
kubectl apply -f ecommerce-microservice/api-gateway/k8s-deployment.yaml
kubectl apply -f ecommerce-microservice/user-service/k8s-deployment.yaml
kubectl apply -f ecommerce-microservice/cart-service/k8s-deployment.yaml
# ... repeat for other services
```

---

## Architecture

For a detailed architectural breakdown, see [ARCHITECTURE.md](./ARCHITECTURE.md).

---

## License

This project is for educational and portfolio purposes.
