# Docker Setup Guide for LMS Catalogue

This project is configured with Docker for easy development and deployment.

## Prerequisites

- [Docker](https://docs.docker.com/get-docker/)
- [Docker Compose](https://docs.docker.com/compose/install/)

## Quick Start

### Build and Run All Services

```bash
# Build all services
docker-compose build

# Start all services (MySQL, Backend, Frontend)
docker-compose up -d

# View logs
docker-compose logs -f

# Stop all services
docker-compose down
```

### Services

1. **MySQL Database** (Port 3306)
   - Database: `lms_catalog`
   - User: `lms_app`
   - Password: `lms_app_password`
   - Volume: `mysql_data` (persistent storage)

2. **Spring Boot Backend** (Port 8080)
   - API endpoints: http://localhost:8080/api/*
   - Health check: http://localhost:8080/actuator/health
   - Database: Connected to MySQL service

3. **React Frontend** (Port 5173)
   - Frontend: http://localhost:5173
   - Vite dev server with HMR

## Detailed Commands

### Database Setup

```bash
# Start only MySQL
docker-compose up -d mysql

# Initialize the database with migrations
# (Automatically runs on startup via Flyway)
```

### Backend Development

```bash
# Build backend only
docker-compose build backend

# Run backend only
docker-compose up -d backend

# View backend logs
docker-compose logs -f backend

# Connect to backend container
docker-compose exec backend sh
```

### Frontend Development

```bash
# Build frontend only
docker-compose build frontend

# Run frontend only
docker-compose up -d frontend

# View frontend logs
docker-compose logs -f frontend
```

### All Services

```bash
# Start all services with output
docker-compose up

# Start all services in background
docker-compose up -d

# View all services status
docker-compose ps

# View all logs
docker-compose logs -f

# Restart a service
docker-compose restart backend

# Stop all services
docker-compose stop

# Remove all containers and networks
docker-compose down

# Remove all containers, networks, and volumes
docker-compose down -v
```

## Environment Variables

### Backend (`application.yml` in container)

```yaml
spring:
  datasource:
    url: jdbc:mysql://mysql:3306/lms_catalog
    username: lms_app
    password: lms_app_password
  jpa:
    hibernate:
      ddl-auto: validate
```

### Frontend

- `VITE_API_URL`: Backend API base URL (http://backend:8080)

## Troubleshooting

### Database Connection Issues

```bash
# Check if MySQL is running
docker-compose logs mysql

# Verify database is initialized
docker-compose exec mysql mysql -u lms_app -p lms_catalog -e "SHOW TABLES;"
```

### Backend Won't Start

```bash
# Check backend logs
docker-compose logs backend

# Ensure MySQL is healthy
docker-compose ps mysql

# Restart the stack
docker-compose down && docker-compose up -d
```

### Frontend Build Issues

```bash
# Clear node_modules and rebuild
docker-compose build --no-cache frontend
docker-compose up -d frontend
```

## Production Deployment

For production, consider:

1. Using environment-specific compose files (`docker-compose.prod.yml`)
2. Setting up health checks with proper monitoring
3. Using Docker secrets for sensitive data
4. Implementing log aggregation
5. Setting up reverse proxy (nginx)
6. Using external database management
7. Implementing CI/CD with Docker image registries

## Docker Images

### Base Images Used

- **Backend**: `maven:3.9-eclipse-temurin-21` (build) → `eclipse-temurin:21-jre-alpine` (runtime)
- **Frontend**: `node:20-alpine` (multi-stage build)
- **MySQL**: `mysql:8.4`

These Alpine-based images keep container sizes small and secure.
