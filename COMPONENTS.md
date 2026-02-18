# Ad Services Backend API - Components & Modules Guide

This document explains the key components and modules involved in the Ad Services Backend API system, designed to help you understand how everything fits together.

## Core Components Overview

### 1. Service Discovery (Eureka)
**Purpose**: Central registry for all microservices

**Why it's needed**:
- Services need to find each other dynamically
- Handles service registration and health checks
- Enables load balancing across service instances

**Key Files**:
- `discovery-service/src/main/java/com/walmart/ads/discovery/DiscoveryServiceApplication.java`
- `discovery-service/src/main/resources/application.yml`

**How it works**:
- Each service registers itself with Eureka on startup
- Services query Eureka to find other services by name
- API Gateway uses Eureka for service routing

---

### 2. Campaign Service
**Purpose**: Manage ad campaigns (create, update, delete, retrieve)

**Key Components**:

#### Model (`Campaign.java`)
- Represents a campaign entity
- Fields: name, advertiserId, budget, dates, status, targeting rules
- Uses JPA annotations for MySQL persistence

#### Repository (`CampaignRepository.java`)
- Spring Data JPA repository
- Custom queries: findActiveCampaigns, findByAdvertiserId
- Handles database operations

#### Service (`CampaignService.java`)
- Business logic layer
- Converts between DTOs and entities
- Publishes events to Kafka when campaigns change
- Transactional operations

#### Controller (`CampaignController.java`)
- REST API endpoints
- Handles HTTP requests/responses
- Input validation
- Returns standardized ApiResponse

**Database**: MySQL
- Why MySQL: ACID transactions, relational integrity for campaign data
- Schema: Normalized tables for campaigns, advertisers

**Key Files**:
- `campaign-service/src/main/java/com/walmart/ads/campaign/model/Campaign.java`
- `campaign-service/src/main/java/com/walmart/ads/campaign/service/CampaignService.java`
- `campaign-service/src/main/java/com/walmart/ads/campaign/controller/CampaignController.java`

---

### 3. Ad Serving Service (Most Critical)
**Purpose**: Serve ads to users in real-time (latency-critical)

**Key Components**:

#### Service (`AdServingService.java`)
**Core Logic Flow**:
1. Get active campaigns (cached)
2. Get user profile for targeting (cached)
3. Match campaigns to user based on targeting
4. Select best ad (auction logic)
5. Create ad response
6. Record impression asynchronously
7. Publish analytics event

**Performance Optimizations**:
- Caching active campaigns (Redis/Caffeine)
- Caching user profiles
- Async impression recording (non-blocking)
- Optimized database queries

#### Feign Clients
- `CampaignServiceClient`: Calls Campaign Service to get active campaigns
- `UserTargetingServiceClient`: Calls User Targeting Service for user data

#### Repository (`AdImpressionRepository.java`)
- Cassandra repository for high-volume writes
- Stores every ad impression (900M+ records)

**Databases**:
- **Cassandra**: High-volume impression writes
- **Redis**: Caching active campaigns and user profiles

**Key Files**:
- `ad-serving-service/src/main/java/com/walmart/ads/adserving/service/AdServingService.java`
- `ad-serving-service/src/main/java/com/walmart/ads/adserving/controller/AdServingController.java`

**Why it's critical**:
- Handles 900M+ user requests
- Must respond in <100ms
- High throughput requirements

---

### 4. Analytics Service
**Purpose**: Track and analyze ad performance

**Key Components**:

#### Kafka Consumer (`AnalyticsService.java`)
- Listens to `analytics-events` topic
- Processes events asynchronously
- Saves to MongoDB

#### Service Methods:
- `trackEvent()`: Direct event tracking
- `getCampaignReport()`: Generate performance reports
- Calculates CTR, conversion rates

**Database**: MongoDB
- Why MongoDB: Flexible schema, good for analytics data
- Stores events, impressions, clicks, conversions

**Key Files**:
- `analytics-service/src/main/java/com/walmart/ads/analytics/service/AnalyticsService.java`
- `analytics-service/src/main/java/com/walmart/ads/analytics/model/AnalyticsEvent.java`

---

### 5. User Targeting Service
**Purpose**: Manage user profiles and segmentation

**Key Components**:

#### Model (`UserProfile.java`)
- User demographics, interests, segments
- Behavior data (browsing, purchases)

#### Service (`UserTargetingService.java`)
- Get/update user profiles
- User segmentation logic
- Kafka consumer for behavior events
- Updates segments based on user behavior

**Database**: Cassandra
- Why Cassandra: Scalable for 900M+ users
- Horizontal scaling
- Fast reads with proper partitioning

**Caching**: Caffeine cache for frequently accessed profiles

**Key Files**:
- `user-targeting-service/src/main/java/com/walmart/ads/usertargeting/service/UserTargetingService.java`
- `user-targeting-service/src/main/java/com/walmart/ads/usertargeting/model/UserProfile.java`

---

### 6. API Gateway
**Purpose**: Single entry point, routing, load balancing

**Key Components**:

#### Gateway Configuration (`application.yml`)
- Routes requests to appropriate services
- Load balancing using service discovery
- Circuit breakers for fault tolerance
- CORS configuration

