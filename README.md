# Real-Time Transaction Fraud Detection System

This project is a Spring Boot application designed for real-time fraud detection in financial transactions. The system utilizes an event-driven architecture with Apache Kafka for processing and analyzing transactions.

## Architecture

The system comprises two main modules:

1. **Producer**: Ingests new transactions via a REST API and sends them to a Kafka topic.
2. **Consumer**: Listens for transactions from the Kafka topic, calls an external service to assess for fraud, and then stores the transaction result (including fraud status) into a MySQL database.

The overall data flow is as follows:
```
Client -> Producer (REST API) -> Kafka Topic -> Consumer -> Fraud Detection Service (External API) -> Database (MySQL)
```

## Technologies Used

- **Java 17**
- **Spring Boot 3.2.2**
- **Spring Kafka**: For Apache Kafka integration
- **Spring Web**: For building REST APIs
- **Spring Data JPA**: For database interaction
- **Apache Kafka**: Event streaming platform
- **MySQL**: Relational database for storing checked transactions
- **Maven**: Project management and build tool
- **Lombok**: Library to reduce boilerplate code
- **Jackson**: Library for JSON processing
- **Spring Cloud Azure**: For Azure service integration (used for Kafka)

## Modules

The project is divided into the following modules (located in the `real-time_fraud_detection` root directory):

### 1. `producer`
- **Description**: Receives transaction information via a REST endpoint and publishes it to the `balance_check_command` Kafka topic.
- **API Endpoint**:
  - `POST /test/sendtransaction`: Accepts a `TransactionRecord` JSON object in the request body.
- **Main Configuration File**: `producer/src/main/resources/application.yml`

### 2. `consumer`
- **Description**: Consumes transaction records from the `balance_check_command` Kafka topic. For each transaction, it calls an external fraud detection API and then stores the transaction details along with the fraud check result into a MySQL database.
- **Main Configuration File**: `consumer/src/main/resources/application.yml`

## Prerequisites

Before running the application, ensure you have the following installed and configured:

- **Java Development Kit (JDK)**: Version 17 or higher
- **Apache Maven**: For building the project
- **Apache Kafka**: A running Kafka cluster. The project is configured to connect to Azure Event Hubs, but you can change the configuration in the `application.yml` file of each module to point to your Kafka cluster
- **MySQL Server**: A running MySQL instance
- **Fraud Detection API**: An accessible external API endpoint for checking transaction fraud

## Configuration

Configuration for each module is located in their respective `src/main/resources/application.yml` files.

### Environment Variables

The following important configurations should be provided as environment variables or updated directly in the `application.yml` files (not recommended for sensitive information in a production environment):

**For both `producer` and `consumer` (Kafka connection):**
- `BOOTSTRAP_SERVERS`: The Kafka bootstrap servers address (e.g., `your-kafka-broker1:9092,your-kafka-broker2:9092`)
- `USERNAME`: Username for SASL/PLAIN (e.g., `$ConnectionString` for Azure Event Hubs)
- `PASSWORD`: Password or connection string for SASL/PLAIN

**For `consumer` only:**
- **MySQL Datasource Configuration** (in `consumer/src/main/resources/application.yml`):
  - `spring.datasource.url`: JDBC connection URL to MySQL (default: `jdbc:mysql://localhost:3306/transactions`)
  - `spring.datasource.username`: MySQL username (default: `root`)
  - `spring.datasource.password`: MySQL password (default: empty)
- `API_URL`: The full URL of the external fraud detection API that the `FraudetectionClient` will call

### Kafka Topic

The project uses a Kafka topic named `balance_check_command`. Ensure this topic is created in your Kafka cluster.

## How to Run

### 1. Build the Project

Navigate to the project's root directory (`real-time_fraud_detection`) and run the following Maven command:

```bash
mvn clean install
```

### 2. Run the Producer Module

Navigate to the producer module's directory:

```bash
cd producer
```

Then, run the Spring Boot application (ensure Kafka environment variables are set):

```bash
mvn spring-boot:run
```

Or run the built jar file:

```bash
java -jar target/producer-1.0-SNAPSHOT.jar
```

The producer will start and run on port 8085 (configurable in application.yml).

### 3. Run the Consumer Module

Navigate to the consumer module's directory:

```bash
cd ../consumer
```

Then, run the Spring Boot application (ensure Kafka, MySQL, and API_URL environment variables are set):

```bash
mvn spring-boot:run
```

Or run the built jar file:

```bash
java -jar target/consumer-1.0-SNAPSHOT.jar
```

The consumer will start and run on port 8080 (configurable in application.yml).

## Sending a Transaction (Example)

Once the producer is running, you can send a POST request to `http://localhost:8085/test/sendtransaction` with a JSON body like the following:

```json
{
    "cc_num": 1234567890123456,
    "merchant": "Test Merchant",
    "category": "Electronics",
    "amt": 150.75,
    "gender": "M",
    "street": "123 Main St",
    "city": "Anytown",
    "state": "CA",
    "zip": 90210,
    "lat": 34.0522,
    "longitude": -118.2437,
    "city_pop": 3971883,
    "job": "Engineer",
    "merch_lat": 34.0525,
    "merch_long": -118.2440,
    "merch_zipcode": 90210.0
}
```

You can use tools like curl or Postman to send this request.
