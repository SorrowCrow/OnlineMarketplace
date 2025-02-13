# Online Marketplace
Swagger - http://localhost:8080/swagger-ui/index.html

In Latvia, the most popular online service for buying and selling products is ss.lv. Over the years, while the platform has remained widely used, its user interface has not seen significant updates, making it feel outdated. 
We recognised the need for an upgrade to provide a better, more modern, and visually appealing experience for users. That’s why we decided to create a new Online Marketplace—one that not only meets the needs of today’s users but also offers a more intuitive, user-friendly interface and enhanced features.

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
- **Database**: MySQL (relational database for structured data storage).
- **Authentication**: JWT for secure token-based authentication and OAuth2 for social login.
- **Security**: Spring Security for role-based access control and endpoint protection.
- **Caching**: Redis for caching frequently accessed data (e.g., product listings).
- **Logging & Monitoring**: Logback for logging and tools like Prometheus/Grafana for monitoring.

  ---

This setup ensures a **scalable, secure, and efficient** online marketplace with a seamless user experience.

---
## User

- To retrieve user info by ID, make GET request to /users/{id}
    - {id} - user id
- To update existing user's information, make PUT request to /users/{id}

EXAMPLE REQUEST:
```json
{
  "id": 9007199254740991,
  "name": "string",
  "surname": "string",
  "email": "string",
  "password": "string",
  "accountVerified": true,
  "verificationToken": "string",
  "verificationTokenExpiry": "2025-02-12T14:39:41.793Z",
  "roles": [
    {
      "id": 1073741824,
      "name": "ROLE_USER"
    }
  ],
  "cart": {
    "id": 9007199254740991,
    "listings": [
      "string"
    ]
  }
}
```
- To delete a user, make a DELETE request to /users/{id}
  - {id} - user id

## Category

- To retrieve information about a specific category by it's ID, make a GET request to /api/categories/{id}
    - {id} - category id

EXAMPLE RESPONSE: 
```json
{
  "id": 9007199254740991,
  "type": "FAMILY",
  "name": "string",
  "description": "string"
}
```

- To update information for a specific category by it's ID, make a PUT request to /api/categories/{id}
    - {id} - category id

EXAMPLE REQUEST:
```json
{
  "id": 9007199254740991,
  "type": "FAMILY",
  "name": "string",
  "description": "string"
}
```

EXAMPLE RESPONSE: 
```json
{
  "id": 9007199254740991,
  "type": "FAMILY",
  "name": "string",
  "description": "string"
}
```

- To delete a specific category, make a DELETE request to /api/categories/{id}
    - {id} - category id
 
- To retrieve information about all categories, make a GET request to /api/categories

EXAMPLE RESPONSE: 
```json
[
  {
    "id": 9007199254740991,
    "type": "FAMILY",
    "name": "string",
    "description": "string"
  }
]
```

- To add a new category, make a POST request to /api/categories

EXAMPLE REQUEST: 
```json
{
  "id": 9007199254740991,
  "type": "FAMILY",
  "name": "string",
  "description": "string"
}
```
EXAMPLE RESPONSE: 
```json
{
  "id": 9007199254740991,
  "type": "FAMILY",
  "name": "string",
  "description": "string"
}
```
- To retrieve information about categories by type, make a GET request to /api/categories/type
    - **Available values : FAMILY, EMPLOYMENT, CONSTRUCTION, HOBBY, TRANSPORT, REAL_ESTATE, CLOTHING, ANIMALS, ELECTRONICS, FOR_HOME, MANUFACTURING, AGRICULTURE, OTHER**
 
EXAMPLE RESPONSE:
```json
[
  {
    "id": 9007199254740991,
    "type": "FAMILY",
    "name": "string",
    "description": "string"
  }
]
```

## Reviews

- To get all reviews make a GET request to /reviews

EXAMPLE RESPONSE: 

