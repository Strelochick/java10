package com.mpt.dentalclinic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.mpt.dentalclinic")
public class JournalApplication {
    public static void main(String[] args) {
        SpringApplication.run(JournalApplication.class, args);
    }
}
