package org.example;

import org.example.repositories.ApplicationFileRepository;
import org.example.repositories.ProfessorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.*;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    // Can be used for simulating Professors and application files being created when running the application.
    @Bean
    public CommandLineRunner demo(ApplicationFileRepository applicationFileRepository, ProfessorRepository professorRepository) {
        return (args) -> {

        };
    }
}