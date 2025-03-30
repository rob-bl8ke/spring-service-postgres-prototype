package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@ActiveProfiles("testcontainers")
@DataJpaTest
@ContextConfiguration(
	initializers = CustomerRepositoryTest.class,
	classes = {
        DemoApplication.class,
		CustomerRepository.class,
        JpaAuditingConfig.class,
		CustomerRepositoryTest.AdditionalTestConfiguration.class
	}
)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CustomerRepositoryTest extends AbstractPostgresJupiterTest {

    @Autowired
    CustomerRepository customerRepository;

	@TestConfiguration
	public static class AdditionalTestConfiguration {
		@Bean
		public FlywayMigrationStrategy flywayMigrationStrategy() {
			return flyway -> {
				flyway.clean();
				flyway.migrate();
			};
		}
	}

    @Test
	void findAll_returnsAllCustomers() {
        // assertThat(2).isEqualTo(2);
        // assertThat(this.customerRepository).isNotNull();

        var customer = new Customer();
        customer.setName("Brian Goetz");
        customer.setEmail("brian@email.com");
    
    	this.customerRepository.save(customer);
    
    	assertThat(this.customerRepository.findAll())
    		.extracting(Customer::getName)
    		.containsExactly("Brian Goetz");
	}
}
