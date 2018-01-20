#!/bin/sh
sleep 30
exec java -Doracle.jdbc.timezoneAsRegion=false -Djava.security.egd=file:/dev/./urandom -jar /app.jar --spring.datasource.url="$SPRING_DATASOURCE_URL" --spring.datasource.username="$SPRING_DATASOURCE_USERNAME" --spring.datasource.password="$SPRING_DATASOURCE_PASSWORD" --spring.datasource.driver-class-name="$SPRING_DATASOURCE_DRIVER"

