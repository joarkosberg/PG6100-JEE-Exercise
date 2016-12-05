# PG6100 - JEE Exercise
Exercise from PG6100 course.
The application is a quiz game with two REST api's, one SOAP, one JPA/EJB backend and a jacoco module.

Project consists of the following modules:
* quiz (Backend - JPA/EJB)
* quizApi (REST)
* gameApi (REST - Dropwizard)
* quizSoap (SOAP)
* jacoco (Code coverage)

To run project, first install from root folder. It will install all dependencies and run all tests (-DskipTests to skip tests)
```
mvn install
```

Then quizApi and quizSoap can be run by going to desired folder and running:
```
mvn wildfly:run
```

The gameApi can be run with the command:
```
java -jar gameApi-1.0-SNAPSHOT.jar server config.yml
```
