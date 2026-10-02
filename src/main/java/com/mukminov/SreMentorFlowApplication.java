package com.mukminov;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class SreMentorFlowApplication {

    public static void main(String[] args) {
        SpringApplication.run(SreMentorFlowApplication.class, args);
    }

}
