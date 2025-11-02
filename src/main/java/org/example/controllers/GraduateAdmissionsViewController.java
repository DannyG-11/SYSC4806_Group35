package org.example.controllers;

import org.example.repositories.ApplicationFileRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class GraduateAdmissionsViewController {

    private final ApplicationFileRepository applicationFileRepository;

    public GraduateAdmissionsViewController(ApplicationFileRepository applicationFileRepository) {
        this.applicationFileRepository = applicationFileRepository;
    }

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name = "name", required = false, defaultValue = "World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }

    @GetMapping("/apply")
    public String submitApplication(){
        return "apply";
    }

    @GetMapping("/admin")
    public String evaluateNewApplications(Model model) { return "admin"; }
}