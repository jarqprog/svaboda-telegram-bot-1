#!/usr/bin/env bash
cd ../codebase && ./gradlew -p "$SERVICE_NAME" check && ./gradlew "$SERVICE_NAME":bootJar
java -jar "$SERVICE_NAME"/build/libs/"$SERVICE_NAME"-1.0.0-SNAPSHOT.jar
