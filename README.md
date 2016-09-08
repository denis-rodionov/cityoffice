# CityOffice
[![Build Status](https://travis-ci.org/denis-rodionov/cityoffice.svg?branch=master)](https://travis-ci.org/denis-rodionov/cityoffice)

This is a light-weight project which goal is to help managers of all levels to control documents, important deadlines and resource allocation in different projects. At the moment this tool includes the following parts:
- **Documents:** tool for managing deadlines and documents;
- **Projects:** tool for managing employees participation in projects and their loading;
- **Employees:** tool for controlling schedule of any employee (In Development)
- **Settings:** role-based setting tool: it allows to fill any content (Documents, Employees and their schedules, Projects etc.)

### Documents
At the moment the tool is based on the documents which has the following fields:
- name (any short name of the document);
- deadline;
- project;
- responsible (employee which will receive email notifications about the deadline);
- email notification scheme (frequency of notifications);
- description (any notes about the document/deadline);

Every user which subscribed to a project (via Settings -> Users -> Projects) will receice notification emails according to the choosen for the document notification scheme.

### Projects
Light-weight visualization of recourse (employees) allocation by projects. It allow to choose the necessary project and find resource allocation for the nearest future.

## Staging
https://cityoffice.herokuapp.com/index.html#/
login: admin@admin.com
password: admin

## Local installation
- install java 1.8
- check that java installed correctly by command ```java -version```. The command must return java version not bellow 1.8
- check that javac installed correctly by command ```javac -version```. The command must return java version not bellow 1.8
- install maven >= 3.0
- check that maven installed correctly by command ```mvn -version```.
- install MongoDB (preferably install it as a service, otherwise need to start it every time you launch application)
- compile application by command ```mvn install```
- run application by command ```mvn spring-boot:run```
- check application is working on ```localhost:8080```
- log in with admin@admin.com and password: admin
