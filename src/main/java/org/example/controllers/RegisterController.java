package org.example.controllers;

import org.example.models.ApplicantPersonalInfo;
import org.example.models.Professor;
import org.example.models.Role;
import org.example.models.User;
import org.example.repositories.ApplicantPersonalInfoRepository;
import org.example.repositories.ProfessorRepository;
import org.example.repositories.RoleRepository;
import org.example.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @Autowired
    private ApplicantPersonalInfoRepository applicantPersonalInfoRepo;

    //Register Users
    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String email,
                               @RequestParam String role,
                               // Applicant fields
                               @RequestParam(required = false) String applicantFirstName,
                               @RequestParam(required = false) String applicantLastName,
                               @RequestParam(required = false) String applicantPhone,
                               @RequestParam(required = false) String applicantAddress,
                               // Professor fields
                               @RequestParam(required = false) String professorFirstName,
                               @RequestParam(required = false) String professorLastName,
                               @RequestParam(required = false) String professorEmail,
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

        // Validation: Prevent admin role registration
        if ("ROLE_ADMIN".equalsIgnoreCase(role) || "ADMIN".equalsIgnoreCase(role)) {
            model.addAttribute("error", "Cannot register as admin through public registration");
            return "register";
        }

        // Create new user
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);

        // Assign role - ensure ROLE_ prefix
        String roleName = role.startsWith("ROLE_") ? role : "ROLE_" + role;
        Optional<Role> userRole = roleRepo.findByName(roleName);

        if (userRole.isEmpty()) {
            model.addAttribute("error", "Invalid role selected");
            return "register";
        }

        user.addRole(userRole.get());

        // Handle role-specific information
        if ("ROLE_APPLICANT".equals(roleName)) {
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

            applicantInfo = applicantPersonalInfoRepo.save(applicantInfo);
            user.setApplicantInfo(applicantInfo);

        } else if ("ROLE_PROFESSOR".equals(roleName)) {
            // Validate professor fields
            if (professorFirstName == null || professorLastName == null || professorEmail == null) {
                model.addAttribute("error", "Please fill in all professor information");
                return "register";
            }

            // Check if professor with this email already exists
            Professor existingProf = professorRepo.findByEmail(professorEmail);
            if (existingProf != null) {
                // Link to existing professor
                user.setProfessor(existingProf);
            } else {
                // Create new Professor
                Professor professor = new Professor();
                professor.setFirstName(professorFirstName);
                professor.setLastName(professorLastName);
                professor.setEmail(professorEmail);

                professor = professorRepo.save(professor);
                user.setProfessor(professor);
            }
        }

        // Save user
        userRepo.save(user);

        return "redirect:/login?registered";
    }
}
