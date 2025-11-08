package org.example;

import org.example.models.ApplicantPersonalInfo;
import org.example.models.ApplicationFile;
import org.example.models.Document;
import org.example.models.Professor;
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
            Professor prof = new Professor();
            prof.setFirstName("John");
            prof.setLastName("Doe");
            professorRepository.save(prof);

            // Setup arguments for ApplicationFile
            String firstName = "John";
            String lastName = "Doe";
            String email = "johndoe@cmail.carleton.ca";
            String phoneNumber = "1234567890";
            String address = "1125 Colonel By Drive, Ottawa, Ontario";
            ApplicantPersonalInfo applicantPersonalInfo = new ApplicantPersonalInfo(
                    firstName, lastName, email, phoneNumber, address
            );

            String fieldOfResearch = "Biomed";
            List<Professor> professors = new ArrayList<>();
            professors.add(prof);
            List<Document> documents = new ArrayList<>();

            applicationFileRepository.save(new ApplicationFile(
                    applicantPersonalInfo, fieldOfResearch, professors, documents));
        };
    }
}