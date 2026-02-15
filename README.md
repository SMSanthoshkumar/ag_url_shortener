# URL Shortener SaaS

A production-ready URL shortener SaaS application with payment integration, analytics, and user authentication.

## 🚀 Features

- **User Authentication**: Secure signup/login with JWT tokens
- **Payment Integration**: QR code-based UPI payment system for URL generation
- **URL Shortening**: Generate unique short codes for long URLs
- **Click Tracking**: Track clicks with IP, user agent, and referrer data
- **Analytics Dashboard**: Date-wise click statistics with interactive charts
- **Responsive UI**: Modern, professional design with React

## 📋 Tech Stack

### Backend
- **Framework**: Spring Boot 3.2.0
- **Database**: PostgreSQL
- **Authentication**: JWT with BCrypt password hashing
- **Payment**: QR Code generation (ZXing)
- **API Documentation**: Swagger/OpenAPI
- **Build Tool**: Maven

### Frontend
- **Framework**: React 18 with Vite
- **Routing**: React Router v6
- **HTTP Client**: Axios
- **Charts**: Recharts
- **Styling**: Custom CSS

## 📁 Project Structure

```
ag_urlshortener/
├── backend/                 # Spring Boot application
│   ├── src/main/java/
│   │   └── com/urlshortener/
│   │       ├── config/      # Security, CORS, Swagger
│   │       ├── controller/  # REST controllers
│   │       ├── dto/         # Data Transfer Objects
│   │       ├── entity/      # JPA entities
│   │       ├── exception/   # Global exception handling
│   │       ├── repository/  # JPA repositories
│   │       ├── security/    # JWT utilities
│   │       └── service/     # Business logic
│   └── pom.xml
│
├── frontend/               # React application
│   ├── src/
│   │   ├── components/    # React components
│   │   ├── context/       # Auth context
│   │   └── services/      # API services
│   └── package.json
│
├── database/              # SQL schema
└── docs/                  # Documentation
```

## 🔧 Setup Instructions

### Prerequisites

- **Java 17** or higher
- **Node.js 18** or higher
- **PostgreSQL 14** or higher
- **Maven 3.6** or higher
- **UPI ID** (for receiving payments)

### Backend Setup

1. **Navigate to backend directory**:
   ```bash
   cd backend
   ```

2. **Configure Database**:
   - Create a PostgreSQL database:
     ```sql
     CREATE DATABASE urlshortener;
     ```
   - Update `src/main/resources/application.yml` with your credentials:
     ```yaml
     spring:
       datasource:
         url: jdbc:postgresql://localhost:5432/urlshortener
         username: YOUR_USERNAME
         password: YOUR_PASSWORD
     ```

3. **Configure Environment Variables**:
   ```yaml
   app:
     jwt:
       secret: YOUR_JWT_SECRET_KEY_AT_LEAST_256_BITS
     razorpay:
       key-id: YOUR_RAZORPAY_KEY_ID
       key-secret: YOUR_RAZORPAY_SECRET
   ```

4. **Build and Run**:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

   Backend will start at `http://localhost:8080`

5. **Access Swagger UI**:
   Open `http://localhost:8080/swagger-ui.html`

### Frontend Setup

1. **Navigate to frontend directory**:
   ```bash
   cd frontend
   ```

2. **Install dependencies**:
   ```bash
   npm install
   ```

3. **Configure environment**:
   - Copy `.env.example` to `.env`:
     ```bash
     cp .env.example .env
     ```
   - Update values if needed (default connects to `localhost:8080`)

4. **Run development server**:
   ```bash
   npm run dev
   ```

   Frontend will start at `http://localhost:5173`

### Database Setup

Run the schema in `database/schema.sql` to create all required tables:

```bash
psql -U YOUR_USERNAME -d urlshortener -f database/schema.sql
```

## 🔐 Environment Variables

### Backend (`application.yml`)

| Variable | Description | Example |
|----------|-------------|---------|
| `DATABASE_URL` | PostgreSQL connection URL | `jdbc:postgresql://localhost:5432/urlshortener` |
| `DATABASE_USERNAME` | Database username | `postgres` |
| `DATABASE_PASSWORD` | Database password | `your_password` |
| `JWT_SECRET` | Secret key for JWT (256+ bits) | `MyVerySecureJWTSecret...` |
| `UPI_ID` | Your UPI ID for payments | `yourname@paytm` |
| `MERCHANT_NAME` | Business/merchant name | `URL Shortener SaaS` |
| `PAYMENT_AMOUNT` | Amount in paise (100 = ₹1) | `100` |

### Frontend (`.env`)

| Variable | Description | Example |
|----------|-------------|---------|
| `VITE_API_BASE_URL` | Backend API URL | `http://localhost:8080/api` |
| `VITE_APP_BASE_URL` | Base URL for short links | `http://localhost:8080` |

## 💳 Payment Integration

This application uses **QR code-based UPI payments**:

1. Set your **UPI ID** in `application.yml` (e.g., `yourname@paytm`, `yourname@phonepe`)
2. Users scan the QR code with any UPI app (Google Pay, PhonePe, Paytm, etc.)
3. After payment, users confirm to generate their short URL
4. For testing, use your own UPI ID and make small test payments

## 📊 API Endpoints

### Authentication
- `POST /api/auth/signup` - Register new user
- `POST /api/auth/login` - Login user

### Payment
- `POST /api/payment/generate-qr` - Generate payment QR code (protected)
- `POST /api/payment/confirm` - Confirm payment completion (protected)

### URL Management
- `POST /api/url/shorten` - Create short URL (protected, requires payment)
- `GET /api/url/user` - Get user's URLs (protected)
- `GET /{shortCode}` - Redirect to original URL (public)

### Analytics
- `GET /api/analytics/{shortCode}` - Get URL analytics (protected)
- `GET /api/analytics/user` - Get user analytics (protected)

## 🚀 Deployment

See [docs/DEPLOYMENT.md](docs/DEPLOYMENT.md) for detailed deployment instructions.

### Quick Deploy Options

**Backend**:
- Render: Connect GitHub repo, set environment variables
- Railway: One-click deploy with PostgreSQL addon

**Frontend**:
- Vercel: `npm run build` → Deploy dist folder
- Netlify: Connect GitHub repo, build command: `npm run build`

**Database**:
- Supabase: Free PostgreSQL with built-in management
- Railway: Managed PostgreSQL with automatic backups

## 🧪 Testing

1. **Start backend and frontend**
2. **Sign up** for a new account
3. **Login** with credentials
4. **Create a short URL** (QR code will be displayed)
5. **Scan QR code** with your UPI app and pay
6. **Click confirm** after completing payment
6. **View dashboard** with your URLs
7. **Click on short URL** to test redirection
8. **Check analytics** for click tracking

## 📝 License

This project is for educational and demonstration purposes.

## 🤝 Contributing

This is a demo project showcasing a production-ready SaaS architecture.

## 📧 Support

For issues or questions, please check the documentation in the `docs/` folder.

---

**Built with ❤️ using Spring Boot & React**
