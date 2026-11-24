# CampusBooking

CampusBooking is a Java learning project that will become a web
application for booking rooms and study spaces.

The goal is to build a complete application step by step while
learning automated testing, databases, Spring Boot and CI.

## Current status

Early development. The project currently includes:

- A Java application entry point
- A Maven build configuration
- A GitHub Actions workflow for building and testing the project

Room management and booking features are not implemented yet.

## Technology

Currently used:

- Java 25
- Maven
- GitHub Actions

Planned:

- Spring Boot
- PostgreSQL
- Thymeleaf
- JUnit

## Getting started

### Requirements

- JDK 25
- Maven available in your terminal

### Clone the repository

```powershell
git clone https://github.com/glasertim-prog/campus-booking.git
cd campus-booking
```

### Build and verify

`mvn verify`

This compiles the code, runs any available tests and packages
the application. Automated tests have not been added yet.

### Run the application

Open the project in IntelliJ IDEA and run the main method in
CampusBookingApplication.

The application currently prints a startup message to the console.

## Continuous integration

GitHub Actions runs on pushes and pull requests.

The workflow checks out the code, sets up Temurin JDK 25
and runs Maven verify on an Ubuntu runner.

Workflow configuration: [.github/workflows/ci.yml](.github/workflows/ci.yml)

## Next steps

- Implement room and booking models
- Add unit tests for booking rules
- Reject invalid and overlapping bookings
- Introduce a Spring Boot web interface
- Store rooms and bookings in PostgreSQL