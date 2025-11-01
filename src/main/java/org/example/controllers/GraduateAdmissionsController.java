package org.example.controllers;

import org.example.models.ApplicationFile;
import org.example.models.Professor;
import org.example.repositories.ApplicationFileRepository;
import org.example.repositories.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.example.models.RecommendationStatus;

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

    // Get applications by status
    @GetMapping("/status/{status}")
    public Iterable<ApplicationFile> getApplicationsByStatus(@PathVariable String status) {
        try {
            RecommendationStatus recStatus = RecommendationStatus.valueOf(status);
            return applicationFileRepo.findByStatus(recStatus);
        } catch (IllegalArgumentException e) {
            return new ArrayList<>();
        }
    }

    // Update application status
    @PutMapping("/{id}/status")
    public ApplicationFile updateStatus(@PathVariable Long id, @RequestParam String status) {
        Optional<ApplicationFile> applicationOpt = applicationFileRepo.findById(id);
        if (applicationOpt.isPresent()) {
            ApplicationFile application = applicationOpt.get();
            try {
                RecommendationStatus recStatus = RecommendationStatus.valueOf(status);
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
