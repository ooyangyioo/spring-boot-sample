FROM openjdk:11-jre

RUN ln -snf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime; \
    echo Asia/Shanghai > /etc/timezone; \
    useradd -ms /bin/bash postal
COPY ./lib/ /home/postal/lib/
COPY ./cps-postal-server.jar /home/postal/app.jar
COPY ./docker-entrypoint.sh /usr/local/bin/docker-entrypoint.sh
RUN mkdir /home/postal/logs; \
    chmod +x /usr/local/bin/docker-entrypoint.sh; \
    chown postal:postal /home/postal/logs; \
    chown postal:postal /home/postal/lib; \
    chown postal:postal /home/postal/app.jar
USER postal
VOLUME /home/postal/logs
WORKDIR /home/postal

ENV active dev

ENTRYPOINT ["docker-entrypoint.sh"]

EXPOSE 8081
CMD ["java", "-jar", "/home/postal/app.jar"]