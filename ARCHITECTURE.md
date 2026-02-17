# Architecture Overview

This document provides a detailed technical overview of the **E-Commerce Microservices Platform** — a full-stack application built with a distributed microservices backend and a modern React frontend.

---

## Table of Contents

- [High-Level Architecture](#high-level-architecture)
- [Backend Microservices](#backend-microservices)
  - [Service Registry](#1-service-registry)
  - [API Gateway](#2-api-gateway)
  - [User Service](#3-user-service)
  - [Product Service](#4-product-service)
  - [Cart Service](#5-cart-service)
  - [Seller Service](#6-seller-service)
  - [Order Service](#7-order-service)
  - [Seller Order Service](#8-seller-order-service)
  - [Coupon Service](#9-coupon-service)
  - [Payment Service](#10-payment-service)
  - [Review Service](#11-review-service)
  - [Email Service](#12-email-service)
  - [Deal Service](#13-deal-service)
- [Frontend Application](#frontend-application)
- [Service Communication](#service-communication)
- [API Gateway Routing](#api-gateway-routing)
- [Authentication & Security](#authentication--security)
- [Database Architecture](#database-architecture)
- [Event-Driven Messaging (Kafka)](#event-driven-messaging-kafka)
- [Resilience Patterns](#resilience-patterns)
- [Observability](#observability)
- [Infrastructure & Deployment](#infrastructure--deployment)
- [Data Models](#data-models)

---

## High-Level Architecture

```
┌─────────────────────────────────────────────────────────────────────┐
│                        Frontend (React + Vite)                      │
│                    localhost:5173 / Nginx (port 80)                  │
└──────────────────────────────┬──────────────────────────────────────┘
                               │ HTTP/REST
                               ▼
┌─────────────────────────────────────────────────────────────────────┐
│                     API Gateway (port 9000)                          │
│              Spring Cloud Gateway + JWT Authentication               │
└──────────┬──────────┬──────────┬──────────┬──────────┬──────────────┘
           │          │          │          │          │
           ▼          ▼          ▼          ▼          ▼
    ┌──────────┐┌──────────┐┌──────────┐┌──────────┐┌──────────┐
    │  User    ││ Product  ││  Cart    ││  Order   ││ Payment  │
    │ Service  ││ Service  ││ Service  ││ Service  ││ Service  │
    │ :8082    ││ :8081    ││ :8083    ││ :8085    ││ :8088    │
    └────┬─────┘└────┬─────┘└────┬─────┘└────┬─────┘└────┬─────┘
         │          │          │          │          │
         ▼          ▼          ▼          ▼          ▼
    ┌──────────┐┌──────────┐┌──────────┐┌──────────┐┌──────────┐
    │ Seller   ││ Review   ││ Coupon   ││ Seller   ││  Deal    │
    │ Service  ││ Service  ││ Service  ││ Order    ││ Service  │
    │ :8084    ││ :8089    ││ :8087    ││ :8086    ││          │
    └──────────┘└──────────┘└──────────┘└──────────┘└──────────┘
         │                                    │
         ▼                                    ▼
┌─────────────────┐                 ┌─────────────────┐
│  Service Registry│                │  Apache Kafka   │
│  Eureka :8761   │                │  :9092          │
└─────────────────┘                └────────┬────────┘
                                            │
                                            ▼
                                   ┌─────────────────┐
                                   │  Email Service   │
                                   │  :9001           │
                                   └─────────────────┘
```

---

## Backend Microservices

All backend services are built with **Spring Boot 3** and **Java 17**, using **Maven** for dependency management. Each service follows a layered architecture pattern:

```
Controller → Service → Repository → Entity (JPA/MySQL)
```

### 1. Service Registry

| Property       | Value                          |
|----------------|--------------------------------|
| **Port**       | 8761                           |
| **Framework**  | Spring Cloud Netflix Eureka    |
| **Role**       | Service discovery server       |

The Service Registry is a **Eureka Server** that acts as the central directory for all microservices. Each service registers itself on startup and discovers other services through Eureka, enabling dynamic load-balanced communication via service names instead of hardcoded URLs.

- Does **not** register itself (`register-with-eureka=false`)
- Does **not** fetch the registry (`fetch-register=false`)

---

### 2. API Gateway

| Property       | Value                                    |
|----------------|------------------------------------------|
| **Port**       | 9000                                     |
| **Framework**  | Spring Cloud Gateway (Reactive/WebFlux)  |
| **Security**   | JWT-based stateless authentication       |

The API Gateway is the **single entry point** for all client requests. It handles:

- **Request routing** to downstream microservices via load-balanced URIs (`lb://service-name`)
- **JWT authentication** via a custom `JwtWebFilter` (stateless, no server-side sessions)
- **CORS configuration** allowing the frontend at `http://localhost:5173`
- **Authentication endpoints** (`/auth/signing`, `/auth/signing/seller`) — authenticates users by calling the User Service, then generates JWT tokens

Key components:
- `SecurityConfig` — Configures Spring Security for reactive (WebFlux) with JWT filter
- `JwtUtil` — Generates and validates JWT tokens using HMAC-SHA signing
- `JwtWebFilter` — Intercepts requests and validates JWT from the `Authorization` header
- `AuthController` — Handles login for both customers and sellers via WebClient calls to User Service

---

### 3. User Service

| Property       | Value                     |
|----------------|---------------------------|
| **Port**       | 8082                      |
| **Database**   | `user_service_db` (MySQL) |
| **Kafka**      | Producer (OTP events)     |

Manages user accounts, authentication verification, and OTP-based login.

**Entities:**
- `User` — id, firstName, lastName, email, password, mobile, role (`ROLE_CUSTOMER`), addresses, usedCoupons
- `VerificationCode` — OTP verification codes for email-based auth

**Key features:**
- User registration and profile management
- Password-based credential verification (called by API Gateway)
- OTP generation and sending via Kafka producer
- Data initialization on startup (`DataInitialization`)

**Controllers:** `AuthController`, `UserController`

---

### 4. Product Service

| Property       | Value                        |
|----------------|------------------------------|
| **Port**       | 8081                         |
| **Database**   | `product_service_db` (MySQL) |

Manages the product catalog.

**Entities:**
- `Product` — id, title, description, mrpPrice, sellingPrice, discountPercentage, quantity, color, images, numOfRatings, categoryId, sellerId, createdAt, sizes

**Key design decisions:**
- Stores `sellerId` and `categoryId` as foreign key references rather than embedded objects (microservice boundary)
- Reviews are handled by a separate `review-service`

**Controllers:** `ProductController`

---

### 5. Cart Service

| Property       | Value                      |
|----------------|----------------------------|
| **Port**       | 8083                       |
| **Database**   | `cart-service_db` (MySQL)  |
| **Resilience** | Circuit Breaker, Retry, TimeLimiter |

Manages shopping carts for users.

**Entities:**
- `Cart` — id, userId, cartItems, totalSellingPrice, totalItems, totalMrpPrice, discount, couponCode
- `CartItems` — Individual items within a cart (linked via `@OneToMany`)

**Resilience4j Configuration (for Product Service calls):**
- Circuit Breaker: sliding window of 10, 50% failure threshold, 10s open-state wait
- Retry: max 3 attempts, 500ms wait between retries
- TimeLimiter: 2s timeout

**Controllers:** `CartController`

---

### 6. Seller Service

| Property       | Value                        |
|----------------|------------------------------|
| **Port**       | 8084                         |
| **Database**   | `seller-service_db` (MySQL)  |

Manages seller accounts and profiles.

**Entities:**
- `Seller` — id, sellerName, mobile, email, password, businessDetails (embedded), bankDetails (embedded), pickUpAddress, GSTIN, role (`ROLE_SELLER`), isEmailVerified, accountStatus
- `Address` — Pickup address for sellers
- `SellerReport` — Reporting data for sellers

**Account statuses:** `PENDING_VERIFICATION` (default), and others managed by admin

**Controllers:** `SellerController`

---

### 7. Order Service

| Property       | Value                       |
|----------------|-----------------------------|
| **Port**       | 8085                        |
| **Database**   | `order-service_db` (MySQL)  |
| **Kafka**      | Producer                    |

Manages customer orders.

**Entities:**
- `Order` — id, orderId, userId, sellerId, orderItems, shipmentAddressId, paymentId, totalMrpPrice, totalSellingPrice, discount, orderStatus, totalItem, paymentStatus, orderDate, deliverDate
- `OrderItem` — Individual items within an order

**Enums:** `OrderStatus`, `PaymentMethod`, `PaymentOrderStatus`, `PaymentStatus`, `USER_ROLE`

**Controllers:** `OrderController`

---

### 8. Seller Order Service

| Property       | Value                              |
|----------------|------------------------------------|
| **Port**       | 8086                               |
| **Database**   | `seller-order-service_db` (MySQL)  |

Provides seller-facing order and product management.

**Domain enums:** `OrderStatus`, `PaymentStatus`, `USER_ROLE`

**Controllers:** `SellerOrderController`, `SellerProductController`

---

### 9. Coupon Service

| Property       | Value                        |
|----------------|------------------------------|
| **Port**       | 8087                         |
| **Database**   | `coupon-service_db` (MySQL)  |

Manages discount coupons.

**Entities:**
- `Coupon` — id, code, discountPercentage, validityStartDate, validityEndDate, minimumOrderValue, isActive, usedByUsers

**Controllers:** `AdminCouponController`

---

### 10. Payment Service

| Property       | Value                         |
|----------------|-------------------------------|
| **Port**       | 8088                          |
| **Database**   | `payment-service_db` (MySQL)  |

Handles payment processing.

**Entities:**
- `PaymentOrder` — id, amount, paymentOrderStatus (`PENDING`), paymentMethod, paymentLinkId, userId, orders

**Domain enums:** `PaymentMethod`, `PaymentOrderStatus`

**Controllers:** `PaymentController`

---

### 11. Review Service

| Property       | Value                        |
|----------------|------------------------------|
| **Port**       | 8089                         |
| **Database**   | `review-service_db` (MySQL)  |

Manages product reviews and ratings.

**Entities:**
- `Review` — id, reviewText, rating, productImages, productId, userId, createdAt

**Controllers:** `ReviewController`

---

### 12. Email Service

| Property       | Value                    |
|----------------|-------------------------|
| **Port**       | 9001                     |
| **Kafka**      | Consumer (OTP events)    |

Consumes Kafka messages and sends emails (OTP verification, notifications).

**Kafka Consumer:**
- Listens on topic `otp-email-topic` (group: `email-group`)
- Deserializes `OtpEmailEvent` messages
- Sends verification OTP emails via SMTP

**Controllers:** `EmailController`

---

### 13. Deal Service

| Property       | Value          |
|----------------|----------------|
| **Status**     | Early stage    |

Intended for managing deals and promotions. Currently has minimal implementation with entity and service scaffolding.

---

## Frontend Application

| Property        | Value                                        |
|-----------------|----------------------------------------------|
| **Framework**   | React 18 + TypeScript                        |
| **Build Tool**  | Vite                                         |
| **UI Library**  | Material UI (MUI) v6                         |
| **State Mgmt**  | Redux Toolkit + React-Redux                  |
| **Styling**     | TailwindCSS + Emotion + Styled Components    |
| **HTTP Client** | Axios                                        |
| **Routing**     | React Router DOM v7                          |
| **Forms**       | Formik + Yup validation                      |

### Page Structure

```
Frontend Routes
├── /                          → Home page
├── /login                     → Authentication (Login/Signup)
├── /products/:category        → Product listing by category
├── /product-details/:cat/:name/:id → Product detail page
├── /reviews/:productId        → Product reviews
├── /reviews/:productId/create → Write a review
├── /cart                      → Shopping cart
├── /checkout                  → Checkout flow
├── /account/*                 → User account management
├── /wishlist                  → User wishlist
├── /search-products           → Product search
├── /become-seller             → Seller registration
├── /payment-success/:orderId  → Payment confirmation
├── /seller/*                  → Seller dashboard (protected)
└── /admin/*                   → Admin dashboard (protected)
```

### State Management (Redux)

```
State/
├── Store.ts                → Redux store configuration
├── AuthSlice.ts            → Authentication state (JWT, user profile)
├── SnackbarSlice.ts        → Global notification snackbar
├── admin/                  → Admin-specific state slices
├── customer/               → Customer-specific state slices
└── seller/                 → Seller-specific state slices
```

### Three User Portals

1. **Customer Portal** (`/customer`) — Browse products, manage cart, place orders, write reviews, manage account
2. **Seller Portal** (`/seller`) — Manage products, view orders, seller dashboard
3. **Admin Portal** (`/admin`) — Platform administration, manage sellers/coupons

---

## Service Communication

### Synchronous (REST)

- **Frontend → API Gateway** — All client HTTP requests go through the gateway
- **API Gateway → User Service** — Authentication verification via `WebClient`
- **Inter-service calls** — Services communicate via REST using OpenFeign or `WebClient` with Eureka service discovery (e.g., Cart Service → Product Service)

### Asynchronous (Kafka)

- **User Service → Email Service** — OTP email events published to `otp-email-topic`
- **Order Service → Email Service** — Order notification events

---

## API Gateway Routing

All routes use load-balanced URIs via Eureka (`lb://service-name`):

| Route Pattern              | Target Service        | Port |
|----------------------------|-----------------------|------|
| `/api/cart/**`             | cart-service          | 8083 |
| `/api/coupons/**`          | coupon-service        | 8087 |
| `/api/orders/**`           | order-service         | 8085 |
| `/api/payment/**`          | payment-service       | 8088 |
| `/products/**`             | product-service       | 8081 |
| `/api/reviews/**`          | review-service        | 8089 |
| `/api/seller/orders/**`    | seller-order-service  | 8086 |
| `/sellers/**`              | seller-service        | 8084 |
| `/auth/**`                 | user-service          | 8082 |
| `/user/**`                 | user-service          | 8082 |

---

## Authentication & Security

```
┌────────┐   POST /auth/signing    ┌─────────────┐   POST /auth/signing/verify   ┌──────────────┐
│ Client │ ──────────────────────► │ API Gateway │ ─────────────────────────────► │ User Service │
│        │                         │             │                                │              │
│        │ ◄────── JWT Token ───── │  JwtUtil    │ ◄──── UserResponse ─────────── │              │
└────────┘                         └─────────────┘                                └──────────────┘
    │
    │  Subsequent requests with Authorization: Bearer <token>
    ▼
┌─────────────┐   JwtWebFilter validates token
│ API Gateway │ ──────────────────────────────► Route to target service
└─────────────┘
```

1. Client sends login credentials to the API Gateway
2. Gateway forwards to User Service for credential verification
3. On success, Gateway generates a JWT token (HMAC-SHA, 24h expiry) containing email and roles
4. Client includes the JWT in the `Authorization` header for subsequent requests
5. `JwtWebFilter` validates the token on every request to `/api/**` endpoints
6. Public endpoints (`/auth/**`, `/products/**`, `/sellers/**`, `/actuator/**`) are accessible without authentication

**Roles:** `ROLE_CUSTOMER`, `ROLE_SELLER`, `ROLE_ADMIN`

---

## Database Architecture

Each microservice has its own dedicated **MySQL** database, following the **Database per Service** pattern:

| Service              | Database Name             |
|----------------------|---------------------------|
| User Service         | `user_service_db`         |
| Product Service      | `product_service_db`      |
| Cart Service         | `cart-service_db`         |
| Seller Service       | `seller-service_db`       |
| Order Service        | `order-service_db`        |
| Seller Order Service | `seller-order-service_db` |
| Coupon Service       | `coupon-service_db`       |
| Payment Service      | `payment-service_db`      |
| Review Service       | `review-service_db`       |

- **ORM:** Hibernate (JPA) with `ddl-auto=update`
- **Dialect:** `MySQLDialect`
- **Cross-service references** use IDs (e.g., `sellerId`, `userId`, `productId`) rather than direct entity relationships

---

## Event-Driven Messaging (Kafka)

```
┌──────────────┐     otp-email-topic     ┌───────────────┐
│ User Service │ ──────────────────────► │ Email Service │
│  (Producer)  │                         │  (Consumer)   │
└──────────────┘                         └───────┬───────┘
                                                 │
┌──────────────┐     otp-email-topic             ▼
│ Order Service│ ──────────────────────►  Send OTP/Email
│  (Producer)  │                         via SMTP
└──────────────┘
```

- **Broker:** Apache Kafka (Confluent `cp-kafka:7.5.0`)
- **Zookeeper:** Confluent `cp-zookeeper:7.5.0` for Kafka coordination
- **Topic:** `otp-email-topic`
- **Consumer Group:** `email-group`
- **Serialization:** JSON (String serializer/deserializer with Jackson `ObjectMapper`)

---

## Resilience Patterns

The **Cart Service** implements resilience patterns via **Resilience4j** for calls to the Product Service:

| Pattern          | Configuration                                                    |
|------------------|------------------------------------------------------------------|
| **Circuit Breaker** | Sliding window: 10 calls, 50% failure threshold, 10s open wait |
| **Retry**        | Max 3 attempts, 500ms between retries                            |
| **TimeLimiter**  | 2s timeout, cancels running futures                              |

---

## Observability

- **Distributed Tracing:** All services are configured with **Zipkin** (`localhost:9411`) for distributed trace collection
- **Sampling:** 100% sampling probability (`management.tracing.sampling.probability=1.0`)
- **SQL Logging:** `show-sql=true` enabled across services for development debugging

---

## Infrastructure & Deployment

### Docker

- **Backend services** use `openjdk:17-jdk-slim` base images with JAR packaging
- **Frontend** uses a multi-stage build: `node:20-alpine` for building → `nginx:alpine` for serving
- **Docker Compose** provides local development setup for Zookeeper and Kafka

### Kubernetes

Kubernetes manifests are provided for production deployment:

| Resource            | File                  | Description                        |
|---------------------|-----------------------|------------------------------------|
| Namespace           | `namespace.yml`       | Creates `app` namespace            |
| MySQL Deployment    | `mysql.yaml`          | MySQL 8.0 with PVC                 |
| MySQL PV            | `mysql-pv.yml`        | 1Gi PersistentVolume (hostPath)    |
| MySQL PVC           | `mysql-pvc.yml`       | 1Gi PersistentVolumeClaim          |
| Zookeeper           | `zookeeper.yml`       | Zookeeper deployment + service     |
| Kafka               | `kafka.yml`           | Kafka broker deployment + service  |
| API Gateway         | `k8s-deployment.yaml` | Gateway deployment                 |
| Service Registry    | `k8s-deployment.yaml` | Eureka server deployment           |
| User Service        | `k8s-deployment.yaml` | User service deployment            |
| Cart Service        | `k8s-deployment.yaml` | Cart service deployment            |

The frontend's `nginx.conf` is configured to proxy API requests to the backend cluster service.

---

## Data Models

### Entity Relationship Overview

```
User (user-service)
├── id, firstName, lastName, email, password, mobile, role
├── addresses: Set<Long>
└── usedCoupons: Set<Long>

Seller (seller-service)
├── id, sellerName, mobile, email, password, GSTIN, role, accountStatus
├── businessDetails (embedded)
├── bankDetails (embedded)
└── pickUpAddress → Address

Product (product-service)
├── id, title, description, mrpPrice, sellingPrice, discountPercentage
├── quantity, color, sizes, images, numOfRatings
├── categoryId, sellerId
└── createdAt

Cart (cart-service)
├── id, userId, totalSellingPrice, totalItems, totalMrpPrice, discount, couponCode
└── cartItems → Set<CartItems>

Order (order-service)
├── id, orderId, userId, sellerId
├── orderItems → List<OrderItem>
├── shipmentAddressId, paymentId
├── totalMrpPrice, totalSellingPrice, discount
├── orderStatus, paymentStatus
└── orderDate, deliverDate

PaymentOrder (payment-service)
├── id, amount, paymentOrderStatus, paymentMethod
├── paymentLinkId, userId
└── orders: Set<Long>

Review (review-service)
├── id, reviewText, rating, productImages
├── productId, userId
└── createdAt

Coupon (coupon-service)
├── id, code, discountPercentage
├── validityStartDate, validityEndDate
├── minimumOrderValue, isActive
└── usedByUsers: Set<Long>
```
