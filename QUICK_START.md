# Quick Start Guide - Ad Services Backend API

## 🚀 Quick Setup (5 Minutes)

### 1. Start Infrastructure
```bash
docker-compose up -d
```

### 2. Initialize Cassandra
```bash
docker exec -it ads-cassandra cqlsh -e "CREATE KEYSPACE IF NOT EXISTS ads_keyspace WITH REPLICATION = {'class': 'SimpleStrategy', 'replication_factor': 1};"
```

### 3. Build Project
```bash
mvn clean install -DskipTests
```

### 4. Start Services (in separate terminals)
```bash
# Terminal 1: Discovery Service
cd discovery-service && mvn spring-boot:run

# Terminal 2: Campaign Service  
cd campaign-service && mvn spring-boot:run

# Terminal 3: User Targeting Service
cd user-targeting-service && mvn spring-boot:run

# Terminal 4: Analytics Service
cd analytics-service && mvn spring-boot:run

# Terminal 5: Ad Serving Service
cd ad-serving-service && mvn spring-boot:run

# Terminal 6: API Gateway
cd api-gateway && mvn spring-boot:run
```

### 5. Verify
- Eureka Dashboard: http://localhost:8761
- API Gateway: http://localhost:8080/actuator/health

## 📋 Key Endpoints

### Campaign Management
```bash
# Create Campaign
POST http://localhost:8080/api/campaigns
{
  "name": "Test Campaign",
  "advertiserId": "adv-001",
  "budget": 10000.00,
  "startDate": "2026-02-17T00:00:00",
  "endDate": "2026-03-17T23:59:59",
  "status": "ACTIVE",
  "targetSegments": ["SHOPPER"],
  "creativeUrl": "https://example.com/ad.jpg",
  "landingPageUrl": "https://example.com"
}

# Get Campaigns
GET http://localhost:8080/api/campaigns

# Get Active Campaigns
GET http://localhost:8080/api/campaigns/active
```

### User Management
```bash
# Create/Update User Profile
PUT http://localhost:8080/api/users/user-123/profile
{
  "userId": "user-123",
  "email": "user@example.com",
  "segments": ["SHOPPER", "TECH_INTERESTED"]
}

# Get User Profile
GET http://localhost:8080/api/users/user-123/profile

# Get User Segments
GET http://localhost:8080/api/users/user-123/segments
```

### Ad Serving (Core Functionality)
```bash
# Serve Ad
POST http://localhost:8080/api/ads/serve
{
  "userId": "user-123",
  "requestId": "req-001",
  "slotWidth": 300,
  "slotHeight": 250
}
```

### Analytics
```bash
# Track Event
POST http://localhost:8080/api/analytics/events
{
  "eventType": "IMPRESSION",
  "adId": "ad-1",
  "campaignId": "1",
  "userId": "user-123",
  "timestamp": "2026-02-17T12:00:00"
}

# Get Report
GET http://localhost:8080/api/analytics/reports?campaignId=1&startDate=2026-02-17T00:00:00&endDate=2026-02-18T00:00:00
```

## 🏗️ Architecture Summary

```
┌─────────────┐
│   Client    │
└──────┬──────┘
       │
┌──────▼──────────┐
│  API Gateway    │ (Port 8080)
│  - Routing      │
│  - Load Balance │
│  - Circuit Br.  │
└──────┬──────────┘
       │
   ┌───┴───┬──────────┬──────────┐
   │       │          │          │
┌──▼──┐ ┌──▼──┐  ┌───▼───┐  ┌───▼───┐
│Camp │ │ Ad  │  │Analyt │  │ User  │
│Svc  │ │Svc  │  │ Svc   │  │Target │
└──┬──┘ └──┬──┘  └───┬───┘  └───┬───┘
   │       │          │          │
   │       │          │          │
┌──▼──┐ ┌──▼──┐  ┌───▼───┐  ┌───▼───┐
│MySQL│ │Cass │  │MongoDB│  │Cass   │
│     │ │Redis│  │       │  │       │
└─────┘ └─────┘  └───────┘  └───────┘
```

## 📊 Service Ports

| Service | Port | Purpose |
|---------|------|---------|
| API Gateway | 8080 | Entry point |
| Discovery | 8761 | Service registry |
| Campaign | 8081 | Campaign management |
| Ad Serving | 8082 | Ad serving (critical) |
| Analytics | 8083 | Analytics & reporting |
| User Targeting | 8084 | User profiles |

## 🗄️ Database Ports

| Database | Port | Used By |
|----------|------|---------|
| MySQL | 3306 | Campaign Service |
| MongoDB | 27017 | Analytics Service |
| Cassandra | 9042 | Ad Serving, User Targeting |
| Redis | 6379 | Caching |
| Kafka | 9092 | Event streaming |

## 🔑 Key Concepts

### Microservices
- **Independent**: Each service can be deployed separately
- **Scalable**: Scale services independently based on load
- **Fault Tolerant**: One service failure doesn't bring down others

### Database per Service
- **MySQL**: Campaign data (ACID transactions)
- **MongoDB**: Analytics (flexible schema)
- **Cassandra**: High-volume data (impressions, users)
- **Redis**: Caching (performance)

### Event-Driven
- **Kafka**: Async communication between services
- **Decoupling**: Services don't directly depend on each other
- **Scalability**: Process events asynchronously

### Caching Strategy
- **Caffeine**: In-process cache (fast)
- **Redis**: Distributed cache (shared)
- **TTL**: Automatic expiration

## 🎯 Performance Targets

- **Ad Serving Latency**: <100ms
- **Throughput**: Handle 900M+ requests
- **Availability**: 99.9% uptime
- **Scalability**: Horizontal scaling

## 📚 Documentation Files

- `README.md` - Project overview
- `ARCHITECTURE.md` - Detailed architecture
- `COMPONENTS.md` - Component breakdown
- `SETUP.md` - Detailed setup instructions
- `QUICK_START.md` - This file

## 🐛 Troubleshooting

**Services not starting?**
- Check Docker containers: `docker-compose ps`
- Check ports are available: `lsof -i :8080`
- Check logs: `docker-compose logs`

**Database connection errors?**
- Verify containers running: `docker-compose ps`
- Check credentials in `application.yml`
- Wait for databases to initialize (30-60 seconds)

**Kafka not working?**
- Ensure Zookeeper starts before Kafka
- Check Kafka logs: `docker-compose logs kafka`
- Verify topic creation: `docker exec -it ads-kafka kafka-topics --list --bootstrap-server localhost:9092`

## 🚢 Production Deployment

1. **Build Docker Images**:
```bash
mvn clean package
docker build -t campaign-service ./campaign-service
# Repeat for each service
```

2. **Kubernetes Deployment**:
- Use provided Kubernetes manifests
- Configure resource limits
- Set up auto-scaling

3. **CI/CD**:
- Use provided `Jenkinsfile`
- Configure Docker registry
- Set up deployment pipelines

## 💡 Next Steps

1. **Add Authentication**: Implement OAuth2/JWT
2. **Add Monitoring**: Prometheus + Grafana
3. **Add Logging**: ELK Stack or CloudWatch
4. **Add Testing**: Integration and performance tests
5. **Add Real-Time Bidding**: Implement auction logic
6. **Add ML**: Machine learning for ad selection

## 📞 Support

For issues or questions:
1. Check `SETUP.md` for detailed instructions
2. Review `ARCHITECTURE.md` for system design
3. Check service logs for errors
4. Verify all infrastructure services are running

---

**Built with**: Spring Boot, Spring Cloud, MySQL, MongoDB, Cassandra, Redis, Kafka, Docker, Kubernetes
