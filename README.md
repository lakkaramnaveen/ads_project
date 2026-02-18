# Ad Services Backend API - Microservices Architecture

A scalable, production-ready Ad Tech backend API system designed to serve big tech clients like Walmart Ad Tech API. This project demonstrates a microservices architecture using Spring Boot, following industry best practices.

## Architecture Overview

```
┌─────────────────────────────────────────────────────────────┐
│                    API Gateway (Spring Cloud Gateway)       │
│                    - Routing & Load Balancing               │
│                    - Authentication & Authorization         │
└──────────────────────┬──────────────────────────────────────┘
                       │
        ┌──────────────┼──────────────┐
        │              │              │
┌───────▼──────┐ ┌────▼──────┐ ┌─────▼──────┐
│   Campaign   │ │   Ad      │ │  Analytics │
│   Service    │ │  Serving  │ │  Service   │
│              │ │  Service  │ │            │
└───────┬──────┘ └────┬──────┘ └─────┬──────┘
        │             │              │
        └─────────────┼──────────────┘
                      │
        ┌─────────────┼──────────────┐
        │             │              │
┌───────▼──────┐ ┌────▼──────┐ ┌─────▼──────┐
│    User      │ │  Kafka    │ │  Database  │
│  Targeting   │ │  Message  │ │  Layer     │
│   Service    │ │   Queue   │ │ (MySQL/    │
│              │ │           │ │ MongoDB/   │
│              │ │           │ │ Cassandra) │
└──────────────┘ └───────────┘ └────────────┘
```

## Microservices

### 1. **API Gateway Service**
- Entry point for all client requests
- Handles routing, load balancing, and authentication
- Implements rate limiting and circuit breakers

### 2. **Campaign Service**
- Manages ad campaigns (create, update, delete, retrieve)
- Campaign targeting rules and budget management
- Uses MySQL for transactional data

### 3. **Ad Serving Service**
- Core ad serving logic - matches ads to user requests
- Real-time ad selection based on targeting criteria
- High-performance, latency-critical operations
- Uses Redis for caching and Cassandra for high-volume writes

### 4. **Analytics Service**
- Tracks impressions, clicks, conversions
- Real-time analytics and reporting
- Uses Kafka for event streaming
- Stores analytics data in MongoDB

### 5. **User Targeting Service**
- Manages user profiles and targeting data
- User behavior analysis and segmentation
- Uses Cassandra for scalable user data storage
- Integrates with Cosmos DB patterns

## Technology Stack

- **Languages**: Java 17, Spring Boot 3.x
- **Microservices**: Spring Cloud (Gateway, Eureka, Config)
- **Databases**: MySQL, MongoDB, Cassandra, Redis
- **Message Queue**: Apache Kafka
- **Security**: OAuth2, JWT
- **API Documentation**: Swagger/OpenAPI
- **Containerization**: Docker, Kubernetes
- **CI/CD**: Jenkins, GitLab CI
- **Cloud**: AWS (EC2, S3, Lambda)

## Getting Started

### Prerequisites
- Java 17+
- Maven 3.8+
- Docker & Docker Compose
- Kafka (or use Docker)

### Running Locally

1. **Start Infrastructure Services**:
```bash
docker-compose up -d
```

2. **Start Microservices** (in order):
```bash
# Start Eureka Discovery Server
cd discovery-service && mvn spring-boot:run

# Start Config Server
cd config-service && mvn spring-boot:run

# Start all other services
cd campaign-service && mvn spring-boot:run
cd ad-serving-service && mvn spring-boot:run
cd analytics-service && mvn spring-boot:run
cd user-targeting-service && mvn spring-boot:run
cd api-gateway && mvn spring-boot:run
```

3. **Access Services**:
- API Gateway: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- Eureka Dashboard: http://localhost:8761

## API Endpoints

### Campaign Service
- `POST /api/campaigns` - Create campaign
- `GET /api/campaigns/{id}` - Get campaign
- `PUT /api/campaigns/{id}` - Update campaign
- `GET /api/campaigns` - List campaigns

### Ad Serving Service
- `POST /api/ads/serve` - Serve ad for user request
- `GET /api/ads/{adId}` - Get ad details

### Analytics Service
- `POST /api/analytics/events` - Track event (impression, click)
- `GET /api/analytics/reports` - Get analytics reports

### User Targeting Service
- `GET /api/users/{userId}/profile` - Get user profile
- `PUT /api/users/{userId}/profile` - Update user profile
- `GET /api/users/{userId}/segments` - Get user segments

## Database Strategy

- **MySQL**: Campaign data, user accounts (ACID transactions)
- **MongoDB**: Analytics data, flexible schema for reporting
- **Cassandra**: User profiles, high-volume writes, horizontal scaling
- **Redis**: Caching, session management, real-time data

## Scalability Features

1. **Horizontal Scaling**: Stateless services, can scale independently
2. **Database Sharding**: Cassandra for user data partitioning
3. **Caching**: Redis for frequently accessed data
4. **Async Processing**: Kafka for event-driven architecture
5. **Load Balancing**: API Gateway with multiple service instances
6. **Circuit Breakers**: Resilience patterns for fault tolerance

## Security

- OAuth2 authentication
- JWT tokens for service-to-service communication
- API rate limiting
- Input validation and sanitization
- HTTPS/TLS encryption

## Monitoring & Logging

- Centralized logging with SLF4J/Logback
- Distributed tracing (Spring Cloud Sleuth)
- Health checks and metrics endpoints
- Integration with monitoring tools (Prometheus, Grafana)

## CI/CD Pipeline

Jenkins pipeline includes:
- Code compilation and testing
- Docker image building
- Security scanning
- Deployment to staging/production
- Kubernetes deployment automation

## Project Structure

```
ads_project/
├── api-gateway/              # API Gateway service
├── discovery-service/         # Eureka service discovery
├── config-service/            # Spring Cloud Config server
├── campaign-service/          # Campaign management
├── ad-serving-service/        # Ad serving logic
├── analytics-service/         # Analytics and reporting
├── user-targeting-service/    # User targeting and profiles
├── shared-libs/              # Shared libraries/DTOs
├── docker-compose.yml        # Infrastructure setup
└── README.md
