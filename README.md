# FarmYukti: Spatial Agri-Tech Marketplace API

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.0+-brightgreen.svg)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15.0+-blue.svg)
![PostGIS](https://img.shields.io/badge/PostGIS-Spatial-lightgrey.svg)
![JWT Security](https://img.shields.io/badge/Security-JWT-orange.svg)

FarmYukti is a robust, spatially-aware backend platform designed to connect farmers directly with buyers. It leverages geospatial querying to optimize local agricultural supply chains, allowing buyers to discover active crop listings based on precise geographic coordinates and reducing transportation overhead.

## 🚀 System Architecture & Core Features

FarmYukti moves beyond basic CRUD operations by implementing real-world marketplace mechanics:

*   **Secure Identity & Role Management:** Stateless authentication using JSON Web Tokens (JWT). Strict Role-Based Access Control (RBAC) separates `FARMER` and `BUYER` workflows.
*   **Geospatial Land Registry:** Integrates PostGIS and Hibernate Spatial to map physical farm boundaries using exact GPS coordinates (SRID 4326). Validates land ownership before permitting marketplace listings.
*   **Standardized Crop Dictionary:** Utilizes a `CropMaster` reference system to maintain data integrity and prevent fragmented search results.
*   **Proximity-Based Discovery:** Advanced matching engine utilizing spatial algorithms (`ST_DWithin`) to filter active listings based on a buyer's geolocation and radius.
*   **Transactional Order System:** Employs atomic database transactions to safely deduct inventory upon order creation, preventing race conditions and overselling.

---

## 🛠️ Technology Stack

*   **Language:** Java 17+
*   **Framework:** Spring Boot (Web, Data JPA, Security, Validation)
*   **Database:** PostgreSQL
*   **Spatial Extension:** PostGIS & Hibernate Spatial
*   **Authentication:** Spring Security & JWT
*   **API Documentation:** OpenAPI 3.0 / Swagger UI

---

## 🗄️ Database Schema

The system is built on a highly normalized relational database containing 6 core tables:

| Table | Purpose | Key Relationships |
| :--- | :--- | :--- |
| `users` | Manages authentication and user profiles. | Base entity for all relations. |
| `land_parcels` | Stores geographic boundaries (`location`). | Foreign Key to `users` (Farmer). |
| `crop_master` | Standardized dictionary of crops. | Master reference table. |
| `listings` | The core marketplace entity. | Joins `users`, `land_parcels`, and `crop_master`. |
| `listing_media` | Manages file paths/URLs for crop imagery. | Foreign Key to `listings`. |
| `orders` | Tracks buyer transactions and inventory state. | Joins `users` (Buyer) and `listings`. |

---

## 🔌 Core API Endpoints

### Authentication
*   `POST /api/v1/auth/register` - Register a new Farmer or Buyer.
*   `POST /api/v1/auth/login` - Authenticate and retrieve JWT.

### Land Management (Farmer)
*   `POST /api/v1/land-parcels` - Register a new farm using GPS coordinates.
*   `GET /api/v1/land-parcels/my-farms` - Retrieve authenticated user's registered land.

### Marketplace (Farmer & Buyer)
*   `POST /api/v1/listings` - Publish a new crop listing (Ownership verification required).
*   `GET /api/v1/listings/search` - Advanced search (Filters: Price, Crop Type, Geo-radius).

### Orders (Buyer)
*   `POST /api/v1/orders` - Place an order and atomically deduct listing inventory.

*(Full interactive documentation is available via Swagger UI once the application is running).*

---

## 💻 Local Development Setup

### Prerequisites
*   JDK 17 or higher
*   Maven 3.8+
*   PostgreSQL installed with the PostGIS extension enabled.

### 1. Database Configuration
Create a database in PostgreSQL and enable the spatial extension:
```sql
CREATE DATABASE farmyukti;
\c farmyukti
CREATE EXTENSION postgis;
```
### 2. Application Properties
Configure your connection string and JWT secret in **src/main/resources/application.properties:**
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/farmyukti
spring.datasource.username=your_db_user
spring.datasource.password=your_db_password
spring.jpa.properties.hibernate.dialect=org.hibernate.spatial.dialect.postgis.PostgisDialect

jwt.secret=your_super_secret_key_make_it_long
jwt.expiration=86400000
```

### 3. Build and Run
```Bash
mvn clean install
mvn spring-boot:run
```




<img width="1919" height="1036" alt="Screenshot_2026-08-29_22-40-33" src="https://github.com/user-attachments/assets/b19a8c06-7152-4e56-894c-5714f5e07218" />
<img width="1919" height="1036" alt="Screenshot_2026-08-29_22-40-39" src="https://github.com/user-attachments/assets/3e3d12f7-ee0e-4117-a889-c3bba26bf7ac" />

<img width="1919" height="1036" alt="Screenshot_2026-08-29_22-40-54" src="https://github.com/user-attachments/assets/c65e5f8e-5d7e-4773-ad72-1fe47f7c21d2" />
<img width="1919" height="1036" alt="Screenshot_2026-08-29_22-40-51" src="https://github.com/user-attachments/assets/851836ed-d6e0-434b-bb2c-26d9ecbe2dab" />

