#!/bin/bash

# Deploy script for RevPay on EC2

echo "Pulling latest changes..."
git pull origin main

echo "Building and starting services with Docker Compose..."
docker-compose down
docker-compose up --build -d

echo "Deployment complete. Services should be running on ports 80 (frontend) and 8080 (backend)."