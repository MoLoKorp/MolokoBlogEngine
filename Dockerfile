FROM golang:1.15-alpine AS mongo-tools-builder
RUN apk add git gcc musl-dev krb5-dev \
    && git clone https://github.com/mongodb/mongo-tools \
    && cd mongo-tools \
    && ./make build

FROM amazoncorretto:17-alpine-jdk
COPY --from=mongo-tools-builder /go/mongo-tools/bin/mongoexport /bin/mongoexport
COPY --from=mongo-tools-builder /go/mongo-tools/bin/mongoimport /bin/mongoimport
COPY build/libs/*.jar app.jar
RUN apk add krb5
ENTRYPOINT ["java","-jar","/app.jar"]
