# Usar una imagen base de Java
FROM openjdk:17-jdk-slim

# Establecer el directorio de trabajo en el contenedor
WORKDIR /app
RUN cd /app
# Copiar el archivo JAR del microservicio al contenedor
COPY ../habit-ask-notifications/target/habit-ask-notifications-1.0-SNAPSHOT.jar user-notifications-microservice.jar

# Exponer el puerto que usa el microservicio
EXPOSE 8082

# Comando para ejecutar el microservicio
ENTRYPOINT ["java", "-jar", "user-notifications-microservice.jar"]