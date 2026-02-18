# Ad Services Backend API - Architecture Documentation

## System Overview

This is a scalable, microservices-based Ad Tech backend API designed to handle high-volume ad serving requests similar to Walmart Ad Tech API. The system is built using Spring Boot microservices architecture with multiple databases optimized for different use cases.

## Architecture Principles

1. **Microservices Architecture**: Each service is independently deployable and scalable
2. **Database per Service**: Each service uses the most appropriate database for its use case
3. **Event-Driven Communication**: Kafka for async event processing
4. **Service Discovery**: Eureka for service registration and discovery
5. **API Gateway**: Single entry point with routing, load balancing, and circuit breakers
6. **Caching Strategy**: Redis and Caffeine for high-performance data access
7. **Horizontal Scalability**: Stateless services that can scale independently

## Service Breakdown

### 1. Discovery Service (Eureka)
- **Port**: 8761
- **Purpose**: Service registry and discovery
- **Technology**: Spring Cloud Netflix Eureka Server

### 2. Campaign Service
- **Port**: 8081
- **Purpose**: Campaign management (CRUD operations)
- **Database**: MySQL (ACID transactions, relational data)
- **Key Features**:
  - Create, update, delete campaigns
  - Campaign targeting rules
  - Budget management
  - Publishes campaign events to Kafka

### 3. Ad Serving Service (Core Service)
- **Port**: 8082
- **Purpose**: Real-time ad serving - latency-critical operations
- **Databases**: 
  - Cassandra (high-volume impression writes)
  - Redis (caching active campaigns and user profiles)
- **Key Features**:
  - Handles 900M+ user requests
  - Sub-100ms response time target
  - Campaign matching based on targeting
  - Async impression recording
  - Caching for performance optimization

### 4. Analytics Service
- **Port**: 8083
- **Purpose**: Event tracking and reporting
- **Database**: MongoDB (flexible schema for analytics data)
- **Key Features**:
  - Tracks impressions, clicks, conversions
  - Real-time analytics via Kafka consumers
  - Campaign performance reports
  - CTR and conversion rate calculations

### 5. User Targeting Service
- **Port**: 8084
- **Purpose**: User profile and segmentation management
- **Database**: Cassandra (scalable user data storage)
- **Key Features**:
  - User profile management
  - User segmentation
  - Behavior-based targeting
  - Caching for fast profile lookups

### 6. API Gateway
- **Port**: 8080
- **Purpose**: Single entry point, routing, load balancing
- **Technology**: Spring Cloud Gateway
- **Key Features**:
  - Route requests to appropriate services
  - Circuit breakers for fault tolerance
  - CORS configuration
  - Rate limiting (configurable)

## Data Flow

### Ad Serving Flow (Critical Path)
```
Client Request → API Gateway → Ad Serving Service
                                    ↓
                    ┌───────────────┴───────────────┐
                    ↓                               ↓
            Campaign Service              User Targeting Service
            (Get Active Campaigns)        (Get User Profile)
                    ↓                               ↓
                    └───────────────┬───────────────┘
                                    ↓
                        Match Campaigns to User
                                    ↓
                        Select Best Ad (Auction)
                                    ↓
                    ┌───────────────┴───────────────┐
                    ↓                               ↓
            Return Ad Response          Record Impression (Async)
                    ↓                               ↓
            Client Receives Ad          Kafka → Analytics Service
                                        Cassandra (Impression Log)
```

### Campaign Creation Flow
```
Client → API Gateway → Campaign Service
                            ↓
                    Save to MySQL
                            ↓
                    Publish to Kafka
                            ↓
            Ad Serving Service (Cache Invalidation)
            Analytics Service (Campaign Created Event)
```

### Analytics Flow
```
Ad Serving Service → Kafka Topic (analytics-events)
                            ↓
                    Analytics Service Consumer
                            ↓
                    Save to MongoDB
                            ↓
                    Generate Reports
```

## Database Strategy

