# API Documentation

## Base URL

**Development**: `http://localhost:8080/api`  
**Production**: `https://your-domain.com/api`

---

## Authentication

All protected endpoints require a JWT token in the Authorization header:

```
Authorization: Bearer <your-jwt-token>
```

---

## Endpoints

### 🔐 Authentication

#### Register User

```http
POST /auth/signup
```

**Request Body**:
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password123"
}
```

**Response** (200 OK):
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "email": "john@example.com",
  "name": "John Doe",
  "userId": 1
}
```

---

#### Login

```http
POST /auth/login
```

**Request Body**:
```json
{
  "email": "john@example.com",
  "password": "password123"
}
```

**Response** (200 OK):
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "email": "john@example.com",
  "name": "John Doe",
  "userId": 1
}
```

---

### 💳 Payment

#### Generate Payment QR Code

```http
POST /payment/generate-qr
Authorization: Bearer <token>
```

**Response** (200 OK):
```json
{
  "qrCodeBase64": "iVBORw0KGgoAAAANSUhEUgAA...",
  "paymentReferenceId": "PAY-12AB34CD",
  "amount": 100,
  "upiId": "yourname@paytm",
  "merchantName": "URL Shortener SaaS" 
}
```

---

#### Confirm Payment

```http
POST /payment/confirm?paymentReferenceId=PAY-12AB34CD
Authorization: Bearer <token>
```

**Response** (200 OK):
```json
{
  "success": true,
  "message": "Payment confirmed successfully"
}
```

---

### 🔗 URL Management

#### Create Short URL

```http
POST /url/shorten
Authorization: Bearer <token>
```

**Request Body**:
```json
{
  "originalUrl": "https://example.com/very-long-url",
  "paymentReferenceId": "PAY-12AB34CD"
}
```

**Response** (200 OK):
```json
{
  "id": 1,
  "originalUrl": "https://example.com/very-long-url",
  "shortCode": "aBcD123",
  "shortUrl": "http://localhost:8080/aBcD123",
  "totalClicks": 0,
  "createdAt": "2024-02-10T10:30:00"
}
```

---

#### Get User URLs

```http
GET /url/user
Authorization: Bearer <token>
```

**Response** (200 OK):
```json
[
  {
    "id": 1,
    "originalUrl": "https://example.com/page1",
    "shortCode": "aBcD123",
    "shortUrl": "http://localhost:8080/aBcD123",
    "totalClicks": 42,
    "createdAt": "2024-02-10T10:30:00"
  },
  {
    "id": 2,
    "originalUrl": "https://example.com/page2",
    "shortCode": "xYz789W",
    "shortUrl": "http://localhost:8080/xYz789W",
    "totalClicks": 15,
    "createdAt": "2024-02-09T14:20:00"
  }
]
```

---

#### Redirect to Original URL

```http
GET /{shortCode}
```

**Example**: `GET /aBcD123`

**Response**: `302 Redirect` to original URL

---

### 📊 Analytics

#### Get URL Analytics

```http
GET /analytics/{shortCode}
Authorization: Bearer <token>
```

**Example**: `GET /analytics/aBcD123`

**Response** (200 OK):
```json
{
  "urlId": 1,
  "shortCode": "aBcD123",
  "totalClicks": 42,
  "clicksByDate": {
    "2024-02-09": 15,
    "2024-02-10": 27
  }
}
```

---

#### Get User Analytics

```http
GET /analytics/user
Authorization: Bearer <token>
```

**Response** (200 OK):
```json
{
  "2024-02-08": 5,
  "2024-02-09": 23,
  "2024-02-10": 34
}
```

---

## Error Responses

### 400 Bad Request
```json
{
  "timestamp": "2024-02-10T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "errors": {
    "email": "Email should be valid",
    "password": "Password must be at least 6 characters"
  }
}
```

### 401 Unauthorized
```json
{
  "timestamp": "2024-02-10T10:30:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Invalid email or password"
}
```

### 404 Not Found
```json
{
  "timestamp": "2024-02-10T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Short URL not found"
}
```

### 500 Internal Server Error
```json
{
  "timestamp": "2024-02-10T10:30:00",
  "status": 500,
  "error": "Internal Server Error",
  "message": "An unexpected error occurred"
}
```

---

## Swagger UI

Interactive API documentation is available at:

**Development**: `http://localhost:8080/swagger-ui.html`  
**Production**: `https://your-domain.com/swagger-ui.html`

---

## Rate Limiting

Currently no rate limiting is implemented. Consider adding rate limiting for production:

- Login attempts: 5 per minute
- URL creation: 10 per hour per user
- API calls: 100 per minute per IP

---

## Authentication Flow

1. User signs up or logs in
2. Backend returns JWT token
3. Frontend stores token in localStorage
4. Token is sent with every protected API request
5. Token expires after 24 hours (configurable)

---

## Payment Flow

1. User enters URL to shorten
2. Frontend calls `POST /payment/generate-qr`
3. Backend generates QR code with UPI payment link and returns Base64 image
4. Frontend displays QR code to user
5. User scans QR code with UPI app and completes payment
6. User clicks "I've completed payment" button
7. Frontend calls `POST /payment/confirm` to mark payment as confirmed
8. Frontend calls `POST /url/shorten` with payment reference ID
9. Backend verifies payment is confirmed, creates short URL and returns details

---

**For more details, check the Swagger UI documentation.**
