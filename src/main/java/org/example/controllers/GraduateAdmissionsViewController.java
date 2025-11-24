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
    public String home(org.springframework.security.core.Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof org.springframework.security.authentication.AnonymousAuthenticationToken)) {
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            boolean isProfessor = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_PROFESSOR"));
            if (isAdmin) {
                return "admin-home";
            }
            if (isProfessor) {
                return "professor-home";
            }
        }
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
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