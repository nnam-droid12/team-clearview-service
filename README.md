# ClearView Backend  

The backend for **ClearView**, a project that simplifies agreement management using Java Spring Boot, PostgreSQL, MongoDB, Redis, Google Calendar, and DocuSign APIs.

## Features  
- Upload, send, and sign documents with DocuSign API integration.  
- Real-time status updates using webhook notifications.  
- Sync agreements with milestones and deadlines to Google Calendar.  
- Data storage and management using PostgreSQL, MongoDB, and Redis.

---

## Getting Started  

### Prerequisites  
Ensure you have the following installed:  
- **Java 17+**  
- **Maven 3+**  
- **PostgreSQL 13+**  
- **MongoDB 6.0+**  
- **Redis 6.2+**  

### Environment Variables  
Set the following environment variables before starting the application:  
```env
SPRING_DATASOURCE_USERNAME=postgres  
SPRING_DATASOURCE_PASSWORD=1234  
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/contract_db  
SPRING_DATA_MONGODB_URI=mongodb://localhost:27017/contract_db  
REDIS_HOST=localhost  
REDIS_PORT=6379  
DOCUSIGN_PRIVATE_KEY=<Your Private Key>  
DOCUSIGN_INTEGRATION_KEY=<Your Integration Key>  
DOCUSIGN_USER_ID=<Your DocuSign User ID>  
DOCUSIGN_ACCOUNT_ID=<Your Account ID>  
DOCUSIGN_AUTH_SERVER=<Your Auth Server URL>  

### Installation
Clone the Repository:
   git clone [https://github.com/nnam-droid12/team-clearview-service]
cd team-clearview-service

### Set Up the Database:

1. Create a PostgreSQL database: contract_db.
2. Start MongoDB and Redis servers.
3. Build the Project:
mvn clean install

### Run the Application:
  mvn spring-boot:run
