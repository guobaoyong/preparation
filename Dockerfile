FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER admin@qqzsh.top

RUN mkdir -p /preparation

WORKDIR /preparation

EXPOSE 10010

ADD ./target/preparation-3.0.0-SNAPSHOT.jar ./app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]

CMD ["--spring.profiles.active=test"]
