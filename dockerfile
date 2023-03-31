FROM openjdk:20-ea-9-jdk-bullseye
ADD target/myapp-1.0-SNAPSHOT.jar .
EXPOSE 8090
CMD java -jar myapp-1.0-SNAPSHOT.jar

