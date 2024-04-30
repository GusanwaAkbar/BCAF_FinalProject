#FROM khipu/openjdk17-alpine
#
#EXPOSE 8080
#
#ADD target/finalproject.jar finalproject.jar
#
#ENTRYPOINT ["java", "-jar", "finalproject.jar"]

FROM khipu/openjdk17-alpine

WORKDIR /app

COPY target/mgaspringfinalexam.jar mgaspringfinalexam.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "mgaspringfinalexam.jar"]
