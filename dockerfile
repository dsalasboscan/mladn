FROM openjdk:8

ADD build/libs/mladn-1.0.0-RELEASE.jar /tmp/mladn-1.0.0-RELEASE.jar

WORKDIR /tmp

ADD src/main/resources/application.yml /tmp/config/application.yml

ENTRYPOINT ["java", "-jar", "/tmp/mladn-1.0.0-RELEASE.jar"]