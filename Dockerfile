FROM openjdk:8-jre-alpine

RUN apk update
RUN apk add git

RUN mkdir /app

WORKDIR /app
RUN git clone https://github.com/Laranjada/scarif-starwars.git scarif
RUN cp ./scarif/config.yml /app
RUN chmod 711 ./scarif/gradlew

WORKDIR /app/scarif
RUN ./gradlew build --exclude-task test
RUN cp ./build/distributions/*.tar /app

WORKDIR /app


