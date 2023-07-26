FROM adoptopenjdk:11-jdk-hotspot

WORKDIR /app

COPY . .

RUN ./mvnw  package -DskipTests

RUN adduser --system user
USER user

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "target/mark-sms.jar"]

