package org.example.controllers;

import org.example.repositories.ApplicationFileRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class GraduateAdmissionsViewController {

    private final ApplicationFileRepository applicationFileRepository;

    public GraduateAdmissionsViewController(ApplicationFileRepository applicationFileRepository) {
        this.applicationFileRepository = applicationFileRepository;
    }

    @GetMapping("/apply")
    public String submitApplication(){
        return "apply";
    }

    @GetMapping("/evaluations")
    public String evaluatedApplications(){
        return "evaluations";
    }

    @GetMapping("/admin")
    public String adminDashboard(Model model) { return "admin"; }
}