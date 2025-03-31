package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import javax.sql.DataSource;

// Set specifically so as not to pull local database settings from the application.yml file.
@ActiveProfiles("local")
@Testcontainers
@DataJpaTest
@TestPropertySource(properties = {
    "spring.flyway.enabled=true",
    "spring.flyway.locations=classpath:db/migration",
    "spring.datasource.url=jdbc:postgresql://localhost:5532/db-postgresql",
    "spring.datasource.driverClassName=org.postgresql.Driver",
    "spring.datasource.username=admin",
    "spring.datasource.password=admin",
    "spring.flyway.clean-disabled=false"
})


@ContextConfiguration(
    classes = {
        DemoApplication.class,
        CustomerRepository.class,
        JpaAuditingConfig.class,
        CustomerRepositoryTest.AdditionalTestConfiguration.class
    }
)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestExecutionListeners(
    value = {CleanDatabaseTestExecutionListener.class},
    mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS
)
@Commit
public class CustomerRepositoryTest {

    @Container
    static final PostgreSQLContainer postgresContainer = new PostgreSQLContainer("postgres:17.4");

    // Specifically for testing the database connection.
    // Get insight into the database connection settings.
    @Autowired
    DataSource dataSource;

    @Autowired
    CustomerRepository customerRepository;

    @TestConfiguration
    public static class AdditionalTestConfiguration {
        @Bean
        public FlywayMigrationStrategy flywayMigrationStrategy() {
            return flyway -> {
                // Do nothing to disable the Flyway migration action on startup without having to disable the Flyway
                // autoconfiguration which is what spring.flyway.enabled=false would do.
            };
        }
    }

    @Test
    void findAll_returnsAllCustomers() {

        var customer = new Customer();
        customer.setName("Brian Goetz");
        customer.setEmail("brian@email.com");
    
        this.customerRepository.save(customer);
    
        assertThat(this.customerRepository.findAll())
            .extracting(Customer::getName)
            .containsExactly("Brian Goetz");
    }

    @Test
    void findAll_returnsAllCustomers2() {

        // Prove that database is clean before test.
        assertThat(this.customerRepository.findAll()).isEmpty();

        var customer = new Customer();
        customer.setName("Brian Goetz");
        customer.setEmail("brian@email.com");
    
        this.customerRepository.save(customer);
    
        assertThat(this.customerRepository.findAll())
            .extracting(Customer::getName)
            .containsExactly("Brian Goetz");
    }
}
