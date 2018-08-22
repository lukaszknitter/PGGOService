# PGGOService

## Project configuration

## Database
Configuration:

Ip, port, credentials and database run mode are set up in `application.yml` file.

`docker-compose.yml` has configuration for database to create - user, password etc.

Starting database:

```bash
docker-compose up
```

## Launch
```bash
mvn clean package spring-boot:run -DskipTests
```
