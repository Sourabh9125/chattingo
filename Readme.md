# 🚀 Chattingo Hackathon

A full-stack real-time chat application built with React, Spring Boot, and WebSocket technology. **Your mission**: Containerize this application using Docker and deploy it to Hostinger VPS using Jenkins CI/CD pipeline.


# 🚀 Chattingo - Full Stack Chat Application

This is a full-stack real-time chat application built with:
- Frontend → React (served via Nginx)
- Backend → Spring Boot (REST APIs + WebSockets)
- Database → MySQL
- Containerization → Docker & Docker Compose
- CI/CD → Jenkins Pipeline
- SSL/HTTPS → Nginx reverse proxy with Let’s Encrypt

# 🚀 Prerequisites
- A VM/VPS with Docker & Docker Compose (v2+) installed.
- Domain chattingo.deployit.shop pointed to your server IP (A record).
- Ports 80 and 443 open.
- Jenkins installed (for CI/CD)

🐳 Docker Setup

👉 Frontend Dockerfile
The frontend Dockerfile follows a multi-stage build approach:

👉 Build Stage:
Uses a lightweight Node.js image.
Installs dependencies and builds the React app into static files.

👉 Production Stage:
Uses a minimal Nginx image to serve the static files.
Exposes port 80 for HTTP traffic.
Starts Nginx in the foreground to serve the app.

👉 Backend Dockerfile
The backend Dockerfile also uses a multi-stage build:

👉 Build Stage:
Uses a Java JDK image to compile the Spring Boot application.
Downloads dependencies offline and packages the app into a single executable JAR.

👉 Production Stage:
Uses a lightweight JRE image to run the application.
Adds a non-root user for better security.
Sets environment variables for Spring profiles and database connection.
Runs the Spring Boot application using java -jar.

# Why Multi-Stage Builds?
Reduces final image size
Improves security (removes unnecessary build tools)
Separates build environment from runtime environment
Makes deployment faster and more efficient

🐳 Docker Compose Setup

# Services Overview

👉 Nginx (Reverse Proxy):
Routes traffic to frontend and backend services.
Handles HTTPS termination with Let’s Encrypt certificates.
Depends on frontend and backend to be running before starting.

👉 Frontend:
Built from the React app using a multi-stage Dockerfile.
Serves the compiled static files via Nginx.
Runs in its own isolated container connected to the chattingo-app network.

👉 Backend:
Built from Spring Boot using a multi-stage Dockerfile.
Connects to MySQL database with environment variables for credentials and connection settings.
Runs under a non-root user for security and restarts automatically if it fails.

👉 MySQL:
Provides a persistent database for the application.
Health-checked to ensure the backend starts only when the database is ready.
Data persisted in a Docker volume for durability across container restarts.

👉 Networks & Volumes:
All containers share the chattingo-app network for secure communication.
Database data is persisted in springboot_data volume.

# Jenkins CI/CD Pipeline ⚙️

The project uses Jenkins for continuous integration and deployment:
Pipeline Highlights

👉 Clean Workspace:
Ensures old files are removed before starting a new build.

👉 Code Cloning:
Pulls the latest code from the GitHub repository development branch.

👉 Build Docker Images:
Frontend Image: Built from frontend directory.
Backend Image: Built from backend directory.
Uses dynamic version tagging with BUILD_NUMBER.
Optional DockerHub Push: (commented for hackathon demo)
Pushes built images to DockerHub under the user sourabhlodhi.

👉 Testing:
Runs project-specific test scripts (currently placeholder echo step).
Docker Compose Deployment:
Uses Docker Compose to start all services together, ensuring proper order and dependencies.

✅ Benefits:
Full containerized environment ensures the app runs the same on all machines.
Automated CI/CD with Jenkins reduces manual deployment effort.
Multi-stage Dockerfiles and volumes make images lightweight and data persistent.
Secure configuration with HTTPS and non-root backend execution.

# Nginx Reverse Proxy Configuration 🌐
The Chattingo project uses Nginx as a reverse proxy to route traffic to the frontend and backend, enforce HTTPS, and optimize performance.

Key Features
- HTTP → HTTPS Redirection:
All traffic on port 80 is automatically redirected to HTTPS on port 443 for secure communication.
- Frontend Routing:
Requests to the root path (/) are forwarded to the React frontend container.
- Backend API Routing:
Requests starting with /api/ are forwarded to the Spring Boot backend container.
- SSL Certificates:
Uses Let’s Encrypt certificates for secure HTTPS communication.
- Proxy Headers:
Preserves client information such as original IP and protocol using standard headers (Host, X-Real-IP, X-Forwarded-For, X-Forwarded-Proto).
- Performance Settings:
Configured worker connections ensure the server can handle multiple simultaneous connections efficiently.


🚀 Deployment
Clone repo: 
git clone https://github.com/Sourabh9125/chattingo.git
cd chattingo 
git checkout development

Start services:
docker-compose up -d --build

🏆 Hackathon Highlights
✅ Deployment via Docker Compose
✅ Secure HTTPS with Let’s Encrypt
✅ CI/CD with Jenkins
✅ Scalable, production-ready setup
