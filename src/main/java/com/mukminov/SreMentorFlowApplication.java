package com.mukminov;

import org.flywaydb.core.Flyway;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

import java.util.Arrays;

@SpringBootApplication
@ConfigurationPropertiesScan
public class SreMentorFlowApplication {

    public static void main(String[] args) {
        if (Arrays.asList(args).contains("--migrate")) {
            Flyway.configure()
                    .dataSource(
                            System.getenv("POSTGRES_URL"),
                            System.getenv("POSTGRES_USER"),
                            System.getenv("POSTGRES_PASSWORD")
                    )
                    .baselineOnMigrate(true)
                    .load()
                    .migrate();
            return;
        }
        SpringApplication.run(SreMentorFlowApplication.class, args);
    }

}