```json
 {
    "id": 9007199254740991,
    "reviewerName": "string",
    "content": "string",
    "rating": 1073741824
  }
```
- To create a review, make a POST request to /reviews

EXAMPLE REQUEST:
```json
{
  "id": 9007199254740991,
  "reviewerName": "string",
  "content": "string",
  "rating": 1073741824
}
```
EXAMPLE RESPONSE:
```json
{
  "id": 9007199254740991,
  "reviewerName": "string",
  "content": "string",
  "rating": 1073741824
}
```

- To get a specific review based on ID make GET a request to /reviews/{id}
    - {id} - review id
  

EXAMPLE RESPONSE: 
```json
{
  "id": 9007199254740991,
  "reviewerName": "string",
  "content": "string",
  "rating": 1073741824
}
```

- To delete a review, make DELETE request to /reviews/{id}
    - {id} - review id
 

## Payment

- To create a session for payment of items from the cart, make POST request to /api/payments/create-session

EXAMPLE RESPONSE: 

```java
https://checkout.stripe.com/c/pay/cs_test_a1Mygh7dRNsTCrmaIjDYlad1wniEPjqoVGbhtmRbDVuH0nFkxRBHSdfoRk#fidkdWxOYHwnPyd1blpxYHZxWjA0VHVHNVREbF1RZ3J3YT1dRHNRQHFvTEdAcF1GN2IyblB%2FR1JTbkdmbXU3SXwyaV9%2FSEkwTWpGV2YzMzJMUzNGRFxLUTxCX1AxcHV3QERPdXRMbWtAV1BTNTVTQGk0Z0B0aycpJ2N3amhWYHdzYHcnP3F3cGApJ2lkfGpwcVF8dWAnPyd2bGtiaWBabHFgaCcpJ2BrZGdpYFVpZGZgbWppYWB3dic%2FcXdwYHgl
```
After payment you will be redirected to a new page with a notification about payment status

- To see if the payment came through successfully, make GET request to /api/payments/success
    - {session_id} - payment session id

EXAMPLE REQUEST:
```java
{
    "session_id": cs_test_a1Mygh7dRNsTCrmaIjDYlad1wniEPjqoVGbhtmRbDVuH0nFkxRBHSdfoRk
}
```
EXAMPLE RESPONSE: 
```json
{
    Payment successful! Orders have been created for user: example@example.com
}
```

- To see a failure notification, make GET request to /api/payments/failure

EXAMPLE RESPONSE: 
```json
{
    Payment cancelled. Try again.
}
```

## Order

- To create a new order, make POST request to /api/orders/create/{listringId}/{buyerId}
    - {listingId} - listing id
    - {buyerId} - users id that purchased this listing

- To get any particular order, make GET request to /api/orders/get order/{id}
    - {id} - order id

- To get all orders, make GET request to /api/orsers/get all orders
- To delete order, make DELETE request to /api/orders/{id}
    - {id} - order id

For almost all of these requests there will be a similar response like:
 
EXAMPLE RESPONSE: 
```json
{
  "id": 1,
  "buyer": {
    "id": 1,
    "name": "example",
    "surname": "example",
    "email": "example@example.com",
    "accountVerified": true,
    "roles": [],
    "cart": null
  },
  "type": "SELL",
  "title": "Wireless Noise-Canceling Headphones",
  "description": "Experience immersive sound with Wireless Noise-Canceling Headphones, featuring advanced ANC technology and up to 40 hours of battery life.",
  "price": 129.99,
  "unit": "PIECE",
  "startDate": "2025-02-13T15:30:55.704583",
  "endDate": "2025-03-13T15:30:55.669373",
  "category": {
    "id": 24,
    "type": "ELECTRONICS",
    "name": "default name",
    "description": "default description"
  },
  "location": "RIGA",
  "seller": {
    "id": 8,
    "name": "example",
    "surname": "example",
    "email": "example@example.com",
    "accountVerified": true,
    "roles": [],
    "cart": {
      "id": 1,
      "listings": []
    }
  }
}
```

