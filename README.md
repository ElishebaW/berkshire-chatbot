# Berkshire Chatbot

![Build Status](https://img.shields.io/github/actions/workflow/status/ElishebaW/berkshire-chatbot/ci.yml?branch=main)
![License](https://img.shields.io/github/license/ElishebaW/berkshire-chatbot)
![Java Version](https://img.shields.io/badge/java-21%2B-blue)

A full-stack AI-powered chatbot application with PDF embedding, built with Spring Boot (Java, Spring AI) and a modern Next.js frontend. Designed for seamless Q&A and document search using LLMs and vector stores.

---

## 🚀 Key Features

- **Conversational AI Chatbot**: Interact with an LLM-powered chatbot via a modern web UI
- **PDF Embedding**: Upload and embed PDF documents for knowledge ingestion
- **Chroma Vector Store Integration**: Uses Chroma for semantic search and retrieval
- **Spring AI Backend**: Leverages Spring AI and Ollama for LLM inference
- **Next.js Frontend**: Responsive, user-friendly React-based chat interface
- **Easy Local Development**: Simple setup for both backend and frontend

---

## 🛠️ Getting Started

### Prerequisites
- Java 21+
- Maven 3.8+
- Node.js 18+
- npm 9+

### 1. Clone the Repository
```sh
git clone https://github.com/ElishebaW/berkshire-chatbot.git
cd berkshire-chatbot
```

### 2. Start the Spring Boot Backend
```sh
mvn clean install
# If using a local build of Spring AI, ensure you have built and installed it first
mvn spring-boot:run
```
- Backend runs on `http://localhost:8080`

### 3. Start the Next.js Frontend
```sh
cd frontend
npm install
npm run dev
```
- Frontend runs on `http://localhost:3000`
- API requests are proxied to the backend

---

## 💬 Example Usage

- **Ask Questions:**
  1. Open [http://localhost:3000](http://localhost:3000)
  2. Type a question in the chat window
  3. Get instant answers from the AI (backend must be running)

- **Embed PDFs:**
  1. Click the "Embed PDF" button
  2. Select a PDF to upload
  3. The backend will ingest and index the document for semantic search

---

## 🧪 Running Tests

### Backend (Spring Boot)
```sh
mvn test
```

### Frontend (Next.js)
```sh
cd frontend
npm test
```

---

## 📁 Project Structure

```
berkshire-chatbot/
├── frontend/           # Next.js React frontend
│   ├── components/     # ChatWindow, EmbedButton, etc.
│   ├── pages/          # Next.js pages (index.js, _app.js)
│   └── ...
├── src/                # Spring Boot backend
│   └── main/
│       ├── java/
│       └── resources/
├── pom.xml             # Maven config for backend
├── README.md           # This file
└── ...
```

---

## 📝 Notes
- The backend uses Spring AI and may require a patched version for Chroma v2 support. See project issues for latest instructions.
- All sensitive files and build artifacts are excluded via `.gitignore`.
- For advanced usage (custom LLMs, vector stores), see the code and configuration files.

---

## 🤝 Contributing
Pull requests and issues are welcome!

---

## 📜 License
MIT