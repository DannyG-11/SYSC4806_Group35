package org.example.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GraduateAdmissionsViewController {

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name = "name", required = false, defaultValue = "World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/apply")
    public String submitApplication(){
        return "apply";
    }

    @GetMapping("/admin")
    public String adminDashboard(){
        return "admin";
    }

    @GetMapping("/professor")
    public String professorDashboard(){
        return "professor";
    }

    @GetMapping("/applications")
    public String viewApplications(){
        return "applications";
    }
}