## Listing 

- To get all listings, make GET request to /api/listings

EXAMPLE RESPONSE: 
```json
[
  {
    "listingID": 9007199254740991,
    "type": "SELL",
    "title": "string",
    "description": "string",
    "price": 0,
    "unit": "PIECE",
    "location": "RIGA",
    "startDate": "2025-02-13T13:51:35.013Z",
    "endDate": "2025-02-13T13:51:35.013Z",
    "user": "string",
    "category": {
      "id": 9007199254740991,
      "type": "FAMILY",
      "name": "string",
      "description": "string"
    }
  }
]
```

- To create new listing, make POST request to /api/listings

EXAMPLE REQUEST:
```json
{
  "type": "SELL",
  "title": "string",
  "description": "string",
  "price": 2147483647,
  "priceUnit": "PIECE",
  "location": "RIGA",
  "userID": 9007199254740991,
  "categoryID": 9007199254740991
}
```

EXAMPLE RESPONSE: 
```json
{
  "listingID": 9007199254740991,
  "type": "SELL",
  "title": "string",
  "description": "string",
  "price": 0,
  "unit": "PIECE",
  "location": "RIGA",
  "startDate": "2025-02-13T13:53:07.101Z",
  "endDate": "2025-02-13T13:53:07.101Z",
  "user": "string",
  "category": {
    "id": 9007199254740991,
    "type": "FAMILY",
    "name": "string",
    "description": "string"
  }
}
```

- To get particular listing, make GET request to /api/listings/{id}
    - {id} - listing id

EXAMPLE RESPONSE: 
```json
{
  "listingID": 9007199254740991,
  "type": "SELL",
  "title": "string",
  "description": "string",
  "price": 0,
  "unit": "PIECE",
  "location": "RIGA",
  "startDate": "2025-02-13T13:56:11.636Z",
  "endDate": "2025-02-13T13:56:11.636Z",
  "user": "string",
  "category": {
    "id": 9007199254740991,
    "type": "FAMILY",
    "name": "string",
    "description": "string"
  }
}
```

- To delete listing, make DELETE request to /api/listings/{id}
    - {id} - listing id

- To update listing, make PATCH request to /api/listings/{id}
    - {id} - listing id

EXAMPLE RESPONSE: 
```json
{
  "type": "SELL",
  "title": "string",
  "description": "string",
  "price": 2147483647,
  "priceUnit": "PIECE",
  "location": "RIGA",
  "categoryID": 9007199254740991
}
```
- To find a listing by a specific parameter, make GET request to /api/listings/...
    - .../users/{userId} - find by user id
    - .../users/type - find by type,   **Available values : SELL, RENT**
    - .../users/price - price range listing search 
    - .../users/page - listings page retrieval 
    - .../users/location  - find by location,  **Available values : RIGA, VILNIUS, ALL, OTHER**
    - .../users/keyword - find by specific word in description
    - .../users/category - find by category 

For almost all of these requests there will be a similar response like:

EXAMPLE RESPONSE: 
```json
[
  {
    "listingID": 9007199254740991,
    "type": "SELL",
    "title": "string",
    "description": "string",
    "price": 0,
    "unit": "PIECE",
    "location": "RIGA",
    "startDate": "2025-02-13T14:36:01.244Z",
    "endDate": "2025-02-13T14:36:01.244Z",
    "user": {
      "id": 9007199254740991,
      "name": "string",
      "surname": "string",
      "email": "string",
      "accountVerified": true,
      "roles": [
        {
          "id": 1073741824,
          "name": "ROLE_USER"
        }
      ],
      "cart": {
        "id": 9007199254740991,
        "listings": [
          "string"
        ]
      }
    },
    "category": {
      "id": 9007199254740991,
      "type": "FAMILY",
      "name": "string",
      "description": "string"
    }
  }
]
```

