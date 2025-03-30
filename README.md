
# Auto-startup Docker Compose

Using the pom dependency `spring-boot-docker-compose`, the docker compose command is run automatically on launch if Docker or Rancher are available and properly configured. See [official spring docs](https://docs.spring.io/spring-boot/how-to/docker-compose.html) and [the maven repository](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-docker-compose) for references.

To turn this behavior off, simply comment out or remove the `compose.yml` file. To remove the behavior permanently, remove the pom dependency and rename the `compose.yml` file to `docker-compose.yml`. You can then run docker compose manually.


# Integration Test Configuration

### `CustomerControllerIntegrationTest`



# Repository Test Configuration

The `FasterCustomerRepositoryTest` class is designed to test the `CustomerRepository` with a focus on performance and database cleanliness. It integrates with `AbstractPostgresJupiterTest` and `CleanDatabaseTestExecutionListener` to ensure a consistent and isolated test environment.

### Key Features:

- Shared PostgreSQL Test Container: The test extends `AbstractPostgresJupiterTest`, which initializes a singleton PostgreSQL Testcontainers instance for the entire JVM.
This approach significantly reduces test execution time by reusing the same database instance across all tests in the JVM.

- Database Cleanup Between Tests: The `CleanDatabaseTestExecutionListener` is used to clean and reinitialize the database before each test method. This ensures that each test starts with a clean database state, avoiding interference from previous tests.

- Custom Flyway Migration Strategy: A custom `FlywayMigrationStrategy` is defined in the test class to prevent Flyway migrations from running automatically during test startup.
Instead, migrations are explicitly handled by `CleanDatabaseTestExecutionListener`.

- Real Database Testing: The `@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)` annotation ensures that the tests use the actual PostgreSQL database provided by `Testcontainers`, rather than an in-memory database like H2.


`AbstractPostgresJupiterTest` starts a PostgreSQL container and applies the necessary database connection properties (`spring.datasource.url`, `spring.datasource.username`, etc.) to the Spring context. It also sets `spring.flyway.clean-disabled=false` to allow database cleaning.

Before each test method, `CleanDatabaseTestExecutionListener` cleans the database and reapplies all Flyway migrations. This ensures that each test starts with a consistent and clean database state.

The use of `@Commit` ensures that changes made during a test are committed, allowing the database state to be verified. However, the database is reset before the next test, ensuring no cross-test contamination.

### Benefits:

- Performance: Reusing a single PostgreSQL container reduces test setup time.
- Isolation: Cleaning the database before each test ensures that tests do not interfere with each other.
- Realistic Testing: Using a real PostgreSQL instance provides more accurate results compared to in-memory databases.

This setup ensures reliable, fast, and maintainable repository tests while leveraging `Testcontainers` and Flyway effectively.

# PostgreSQL Docker and `psql` Terminal Commands

Open a terminal session inside the PostgreSQL container...

```bash
docker exec -it db-postgresql bash
```

Working with psql CLI...

```bash
# To connect to the PostgreSQL database using the psql CLI
docker exec -it db-postgresql psql -U admin -d db-postgresql

## Once inside the psql CLI, list all databases:
\l

## To switch to a specific database (e.g., db-postgresql)
\c db-postgresql

## To list all tables in the current database:
\dt

# View table schema
\d documents

# Execute a query (remember the semi-colon)
SELECT id, application_id FROM documents;

# or
INSERT INTO <table_name> (column1, column2) VALUES ('value1', 'value2');
# or
UPDATE <table_name> SET column1 = 'new_value' WHERE column2 = 'condition';
# or 
DELETE FROM <table_name> WHERE column1 = 'condition';
```

To view the logs of the PostgreSQL container
```bash
docker logs db-postgresql
```

Exit psql

```
\q
```

Restart the container

```bash
docker restart db-postgresql

# stop the database container
docker stop db-postgresql
```

To inspect the PostgreSQL container for details like IP address, environment variables, etc.

```bash
docker inspect db-postgresql

# Monitor resource usage
docker stats db-postgresql
```

Importing, exporting, and backups

```bash
# Create a backup
docker exec -t db-postgresql pg_dump -U admin db-postgresql > backup.sql

# Restore from backup
cat backup.sql | docker exec -i db-postgresql psql -U admin -d db-postgresql
```

# References

- [Initial approach sourced from here](https://github.dev/aahlenst/fast-tests-spring-boot-flyway/blob/main/src/test/java/com/example/testing/)