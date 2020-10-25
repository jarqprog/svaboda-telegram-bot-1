#!/usr/bin/env bash
cd ../codebase && gradle clean && gradle test && gradle bootJar
java -jar "$SERVICE_NAME"/build/libs/"$SERVICE_NAME"-1.0.0-SNAPSHOT.jar
