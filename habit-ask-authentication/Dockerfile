# Usar una imagen base de Java
FROM openjdk:17-jdk-slim

# Establecer el directorio de trabajo en el contenedor
WORKDIR /app
RUN cd /app

# Copiar el archivo JAR del microservicio al contenedor
COPY  habit-ask-authentication/target/habit-ask-authentication-1.0.0-SNAPSHOT.jar authentication-microservice.jar

# Exponer el puerto que usa el microservicio
EXPOSE 8080

# Comando para ejecutar el microservicio
ENTRYPOINT ["java", "-jar", "authentication-microservice.jar"]