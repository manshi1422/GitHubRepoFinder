# 🔍 GitHub Repository Finder (Spring Boot + PostgreSQL)

A Spring Boot backend application that lets users search GitHub repositories using GitHub's public API, saves them to a PostgreSQL database, and provides filtering and sorting through custom REST API endpoints.

---

## 📌 Features

- ✅ Search GitHub repositories by name, language, and sort order (stars, forks, updated)
- ✅ Save repository details to PostgreSQL
- ✅ Update existing records if already saved (no duplicates)
- ✅ Filter stored repositories by:
  - Programming language
  - Minimum stars
  - Sort by stars, forks, or last update
- ✅ RESTful APIs
- ✅ JUnit test cases included

---

## 🧰 Tech Stack

- Java 17+
- Spring Boot 3.x
- Spring Data JPA
- PostgreSQL
- RestTemplate (for GitHub API)
- JUnit + Mockito

---

## 🚀 API Endpoints

### 🔹 1. Search and Save GitHub Repositories

**POST** `/api/github/search`/repositories

**Request Body:**

```json
{
  "query": "spring boot",
  "language": "Java",
  "sort": "stars"
}
```
### 🔹 1. Search and Save GitHub Repositories

**GET** `/api/github/repositories`

**Request Body:**

```json
{
  "query": "spring boot",
  "language": "Java",
  "sort": "stars"
}
```
