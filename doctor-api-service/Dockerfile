FROM registry.xzlcorp.com/backend/jre9:latest

MAINTAINER donggan@xinzhili.cn

EXPOSE 9010

ENTRYPOINT ["java", "-jar", "doctor-api-service.jar"]

ADD build/libs/doctor-api-service-*.jar doctor-api-service.jar
