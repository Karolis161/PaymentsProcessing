FROM openjdk:17
MAINTAINER maintainer ="Karolis"
COPY build/libs/Payments.jar Payments.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/Payments.jar", "-web -webAllowOthers -tcp -tcpAllowOthers -browser"]