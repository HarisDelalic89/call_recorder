### This is example app, so far made to track telecom customers, in later stages it is planned to track customer's calls and based on those create billing reports

So far project has been set up and includes:
- database to track all data about telecoms customers*
- using MVC pattern 
- with Data Transfer Objects
- with Controller Advice for globally fetching exception
- with proper HttpResponse codes inside controllers
- tests are separated in two folders (unit and integration) in gradle itself
- with unit tests for Customer entity
- with unit tests for CustomerService
- with integration tests, using slice tests for CustomerController, not to fetch whole spring boot contexts to slow them down
- with Implementation - Interface separation, using CustomerService and ICustomerService
- with REST versioning
- with Swagger for documenting APIs
- with Flyway for managing database scripts
- containerized with Docker
- with CI/CD using GCP cloud build. It is hooked to personal Github account so new push will trigger build and deploy containerized app to GoogleCloudPlatform
- deployed on Google Cloud Platform as Cloud Run

*One note:
Database used for this application is in memory db, called h2
Credentials for db are stored in application.properties, in source code.
This is wrong, and I'm aware of it. To do it properly following actions could be done:
1) GoogleCloudPlatform provides CloudSql service, and database should be chosen between Postgres or MySQL
GCP also supports non-relation databases but based on personal experience working with both,
relation and non-relation, if this app will grow relational DBs are better choice.
2) After creating CloudSql instance, serviceAccount should be created too, to allow access
from Application (deployed on Cloud Run) to database (which would be instance in Cloud Sql, with at least one read replica for performance reasons later when creating bills for customers)
3) Later SecretManager should be used to securely store database credentials (and other secret information)
