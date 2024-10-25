FROM amazoncorretto:17
COPY /target/meal-planner-app.jar /meal-planner-app.jar
ENTRYPOINT ["java","-jar","/meal-planner-app.jar"]