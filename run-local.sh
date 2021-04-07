#!/bin/bash

echo "Running PostgreSQL with Docker Compose"

docker-compose up -d

echo "Running Product API"

mvn spring-boot:run
