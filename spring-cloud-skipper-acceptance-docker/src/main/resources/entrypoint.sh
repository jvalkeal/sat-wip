#!/bin/sh
sleep 5
exec java -Djava.security.egd=file:/dev/./urandom -jar /app.jar --spring.datasource.url="$SPRING_DATASOURCE_URL" --spring.datasource.username=spring --spring.datasource.password=spring --spring.datasource.driver-class-name="$SPRING_DATASOURCE_DRIVER" --spring.datasource.initialize=true --spring.jpa.generate-ddl=true
