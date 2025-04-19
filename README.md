# 🔗 URL Shortener + Analytics

A powerful URL shortener service with built-in analytics. Built using **Spring Boot**, **Redis**, **MongoDB**, and **React.js/Vite**.

## 🌟 Features

- 🔐 **JWT Authentication** – Secure access to the dashboard.
- ✂️ **Short URL Generation** – Shorten long URLs with unique short codes.
- 📊 **Click Analytics** – Track:
  - IP Address
  - Geolocation (City & Country)
  - Device Type (Desktop/Mobile)
  - Timestamp
- 🌐 **MongoDB Atlas** – For cloud-based database storage.
- ⚡ **Redis** – Caching short URLs for fast redirects.
- 💻 **Modern Frontend** – Built with React.js + Vite for speed and responsiveness.

---

## 🛠 Tech Stack

### Backend
- Java 17
- Spring Boot
- Spring Security + JWT
- Redis
- MongoDB (Atlas)
- Maven

### Frontend
- React.js + Vite
- Axios
- Bootstrap / CSS Modules

---

## 🧪 API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/auth/register` | Register a new user |
| `POST` | `/api/auth/login` | Login and receive JWT |
| `POST` | `/api/url` | Create short URL *(JWT required)* |
| `GET`  | `/{shortCode}` | Redirect to original URL & log analytics |
| `GET`  | `/api/analytics/{shortCode}` | View analytics *(JWT required)* |

---

## 🚀 Getting Started

### 📦 Backend Setup
```bash
cd backend
./mvnw spring-boot:run
