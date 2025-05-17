# 📰 News AI - Smart News Aggregator

News AI is a full-stack Spring Boot application that collects news from **5+ APIs**, filters and summarizes it using **Gemini AI**, and delivers top articles to users via email — all through a simple web form.

This project showcases real-world integration of AI, multi-source data aggregation, and email delivery with a modern full-stack architecture.

---

## ✨ Features

- 🔄 Aggregates news from:
  - GNews
  - NewsAPI
  - Reddit (public search)
  - NYTimes
  - AlphaVantage (news sentiment)
- 🧠 Filters and summarizes content using Google **Gemini AI**
- 📬 Sends top 5 relevant articles to your inbox
- 💌 Styled HTML email output with clickable links and summaries
- 🖥️ Simple web interface built with Thymeleaf
- 🔐 Secrets hidden using `.gitignore` + external properties file

---

## 🧰 Tech Stack

- **Backend:** Spring Boot 3, Java 21
- **Frontend:** Thymeleaf + HTML/CSS
- **AI:** Google Gemini API
- **Email:** Spring Mail (Gmail SMTP)
- **Build Tool:** Maven
- **Database:** H2 (for optional storage/logging)

---

## 🛠️ Local Setup

### 1. Clone the Repository

```bash
git clone https://github.com/Vraj-x2/news-ai.git
cd news-ai
```

---

### 2. Create `application-secrets.properties`

Create the following file inside:

```
src/main/resources/application-secrets.properties
```

Add your API keys and Gmail credentials here:

```properties
# Mail
spring.mail.username=your_email@gmail.com
spring.mail.password=your_app_password

# APIs
gnews.api.key=your_gnews_key
newsapi.key=your_newsapi_key
nytimes.api.key=your_nytimes_key
alphavantage.key=your_alphavantage_key
reddit.client.id=your_reddit_client_id
reddit.client.secret=your_reddit_client_secret
gemini.api.key=your_gemini_api_key
```

✅ **Do not commit this file.** It's ignored via `.gitignore`.

---

### 3. Run the App

```bash
./mvnw spring-boot:run
```

Or in an IDE: Run `NewsAiApplication.java`

---

## 🌐 Usage

1. Open [http://localhost:8080](http://localhost:8080)
2. Enter your email and a topic (e.g. `AI`, `Elon Musk`, `Samsung`)
3. Check your inbox for top 5 news summaries, powered by Gemini AI

---

## 📦 Deployment Ideas

- Use **Render**, **Railway**, or **AWS EC2** for hosting the backend
- Add React frontend for modern UX
- Use GitHub Actions for CI/CD

---

## 📷 Screenshots (optional)

> You can drop `index.html` + email output previews here if you'd like

---

## 👨‍💻 Author

**Vraj Contractor**  
[GitHub](https://github.com/Vraj-x2) | [Portfolio](https://vraj-x2.github.io/Portfolio)

---

## 📄 License

This project is open source and available under the [MIT License](LICENSE).
