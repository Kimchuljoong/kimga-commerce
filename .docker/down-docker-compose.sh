#!/bin/bash

PROJECT_NAME="kimga-commerce"
running_containers=$(docker-compose -p $PROJECT_NAME ps -q)

if [ -z "$running_containers" ]; then
  echo "'$PROJECT_NAME' is not running."
else
  echo "Stopping '$PROJECT_NAME'..."
  docker-compose -p $PROJECT_NAME down
  if [ $? -eq 0 ]; then
    echo "'$PROJECT_NAME' stopped successfully."
  else
    echo "Failed to stop '$PROJECT_NAME'."
  fi
fi