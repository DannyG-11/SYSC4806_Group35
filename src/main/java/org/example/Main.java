package org.example;

import org.example.models.*;
import org.example.repositories.ApplicationFileRepository;
import org.example.repositories.ProfessorRepository;
import org.example.repositories.RoleRepository;
import org.example.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }


    private void createRoleIfNotExists(RoleRepository roleRepository, String roleName, String description) {
        if (roleRepository.findByName(roleName).isEmpty()) {
            Role role = new Role(roleName, description);
            roleRepository.save(role);
            System.out.println("Created role: " + roleName);
        }
    }
    // Can be used for simulating Professors and application files being created when running the application.
    @Bean
    public CommandLineRunner demo(ApplicationFileRepository applicationFileRepository, ProfessorRepository professorRepository, RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
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

            //Create Roles and Default Admin Login
            createRoleIfNotExists(roleRepository,"ROLE_APPLICANT", "Applicant - Can submit applications");
            createRoleIfNotExists(roleRepository,"ROLE_PROFESSOR", "Professor - Can evaluate applications");
            createRoleIfNotExists(roleRepository,"ROLE_ADMIN", "Administrator - Full access");

            // Create default admin user if it doesn't exist
            if (!userRepository.existsByUsername("admin")) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123")); // Change in production!
                admin.setEmail("admin@university.edu");

                Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseThrow();
                admin.addRole(adminRole);

                userRepository.save(admin);
                System.out.println("Default admin user created: username=admin, password=admin123");
            }
        };
    }
}