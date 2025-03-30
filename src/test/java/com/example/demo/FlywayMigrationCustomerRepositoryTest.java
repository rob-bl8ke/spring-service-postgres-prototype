// package com.example.demo;

// import org.junit.Test;
// import static org.assertj.core.api.Assertions.assertThat;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
// import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
// import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
// import org.springframework.boot.test.context.TestConfiguration;
// import org.springframework.context.annotation.Bean;
// import org.springframework.test.annotation.Commit;
// import org.springframework.test.annotation.DirtiesContext;
// // import org.springframework.test.context.ContextConfiguration;

// /**
//  * Integration test that uses {@link CleanDatabaseTestExecutionListener} to reinitialise the database after each test.
//  */
// // @ContextConfiguration(
// //     initializers = FlywayMigrationCustomerRepositoryTest.class,
// //     // Whether explicit inclusion of @Repository and @TestConfiguration is necessary depends on the test slice
// //     // (like @JdbcTest) being used. Some slices automatically scan for those. Check the Spring Boot documentation on the
// //     // test slice you are using.
// //     classes = {
// //         CustomerRepository.class
// //         ,
// //         FlywayMigrationCustomerRepositoryTest.AdditionalTestConfiguration.class
// //     }
// // )
// @DataJpaTest
// @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
// @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
// @Commit
// public class FlywayMigrationCustomerRepositoryTest extends AbstractPostgresJupiterTest {
    
//     @Autowired
//     CustomerRepository customerRepository;

// 	@TestConfiguration
// 	public static class AdditionalTestConfiguration {
// 		@Bean
// 		public FlywayMigrationStrategy flywayMigrationStrategy() {
// 			return flyway -> {
// 				flyway.clean();
// 				flyway.migrate();
// 			};
// 		}
// 	}

//     @Test
//     public void add_insertsAuthor() {
//     	// var author = new Author(AuthorId.from("82f4870d-f2e5-4a9f-a2b2-b297f66733a0"), "Brian Goetz");
//         var customer = new Customer();
//         customer.setName("Brian Goetz");
//         customer.setEmail("brian@email.com");
    
//     	this.customerRepository.save(customer);
    
//     	assertThat(this.customerRepository.findAll())
//     		.extracting(Customer::getName)
//     		.containsExactly("Brian Goetz");
//     }

// }


// 	// @Test
// 	// void findAll_returnsAllAuthors() {
// 	// 	var authors = this.authorRepository.findAll();

// 	// 	assertThat(authors)
// 	// 		.extracting(Author::name)
// 	// 		.containsExactly("Bert Bates", "Joshua Bloch", "Kathy Sierra", "Trisha Gee");
// 	// }


// 	// @Test
// 	// void delete_ignoresNullAuthorId() {
// 	// 	this.authorRepository.delete(null);

// 	// 	assertThat(this.authorRepository.findAll())
// 	// 		.extracting(Author::name)
// 	// 		.containsExactly("Bert Bates", "Joshua Bloch", "Kathy Sierra", "Trisha Gee");
// 	// }

// 	// @Test
// 	// void delete_removesAuthor() {
// 	// 	this.authorRepository.delete(AuthorId.from("1a0f9c80-1309-4e9d-a291-752354b51c51"));

// 	// 	assertThat(this.authorRepository.findAll())
// 	// 		.extracting(Author::name)
// 	// 		.containsExactly("Bert Bates", "Kathy Sierra", "Trisha Gee");
// 	// }

// 	// @Test
// 	// void delete_hasNoEffectWhenAuthorIsUnknown() {
// 	// 	this.authorRepository.delete(AuthorId.from("06f4cec2-d26d-43ac-9cd5-351825bf2af9"));

// 	// 	assertThat(this.authorRepository.findAll())
// 	// 		.extracting(Author::name)
// 	// 		.containsExactly("Bert Bates", "Joshua Bloch", "Kathy Sierra", "Trisha Gee");
// 	// }


// // }
