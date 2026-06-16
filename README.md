# Smart Task Management System

A collaborative, workspace-centric task management application built with **Spring Boot 3** (Java 19), **Spring Security + JWT Authentication**, and a modern **React + Vite** frontend.

## 🚀 Key Features

* **JWT-Based Authentication**: Secure registration and login flow.
* **User-Specific Workspaces**: Users see only their own projects and tasks by default.
* **Workspace Collaboration**: 
  * Project owners can invite/add other registered users as members to their projects.
  * Multi-member access to shared projects.
* **Task Assignment**: Tasks belong to projects and can be assigned to any member of that project.
* **Interactive Dashboard**: Dynamically computes statistics (total projects, completed/pending tasks, progress bar) for the logged-in user.
* **Clean & Modern UI**: Responsive SaaS-style layout with elegant badges, tooltips, and transitions.

---

## 🛠️ Technology Stack

### Backend
* **Spring Boot 3.3.0** & **Java 17+**
* **Spring Security & Spring Data JPA**
* **JWT (JSON Web Tokens)** for stateless authentication
* **H2 Database** (in-memory, with auto-seeding via `import.sql`)

### Frontend
* **React 18** (Vite-powered)
* **React Router DOM v7** for routing
* **Axios** for API communication
* **Vanilla CSS** with a custom light-theme design system

---

## 💻 Getting Started

### Prerequisites
* Java JDK 17 or higher
* Node.js v18+ and npm
* Maven

### Run the Backend
1. Navigate to the root directory of the project:
   ```bash
   cd "smart task2"
   ```
2. Compile and run the Spring Boot application using Maven:
   ```bash
   mvn spring-boot:run
   ```
3. The server will start on [http://localhost:8080](http://localhost:8080).
4. The H2 Console is available at [http://localhost:8080/h2-console](http://localhost:8080/h2-console) (JDBC URL: `jdbc:h2:mem:taskdb`, username: `sa`, password: `<blank>`).

### Run the Frontend
1. Navigate to the `frontend` folder:
   ```bash
   cd frontend
   ```
2. Install dependencies:
   ```bash
   npm install
   ```
3. Start the Vite development server:
   ```bash
   npm run dev
   ```
4. Open your browser to [http://localhost:5173](http://localhost:5173).

---

## 🔑 Seeding / Demo Credentials
Upon database startup, the following demo users are seeded via `import.sql`:
1. **John Doe**: `john.doe@example.com` / `password`
2. **Jane Smith**: `jane.smith@example.com` / `password`
