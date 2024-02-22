FROM openjdk:17-oracle
COPY target/*.jar online-gallery-service-demo.jar
EXPOSE 8075
ENTRYPOINT [ "java", "-jar", "online-gallery-service-demo.jar" ]
