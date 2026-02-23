# Mockup API Server - Spring Boot

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.2-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-25-orange.svg)](https://www.oracle.com/java/)
[![License](https://img.shields.io/badge/License-Academic-blue.svg)]()

Un servidor de APIs simuladas (mockup) desarrollado con Spring Boot para la práctica universitaria del curso de Programación Web Avanzada (ICC-354) de la Pontificia Universidad Católica Madre y Maestra.

A powerful Spring Boot-based application for creating, managing, and executing mock API endpoints.

## 📚 Documentation

Comprehensive documentation is available in the [doc/](doc/) directory:

| Document | Description |
|----------|-------------|
| **[README.md](doc/README.md)** | Project overview, features, installation, and quick start guide |
| **[ARCHITECTURE.md](doc/ARCHITECTURE.md)** | System architecture, design patterns, and technical decisions |
| **[DATABASE.md](doc/DATABASE.md)** | Database schema, entities, relationships, and queries |
| **[API.md](doc/API.md)** | REST API reference with request/response examples |
| **[USER_GUIDE.md](doc/USER_GUIDE.md)** | Step-by-step user guide with FAQ and troubleshooting |
| **[SECURITY.md](doc/SECURITY.md)** | Security implementation, JWT, and best practices |
| **[TESTING.md](doc/TESTING.md)** | Testing strategy, test types, and guidelines |
| **[DEPLOYMENT.md](doc/DEPLOYMENT.md)** | Production deployment guide and configuration |
| **[CHANGELOG.md](doc/CHANGELOG.md)** | Version history and release notes |

## 🚀 Quick Start

```bash
# Clone repository
git clone <repository-url>
cd primera_practica

# Run application
./gradlew bootRun

# Access at http://localhost:8080
# Login: admin / admin
```

**Dev/Test token endpoint**

Enable the development-only JWT helper by starting the app with the `dev` or `test` profile (for example `SPRING_PROFILES_ACTIVE=dev ./gradlew bootRun`). It exposes `GET /api/dev/jwt` to return a JWT for the currently authenticated user and is only active under those profiles. The UI only reveals the token on `/mocks/{id}` when the mock requires JWT and you press **Get JWT Token**.


## 🐳 Docker & Docker Compose

El proyecto incluye una configuración completa con Docker Compose para levantar:

- **app**: aplicación Spring Boot
- **db**: MySQL oficial (sin exponer puertos al host)
- **db-admin**: phpMyAdmin para administración web de la base de datos

### Variables de ambiente

1. Copia el archivo de ejemplo:

```bash
cp .env.example .env
```

2. Ajusta los valores requeridos en `.env` (puertos, credenciales, ruta del volumen y nombre de imagen publicada en Docker Hub).

### Levantar el escenario

```bash
docker compose up -d --build
```

- App: `http://localhost:${APP_PORT:-8080}`
- phpMyAdmin: `http://localhost:${DB_ADMIN_PORT:-8082}`
- La base de datos queda accesible **solo dentro de la red de Docker Compose**.

### Publicación en Docker Hub

Antes de desplegar en otro ambiente, publica la imagen de la app en Docker Hub:

```bash
docker build -t <tu-usuario-dockerhub>/mockup-api-server:latest .
docker push <tu-usuario-dockerhub>/mockup-api-server:latest
```

Luego define `APP_IMAGE` en `.env` con esa imagen publicada.

## 🔑 Key Features

- ✅ **Mock API Management** - Create and manage mock endpoints with custom responses
- ✅ **JWT Authentication** - Optional JWT protection for mock endpoints  
- ✅ **Response Configuration** - Custom status codes, headers, delays, and expiration
- ✅ **User Management** - Role-based access control (Admin/User)
- ✅ **Project Organization** - Group endpoints by projects
- ✅ **Web Interface** - Intuitive Bootstrap 5 UI for CRUD operations
- ✅ **Dynamic Execution** - REST API for executing mock endpoints
- ✅ **Internationalization** - Spanish and English support (i18n)
- ✅ **H2 Database** - In-memory database with console access
- ✅ **Complete Documentation** - 9 comprehensive documents

## 📖 Documentation Guide

**New to the project?** Start with [doc/README.md](doc/README.md)

**Want to understand the architecture?** Read [doc/ARCHITECTURE.md](doc/ARCHITECTURE.md)

**Need to deploy?** Follow [doc/DEPLOYMENT.md](doc/DEPLOYMENT.md)

**Looking for API reference?** Check [doc/API.md](doc/API.md)

**End user?** See [doc/USER_GUIDE.md](doc/USER_GUIDE.md)

## 🛠️ Technology Stack

- **Spring Boot 4.0.2** - Application framework
- **Java 25** - Programming language
- **Spring Security 6.4** - Authentication and authorization
- **JWT (JJWT 0.12.6)** - Token-based authentication
- **JPA/Hibernate 7.2** - ORM persistence
- **H2 Database 2.4** - In-memory database
- **Thymeleaf 3.1** - Template engine
- **Bootstrap 5.3** - CSS framework
- **Lombok** - Boilerplate reduction
- **Gradle 9.3** - Build tool

## 📄 License

Este proyecto es una práctica académica para la Pontificia Universidad Católica Madre y Maestra (PUCMM).

**Desarrollado con ❤️ como proyecto académico**
