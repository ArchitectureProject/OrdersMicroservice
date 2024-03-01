FROM openjdk:17-slim

ARG VERSION

EXPOSE 8084

ADD target/ordersmicroservice-$VERSION.jar app.jar

CMD ["java", "-jar", "app.jar"]