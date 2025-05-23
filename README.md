# 📰 News AI

A smart news aggregation and delivery platform that fetches and filters articles from multiple sources using AI, and sends personalized news to users via email.

## 🔧 Tech Stack

- **Backend:** Spring Boot (REST API)
- **AI Integration:** Google Gemini API
- **News Sources:** NewsAPI, GNews, Reddit, NYTimes (planned), AlphaVantage (planned)
- **Email Delivery:** Spring Mail (SMTP)
- **Validation:** Jakarta Validation API
- **Scheduler:** Spring Scheduler for periodic email delivery
- **Deployment:** Docker, GitHub Actions (CI/CD ready)

## 🚀 Features

- Accepts user input (email + topic) via REST endpoint
- Aggregates news from multiple APIs into a unified format
- Uses Gemini AI to filter and personalize articles based on user topic
- Sends formatted news via email on a schedule (daily/hourly)
- Filters subtopics using NLP for more relevant results
- Logs errors and responses for debugging

## 📈 Impact

- Personalized reading experience with Gemini AI filtering
- Centralized aggregation from multiple news sources
- Efficient automated email delivery using Spring Scheduler

## 🛠️ Setup Instructions

### Prerequisites

- Java 17+, Maven, Docker
- API keys for GNews, NewsAPI, and Gemini AI
- Valid SMTP configuration for email sending

### Run Locally

```bash
# Clone the repo
git clone https://github.com/Vraj-x2/news-ai.git
cd news-ai

# Add API keys to application.properties
# Run the app
./mvnw spring-boot:run
```

## ☁️ Deployment

- Use **Render**, **Railway**, or **AWS EC2** for hosting the backend
- Dockerize the app using the provided Dockerfile
- Use **GitHub Actions** for automated testing and deployment

## 👨‍💻 Author

- Vraj Contractor – [LinkedIn](http://www.linkedin.com/in/vraj20) | [Portfolio](https://vraj-x2.github.io/Portfolio)
