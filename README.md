# Weather Forecast Backend Service

This is the backend implementation of a weather forecast application. 
It integrates with the [OpenWeatherMap API](https://openweathermap.org/api), 
supports offline mode, and uses a custom LFU (Least Frequently Used) cache with LRU (Least Recently Used) tie-breaker.

---

## Features

- Fetches 24-hour weather data by city
- Caches API responses using LFU + LRU policy (cache size: 5)
- Offline support with mock fallback
- Swagger integration for API documentation
- Clean architecture adhering to SOLID principles
- Fully tested with unit & integration tests

---

## Architecture Overview

The system is designed in a **modular and layered architecture**.

### Layers:

- **Controller Layer**: Receives city input via REST API
- **Service Layer**: Applies business logic, handles caching
- **Integration Layer**: Handles communication with external weather API
- **Cache Layer**: Custom LFU + LRU-based in-memory caching
- **DTO Layer**: Transports data between layers

---

## Sequence Diagram
- Google drivce link : https://drive.google.com/drive/u/0/home

---

## Design and Implementation Approach

### 1. **API Consumption**
- The backend fetches weather data from the OpenWeatherMap API.
- Data is deserialized into structured DTOs.

### 2. **Caching Strategy**
- A custom in-memory cache implements **LFU eviction**.
- If multiple cities share the same frequency, **LRU (Least Recently Used)** is used as a tie-breaker.

### 3. **Offline Mode Support**
- When the system is offline or an exception occurs during API fetch, a static mock forecast is returned.

### 4. **Error Handling**
- All failures return a fallback response instead of crashing the app.
- Fallback uses `DailyForecast.mock()`.

### 5. **Test Strategy**
- **Unit Tests** for services with mocks
- **Integration Tests** for actual cache and API flow (with `TestContainers` if needed)

---

## Design Patterns Used

| Pattern | Usage |
|--------|-------|
| **Strategy** | Cache strategy is abstracted via the `WeatherCache` interface |
| **Factory (via Spring)** | Beans are auto-wired and resolved using Spring’s DI container |
| **Builder** | Used in DTOs like `DailyForecast` via Lombok's `@Builder` |
| **Adapter** | `OpenWeatherMapApiClient` acts as an adapter to the external API |
| **Decorator (soft)** | Offline fallback wraps the main service logic |
| **Singleton** | Cache is registered as a single shared instance (Spring Bean) |

---

## Test Coverage

- WeatherServiceImplTest — Unit test
- WeatherApiClientTest — Integration test
- LFUCacheWithLRUTieBreakerTest — Eviction policy testing
- Full test coverage for:
  - Cache hit/miss
  - Offline mode
  - API fallback
  - Forecast transformation

---

## API Documentation

Swagger UI is enabled for API testing and exploration : http://localhost:8080/swagger-ui/index.html#/
