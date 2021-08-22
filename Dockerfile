FROM openjdk:11
COPY /build/libs/libraryapp-*-SNAPSHOT.jar /app/libapp.jar
WORKDIR /app
CMD java -jar libapp.jar
EXPOSE 8081