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

### Import Database Structure
There is a file included in the repo which can be imported in mysql_workbench or similar to create the database and table with exact fields

### [Running from an IDE](https://docs.spring.io/spring-boot/docs/1.5.16.RELEASE/reference/html/using-boot-running-your-application.html)
* You can run a Spring Boot application from your IDE as a simple Java application, however, first you will need to import your project. Import steps will vary depending on your IDE and build system. 
* Most IDEs can import Maven projects directly, for example Eclipse users can select Import…​ → Existing Maven Projects from the File menu.
If you can’t directly import your project into your IDE, you may be able to generate IDE metadata using a build plugin. Maven includes plugins for Eclipse and IDEA;


## Funcationing & Features


##  Routes:

### Return all payer point balances
will return JSON response with the list of all transaction for the user in the database 

- Request Type: GET
- Request Route: **```/fetch/v1/all_trans```**
- Requestion Body: none
- Path Parameters: none
- Response Body: JSON

![Alt text](relative/path/to/img.jpg?raw=true "Title")

### Return Total point:
will return integer value with the total points in the database for the user here the get_total(); function is used which internally uses a SQL query to calculate the total sum

- Request Type: GET
- Request Route: **```/fetch/v1/User_points```**
- Requestion Body: none
- Path Parameters: none
- Response Body: JSON


![Alt text](relative/path/to/img.jpg?raw=true "Title")


### Add transactions for a specific payer &/ Date
will return integer value with the total points in the database for the user here the get_total(); function is used which internally uses a SQL query to calculate the total sum

- Request Type: Post
- Request Route: **```/fetch/v1/add```**

- Requestion Body: 

#### With Date
The Request body needs to be a JSON with the following paramenters<br>
Here Payer is a string , points is integer and timestamp is a 
```{ "payer": "DANNON", "points": 1000, "timestamp": "2020-11-02T14:00:00Z" } ```<br>
```{ "payer": "UNILEVER", "points": 200, "timestamp": "2020-10-31T11:00:00Z" } ```<br>
```{ "payer": "DANNON", "points": -200, "timestamp": "2020-10-31T15:00:00Z" } ```<br>
```{ "payer": "DANNON", "points": 300, "timestamp": "2020-10-31T10:00:00Z" } ```<br>
```{ "payer": "MILLER COORS", "points": 10000, "timestamp": "2020-11-01T14:00:00Z" } ```<br>

#### Without Date
xx

- Path Parameters: none
- Response Body: JSON


![Alt text](relative/path/to/img.jpg?raw=true "Title")
### Spend points using the rules above and return a list of { "payer": <string>, "points": <integer> } for each call.  


