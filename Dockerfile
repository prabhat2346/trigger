FROM openjdk:8
VOLUME /tmp

RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app/

ARG NECEVASOCKETADDRESS
ARG SPRINGREDISHOST
ARG SPRINGREDISPORT
ARG SPRINGREDISTIMEOUT
ARG RULEFILENAME
ARG SERVERPORT
ARG EDGEDEVICEID
ARG MESSAGETRIGGERPERCENTAGE
ARG STOREID
ARG TENANTID
ARG CURRENTWEATHER
ARG LOGGINGFILEMAXSIZE=10MB
ARG LOGGINGFILEMAXHISTORY=3

ENV socket.address=${NECEVASOCKETADDRESS}
ENV redis.host=${SPRINGREDISHOST}
ENV redis.port=${SPRINGREDISPORT}
ENV redis.timeout=${SPRINGREDISTIMEOUT}
ENV filename=${RULEFILENAME}
ENV serverport=${SERVERPORT}
ENV device.id=${EDGEDEVICEID}
ENV trigger.percentage=${MESSAGETRIGGERPERCENTAGE}
ENV sid=${STOREID}
ENV tid=${TENANTID}
ENV weather=${CURRENTWEATHER}
ENV logging.file.max-size=${LOGGINGFILEMAXSIZE}
ENV logging.file.max-history=${LOGGINGFILEMAXHISTORY}

ARG JAR_FILE
ARG ACTIVE_PROFILE
ENV buildenv ${ACTIVE_PROFILE}
COPY ${JAR_FILE} app.jar

ARG CLIPSO_FILE

COPY ${CLIPSO_FILE} libCLIPSJNI.so
ENV LD_LIBRARY_PATH "$LD_LIBRARY_PATH:/usr/src/app/libCLIPSJNI.so"

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","-Dspring.profiles.active=${buildenv}", "-Dneceva.socket.address=${socket.address}","-Dspring.redis.host=${redis.host}","-Dspring.redis.port=${redis.port}","-Dspring.redis.timeout=${redis.timeout}","-Drule.fileName=${filename}", "-Dserver.port=${serverport}","-Dedge.device.id=${device.id}","-Dmessage.trigger.percentage=${trigger.percentage}","-Dstore.id=${sid}","-Dtenant.id=${tid}","-Dcurrent.weather=${weather}","-Dspring.logging.file.max-size=${logging.file.max-size}","-Dspring.logging.file.max-history=${logging.file.max-history}","/usr/src/app/app.jar"]
