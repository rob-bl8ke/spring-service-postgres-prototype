package com.example.demo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

// See: https://github.com/springframeworkguru/spring-6-rest-mvc/tree/78.3-using-dynamic-properties/src/test/java/guru/springframework/spring6restmvc/controller
// ... for more examples of how you might go about writing controller integration tests.

@ActiveProfiles("local")
@Testcontainers
@AutoConfigureMockMvc

// Overrides settings in the application.yml and application-local.yml files.
@SpringBootTest(properties = {
    "spring.flyway.clean-disabled=false"
})

// Specifies that the test should use a real database instead of an in-memory one.
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

// Cleans the database before each test method is run.
@TestExecutionListeners(
    value = {CleanDatabaseTestExecutionListener.class},
    mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS // Retains default TestExecutionListeners.
)
public class CustomerControllerIntegrationTest {
    @Container
    static final PostgreSQLContainer postgresContainer = new PostgreSQLContainer("postgres:17.4");
    
    // Specifically for testing the database connection.
    // Get insight into the database connection settings.
    @Autowired
    DataSource dataSource;

    // Overrides the default properties in the application.yml and application-local.yml files.
    // The PostgreSQLContainer class provides a JDBC URL, username and password for the database connection.
    @DynamicPropertySource
    static void mySqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
    }

    @Autowired
    private MockMvc mockMvc;

    // Do nothing to disable the Flyway migration action.
    @TestConfiguration
    public static class AdditionalTestConfiguration {
        @Bean
        public FlywayMigrationStrategy flywayMigrationStrategy() {
            return flyway -> {
				// Do nothing...
            };
        }
    }

    @Test
    void getAllCustomers_returnsEmptyListInitially() throws Exception {
        mockMvc.perform(get("/customers/by-email")
                .param("email", "john.doe@email.com"))
            .andExpect(status().isNotFound());
    }

    @Test
    void addCustomer_andRetrieveIt() throws Exception {
        // Add a customer
        mockMvc.perform(post("/customers")
                .contentType("application/json")
                .content("""
                    {
                        "name": "Brian Goetz",
                        "email": "brian@email.com"
                    }
                """))
            .andExpect(status().isCreated());

        // Retrieve the customer
        mockMvc.perform(get("/customers/by-email")
                .param("email", "brian@email.com"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Brian Goetz"))
            .andExpect(jsonPath("$.email").value("brian@email.com"));
    }
}