#### Fallback Controller
- Handles service failures gracefully
- Returns error responses when services are down

**Technology**: Spring Cloud Gateway (reactive, non-blocking)

**Key Features**:
- Service routing by path
- Circuit breakers
- Rate limiting (configurable)
- Health checks

**Key Files**:
- `api-gateway/src/main/resources/application.yml`
- `api-gateway/src/main/java/com/walmart/ads/gateway/controller/FallbackController.java`

---

## Shared Components

### Shared Libraries (`shared-libs`)
**Purpose**: Common DTOs and utilities used across services

**Key DTOs**:
- `CampaignDTO`: Campaign data transfer object
- `AdRequestDTO`: Ad serving request
- `AdResponseDTO`: Ad serving response
- `AnalyticsEventDTO`: Analytics event data
- `UserProfileDTO`: User profile data
- `ApiResponse<T>`: Standardized API response wrapper

**Why shared**:
- Ensures consistency across services
- Reduces code duplication
- Version control for API contracts

---

## Communication Patterns

### 1. Synchronous (REST)
- **When**: Real-time data needed immediately
- **Example**: Ad Serving Service → Campaign Service (get active campaigns)
- **Technology**: Feign Client (HTTP)

### 2. Asynchronous (Kafka)
- **When**: Event processing, non-blocking operations
- **Examples**:
  - Campaign created → Analytics Service notified
  - Ad impression → Analytics Service processes
  - User behavior → User Targeting Service updates segments
- **Benefits**: Decoupling, scalability, fault tolerance

---

## Database Strategy Explained

### MySQL (Campaign Service)
- **Data**: Campaigns, advertisers, budgets
- **Why**: ACID transactions, relational integrity
- **Access Pattern**: Read-heavy, occasional writes
- **Scaling**: Read replicas

### MongoDB (Analytics Service)
- **Data**: Analytics events, reports
- **Why**: Flexible schema, time-series data
- **Access Pattern**: Write-heavy, complex queries
- **Scaling**: Sharding by date/campaign

### Cassandra (Ad Serving & User Targeting)
- **Data**: Impressions, user profiles
- **Why**: Horizontal scaling, high write throughput
- **Access Pattern**: High-volume writes, fast reads
- **Scaling**: Add nodes, automatic sharding

### Redis (Caching)
- **Data**: Active campaigns, user profiles
- **Why**: Sub-millisecond access, distributed cache
- **Access Pattern**: Frequent reads, TTL-based expiration
- **Scaling**: Redis Cluster

---

## Scalability Features

### 1. Horizontal Scaling
- All services are stateless
- Can run multiple instances
- Load balanced via API Gateway

### 2. Database Scaling
- **MySQL**: Read replicas
- **Cassandra**: Add nodes
- **MongoDB**: Sharding
- **Redis**: Cluster mode

### 3. Caching Layers
- **L1**: Caffeine (in-process)
- **L2**: Redis (distributed)
- **Strategy**: Cache-aside pattern

### 4. Async Processing
- Kafka for event streaming
- Non-blocking operations
- Background processing

---

## Key Design Patterns Used

1. **Microservices**: Independent, deployable services
2. **API Gateway**: Single entry point
3. **Service Discovery**: Dynamic service location
4. **Circuit Breaker**: Fault tolerance
5. **CQRS**: Separate read/write models (Cassandra writes, cached reads)
6. **Event-Driven**: Kafka for async communication
7. **Database per Service**: Each service owns its data
8. **Cache-Aside**: Caching pattern for performance

---

## Request Flow Example: Serving an Ad

```
1. Client → API Gateway (port 8080)
   POST /api/ads/serve
   
2. API Gateway → Ad Serving Service (port 8082)
   Routes request using service discovery
   
3. Ad Serving Service:
   a. Gets active campaigns (cached from Redis/Caffeine)
      OR calls Campaign Service if cache miss
   
   b. Gets user profile (cached)
      OR calls User Targeting Service if cache miss
   
   c. Matches campaigns to user (targeting logic)
   
   d. Selects best ad (auction logic)
   
   e. Returns ad response to client
   
   f. Async: Saves impression to Cassandra
   
   g. Async: Publishes event to Kafka
   
4. Analytics Service (Kafka consumer):
   - Receives impression event
   - Saves to MongoDB
   - Updates campaign metrics
```

---

## Testing Strategy

### Unit Tests
- Service layer logic
- Repository queries
- DTO conversions
- Example: `CampaignServiceTest.java`

### Integration Tests
- Service-to-service communication
- Database operations
- Kafka message processing

### Performance Tests
- Ad serving latency (<100ms target)
- Throughput testing (900M+ requests)
- Database query performance

---

## Monitoring & Observability

### Health Checks
- Actuator endpoints: `/actuator/health`
- Service-specific health indicators

### Metrics
- Response times
- Error rates
- Throughput
- Database connection pool

### Logging
- SLF4J/Logback
- Structured logging
- Correlation IDs for tracing

---

This architecture follows industry best practices for scalable, high-performance ad tech systems, similar to what you'd find at companies like Walmart Ad Tech.
