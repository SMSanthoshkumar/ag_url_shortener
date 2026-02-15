# Architecture Overview

## System Design

```
┌─────────────┐         ┌──────────────┐         ┌─────────────┐
│   Browser   │ ──────> │    React     │ ──────> │  Spring     │
│  (Client)   │ <────── │   Frontend   │ <────── │   Boot      │
└─────────────┘         └──────────────┘         └─────────────┘
                                                         │
                              ┌──────────────────────────┼─────────┐
                              │                          │         │
                        ┌─────▼─────┐           ┌───────▼────┐ ┌─▼────────┐
                        │ PostgreSQL│           │  ZXing QR  │ │   JWT    │
                        │  Database │           │  Generator │ │  Auth    │
                        └───────────┘           └────────────┘ └──────────┘
```

---

## Component Architecture

### Backend (Spring Boot)

```
┌──────────────────────────────────────────────────────┐
│                   Controllers                         │
│  AuthController | PaymentController | UrlController  │
│              AnalyticsController                      │
└────────────────────┬─────────────────────────────────┘
                     │
┌────────────────────▼─────────────────────────────────┐
│                    Services                           │
│  AuthService | PaymentService | UrlService           │
│              AnalyticsService                         │
└────────────────────┬─────────────────────────────────┘
                     │
┌────────────────────▼─────────────────────────────────┐
│                  Repositories                         │
│  UserRepository | UrlRepository | PaymentRepository  │
│              ClickAnalyticsRepository                 │
└────────────────────┬─────────────────────────────────┘
                     │
┌────────────────────▼─────────────────────────────────┐
│                    Database                           │
│  users | urls | payments | click_analytics           │
└──────────────────────────────────────────────────────┘
```

---

## Payment Integration Flow

```
User Action                Frontend                    Backend
────────────────────────────────────────────────────────────────────

Enter URL      ──>  generateQrCode() ──>  POST /payment/generate-qr
                                                    │
                                                    │  Generate reference ID
                                                    │
                                                    │  Create UPI string
                                                    │
                                                    ├──> ZXing QR Generator
                                                    │
                    <────────────────────  QR code (Base64), reference ID

Display QR     ──>  Show QR code image

User scans     ──>  (User pays via UPI app)
with UPI app

Click          ──>  confirmPayment()  ──>  POST /payment/confirm
"I've Paid"                                         │
                                                    │  Update payment status
                                                    │
                    <────────────────────  Payment confirmed

Create URL     ──>  createShortUrl()  ──>  POST /url/shorten
                                                    │
                                                    │  Verify payment reference
                                                    │
                                                    │  Check status is CONFIRMED
                                                    │
                                                    │  Generate short code
                                                    │
                    <──────────────────────  shortUrl created
```

---

## URL Shortening Algorithm

1. **Generate Random Code**:
   - Use alphanumeric characters (a-z, A-Z, 0-9)
   - Length: 7 characters (configurable)
   - Total combinations: 62^7 = 3.5 trillion

2. **Uniqueness Check**:
   - Query database to check if code exists
   - If exists, generate new code
   - Retry until unique code is found

3. **Save Mapping**:
   - Store original URL → short code mapping
   - Associate with user ID
   - Initialize click counter to 0

---

## Click Tracking Flow

```
User clicks          Frontend/Browser           Backend
short URL
────────────────────────────────────────────────────────────

GET /aBcD123  ─────────────────────────────>  UrlController
                                                    │
                                                    │  Find by shortCode
                                                    │
                                                    ├──> UrlRepository
                                                    │
                                                    │  Track click
                                                    │
                                                    ├──> AnalyticsService
                                                    │    - Save IP address
                                                    │    - Save User-Agent
                                                    │    - Save Referrer
                                                    │    - Timestamp
                                                    │
                                                    │  Increment counter
                                                    │
                                                    ├──> Update totalClicks
                                                    │
Browser         <─────302 Redirect──────────  originalUrl
redirects
```

---

## Security Architecture

