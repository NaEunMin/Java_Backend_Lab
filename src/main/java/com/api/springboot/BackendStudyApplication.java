package com.api.springboot;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "com.api.springboot",
        "com.api.spring.scan"
})
public class BackendStudyApplication {
    public static void main(String[] args) {
        SpringApplication.run(BackendStudyApplication.class, args);
    }
}
