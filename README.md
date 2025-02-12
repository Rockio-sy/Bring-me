
# Bring-Me: A Spring Web Application

**Bring-Me** is a Spring-based web application designed to connect people who need to send items with those willing to carry them. Whether you're sending a package or going on a trip, Bring-Me helps make the process easier and more efficient.


# Get started
```
git clone https://github.com/Rockio-sy/Bring-me.git
```
### Rename .env.example to .env with valid values
````shell
cd Source
nano .env.example
mv .env.example .env
````

### Docker 
```shell
cd Source
sudo docker-compose up --build
```
**Note**: Docker will run the application in dev profile, you can change it in the Dockerfile

### Try
```shell
curl -X 'GET'   'http://localhost:8080/bring-me/trips/all'   -H 'accept: application/json' | json_pp
```
## Swagger UI
Check [Swagger UI](http://localhost:8080/swagger-ui/index.html#/)
to check all endpoints (make sure the application is running), <br>**NOTE** It asks for http authentication.<br>
**email**: none@none.com<br>
**Password**: 12345678s
## Locally
#### 1. Development Profile
```sh
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```
#### 2. Production

```sh
mvn spring-boot:run
```
## Endpoints examples
Check Postman endpoints collection in [Postman Collections](https://github.com/Rockio-sy/Bring-me/tree/main/Postman_Collections)

# Arch
Check more about the application architecture in [Readme.md](https://github.com/Rockio-sy/Bring-me/blob/67b4e756089f592af2cfea4e19449d3d71292204/Docs/README.md)

## Stack

- **openjdk 21.0.5 2024-10-15** –Java version
- **Spring Boot 3.3.5** – Main framework for application development  
- **PostgreSQL 42.7.3** – Relational database   
- **Flyway 10.14.0** – Database migration tool   
- **Maven** – Build automation and dependency management  


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
