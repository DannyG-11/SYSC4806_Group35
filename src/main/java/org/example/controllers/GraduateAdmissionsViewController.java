package org.example.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class GraduateAdmissionsViewController {

    public GraduateAdmissionsViewController() {
    }

    @GetMapping("/apply")
    public String submitApplication(){
        return "apply";
    }
}