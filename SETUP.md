# Setup Guide - Ad Services Backend API

## Prerequisites

- Java 17 or higher
- Maven 3.8+
- Docker and Docker Compose
- IDE (IntelliJ IDEA recommended)

## Step 1: Start Infrastructure Services

Start all required infrastructure services using Docker Compose:

```bash
docker-compose up -d
```

This will start:
- MySQL (port 3306)
- MongoDB (port 27017)
- Cassandra (port 9042)
- Redis (port 6379)
- Zookeeper (port 2181)
- Kafka (port 9092)

Verify all services are running:
```bash
docker-compose ps
```

## Step 2: Initialize Cassandra Keyspace

Connect to Cassandra and create the keyspace:

```bash
docker exec -it ads-cassandra cqlsh
```

In CQL shell:
```cql
CREATE KEYSPACE IF NOT EXISTS ads_keyspace 
WITH REPLICATION = {'class': 'SimpleStrategy', 'replication_factor': 1};

USE ads_keyspace;

CREATE TABLE IF NOT EXISTS user_profiles (
    user_id TEXT PRIMARY KEY,
    email TEXT,
    first_name TEXT,
    last_name TEXT,
    age INT,
    gender TEXT,
    location TEXT,
    interests LIST<TEXT>,
    segments LIST<TEXT>,
    demographics MAP<TEXT, TEXT>,
    behavior_data MAP<TEXT, TEXT>,
    last_updated TIMESTAMP
);

CREATE TABLE IF NOT EXISTS ad_impressions (
    id UUID PRIMARY KEY,
    ad_id TEXT,
    campaign_id TEXT,
    user_id TEXT,
    request_id TEXT,
    timestamp TIMESTAMP,
    user_agent TEXT,
    ip_address TEXT
);
```

## Step 3: Build the Project

Build all modules:

```bash
mvn clean install -DskipTests
```

## Step 4: Start Services in Order

### 1. Start Discovery Service
```bash
cd discovery-service
mvn spring-boot:run
```
Wait for it to start (check http://localhost:8761)

### 2. Start Campaign Service
```bash
cd campaign-service
mvn spring-boot:run
```

### 3. Start User Targeting Service
```bash
cd user-targeting-service
mvn spring-boot:run
```

### 4. Start Analytics Service
```bash
cd analytics-service
mvn spring-boot:run
```

### 5. Start Ad Serving Service
```bash
cd ad-serving-service
mvn spring-boot:run
```

### 6. Start API Gateway
```bash
cd api-gateway
mvn spring-boot:run
```

## Step 5: Verify Services

1. **Eureka Dashboard**: http://localhost:8761
   - Should show all services registered

2. **API Gateway**: http://localhost:8080
   - Health check: http://localhost:8080/actuator/health

3. **Swagger UI** (for each service):
   - Campaign Service: http://localhost:8081/swagger-ui.html
   - Ad Serving Service: http://localhost:8082/swagger-ui.html
   - Analytics Service: http://localhost:8083/swagger-ui.html
   - User Targeting Service: http://localhost:8084/swagger-ui.html

## Step 6: Test the API

### Create a Campaign
```bash
curl -X POST http://localhost:8080/api/campaigns \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Summer Sale Campaign",
    "advertiserId": "adv-001",
    "budget": 10000.00,
    "startDate": "2026-02-17T00:00:00",
    "endDate": "2026-03-17T23:59:59",
    "status": "ACTIVE",
    "targetSegments": ["SHOPPER", "TECH_INTERESTED"],
    "creativeUrl": "https://example.com/creative.jpg",
    "landingPageUrl": "https://example.com/landing"
  }'
```

### Create a User Profile
```bash
curl -X PUT http://localhost:8080/api/users/user-123/profile \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "user-123",
    "email": "user@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "age": 30,
    "gender": "MALE",
    "location": "CA",
    "interests": ["TECH", "SHOPPING"],
    "segments": ["SHOPPER", "TECH_INTERESTED"]
  }'
```

### Serve an Ad
```bash
curl -X POST http://localhost:8080/api/ads/serve \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "user-123",
    "requestId": "req-001",
    "pageUrl": "https://example.com/page",
    "userAgent": "Mozilla/5.0",
    "ipAddress": "192.168.1.1",
    "slotWidth": 300,
    "slotHeight": 250
  }'
```

### Track Analytics Event
```bash
curl -X POST http://localhost:8080/api/analytics/events \
  -H "Content-Type: application/json" \
  -d '{
    "eventType": "IMPRESSION",
    "adId": "ad-1",
    "campaignId": "1",
    "userId": "user-123",
    "timestamp": "2026-02-17T12:00:00",
    "requestId": "req-001"
  }'
```

### Get Analytics Report
```bash
curl "http://localhost:8080/api/analytics/reports?campaignId=1&startDate=2026-02-17T00:00:00&endDate=2026-02-18T00:00:00"
```

## Troubleshooting

### Services not registering with Eureka
- Check that Discovery Service is running first
- Verify Eureka URL in service configuration
- Check network connectivity

### Database connection issues
- Verify Docker containers are running: `docker-compose ps`
- Check database ports are accessible
- Verify credentials in application.yml

### Kafka connection issues
- Ensure Zookeeper is running before Kafka
- Check Kafka is accessible on port 9092
- Verify topic auto-creation is enabled

### High latency in ad serving
- Check Redis is running and accessible
- Verify caching is enabled
- Check Cassandra connection pool settings
- Monitor service logs for errors

## Development Tips

1. **Run services individually**: Each service can run independently for development
2. **Use IDE run configurations**: Set up run configurations for each service
3. **Hot reload**: Use Spring Boot DevTools for automatic reloading
4. **Database tools**: Use DBeaver, MongoDB Compass, or DataStax Studio for database inspection
5. **Kafka tools**: Use Kafka Tool or Conduktor for Kafka topic inspection

## Production Deployment

For production deployment:
1. Use Kubernetes for orchestration
2. Configure proper resource limits
3. Set up monitoring and alerting
4. Configure SSL/TLS certificates
5. Set up backup strategies for databases
6. Configure auto-scaling policies
7. Set up CI/CD pipeline (Jenkinsfile provided)
