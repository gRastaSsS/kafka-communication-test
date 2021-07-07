FROM gradle:6.7-jdk11 AS build
COPY --chown=gradle:gradle ./connect-transformers /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

FROM confluentinc/cp-kafka-connect-base:6.2.0
RUN confluent-hub install --no-prompt confluentinc/kafka-connect-jdbc:10.2.0
ENV MYSQL_DRIVER_VERSION 8.0.24
RUN curl -k -SL "http://dev.mysql.com/get/Downloads/Connector-J/mysql-connector-java-${MYSQL_DRIVER_VERSION}.tar.gz" \
    | tar -xzf - -C /etc/kafka-connect/jars --strip-components=1 mysql-connector-java-${MYSQL_DRIVER_VERSION}/mysql-connector-java-${MYSQL_DRIVER_VERSION}.jar
COPY --from=build /home/gradle/src/build/libs/*.jar /etc/kafka-connect/jars/
