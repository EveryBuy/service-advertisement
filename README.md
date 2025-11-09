# Advertisement Microservice API

This microservice provides APIs for managing advertisements, categories, cities, and user interactions with advertisements.

## Architecture Diagram

```mermaid
graph TD  
    A[Client] --> B[API Gateway]  
    B --> C[Advertisement Service]  
    C --> D[(PostgreSQL)]  
    C --> E[(Elasticsearch)]  
    C --> F[(Redis)]  
    C --> G[[Amazon S3]]
    C --> H[Apache Kafka]
    style A fill:#4A90E2,color:white
    style B fill:#9013FE,color:white
    style C fill:#6DB33F,color:white
    style D fill:#336791,color:white
    style E fill:#005571,color:white
    style F fill:#DC382D,color:white
    style G fill:#FF9900,color:white
    style H fill:#231F20,color:white
```

## Introduction

The Advertisement Microservice is part of the EveryBuy marketplace ecosystem designed to handle advertisement. It provides a range of functionalities including creating, updating, filtering, and deleting advertisements. Additionally, it allows managing categories, cities, and regions to support the advertisement listings.

 ## 🛠️ Technologies 

| **Category**       | **Technology**                                                                                                                                | **Use Case**                          |  
|--------------------|-----------------------------------------------------------------------------------------------------------------------------------------------|---------------------------------------|  
| **Framework**      | ![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)                           | REST API foundation                   |  
| **Database**       | ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-336791?style=for-the-badge&logo=postgresql&logoColor=white)                             | Primary relational data storage       |  
| **Search**         | ![Elasticsearch](https://img.shields.io/badge/Elasticsearch-005571?style=for-the-badge&logo=elasticsearch&logoColor=white)                    | Full-text ad search              
| **Caching**        | ![Redis](https://img.shields.io/badge/Spring_Data_Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white)                                | High-performance caching layer        |  
| **Message Broker** | ![ApacheKafka](https://img.shields.io/badge/Apache_Kafka-231F20?style=for-the-badge&logo=apachekafka&logoColor=white)                         | Asynchronous event processing         |  
| **Cloud Storage**  | ![Amazon S3](https://img.shields.io/badge/Amazon_S3-569A31?style=for-the-badge&logo=amazons3&logoColor=white)                                 | Ad image storage & retrieval          |  
| **Dev Tools**      | ![MapStruct](https://img.shields.io/badge/MapStruct-3A75BD?logo=java&logoColor=white), ![Lombok](https://img.shields.io/badge/Lombok-pink?logo=projectlombok&logoColor=white) | Boilerplate reduction & DTO mapping |  




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


