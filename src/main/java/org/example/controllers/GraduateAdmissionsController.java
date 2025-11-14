package org.example.controllers;

import org.example.models.*;
import org.example.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.example.models.ApplicationStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/applications")
public class GraduateAdmissionsController {
    @Autowired
    private ApplicationFileRepository applicationFileRepo;

    @Autowired
    private ProfessorRepository professorRepo;

    // Get all applications
    @GetMapping
    public Iterable<ApplicationFile> getAllApplications() {
        return applicationFileRepo.findAll();
    }

    // Get a specific application by ID
    @GetMapping("/{id}")
    public Optional<ApplicationFile> getApplication(@PathVariable Long id) {
        return applicationFileRepo.findById(id);
    }

    // Create a new application
    @PostMapping
    public ApplicationFile saveApplication(@RequestBody ApplicationFile applicationFile){
        // Handle professors - check if they exist or create new ones
        List<Professor> managedProfessors = new ArrayList<>();
        for (Professor prof : applicationFile.getProfessors()) {
            // Check if professor with same email already exists
            Professor existingProf = professorRepo.findByEmail(prof.getEmail());
            if (existingProf != null) {
                managedProfessors.add(existingProf);
            } else {
                // Save new professor first
                Professor savedProf = professorRepo.save(prof);
                managedProfessors.add(savedProf);
            }
        }
        applicationFile.setProfessors(managedProfessors);
        return applicationFileRepo.save(applicationFile);
    }

    // Request professor evaluations
    @PostMapping("/{id}/request-evaluation")
    public ResponseEntity<ApplicationFile> requestEvaluation(@PathVariable Long id){
        Optional<ApplicationFile> applicationFileOptional = applicationFileRepo.findById(id);
        if (applicationFileOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        ApplicationFile applicationFile = applicationFileOptional.get();
        applicationFile.setStatus(ApplicationStatus.PENDING);
        applicationFileRepo.save(applicationFile);

        return ResponseEntity.ok(applicationFile);
    }

    // Reject application
    @PostMapping("/{id}/reject")
    public ResponseEntity<String> rejectApplication(@PathVariable Long id) {
        Optional<ApplicationFile> appOpt = applicationFileRepo.findById(id);
        if (appOpt.isEmpty()) return ResponseEntity.notFound().build();
        ApplicationFile applicationFile = appOpt.get();

        // false if recommendation not set
        if (applicationFile.setFinalRecommendationStatus(applicationFile.getStatus())) {
            applicationFile.setStatus(ApplicationStatus.REJECTED);
            applicationFileRepo.save(applicationFile);
            return ResponseEntity.ok("Application rejected.");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Cannot accept: application has no final recommendation status.");
    }

    // Get applications by status
    @GetMapping("/status/{status}")
    public Iterable<ApplicationFile> getApplicationsByStatus(@PathVariable String status) {
        try {
            ApplicationStatus recStatus = ApplicationStatus.valueOf(status);
            return applicationFileRepo.findByStatus(recStatus);
        } catch (IllegalArgumentException e) {
            return new ArrayList<>();
        }
    }

    // admin accepts an application
    @PostMapping("/{id}/accept")
    public ResponseEntity<String> acceptApplication(@PathVariable Long id){
        Optional<ApplicationFile> appOpt = applicationFileRepo.findById(id);
        if (appOpt.isEmpty()) {return ResponseEntity.notFound().build();};
        ApplicationFile applicationFile = appOpt.get();
        // false if recommendation not set
        if (applicationFile.setFinalRecommendationStatus(applicationFile.getStatus())) {
            applicationFile.setStatus(ApplicationStatus.ACCEPTED);
            applicationFileRepo.save(applicationFile);
            return ResponseEntity.ok("Application accepted.");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Cannot accept: application has no final recommendation status.");
    }

    // Update application status
    @PutMapping("/{id}/status")
    public ApplicationFile updateStatus(@PathVariable Long id, @RequestParam String status) {
        Optional<ApplicationFile> applicationOpt = applicationFileRepo.findById(id);
        if (applicationOpt.isPresent()) {
            ApplicationFile application = applicationOpt.get();
            try {
                ApplicationStatus recStatus = ApplicationStatus.valueOf(status);
                application.setStatus(recStatus);
                return applicationFileRepo.save(application);
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
        return null;
    }

    // Delete an application
    @DeleteMapping("/{id}")
    public void deleteApplication(@PathVariable Long id) {
        applicationFileRepo.deleteById(id);
    }
}
