FROM openjdk:11

ENV APP_ENV production
ENV DATA_PATH /data
VOLUME ["/data"]

WORKDIR "/app"
COPY map_points_display.jar /app

CMD ["java", "-jar", "map_points_display.jar"]
