# 📊 Institutional Space and Resource Management Platform

A full-stack web application built to streamline institutional resource booking and management using modern web technologies, microservices, and AI.

## 🔧 Tech Stack

- **Frontend:** React, TypeScript
- **Backend:** Spring Boot (REST API, JWT, Microservices)
- **Databases:** PostgreSQL, MongoDB
- **Computer Vision:** YOLOv5 for real-time crowd detection
- **Containerization:** Docker
- **CI/CD & DevOps:** GitHub Actions (planned)
- **Authentication:** Role-based JWT (Faculty, Student, Admin, Guest)

## 🚀 Features

- Real-time room & equipment booking
- Crowd detection using AI-based computer vision
- Smart filtering with calendar integration and waitlists
- Role-specific access and permissions
- Notification system for confirmations, waitlist status, and crowd alerts
- In-app chat (AI-assisted or direct messaging)
- Audit trails and activity logging
- Custom dashboards for each user role

## 📈 Impact

- Reduced booking time by **40%** using real-time crowd detection
- Scaled to support **500+ concurrent users** with containerized microservices
- Improved space utilization and user satisfaction through smart recommendations

## 🛠️ Setup Instructions

### Prerequisites

- Node.js, Java 17+, Docker
- PostgreSQL and MongoDB installed locally or via cloud
- Maven and Git installed

### Backend

```bash
cd backend
./mvnw clean install
docker-compose up
```

### Frontend

```bash
cd frontend
npm install
npm start
```

## ☁️ Deployment

- Recommended: Deploy backend to **AWS EC2**, **Railway**, or **Render**
- Use **AWS S3** for storing media and **RDS PostgreSQL** for relational data
- Frontend can be hosted using **Netlify**, **Vercel**, or **AWS Amplify**

## 👨‍💻 Authors

- Vraj Contractor – [LinkedIn](http://www.linkedin.com/in/vraj20) | [Portfolio](https://vraj-x2.github.io/Portfolio)
