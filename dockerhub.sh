#!/bin/sh

# Exit immediately if a command exits with a non-zero status.
set -e

# Define variables
IMAGE_NAME="user-service"
DOCKERHUB_USERNAME="home1docker1"
TAG="latest"

# Build the Docker image
echo "Building the Docker image..."
docker build -t ${IMAGE_NAME}:${TAG} .

# Check if the image was built successfully
if docker image inspect ${IMAGE_NAME}:${TAG} > /dev/null 2>&1; then
  echo "Docker image ${IMAGE_NAME}:${TAG} built successfully."
else
  echo "Error: Docker image ${IMAGE_NAME}:${TAG} not found."
  exit 1
fi

# Tag the Docker image
echo "Tagging the Docker image..."
docker tag ${IMAGE_NAME}:${TAG} ${DOCKERHUB_USERNAME}/${IMAGE_NAME}:${TAG}

# Check if the image was tagged successfully
if docker image inspect ${DOCKERHUB_USERNAME}/${IMAGE_NAME}:${TAG} > /dev/null 2>&1; then
  echo "Docker image ${DOCKERHUB_USERNAME}/${IMAGE_NAME}:${TAG} tagged successfully."
else
  echo "Error: Docker image ${DOCKERHUB_USERNAME}/${IMAGE_NAME}:${TAG} not found."
  exit 1
fi

# Push the Docker image to Docker Hub
echo "Pushing the Docker image to Docker Hub..."
docker push ${DOCKERHUB_USERNAME}/${IMAGE_NAME}:${TAG}

echo "Image ${DOCKERHUB_USERNAME}/${IMAGE_NAME}:${TAG} pushed to Docker Hub successfully."
