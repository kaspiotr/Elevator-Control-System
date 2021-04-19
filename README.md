# Elevator Control System

## How to run the application
This application is a backend app for Elevator Control System
written in Spring Boot and communicates with frontend application (available here:
[Frontend App](https://github.com/kaspiotr/Elevator-Control-System-FrontEnd-App))
using its REST API.  
The app could be also used as a standalone app by making requests on its
endpoints with the use of the web browser.

The app is available at _localhost:8081_ after running.  
[Frontend App](https://github.com/kaspiotr/Elevator-Control-System-FrontEnd-App) is available at _localhost:4200_ after running.

Application is built with maven tool so all dependencies are
available in _pom.xml_ file.

The app leverages also:
- Lombok library to handle constructors, getter and setters creation,
- JUnit 5 library for tests,
- Travis CI as continuous integrations tool
