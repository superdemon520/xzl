FROM registry.xzlcorp.com/backend/jre9:latest

MAINTAINER donggan@xinzhili.cn

EXPOSE 8060

ENTRYPOINT ["java", "-jar", "auth-server.jar"]

ADD build/libs/auth-server-*.jar auth-server.jar