### MySQL (Campaign Service)
- **Use Case**: Campaign data, advertiser accounts
- **Why**: ACID transactions, relational integrity
- **Schema**: Normalized relational schema
- **Scaling**: Read replicas, sharding by advertiser ID

### MongoDB (Analytics Service)
- **Use Case**: Analytics events, reporting data
- **Why**: Flexible schema, good for time-series data
- **Schema**: Document-based, denormalized for queries
- **Scaling**: Sharding by date/campaign ID

### Cassandra (Ad Serving & User Targeting)
- **Use Case**: 
  - Ad impressions (high-volume writes)
  - User profiles (900M+ users)
- **Why**: Horizontal scaling, high write throughput, eventual consistency
- **Schema**: Denormalized, partition key optimized
- **Scaling**: Add nodes, partition by user ID / campaign ID

### Redis (Caching)
- **Use Case**: 
  - Active campaigns cache
  - User profile cache
  - Session data
- **Why**: In-memory, sub-millisecond access
- **TTL**: 5-10 minutes for campaigns, 10 minutes for user profiles

## Scalability Patterns

### 1. Horizontal Scaling
- All services are stateless
- Can run multiple instances behind load balancer
- Service discovery handles instance registration

### 2. Database Scaling
- **MySQL**: Read replicas, connection pooling
- **Cassandra**: Add nodes, automatic sharding
- **MongoDB**: Sharding by date/campaign
- **Redis**: Redis Cluster for distributed caching

### 3. Caching Strategy
- **L1 Cache**: Caffeine (in-process) for frequently accessed data
- **L2 Cache**: Redis (distributed) for shared cache
- **Cache Invalidation**: Event-driven via Kafka

### 4. Async Processing
- Kafka for event streaming
- Non-blocking impression recording
- Background analytics processing

## Performance Optimizations

### Ad Serving Service (Critical)
1. **Caching**: Active campaigns cached in Redis/Caffeine
2. **Async Writes**: Impressions saved asynchronously
3. **Connection Pooling**: Optimized database connections
4. **Circuit Breakers**: Prevent cascading failures
5. **Request Batching**: Batch database queries where possible

### Database Optimizations
1. **Indexing**: Proper indexes on query patterns
2. **Partitioning**: Cassandra partitioned by user ID
3. **Read Replicas**: MySQL read replicas for scaling reads
4. **Connection Pooling**: HikariCP for MySQL, optimized pool sizes

## Security Considerations

1. **Authentication**: OAuth2/JWT (to be implemented)
2. **API Rate Limiting**: Per client/IP rate limits
3. **Input Validation**: All DTOs validated
4. **HTTPS/TLS**: Encrypted communication
5. **Secrets Management**: Environment variables, Vault integration

## Monitoring & Observability

1. **Health Checks**: Actuator endpoints for each service
2. **Metrics**: Prometheus metrics export
3. **Logging**: Centralized logging (SLF4J/Logback)
4. **Distributed Tracing**: Spring Cloud Sleuth (to be configured)
5. **Alerting**: Set up alerts for service health, latency, errors

## Deployment Strategy

1. **Containerization**: Docker for each service
2. **Orchestration**: Kubernetes for production
3. **CI/CD**: Jenkins pipeline for automated deployment
4. **Blue-Green Deployment**: Zero-downtime deployments
5. **Rolling Updates**: Gradual service updates

## Cost Optimization (Based on Resume Experience)

1. **Database Query Optimization**: Reduce Cosmos DB calls (similar pattern)
2. **Caching**: Reduce database load
3. **Async Processing**: Offload heavy operations
4. **Resource Right-Sizing**: Monitor and adjust instance sizes
5. **Auto-Scaling**: Scale down during low traffic

## Future Enhancements

1. **Real-Time Bidding (RTB)**: Implement auction logic
2. **Machine Learning**: ML-based ad selection
3. **A/B Testing**: Campaign performance testing
4. **Fraud Detection**: Bot detection and filtering
5. **Multi-Tenancy**: Support multiple advertisers
6. **GraphQL API**: Alternative to REST
7. **gRPC**: High-performance inter-service communication
