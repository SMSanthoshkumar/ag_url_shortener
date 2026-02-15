# Backend Deployment Guide

## Deployment Options

### Option 1: Deploy to Render

Render provides easy deployment for Spring Boot applications with free PostgreSQL.

#### Steps:

1. **Create Render Account**:
   - Visit [https://render.com](https://render.com)
   - Sign up and create a new account

2. **Create PostgreSQL Database**:
   - Click "New +" → "PostgreSQL"
   - Choose a name (e.g., `urlshortener-db`)
   - Select free tier
   - Click "Create Database"
   - Copy the **Internal Database URL**

3. **Deploy Spring Boot App**:
   - Click "New +" → "Web Service"
   - Connect your GitHub repository
   - Configure:
     - **Name**: `urlshortener-api`
     - **Environment**: `Java`
     - **Build Command**: `mvn clean install`
     - **Start Command**: `java -jar target/url-shortener-saas-1.0.0.jar`
     - **Instance Type**: Free

4. **Set Environment Variables**:
   ```
   DATABASE_URL=<your-render-postgres-url>
   JWT_SECRET=<your-strong-secret-key>
   UPI_ID=<your-upi-id>
   MERCHANT_NAME=URL Shortener SaaS
   BASE_URL=https://your-app.onrender.com
   ```

5. **Deploy**:
   - Click "Create Web Service"
   - Wait for build to complete (~5-10 minutes)
   - Your API will be live at `https://your-app.onrender.com`

---

### Option 2: Deploy to Railway

Railway offers seamless deployment with automatic PostgreSQL provisioning.

#### Steps:

1. **Create Railway Account**:
   - Visit [https://railway.app](https://railway.app)
   - Sign up with GitHub

2. **Create New Project**:
   - Click "New Project"
   - Select "Deploy from GitHub repo"
   - Connect your repository
   - Select `backend` folder as root

3. **Add PostgreSQL**:
   - Click "New" → "Database" → "PostgreSQL"
   - Railway auto-generates connection URL

4. **Configure Environment**:
   - Go to your service → "Variables"
   - Add:
     ```
     DATABASE_URL=${{Postgres.DATABASE_URL}}
     JWT_SECRET=<your-secret>
     UPI_ID=<your-upi-id>
     MERCHANT_NAME=URL Shortener SaaS
     BASE_URL=${{RAILWAY_PUBLIC_DOMAIN}}
     ```

5. **Configure Build**:
   - Railway auto-detects Maven
   - Build command: `mvn clean package`
   - Start command: `java -jar target/*.jar`

6. **Deploy**:
   - Railway automatically deploys on push
   - Domain: `https://your-app.up.railway.app`

---

## Database Deployment

### Option 1: Supabase (Recommended)

1. Visit [https://supabase.com](https://supabase.com)
2. Create new project
3. Go to "Database" → "Connection String"
4. Copy **JDBC URL**
5. Use in `DATABASE_URL` environment variable
6. Run schema from `database/schema.sql` in SQL Editor

### Option 2: Railway Postgres

- Included automatically with Railway deployment
- Access via Railway dashboard
- Connection pooling enabled by default

### Option 3: ElephantSQL

1. Visit [https://www.elephantsql.com](https://www.elephantsql.com)
2. Create free "Tiny Turtle" plan
3. Copy connection URL
4. Use in `DATABASE_URL` environment variable

---

## Frontend Deployment

### Option 1: Vercel (Recommended)

1. **Install Vercel CLI**:
   ```bash
   npm i -g vercel
   ```

2. **Build Frontend**:
   ```bash
   cd frontend
   npm run build
   ```

3. **Deploy**:
   ```bash
   vercel
   ```

4. **Set Environment Variables** in Vercel Dashboard:
   ```
   VITE_API_BASE_URL=https://your-backend.onrender.com/api
   VITE_APP_BASE_URL=https://your-backend.onrender.com
   ```

5. **Redeploy** after setting variables

---

### Option 2: Netlify

1. **Create `netlify.toml`** in frontend folder:
   ```toml
   [build]
     command = "npm run build"
     publish = "dist"

   [[redirects]]
     from = "/*"
     to = "/index.html"
     status = 200
   ```

2. **Deploy via Netlify CLI** or GitHub:
   ```bash
   npm run build
   netlify deploy --prod --dir=dist
   ```

3. **Set Environment Variables** in Netlify Dashboard

---

## Post-Deployment Steps

### 1. Update CORS Configuration

In `backend/src/main/java/com/urlshortener/config/CorsConfig.java`:

```java
configuration.setAllowedOrigins(Arrays.asList(
    "https://your-frontend.vercel.app",
    "http://localhost:5173"
));
```

### 2. Configure UPI for Production

- Use your business UPI ID for production
- Update `UPI_ID` and `MERCHANT_NAME` in environment variables
- Test QR code generation and payment flow

### 3. Setup SSL/HTTPS

- Most platforms (Render, Railway, Vercel, Netlify) provide SSL automatically
- Ensure all URLs use `https://`

### 4. Monitor Application

- **Render**: Built-in logs and metrics
- **Railway**: Real-time logs in dashboard  
- **Vercel/Netlify**: Analytics and performance metrics

---

## Environment Variables Reference

### Backend (Production)

```yaml
# Database
DATABASE_URL=jdbc:postgresql://host:5432/dbname
DATABASE_USERNAME=username
DATABASE_PASSWORD=password

# Security
JWT_SECRET=your-256-bit-secret-key
JWT_EXPIRATION=86400000

# Payment
UPI_ID=yourbusiness@bank
MERCHANT_NAME=Your Business Name

# Application
BASE_URL=https://your-backend.com
PAYMENT_AMOUNT=100
PORT=8080
```

### Frontend (Production)

```
VITE_API_BASE_URL=https://your-backend.com/api
VITE_APP_BASE_URL=https://your-backend.com
```

---

## Troubleshooting

### Backend won't start
- Check database connection URL
- Verify all environment variables are set
- Check logs for specific errors

### Frontend can't connect to backend
- Verify `VITE_API_BASE_URL` is correct
- Check CORS configuration on backend
- Ensure backend is running

### Payment not working
- Verify UPI ID is correct and active
- Check if QR code is being generated properly
- Ensure ZXing library is loaded correctly

---

## Cost Estimates

### Free Tier Deployment

| Service | Free Tier | Limits |
|---------|-----------|--------|
| Render (Backend) | ✅ | 750 hours/month, sleeps after 15 min |
| Railway (DB) | ✅ | $5 credit/month |
| Vercel (Frontend) | ✅ | 100 GB bandwidth |
| Supabase (DB) | ✅ | 500 MB storage |

**Total Cost**: $0/month for development and small-scale production

---

**Need help?** Check the main README.md or open an issue.
