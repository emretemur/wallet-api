# WALLET-API

**Technologies used** 
- Java 11
- H2 DB
- Spring Boot, Spring Data JPA
  
 **Running instructions**

Run the following commands in the root folder of the application

./mvnw instal

./mvnw spring-boot:run


When the application is up and running, the balance will be automatically added for the playerId 10001.
You can use this playerId while calling credit, withdraw, balance, transaction history services.

Swagger url: http://localhost:4591/swagger-ui/index.html#/

**1) Current balance per player**

http://localhost:4591/swagger-ui/index.html#/balance-controller/getPlayerBalance

playerId = 10001

**2) Withdrawal per player**

Withdrawals can be made with the following requestHeader and requestBody.

http://localhost:4591/swagger-ui/index.html#/transaction-controller/withdrawMoney

RequestHeader -> playerId = 10001

RequestBody

{

"amount": 5,

"currency": "DOLLAR",

"transactionId": "000A-OOO1",

"channel": "WEB",

"description": "test"

}

**3) Credit per player**  

Deposits can be made with the following requestHeader and requestBody.

http://localhost:4591/swagger-ui/index.html#/transaction-controller/creditMoney

RequestHeader -> playerId = 10001

RequestBody

{

"amount": 20,

"currency": "DOLLAR",

"transactionId": "000A-OOO2",

"channel": "WEB",

"description": "test"

}


**4) Transaction history per player**

http://localhost:4591/swagger-ui/index.html#/transaction-controller/getTransactionHistory

The transaction history can be viewed using the request params below.

playerId: 10001

startDate: 2022-03-03 12:00:00

endDate: 2022-07-03 12:00:00

pageIndex: 0

pageSize: 20
