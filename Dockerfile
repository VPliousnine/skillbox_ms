FROM openjdk:17
RUN mkdir /app
COPY users.jar /app
EXPOSE 8080
ENV DB_NAME=users
ENV DB_PORT=5432
ENV DB_USER=postgres
ENV DB_PASS=123
ENV DB_ADDR=ms_study_pg_u
WORKDIR /app
CMD java -jar users.jar
