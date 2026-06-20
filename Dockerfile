# Stage 1: Build the React frontend
FROM node:18-alpine AS frontend-builder
WORKDIR /frontend
COPY frontend/package*.json ./
RUN npm install
COPY frontend/ ./
RUN npm run build

# Stage 2: Build the Spring Boot backend
FROM maven:3.9-eclipse-temurin-17-alpine AS backend-builder
WORKDIR /app
COPY pom.xml ./
RUN mvn dependency:go-offline
COPY src ./src
# Create the target directory for static resources and copy frontend build
RUN mkdir -p src/main/resources/static
COPY --from=frontend-builder /frontend/dist src/main/resources/static
# Build the Spring Boot JAR
RUN mvn clean package -DskipTests

# Stage 3: Setup the JRE environment to run the JAR
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=backend-builder /app/target/smart-task-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
