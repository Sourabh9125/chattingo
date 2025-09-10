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

# Project Structure
  chattingo/
├── frontend/
│   ├── Dockerfile       # React build + Nginx
│   ├── nginx.conf       # Reverse proxy config
├── backend/
│   ├── Dockerfile       # Spring Boot backend
│   ├── src/...
├── docker-compose.yml   # Orchestrates services
├── Jenkinsfile          # CI/CD pipeline

#🐳 Docker Setup

#Frontend Dockerfile

# Stage 1: Build React app
FROM node:20-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

# Stage 2: Serve with Nginx
FROM nginx:alpine
COPY --from=build /app/build /usr/share/nginx/html
EXPOSE 80

# Backend Dockerfile (backend/Dockerfile)

# Stage 1
FROM eclipse-temurin:17-jdk-alpine AS build 
WORKDIR /app
COPY pom.xml ./
COPY . .
RUN ./mvnw dependency:go-offline
RUN ./mvnw clean package -DskipTests=true

# Stage 2
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
RUN addgroup --system user && adduser --system --ingroup user user
USER user
CMD ["java", "-jar", "app.jar"]

#Docker Compose
We orchestrate all services with docker-compose.yml.

services:
  nginx:
    image: nginx:alpine
    ports:
      - "80:80"
      - "443:443"
    volumes:
      - ./nginx.conf:/etc/nginx/nginx.conf:ro
      - /etc/letsencrypt:/etc/letsencrypt:ro
    depends_on:
      - frontend
      - backend
    networks:
      - chattingo-app

  frontend:
    build:
      context: ./frontend/.
    container_name: frontend
    networks:
      - chattingo-app

  backend:
    build:
      context: ./backend/.
    container_name: backend
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/chattingo_db?createDatabaseIfNotExist=true&allowPublicKeyRetrieval=true&useSSL=false
      SPRING_PROFILES_ACTIVE: development
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: root
      JWT_SECRET: super-secret-key
    depends_on:
      mysql: 
        condition: service_healthy
    restart: always
    networks:
      - chattingo-app

  mysql:
    image: mysql:latest
    container_name: mysql
    networks:
      - chattingo-app
    volumes:
      - springboot_data:/var/lib/mysql
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: chattingo_db
    ports:
      - "3306:3306"
    healthcheck:
      test: ["CMD","mysqladmin","ping","-h","localhost","-uroot","-proot"]
      interval: 10s
      retries: 5
      start_period: 60s
      timeout: 5s
     
volumes:
  springboot_data:

networks:
  chattingo-app:
    name: chattingo-app
# Nginx Reverse Proxy + SSL
🤖 Jenkins Pipeline (CI/CD)
@Library ('Shared') _
pipeline {
    agent any
    environment{
        GIT_URL = "https://github.com/Sourabh9125/chattingo.git/"
        GIT_BRANCH = "development"
        DOCKER_HUB_USERNAME = "sourabhlodhi"
        DOCKER_IMAGE_TAG = "V${BUILD_NUMBER}"
        DOCKER_FRONTEND_IMAGE = "chattingo-frontend"
        DOCKER_BACKEND_IMAGE = "chattingo-backend"
    }
    stages {
        stage('Clean Worksapce') {
            steps {
                script{
                    clean_ws()
                }
            }
        }
        stage('Code Cloning') {
            steps {
                script{
                    clone(env.GIT_URL, env.GIT_BRANCH)
                }
            }
        }
        stage("Build image"){
            parallel{
                 stage("Build Frontend Image"){
                    steps{
                        script{
                            docker_build(
                                imageName: env.DOCKER_FRONTEND_IMAGE,
                                imageTag: env.DOCKER_IMAGE_TAG,
                                context: "frontend",
                                dockerfile: "frontend/Dockerfile",
                                dockerHubUser: env.DOCKER_HUB_USERNAME
                                )
                        }
                    }
            }
                 stage("Build Backend Image"){
                   steps{
                       script{
                           docker_build(
                               imageName: env.DOCKER_BACKEND_IMAGE,
                               imageTag: env.DOCKER_IMAGE_TAG,
                               context: "backend",
                               dockerfile: "backend/Dockerfile",
                               dockerHubUser: env.DOCKER_HUB_USERNAME
                               )
                       }
                   }
               }
        }
    }
        stage("Testing the code"){
            steps{
                script{
                    echo "Testing the code"
                }
            }
        }
        stage("Docker compose"){
            steps{
               script{
                   docker_compose()
               }
            }
        }
  }
}

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
