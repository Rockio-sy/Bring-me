
# Bring-Me: A Spring Web Application

**Bring-Me** is a Spring-based web application designed to connect people who need to send items with those willing to carry them. Whether you're sending a package or going on a trip, Bring-Me helps make the process easier and more efficient.

## Technologies Used  
### Java Version
- **openjdk 21.0.5 2024-10-15** –Java version

### Core Framework  
- **Spring Boot 3.3.5** – Main framework for application development  
- **Spring Boot Security** – Handles authentication and authorization  
- **Spring Boot Validation** – Provides validation support  
- **Spring Boot Web** – REST API development  

### Database & Persistence  
- **PostgreSQL 42.7.3** – Relational database  
- **Spring Boot JDBC** – Database interaction support  
- **Flyway 10.14.0** – Database migration tool  

### API Documentation  
- **Springdoc OpenAPI 2.1.0** – Generates OpenAPI documentation  
- **Springfox Swagger UI 3.0.0** – API documentation interface  

### JSON Processing  
- **Jackson Databind 2.15.0** – JSON serialization and deserialization  
- **Jackson Annotations 2.15.0** – JSON annotation support  
- **Jackson Core 2.15.0** – Core JSON processing library  

### Security & Authentication  
- **Spring Security** – Provides authentication and security features  
- **JWT (JJWT 0.12.6)** – JSON Web Token implementation  

### Environment Configuration  
- **Java-Dotenv 5.2.2** – Loads environment variables from `.env` file  

### Logging  
- **Logback Classic 1.5.6** – Logging framework  

### Build & Dependency Management  
- **Maven** – Build automation and dependency management  

### Testing  
- **Spring Boot Starter Test** – Testing utilities for unit and integration tests  


## How It Works

1. **Post an Item**  
   A person (Person 1) can post an item they need to send. The listing includes details such as:  
   - **Origin**  
   - **Destination**  
   - **Size (Width, Length, Height)**  
   - **Photo**  
   And other relevant information about the item.

2. **Search for Items**  
   A second person (Person 2) can search for items that are along their route and that they are able to carry.

3. **Request to Carry**  
   Person 2 can send a request to Person 1, indicating their willingness to transport the item on their way.

4. **Review and Approve**  
   Person 1 can review Person 2’s account, including ratings and comments from other users, before approving the request.

5. **Contact Exchange**  
   If Person 1 approves the request, both parties will be able to exchange contact information to finalize the details of the exchange.

---

**Note:** This process is reciprocal. If Person 2 wishes to post a trip instead, Person 1 can search for compatible trips to find something to send along the way.

## Clone 
```
git clone https://github.com/Rockio-sy/Bring-me.git
cd Bring-me
```

## Build  

**Note:**  Tests will run automatically during the build process. You can skip the tests by adding the flag:  
`-Dmaven.test.skip=true`


To build the project, run:  
```
mvn install
```

## Clean  

To clean the project, use:  
```
mvn clean
```

## Run  

### By Docker  
**Note:**  This will run the application in the **developing** profile. You can change the profile in the `Dockerfile`.  

```sh
docker-compose up --build
```

**Note for Linux users:**  
>Running Docker may require **sudo** privileges.  

### Locally (Maven)  

#### 1. Production  
```sh
mvn spring-boot:run
```

#### 2. Development Profile  
```sh
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```
