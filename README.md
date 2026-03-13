# VanOpt — Van Loading Optimiser

A Spring Boot REST API that solves the 0/1 Knapsack problem to maximise delivery revenue without exceeding van capacity. Every optimisation run is persisted to PostgreSQL for auditing.

---

## Tech Stack

- Java 17
- Spring Boot 3.4.3
- Gradle (Kotlin DSL)
- PostgreSQL 15
- Flyway (database migrations)
- Docker Compose (database setup)
- JUnit 5 + Mockito (tests)

---

## Prerequisites

- Java 17+
- Docker Desktop
- Gradle (or use the wrapper `./gradlew`)

---

## Database Setup

Start PostgreSQL using Docker Compose:
```bash
docker-compose up -d
```

This will:
- Pull `postgres:15-alpine`
- Create database `vanopt` with user `vanopt` / password `vanopt`
- Expose port `5439`

To stop the database:
```bash
docker-compose down
```

To stop and remove all data:
```bash
docker-compose down -v
```

---

## Build & Run

**Run the application:**
```bash
./gradlew bootRun
```

Flyway will automatically run migrations on startup and create the required tables.

**Build a JAR:**
```bash
./gradlew build
java -jar build/libs/vanopt-0.0.1-SNAPSHOT.jar
```

**Run tests:**
```bash
./gradlew clean test
```

Test report: `build/reports/tests/test/index.html`

---

## API Endpoints

| Method | URL | Description |
|--------|-----|-------------|
| POST | `/api/v1/optimizations` | Run optimisation |
| GET | `/api/v1/optimizations/{id}` | Get result by ID |
| GET | `/api/v1/optimizations` | List all past runs |

---

## Example cURL Requests

### 1. Run Optimisation
```bash
curl -X POST http://localhost:8080/api/v1/optimizations \
  -H "Content-Type: application/json" \
  -d '{
    "maxVolume": 15,
    "availableShipments": [
      { "name": "Parcel A", "volume": 5, "revenue": 120 },
      { "name": "Parcel B", "volume": 10, "revenue": 200 },
      { "name": "Parcel C", "volume": 3, "revenue": 80 },
      { "name": "Parcel D", "volume": 8, "revenue": 160 }
    ]
  }'
```

**Response (201 Created):**
```json
{
  "requestId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "selectedShipments": [
    { "name": "Parcel A", "volume": 5, "revenue": 120 },
    { "name": "Parcel B", "volume": 10, "revenue": 200 }
  ],
  "totalVolume": 15,
  "totalRevenue": 320,
  "createdAt": "2025-06-01T10:00:00"
}
```

---

### 2. Get Result by ID
```bash
curl -X GET http://localhost:8080/api/v1/optimizations/a1b2c3d4-e5f6-7890-abcd-ef1234567890
```

**Response (200 OK):**
```json
{
  "requestId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "selectedShipments": [
    { "name": "Parcel A", "volume": 5, "revenue": 120 },
    { "name": "Parcel B", "volume": 10, "revenue": 200 }
  ],
  "totalVolume": 15,
  "totalRevenue": 320,
  "createdAt": "2025-06-01T10:00:00"
}
```

---

### 3. List All Past Runs
```bash
curl -X GET http://localhost:8080/api/v1/optimizations
```

**Response (200 OK):**
```json
[
  {
    "requestId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "selectedShipments": [ ],
    "totalVolume": 15,
    "totalRevenue": 320,
    "createdAt": "2025-06-01T10:00:00"
  }
]
```

---

### 4. Invalid Input (400 Bad Request)
```bash
curl -X POST http://localhost:8080/api/v1/optimizations \
  -H "Content-Type: application/json" \
  -d '{
    "maxVolume": -1,
    "availableShipments": []
  }'
```

**Response (400 Bad Request):**
```json
{
  "status": 400,
  "message": "{maxVolume=maxVolume must be at least 1, availableShipments=availableShipments must not be empty}"
}
```

---

## Database Schema

### Tables

**`optimization_requests`** — one row per optimisation run

| Column | Type | Description |
|--------|------|-------------|
| id | UUID | Primary key, generated per run |
| max_volume | INTEGER | Van capacity provided in request |
| total_volume | INTEGER | Sum of selected shipment volumes |
| total_revenue | BIGINT | Sum of selected shipment revenues |
| created_at | TIMESTAMP | When the run was created |

**`selected_shipments`** — one row per selected shipment per run

| Column | Type | Description |
|--------|------|-------------|
| id | UUID | Primary key |
| request_id | UUID | Foreign key → optimization_requests(id) |
| name | VARCHAR(255) | Shipment label |
| volume | INTEGER | Shipment volume in dm³ |
| revenue | BIGINT | Shipment revenue |

### Indexes

| Index | Table | Column | Reason |
|-------|-------|--------|--------|
| `idx_optimization_requests_created_at` | optimization_requests | created_at DESC | Fast sorting when listing all runs |
| `idx_selected_shipments_request_id` | selected_shipments | request_id | Fast lookup of shipments by run ID |

### Relationships

- `optimization_requests` → `selected_shipments`: one-to-many
- `ON DELETE CASCADE`: deleting a request automatically deletes its shipments

---

## Algorithm

The optimisation uses **Dynamic Programming (0/1 Knapsack)**:

- **Time complexity:** O(n × W) where n = number of shipments, W = maxVolume
- **Space complexity:** O(W) — 1D array, space-optimised
- Each shipment is either fully included or excluded (no splitting)
- Backtracking is used to reconstruct which shipments were selected