## Cart
Each user, when creating an account, his/her cart appears, a user has only one cart

- To remove listing from cart, make POST request to /api/cart/remove/{listingId}
    - {listingId} - listing id

EXAMPLE RESPONSE: 
```json
[
  {
    "id": 1,
    "listings": []
  }
]
```
Or it will show you the rest listings 

- To add listing to cart, make POST request to /api/cart/addToCart/{listingId}
    - {listingId} - listing id 

EXAMPLE RESPONSE:
```json
{
  "id": 1,
  "listings": [
    {
      "listingID": 1,
      "type": "SELL",
      "title": "Wireless Noise-Canceling Headphones",
      "description": "Experience immersive sound with Wireless Noise-Canceling Headphones, featuring advanced ANC technology and up to 40 hours of battery life.",
      "price": 129.99,
      "unit": "PIECE",
      "location": "RIGA",
      "startDate": "2025-02-13T16:08:04.921884",
      "endDate": "2025-03-13T16:08:04.8907",
      "user": {
        "id": 8,
        "name": "example",
        "surname": "example",
        "email": "example@example.com",
        "accountVerified": true,
        "roles": []
      },
      "category": {
        "id": 24,
        "type": "ELECTRONICS",
        "name": "default name",
        "description": "default description"
      }
    }
  ]
}
```

- To get all listings from cart, make GET request to /api/cart

EXAMPLE RESPONSE:
```json
{
  "id": 1,
  "listings": [
    {
      "listingID": 1,
      "type": "SELL",
      "title": "Wireless Noise-Canceling Headphones",
      "description": "Experience immersive sound with Wireless Noise-Canceling Headphones, featuring advanced ANC technology and up to 40 hours of battery life.",
      "price": 129.99,
      "unit": "PIECE",
      "location": "RIGA",
      "startDate": "2025-02-13T16:08:04.921884",
      "endDate": "2025-03-13T16:08:04.8907",
      "user": {
        "id": 8,
        "name": "example",
        "surname": "example",
        "email": "example@example.com",
        "accountVerified": true,
        "roles": []
      },
      "category": {
        "id": 24,
        "type": "ELECTRONICS",
        "name": "default name",
        "description": "default description"
      }
    }
  ]
}
```
## Authentication

- To sign up, make POST request to /api/auth/signup

EXAMPLE REQUEST:
```json
{
  "email": "string",
  "password": "string",
  "name": "string",
  "surname": "string"
}
```
- To sign out, make POST request to /api/auth/sign

EXAMPLE RESPONSE:
```json
{
  "message": "You've been signed out!"
}
```
- To verify email you need a token that will be sent to your e-mail after registration, than make GET request to /api/auth/verify

EXAMPLE RESPONSE:
```json
{
  "message": "Email verified successfully! You can now log in."
}
```
- To resend verification email, make POST request to /api/auth/resend-verification 

EXAMPLE REQUEST:
```json
{
  "email": "string",
}
```
- To login, make POST request to /api/auth/login

EXAMPLE REQUEST:
```json
{
  "email": "string",
  "password": "string"
}
```

## Role
- To get all roles, make GET request to /api/roles

EXAMPLE RESPONSE:
```json
[
  {
    "id": 1,
    "name": "ROLE_USER"
  },
  {
    "id": 2,
    "name": "ROLE_ADMIN"
  }
]
```
- To get role by name, make GET request to /api/roles/{name}
    - {name} - role name, **Available values : ROLE_USER, ROLE_MODERATOR, ROLE_ADMIN**

EXAMPLE RESPONSE:
```json
{
  "id": 1,
  "name": "ROLE_USER"
}
```
- To get role by id, make GET request to /api/roles/id/{id}
    - {id} - role id

EXAMPLE RESPONSE:
```json
{
  "id": 1073741824,
  "name": "ROLE_USER"
}
```