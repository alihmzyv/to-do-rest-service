
# To Do REST SERVICE

The main idea of the project is to build a Trello-like application that's useful for managing tasks digitally instead of writing them down on paper. This the REST service for it.
## Documentation
After running the application locally as described below, you can access the documentation at:
[Local Swagger Documentation](http://localhost:8080/swagger-ui.html)
## Environment Variables

To run this project, you will need to set the following environment variables:

`JWT_SECRET`

`DATABASE_HOST`

`DATABASE_PORT`

`DATABASE_NAME`

`DATABASE_USERNAME`

`DATABASE_PASSWORD`

`MAIL_USERNAME`

`MAIL_PASSWORD`


## Run Locally

Clone the project

```bash
  git clone https://github.com/alihmzyv/to-do-rest-service.git
```

Build the project: Once you have Maven installed, navigate to the root directory of the project in your terminal and run the following command to build the project:
```bash
  mvn clean install
```

Run the application: Once the project is built successfully, you can run the Spring Boot application using the following command:
```bash
  mvn spring-boot:run
```
This command will start the Spring Boot application on your local server, and you can access it by opening a web browser and entering the URL http://localhost:8080.

## Tech Stack
**Server**: Spring Boot

**Database**: PostgreSQL

**API Documentation**: Springdoc OpenAPI

**ORM**: Spring Data JPA

**Database Migration**: Liquibase

**Security**: Spring Security, JWT

**Build Tool**: Maven

**Boilerplate Code Generators**: MapStruct, Lombok

**Caching**: Hazelcast
## Acknowledgements

- This project was inspired by [DAN.IT](https://dan-it.com/) company, and I would like to express my sincere gratitude to their team for providing the idea and inspiration for this project.