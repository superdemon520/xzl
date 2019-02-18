FROM registry.xzlcorp.com/backend/jre9:latest

MAINTAINER dongkewei@xinzhili.cn

EXPOSE 9011

ENTRYPOINT ["java", "-jar", "chat-service.jar"]

ADD build/libs/chat-service-*.jar chat-service.jar
