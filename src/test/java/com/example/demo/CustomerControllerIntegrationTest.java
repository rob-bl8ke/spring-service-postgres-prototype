package com.example.demo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.web.servlet.MockMvc;

// Set specifically so as not to pull local database settings from the application.yml file.
@ActiveProfiles("testcontainers")
@SpringBootTest(properties = {
    "spring.flyway.enabled=true",
    "spring.flyway.locations=classpath:db/migration",
    "spring.datasource.url=jdbc:postgresql://localhost:5532/db-postgresql",
    "spring.datasource.driverClassName=org.postgresql.Driver",
    "spring.datasource.username=admin",
    "spring.datasource.password=admin",
    "spring.flyway.clean-disabled=false"
})
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestExecutionListeners(
    value = {CleanDatabaseTestExecutionListener.class},
    mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS // Retains default TestExecutionListeners.
)
public class CustomerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

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