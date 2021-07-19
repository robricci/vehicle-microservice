FROM alpine:latest

# Install Java
RUN apk --no-cache add openjdk11-jre

ENV JAVA_HOME /usr/lib/jvm/default-jvm

# Set Unlimited Strength Jurisdiction Policy Files
COPY UnlimitedJCEPolicyJDK8/ $JAVA_HOME/jre/lib/security/

RUN mkdir /app \
    && cd /app

WORKDIR /app

ADD vehicle-0.0.1-SNAPSHOT.jar vehicle-0.0.1-SNAPSHOT.jar

ENV APP_HOME /app

ENV PATH $APP_HOME/bin:$PATH

EXPOSE 8100

RUN chmod 777 -R /app

CMD ["java", "-jar", "vehicle-0.0.1-SNAPSHOT.jar"]
