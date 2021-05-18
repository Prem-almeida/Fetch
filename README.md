# Fetch-Points Backend Api

a web service that accepts HTTP requests and returns responses based on the condition. Users have points in their accounts. Users only see a single balance in their accounts. But for reporting purposes we actually track their points per payer/partner. In our system, each transaction record contains: payer (string), points (integer), timestamp (date). 
For earning points it is easy to assign a payer, which actions earned the points are recorded. And thus which partner should be paying for the points. 
When a user spends points, they don't know or care which payer the points come from. But, our accounting team does care how 

the points are spent. There are two rules for determining what points to "spend" first: 
* Oldest points should spent first (oldest based on transaction timestamp, not the order they’re received)  
 
* No payer's points should go negative. 

## NOTE
- please contact me on premalmeida17@gmail.com if any issues with running the project
- The project is designed to store transactions in a sql database for a single user
- Some bonus routes are included

## Installation

###Install mySQL & mySQl workbench
you will need mysql workbench to import the database schema from the .sql file [Steps To Download & Install](https://dev.mysql.com/doc/workbench/en/wb-installing-mac.html) please remember the username and password of mysql as this is needed for configuration & connection of the database to the application

### Import Database Structure
There is a file included in the repo which can be imported in mysql_workbench or similar to create the database and table with exact fields
<br>
Steps
<br>
- Open MySQL workbench
- Login 
- Go to ```Server > Data Import```
- Select the ```Make_database.sql``` file from the repo
this should create the database in the system 
- [How to Import Data](https://dev.mysql.com/doc/workbench/en/wb-admin-export-import-management.html)

### Connecting to Database
```go to Fetch/Fetch-points/src/main/resources/application.properties``` here update the following fields
- spring.datasource.url=jdbc:mysql://localhost:3306/fetch?useSSL=false <br>this should be same for most system but if not please change 
- spring.datasource.username= [YOUR USERNAME]
- spring.datasource.password= [YOUR PASSWORD]


### [Running from an IDE](https://docs.spring.io/spring-boot/docs/1.5.16.RELEASE/reference/html/using-boot-running-your-application.html)
* You can run a Spring Boot application from your IDE as a simple Java application, however, first you will need to import your project. Import steps will vary depending on your IDE and build system. 
* Most IDEs can import Maven projects directly, for example Eclipse users can select Import…​ → Existing Maven Projects from the File menu.
If you can’t directly import your project into your IDE, you may be able to generate IDE metadata using a build plugin. Maven includes plugins for Eclipse and IDEA;


##  Routes:

### Add transactions for a specific payer &/ Date
will return integer value with the total points in the database for the user here the get_total(); function is used which internally uses a SQL query to calculate the total sum

- **Request Type:** POST
-** Request Route:** **```/fetch/v1/add```**

- **8Requestion Body: **

#### With Date
The Request body needs to be a JSON with the following paramenters<br>
Here Payer is a string , points is integer and timestamp is a ISO-standard date time format<br>

![Alt text](https://github.com/Prem-almeida/Fetch/blob/master/Fetch-points/Screenshots/add.png?raw=true)

```{ "payer": "DANNON", "points": 1000, "timestamp": "2020-11-02T14:00:00Z" } ```<br>
```{ "payer": "UNILEVER", "points": 200, "timestamp": "2020-10-31T11:00:00Z" } ```<br>
```{ "payer": "DANNON", "points": -200, "timestamp": "2020-10-31T15:00:00Z" } ```<br>
```{ "payer": "DANNON", "points": 300, "timestamp": "2020-10-31T10:00:00Z" } ```<br>
```{ "payer": "MILLER COORS", "points": 10000, "timestamp": "2020-11-01T14:00:00Z" } ```<br>

#### Without Date

![Alt text](https://github.com/Prem-almeida/Fetch/blob/master/Fetch-points/Screenshots/add_wo.png?raw=true)

```{ "payer": "DANNON", "points": 1000}```
if tthe timestamp is null then the system will assign the current timestamp of the system for the transaction


- Path Parameters: none
- Response Body: JSON


### Return all payer point balances
will return JSON response with the list of all transaction for the user in the database 

-- **Request Type:** GET
-- **Request Route:** **```/fetch/v1/all_trans```**
-- **Requestion Body:** none
-- **Path Parameters:s:** none
-- **Response Body:** JSON

![Alt text](https://github.com/Prem-almeida/Fetch/blob/master/Fetch-points/Screenshots/all_trans.png?raw=true)

### Return Total point:
will return integer value with the total points in the database for the user here the get_total(); function is used which internally uses a SQL query to calculate the total sum

- **Request Type:** GET
- **Request Route:** **```/fetch/v1/User_points```**
- **Requestion Body:** none
- **Path Parameters:** none
- **Response Body:** JSON


![Alt text](https://github.com/Prem-almeida/Fetch/blob/master/Fetch-points/Screenshots/user_points.png?raw=true)


### Redeem Points:
will return a list of items which were redeemed
reedem is done based on the two rules: 
1. Will sort for the oldest transaction timestamp and redeem the oldest transaction first
2. Will check if the total avaialbe points are enough for redeem and will return error if there are not enough points

- **Request Type:** PUT
- **Request Route:** **```/fetch/v1/redeem/{points}```**
- **Requestion Body:** none
- **Path Parameters:** int Points
- **Response Body:** JSON


![Alt text](https://github.com/Prem-almeida/Fetch/blob/master/Fetch-points/Screenshots/add.png?raw=true)

## Bonus Routes:

### Delete BY ID
will return JSON response with the status if the element with the id was deleted

-- **Request Type:** DELETE<br>
-- **Request Route:** **```/fetch/v1/remove/{id}```**<br>
-- **Requestion Body:** none<br>
-- **Path Parameters:s:** long id<br>
-- **Response Body:** JSON<br>

![Alt text](https://github.com/Prem-almeida/Fetch/blob/master/Fetch-points/Screenshots/remove_id.png?raw=true)

### Delete All Zeros
will delete all the values that have zero in the points section

-- **Request Type:** DELETE<br>
-- **Request Route:** **```/fetch/v1/remove_all```**<br>
-- **Requestion Body:** none<br>
-- **Path Parameters:s:** long id<br>
-- **Response Body:** JSON<br>

![Alt text](https://github.com/Prem-almeida/Fetch/blob/master/Fetch-points/Screenshots/remove_all.png?raw=true)


