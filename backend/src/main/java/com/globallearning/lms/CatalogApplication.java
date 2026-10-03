package com.globallearning.lms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the Global Learning LMS platform.
 *
 * Component scanning starts at {@code com.globallearning.lms}, so a new slice added
 * alongside {@code catalog} is picked up without touching this class.
 */
@SpringBootApplication
public class CatalogApplication {

    public static void main(String[] args) {
        SpringApplication.run(CatalogApplication.class, args);
    }
}
