package org.example.controllers;

import org.example.models.ApplicationFile;
import org.example.models.Professor;
import org.example.repositories.ApplicationFileRepository;
import org.example.repositories.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        applicationFile.setAvailableToProfs(true);
        applicationFileRepo.save(applicationFile);

        return ResponseEntity.ok(applicationFile);
    }

    // Reject application
    @PostMapping("/{id}/reject")
    public ResponseEntity<String> rejectApplication(@PathVariable Long id) {
        Optional<ApplicationFile> appOpt = applicationFileRepo.findById(id);
        if (appOpt.isEmpty()) return ResponseEntity.notFound().build();

        applicationFileRepo.deleteById(id);
        return ResponseEntity.ok("Application rejected.");
    }
}
