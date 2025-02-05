# Online Marketplace
Swagger - http://localhost:8080/swagger-ui/index.html
## Key Features

1. **User Management**:
    - User registration and login with **JWT-based authentication**.
    - Role-based access control (e.g., Admin, Seller, Buyer) using **Spring Security**.
    - Social login integration via **OAuth2** (e.g., Google, Facebook).

2. **Product Management**:
    - CRUD operations for products (sellers can add, update, or delete products).
    - Product categorization and filtering (e.g., by price, category, ratings).
    - Search functionality with pagination and sorting.

3. **Order Management**:
    - Shopping cart functionality for buyers.
    - Order creation, tracking, and status updates (e.g., pending, shipped, delivered).
    - Integration with payment gateways (e.g., Stripe, PayPal) for secure transactions.

4. **Reviews & Ratings**:
    - Buyers can leave reviews and ratings for products.
    - Average rating calculation for products.

5. **Notifications**:
    - Email or in-app notifications for order confirmations, updates, and promotions.
    - Real-time updates using WebSocket or messaging queues (e.g., RabbitMQ, Kafka).

6. **Admin Dashboard**:
    - Manage users, products, and orders.
    - Analytics and reporting (e.g., sales, revenue, user activity).

---

## Technical Stack

- **Backend Framework**: Spring Boot (RESTful APIs, dependency injection, and modular architecture).
- **Database**: PostgreSQL (relational database for structured data storage).
- **Authentication**: JWT for secure token-based authentication and OAuth2 for social login.
- **Security**: Spring Security for role-based access control and endpoint protection.
- **Caching**: Redis for caching frequently accessed data (e.g., product listings).
- **Logging & Monitoring**: Logback for logging and tools like Prometheus/Grafana for monitoring.

---

This setup ensures a **scalable, secure, and efficient** online marketplace with a seamless user experience.