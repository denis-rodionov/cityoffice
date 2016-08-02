# CityOffice
[![Build Status](https://travis-ci.org/denis-rodionov/cityoffice.svg?branch=master)](https://travis-ci.org/denis-rodionov/cityoffice)

This is a light-weight project which goal is to help managers of all levels to control important deadlines in different projects. At the moment system is based on the documents which has the following fields:
- name
- deadline
- project
- responsible
- email notification scheme

Currently the project consists ofr three parts:
- dashboard: where documents can be viewed grouped by months
- admin: the tool for all necessary settings: users, projects, notifications, documents
- email notifications: web-based email sender which notify users about deadline.

Future plans:
- light-weight resourse planning tool

## Local installation
- install java 1.8
- check that java installed correctly by command ```java -version```. The command must return java version not bellow 1.8
- install maven >= 3.0
- check that maven installed correctly by command ```mvn -version```.
- install MongoDB (preferably install it as a service, otherwise need to start it every time you launch application)
- run application by command ```mvn spring-boot:run```
- check application is working on ```localhost:8080```
