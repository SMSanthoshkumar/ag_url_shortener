# Frontend - React URL Shortener

This is the frontend application for the URL Shortener SaaS.

## Tech Stack

- **React 18**
- **Vite** (build tool)
- **React Router v6**
- **Axios** (HTTP client)
- **Recharts** (analytics visualization)
- **QR Code Display**

## Prerequisites

- Node.js 18+
- npm or yarn

## Quick Start

1. **Install Dependencies**:
   ```bash
   npm install
   ```

2. **Configure Environment**:
   Create `.env` file:
   ```
   VITE_API_BASE_URL=http://localhost:8080/api
   VITE_APP_BASE_URL=http://localhost:8080
   ```

3. **Run Development Server**:
   ```bash
   npm run dev
   ```

   App starts at: `http://localhost:5173`

## Available Scripts

- `npm run dev` - Start development server
- `npm run build` - Build for production
- `npm run preview` - Preview production build

## Project Structure

```
src/
├── components/
│   ├── auth/         # Login & Signup
│   ├── common/       # Navbar, PrivateRoute
│   ├── dashboard/    # Dashboard, UrlList, Analytics
│   └── url/          # UrlShortener
├── context/          # Auth context
├── services/         # API services
│   ├── api.js        # Axios instance
│   ├── authService.js
│   ├── urlService.js
│   └── paymentService.js
├── App.jsx           # Main app component
└── main.jsx          # Entry point
```

## Features

- User authentication (signup/login)
- QR code payment integration
- URL shortening with payment flow
- Dashboard with statistics
- Click analytics visualization
- Copy-to-clipboard functionality

## Environment Variables

| Variable | Description |
|----------|-------------|
| `VITE_API_BASE_URL` | Backend API URL |
| `VITE_APP_BASE_URL` | Base URL for short links |

## Production Build

```bash
npm run build
```

Output directory: `dist/`

## Deployment

See `../docs/DEPLOYMENT.md` for deployment to Vercel/Netlify.

## Design

The application uses a modern, gradient-based design with:
- Purple gradient color scheme
- QR code display for payments
- Smooth animations and transitions
- Responsive layout
- Interactive charts
