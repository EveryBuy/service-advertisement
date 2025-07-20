# Advertisement Microservice API

This microservice provides APIs for managing advertisements, categories, cities, and user interactions with advertisements.

## Introduction

The Advertisement Microservice is part of the EveryBuy marketplace ecosystem designed to handle advertisement. It provides a range of functionalities including creating, updating, filtering, and deleting advertisements. Additionally, it allows managing categories, cities, and regions to support the advertisement listings.

 ## 🛠️ Technologies

| **Category**       | **Technology**                                                                                                                                | **Use Case**                          |  
|--------------------|-----------------------------------------------------------------------------------------------------------------------------------------------|---------------------------------------|  
| **Framework**      | ![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2%2B-6DB33F?logo=springboot)                                                        | REST API foundation                   |  
| **Database**       | ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-336791?logo=postgresql)                                                              | Primary relational data storage       |  
| **Search**         | ![Elasticsearch](https://img.shields.io/badge/Elasticsearch-7.10-005571?logo=elasticsearch)                                                   | Full-text ad search                  |  
| **Caching**        | ![Redis](https://img.shields.io/badge/Redis-7.2-DC382D?logo=redis)                                                                            | High-performance caching layer        |  
| **Cloud Storage**  | ![Amazon S3](https://img.shields.io/badge/Amazon_S3-569A31?logo=amazons3)                                                                     | Ad image storage & retrieval          |  
| **Dev Tools**      | ![MapStruct](https://img.shields.io/badge/MapStruct-1.6-3A75BD?logo=java), ![Lombok](https://img.shields.io/badge/Lombok-1.18-pink?logo=java) | Boilerplate reduction & DTO mapping |  



- **Spring Boot Web** – provides the foundation for building RESTful APIs.

- **Spring Data JPA** – simplifies database interactions using JPA with Hibernate.

- **Spring Security** – used for securing API endpoints and managing authentication/authorization.

- **Spring Validation** – used for request validation to ensure data consistency.

- **Spring Data Redis** – adds support for Redis-based caching and data storage.

- **Elasticsearch** – used for implementing full-text search capabilities within advertisement listings using the High-Level REST Client

- **Amazon S3 (AWS SDK)** – used for storing and retrieving advertisement images in a cloud-based storage solution

- **MapStruct** – generates mappers for converting between DTOs and entities.

- **Flyway** – used for versioned database schema migrations.

- **PostgreSQL JDBC Driver** – used to connect the microservice with the PostgreSQL database

- **Lombok** – reduces boilerplate code by generating getters, setters, constructors, etc.


## Base URL

https://service-advertisement-r8dt.onrender.com

## Open API Documentation

The OpenAPI documentation for this microservice can be found [here](https://app.swaggerhub.com/apis-docs/OlesiaSmahlii/EveryBuy/1.0#/Advertisement%20service).

## 🗺️ Architecture Diagram

```mermaid
graph TD  
    A[Client] --> B[API Gateway]  
    B --> C[Advertisement Service]  
    C --> D[(PostgreSQL)]  
    C --> E[(Elasticsearch)]  
    C --> F[(Redis)]  
    C --> G[[Amazon S3]]  
