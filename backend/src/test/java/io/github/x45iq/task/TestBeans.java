package io.github.x45iq.task;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.PostgreSQLContainer;

@TestConfiguration
public class TestBeans {
    @Bean
    @ServiceConnection
    PostgreSQLContainer<?> postgreSQLContainer(DynamicPropertyRegistry registry){
        var container = new PostgreSQLContainer<>("postgres:16");
        container.withInitScript("sql/initial/initial_schema.sql");
        registry.add("postgresql.driver",container::getDriverClassName);
        return container;
    }
}
