
# Auto-startup Docker Compose

Using the pom dependency `spring-boot-docker-compose`, the docker compose command is run automatically on launch if Docker or Rancher are available and properly configured. See [official spring docs](https://docs.spring.io/spring-boot/how-to/docker-compose.html) and [the maven repository](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-docker-compose) for references.

To turn this behavior off, simply comment out or remove the `compose.yml` file. To remove the behavior permanently, remove the pom dependency and rename the `compose.yml` file to `docker-compose.yml`. You can then run docker compose manually.

# PostgreSQL

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