#!/usr/bin/env bash

cd ..
docker build -f statistics/Dockerfile \
    --build-arg _port="$PORT" \
    --build-arg _intervalSec="$INTERVAL_SEC" \
    --build-arg _servicesBaseUrls="$SERVICES_BASE_URLS" \
    --build-arg _dbUrl="$DB_URL" \
    --build-arg _dbName="$DB_NAME" \
    -t statistics-service .
docker run -p "$PORT":"$PORT" statistics-service:latest
