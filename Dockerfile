FROM openjdk:17-alpine
EXPOSE 8081
COPY ./build/libs/authservice-0.0.1.jar /tmp/
WORKDIR /tmp
ENTRYPOINT ["java","-jar", "-Dspring.profiles.active=docker", "authservice-0.0.1.jar"]