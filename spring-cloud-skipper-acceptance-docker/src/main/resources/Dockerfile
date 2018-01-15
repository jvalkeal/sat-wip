FROM java:8
VOLUME /tmp
ARG JAR_FILE
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar","--spring.datasource.url=jdbc:postgresql://postgres:5432/skipper","--spring.datasource.username=spring","--spring.datasource.password=spring","--spring.datasource.driver-class-name=org.postgresql.Driver","--spring.datasource.initialize=true","--spring.jpa.generate-ddl=true"]


