package com.example.demo;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;

public class AbstractPostgresIntegrationTest implements ApplicationContextInitializer<ConfigurableApplicationContext> {

	// These can be moved to the test class if you want to have a different database for each test class.
	static final PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:17.4");

	static {
		postgres.start();
	}

	@Override
	public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {
		TestPropertyValues
			.of(
				// Could add all properties here as an alternative to the @TestPropertySource annotation in the test class.

				// "spring.datasource.url=" + postgres.getJdbcUrl(),
				// "spring.datasource.password=" + postgres.getPassword(),
				// "spring.datasource.username=" + postgres.getUsername(),
				// "spring.flyway.clean-disabled=false"
			)
			.applyTo(applicationContext);
	}

}