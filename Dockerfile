FROM openjdk:11

EXPOSE 8080

RUN mkdir ./app

COPY ./build/libs/RateToGif-*.jar ./app/app.jar

ENTRYPOINT ["java","-jar","/app/app.jar"]
