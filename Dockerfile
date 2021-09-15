FROM openjdk:8-jdk-alpine

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

#Escaping is possible by adding a \ before the variable: \$foo or \${foo}
#For example, will translate to $foo and ${foo} literals respectively
ENV APP_OPTS="--spring.datasource.url=\${CONNECTION_URL}"
ENV APP_OPTS="${APP_OPTS} --spring.datasource.username=\${CONNECTION_USERNAME}"
ENV APP_OPTS="${APP_OPTS} --spring.datasource.password=\${CONNECTION_PASSWORD}"
ENV APP_OPTS="${APP_OPTS} --file.game-dir=./data"

RUN echo ${APP_OPTS}
ENTRYPOINT java -jar /app.jar ${APP_OPTS}