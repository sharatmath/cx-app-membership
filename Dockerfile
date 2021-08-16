FROM sgrio/java:jdk_11_ubuntu
VOLUME /tmp
ADD oci /oci
ADD wallet-dev /wallet-dev
ARG jar=target/prepaid-membership-0.0.1-SNAPSHOT.jar
ADD $jar app.jar
ENV JAVA_OPTS $JAVA_OPTS "-Xms2048m -Xmx4096m"
ENV JAVA_OPTS $JAVA_OPTS "-Dserver.port=8080"
ENV JAVA_OPTS $JAVA_OPTS "-Dspring.profiles.active=sit"
ENV JAVA_OPTS $JAVA_OPTS "-Djava.security.egd=file:/dev/./urandom"
ENTRYPOINT exec java $JAVA_OPTS -jar /app.jar

