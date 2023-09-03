# Étape 1 : Construire l'application Spring Boot avec Maven
FROM maven:3.8.4-openjdk-11 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Étape 2 : Créer une image Docker avec l'application construite
FROM adoptopenjdk/openjdk11:jre-11.0.9_11-alpine

# Copier le fichier JAR généré dans l'étape précédente dans le conteneur
COPY --from=builder /app/target/mark-sms.jar /app/mark-sms.jar

# Définir le port sur lequel l'application Spring Boot écoutera (par défaut : 8080)
EXPOSE 8080

# Créer un utilisateur non privilégié pour exécuter l'application
RUN adduser -D myuser
USER myuser

# Commande pour exécuter l'application lors du démarrage du conteneur
CMD ["java", "-jar", "/app/mark-sms.jar"]
