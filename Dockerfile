FROM maven:3.9-eclipse-temurin-17

WORKDIR /app

COPY . .

RUN mvn clean package -Dmaven.test.skip=true

CMD ["java", "-jar", "target/*.jar"]
