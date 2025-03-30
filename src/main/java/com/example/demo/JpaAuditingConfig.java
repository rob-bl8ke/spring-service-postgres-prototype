package com.example.demo;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
    // This class enables JPA Auditing, automatically populating the createdDate and updatedDate fields of an entity.
    // Why not apply this to the main application class?
    //  1. Keep the main application class as clean as possible.
    //  2. Importantly: Create slice tests without pulling in unnecessary context.

    // See:
    // Specific issue: https://github.com/spring-projects/spring-boot/issues/6016#issuecomment-370481134
    // https://docs.spring.io/spring-boot/reference/testing/spring-boot-applications.html#testing.spring-boot-applications.user-configuration-and-slicing
}