### JWT Authentication

```
Login Request
     │
     ├──> AuthService.login()
     │         │
     │         ├──> Validate credentials
     │         │
     │         ├──> BCrypt.matches(password, hashedPassword)
     │         │
     │         ├──> JwtUtil.generateToken(email)
     │         │
     │         └──> Return JWT token
     │
Frontend stores token
     │
Subsequent Requests
     │
     ├──> Include: Authorization: Bearer <token>
     │
     ├──> JwtAuthFilter intercepts request
     │         │
     │         ├──> Extract token from header
     │         │
     │         ├──> JwtUtil.validateToken(token)
     │         │
     │         ├──> Extract username from token
     │         │
     │         ├──> Load user details
     │         │
     │         └──> Set SecurityContext
     │
Request proceeds to controller
```

---

## Database Schema

```sql
┌──────────────┐         ┌─────────────┐
│    users     │         │    urls     │
├──────────────┤         ├─────────────┤
│ id (PK)      │<────────│ user_id(FK) │
│ email        │         │ id (PK)     │
│ password     │         │ short_code  │
│ name         │         │ original_url│
│ created_at   │         │ total_clicks│
└──────────────┘         │ is_active   │
       │                 │ created_at  │
       │                 └─────────────┘
       │                        │
       │                        │
       │                 ┌──────▼────────────┐
       │                 │ click_analytics   │
       │                 ├───────────────────┤
       │                 │ id (PK)           │
       │                 │ url_id (FK)       │
       │                 │ clicked_at        │
       │                 │ ip_address        │
       │                 │ user_agent        │
       │                 │ referrer          │
       │                 └───────────────────┘
       │
       │
       │
┌──────▼───────┐
│   payments   │
├──────────────┤
│ id (PK)      │
│ user_id (FK) │
│ razorpay_*   │
│ amount       │
│ status       │
│ created_at   │
└──────────────┘
```

---

## Frontend Architecture

```
┌────────────────────────────────────────────┐
│              App.jsx (Router)              │
└────────────────┬───────────────────────────┘
                 │
    ┌────────────┼─────────────┐
    │            │             │
┌───▼───┐  ┌────▼────┐  ┌─────▼──────┐
│ Login │  │ Signup  │  │ Dashboard  │
└───────┘  └─────────┘  └─────┬──────┘
                              │
              ┌───────────────┼───────────────┐
              │               │               │
      ┌───────▼────┐  ┌───────▼─────┐  ┌─────▼────────┐
      │UrlShortener│  │  UrlList    │  │AnalyticsChart│
      └────────────┘  └─────────────┘  └──────────────┘
              │
      ┌───────┴────────┐
      │                │
┌─────▼────┐    ┌──────▼─────┐
│ Payment  │    │ URL Service│
│ Service  │    └────────────┘
└──────────┘
```

---

## Scalability Considerations

### Current Implementation
- Single server architecture
- Suitable for small to medium traffic

### Future Enhancements
1. **Caching**: Add Redis for frequently accessed URLs
2. **Load Balancing**: Multiple backend instances
3. **CDN**: Distribute static frontend assets
4. **Database Sharding**: Partition by user ID or short code prefix
5. **Message Queue**: Async click tracking with Kafka/RabbitMQ
6. **Rate Limiting**: Prevent abuse

---

## Technology Choices

| Component | Technology | Reason |
|-----------|-----------|---------|
| Backend Framework | Spring Boot | Enterprise-grade, extensive ecosystem |
| Database | PostgreSQL | ACID compliance, robust analytics |
| Authentication | JWT | Stateless, scalable |
| Payment | QR Code + UPI | No gateway fees, direct payments |
| QR Generation | ZXing | Open-source, reliable |
| Frontend | React | Component-based, large community |
| Build Tool | Vite | Fast development, optimized builds |
| Charts | Recharts | Declarative, React-friendly |

---

This architecture provides a solid foundation for a production-ready SaaS application with room for scaling and future enhancements.
