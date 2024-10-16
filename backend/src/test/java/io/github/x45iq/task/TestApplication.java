package io.github.x45iq.task;

import io.github.x45iq.Application;
import org.springframework.boot.SpringApplication;

public class TestApplication {
    public static void main(String[] args) {
        SpringApplication.from(Application::main)
                .with(TestBeans.class)
                .run(args);
    }
}
