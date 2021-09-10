#!/bin/sh

IMAGE=rate_to_gif
PORT=8080

echo "Building application with gradle..."
gradlew clean build

echo "Building docker image $IMAGE..."
docker build -t $IMAGE .

echo "Running docker image on port $PORT..."
docker run -p $PORT:$PORT $IMAGE