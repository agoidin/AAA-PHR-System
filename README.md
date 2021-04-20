# AAA PHR Management System

AAA - Authentication, Authorization and Accounting

PHR - Patient Health Record

## :clipboard: Table of contents 
  - [General Info](#information_source-general-info)
  - [Task Description](#white_check_mark-task-description)
  - [Technologies](#computer-technologies)
  - [Setup](#gear-setup)
  - [Implementation Notes](#bangbang-implementation-notes)

## :information_source: General Info
The project implements a secure AAA service for managing hospital operations and data (electronic health records). 
There are a client and a server side in the system. Server is responsible for sending queries to database, recieve and validate requests coming from client and create logs on system activity. Client side shows a graphical user interface to users and handles their requests.

System has a role-based access control model and policies and supports 4 types of users with different permissions:
 -  **Patients** are only able to see health information about themselves and they cannot edit any of the data.
 -  **Staff** is able to see information about all the patients and can create, delete, edit and read their records.
 -  **Regulators** are only able to read all records but are unable to change or edit them in any way.
 -  **Administrators** are only able to manage (create/delete) user accounts, they cannot see medical records.

Some of the features implemented in this project are encyption/decription keys exchange, session-key and credential negotiation, first-time login password, signup and login operations, support for password strength evaluation, storing password hash+salt, creation of audit logs for various (sensitive) activities or data access.

**System Design document** can be found in supporting docs repository folder *file: [System Design Document.pdf](https://github.com/agoidin/Patients-Health-Records-Management-System/blob/master/SupportingDocs/System%20Design%20Document.pdf)*
## :white_check_mark: Task Description
This project was a university group task (4 members in our team) for ***SCC363:Security and Risk*** module. Main idea is to design,document and develop a secure AAA system. The full task can be found in supporting docs repository folder *file: [Coursework Task.pdf](https://github.com/agoidin/Patients-Health-Records-Management-System/blob/master/SupportingDocs/Coursework%20Task.pdf)*
Second part of the project was an individual report. It describes risk analysis and security measures for our AAA service. My report can be in supporting docs repository folder *file: [Risk Report.pdf](https://github.com/agoidin/Patients-Health-Records-Management-System/blob/master/SupportingDocs/Risk%20Report.pdf)*
	
## :computer: Technologies
Project is created with:
* Java version: 8.0
* SQLite version: 3.8.11.2
* Java RMI
* Swing
* JDBC
* Java JCA
	
## :gear: Setup

**-- JAVA 8+ REQUIRED --**

1. FIRST RUN THE SERVER - Go to /Server:
OPEN COMMAND-LINE IN THAT FOLDER and run following command:

```
$ java -classpath ".;sqlite-jdbc-3.8.11.2.jar" Main
```

KEEP THE COMMAND-LINE OPEN

2. RUN THE CLIENT - Go to /Client
OPEN NEW COMMAND-LINE IN THAT FOLDER and run following command:

```
$ java Main
```

3. USE ONE OF THE USERS TO LOG IN:
(please clear all whitespaces)

|       Username       |      Password      |             Additional info             |
| :------------------: | :----------------: | :-------------------------------------: |
|   admin@phr.co.uk    |     Admin123!      |                                         |
|  patient1@email.com  | PatientPatient111! |                                         |
|  patient2@email.com  | PatientPatient222! |                                         |
|   staff1@phr.co.uk   |   StaffStaff111!   |                                         |
|   staff2@phr.co.uk   |   StaffStaff222!   | (first login, requires password change) |
| regulator1@phr.co.uk |   Regulator111!    |                                         |
| regulator2@phr.co.uk |   Regulator222!    | (first login, requires password change) |
| newpatient@test.com  |    TestTest123!    | (first login, requires password change) |



## :bangbang: Implementation Notes
As it was a university project with limited timeframes and certain learning objectives, the main focus was on functinality rather than interface design. Therefore, GUI is very simple and was developed just for easier visual testing purposes. Also, project layout is simple and files are not split in different packages.
