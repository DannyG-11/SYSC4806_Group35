package org.example.controllers;

import org.example.models.*;
import org.example.repositories.ProfessorRepository;
import org.example.repositories.RoleRepository;
import org.example.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class RegisterController {

    @Autowired
    private ProfessorRepository professorRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //Register Users
    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String email,
                               // Applicant fields
                               @RequestParam(required = false) String applicantFirstName,
                               @RequestParam(required = false) String applicantLastName,
                               @RequestParam(required = false) String applicantPhone,
                               @RequestParam(required = false) String applicantAddress,
                               Model model) {

        // Validation: Check if username or email already exists
        if (userRepo.existsByUsername(username)) {
            model.addAttribute("error", "Username already exists");
            return "register";
        }

        if (userRepo.existsByEmail(email)) {
            model.addAttribute("error", "Email already exists");
            return "register";
        }

        // Create new user
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);

        // Assign APPLICANT role
        Optional<Role> applicantRole = roleRepo.findByName("ROLE_APPLICANT");

        if (applicantRole.isEmpty()) {
            model.addAttribute("error", "System error: Applicant role not found. Please contact administrator.");
            return "register";
        }

        user.addRole(applicantRole.get());

        // Validate applicant fields
        if (applicantFirstName == null || applicantLastName == null ||
                applicantPhone == null || applicantAddress == null) {
            model.addAttribute("error", "Please fill in all applicant information");
            return "register";
        }

        // Create ApplicantPersonalInfo
        ApplicantPersonalInfo applicantInfo = new ApplicantPersonalInfo();
        applicantInfo.setFirstName(applicantFirstName);
        applicantInfo.setLastName(applicantLastName);
        applicantInfo.setEmail(email);
        applicantInfo.setPhoneNumber(applicantPhone);
        applicantInfo.setAddress(applicantAddress);

        user.setApplicantInfo(applicantInfo);

        // Save user
        userRepo.save(user);

        return "redirect:/login?registered";
    }

    //Register Professor
    @PostMapping("/registerprofessor")
    public String registerProfessor(@RequestParam String firstName,
                                    @RequestParam String lastName,
                                    @RequestParam String email,
                                    @RequestParam String username,
                                    @RequestParam String password,
                                    Model model) {
        // Validation: Check if username or email already exists
        if (userRepo.existsByUsername(username)) {
            model.addAttribute("error", "Username already exists");
            return "professors";
        }

        if (userRepo.existsByEmail(email)) {
            model.addAttribute("error", "Email already exists");
            return "professors";
        }

        // Create new user
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);

        // Assign APPLICANT role
        Optional<Role> applicantRole = roleRepo.findByName("ROLE_PROFESSOR");

        if (applicantRole.isEmpty()) {
            model.addAttribute("error", "System error: Applicant role not found. Please contact administrator.");
            return "professors";
        }

        user.addRole(applicantRole.get());

        /// Validate professor fields
        if (firstName == null || lastName == null || email == null) {
            model.addAttribute("error", "Please fill in all professor information");
            return "professors";
        }

        // Check if professor with this email already exists
        Professor existingProf = professorRepo.findByEmail(email);
        if (existingProf != null) {
            // Link to existing professor
            user.setProfessor(existingProf);
        } else {
            // Create new Professor
            Professor professor = new Professor();
            professor.setFirstName(firstName);
            professor.setLastName(lastName);
            professor.setEmail(email);

            professor = professorRepo.save(professor);
            user.setProfessor(professor);
        }

        // Save user
        userRepo.save(user);

        return "professors";
    }

    //Register Professor
    @DeleteMapping("/registerprofessor")
    public String deleteProfessor(@RequestBody String email) {
        Optional<User> user = userRepo.findByEmail(email);

        if (user.isEmpty()) {
            return "professors";
        }

        Long id = user.get().getId();

        userRepo.deleteById(id);

        return "professors";
    }
}
