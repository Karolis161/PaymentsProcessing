FROM openjdk:17
MAINTAINER maintainer ="Karolis"
COPY build/libs/Notifications.jar Notifications.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/Notifications.jar", "-web -webAllowOthers -tcp -tcpAllowOthers -browser"]