package org.example.controllers;

import org.example.repositories.ApplicationFileRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.*;

@Controller
public class GraduateAdmissionsViewController {
    private final ApplicationFileRepository applicationFileRepository;

    public GraduateAdmissionsViewController(ApplicationFileRepository applicationFileRepository) {
        this.applicationFileRepository = applicationFileRepository;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/apply")
    public String submitApplication(){
        return "apply";
    }

    @GetMapping("/addprofessors")
    public String addProfessors(){
        return "professors";
    }

    @GetMapping("/evaluations")
    public String evaluatedApplications(){
        return "evaluations";
    }

    @GetMapping("/admin")
    public String adminDashboard() { return "admin"; }
}