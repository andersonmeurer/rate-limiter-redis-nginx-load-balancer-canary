# Rate Limiter with Spring Boot, Redis and NGINX

![System Architecture](docs/system-design.png)

This project implements a distributed rate limiting system for REST APIs using Spring Boot, Redis, and NGINX. The goal is to limit the number of requests a client can make within a configurable time window, even when the application is horizontally scaled.

## Features

- Limits the number of requests per IP in a configurable time window.
- Uses Redis to store request counters.
- Returns HTTP 429 (Too Many Requests) when the limit is exceeded.
- Automated tests with MockMvc.

## Project Structure

- `src/main/java/com/meurer/ratelimiter/`: Main application code.
- `src/main/java/com/meurer/ratelimiter/config/`: Rate limiter configurations.
- `src/main/java/com/meurer/ratelimiter/controller/`: Test controller.
- `src/test/java/com/meurer/ratelimiter/`: Automated tests.
- `docker-compose.yml`: Defines Redis, multiple Spring Boot instances, and NGINX load balancer.
- `nginx.conf`: NGINX load balancer configuration.

## Prerequisites

- Java 17+
- Maven
- Docker & Docker Compose (to run Redis, Spring Boot apps, and NGINX)

## Step-by-step to run the application

1. **Start the full stack with Docker Compose**

   In the project directory, run:

   ```sh
   docker-compose up --build -d
   ```

   This will start:
   - Redis (request counter storage)
   - Two Spring Boot application instances (`app1` and `app2`)
   - NGINX load balancer (exposes port 80)

2. **Access the API via NGINX**

    - Test endpoint: `http://localhost/test`
    - NGINX will distribute requests between `app1` and `app2`, both sharing rate limit data via Redis.

3. **Run automated tests**

   ```sh
   mvn test
   ```

## Architecture Overview

The architecture uses Docker Compose to orchestrate the following services:
- **Redis**: Stores request counters per IP, ensuring consistency between instances.
- **app1 & app2**: Identical Spring Boot instances, each connected to Redis for rate limiting.
- **NGINX**: Load balancer, distributes requests between application instances.

### Flow Diagram
```
Client → NGINX (port 80) → app1/app2 (port 8080) → Redis (port 6379)
```
- NGINX receives requests and forwards them to one of the application instances.
- Each instance checks and increments the request counter in Redis.
- If the limit is exceeded, HTTP 429 is returned.
- Redis ensures the rate limit is shared across all instances.

## Running with Docker on WSL (Windows Subsystem for Linux)

If you are using Docker installed on WSL:

1. Open your WSL terminal (e.g., Ubuntu).
2. Navigate to your project directory (where the `docker-compose.yml` file is located). For example:
   ```sh
   cd /path/to/your/project
   ```
3. Make sure Docker Desktop is running on Windows.
4. Run the following command to start Redis:
   ```sh
   docker-compose up -d
   ```

If you encounter permission issues, try running the command with sudo:
```sh
sudo docker-compose up -d
```

Now you can proceed with the rest of the steps (build, run, test) as described above.
