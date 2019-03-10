FROM jetty:9-jre11

ENV APP_ENV production
ENV DATA_PATH /data
VOLUME ["/data"]

COPY map_points_display.war /var/lib/jetty/webapps/ROOT.war
