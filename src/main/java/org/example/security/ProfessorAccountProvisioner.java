package org.example.security;

import org.example.models.Professor;
import org.example.models.Role;
import org.example.models.User;
import org.example.repositories.RoleRepository;
import org.example.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;

@Component
@RepositoryEventHandler(Professor.class)
public class ProfessorAccountProvisioner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public ProfessorAccountProvisioner(UserRepository userRepository,
                                       RoleRepository roleRepository,
                                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @HandleAfterCreate
    @Transactional
    public void handleProfessorCreated(Professor professor) {
        String first = safe(professor.getFirstName());
        String last = safe(professor.getLastName());
        if (first.isEmpty() || last.isEmpty()) {
            return; // cannot derive username
        }
        String baseUsername = (first + "." + last).toLowerCase().replaceAll("\\s+", "");
        String username = baseUsername;

        if (userRepository.existsByUsername(username)) {
            // If occupied, fall back to first.last1, first.last2, ...
            int suffix = 1;
            while (userRepository.existsByUsername(username)) {
                username = baseUsername + suffix;
                suffix++;
            }
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode("password123"));
        // Prefer professor email if present
        if (StringUtils.hasText(professor.getEmail())) {
            user.setEmail(professor.getEmail());
        } else {
            user.setEmail(username + "@example.com");
        }
        // Link to professor
        user.setProfessor(professor);

        Role profRole = roleRepository.findByName("ROLE_PROFESSOR")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_PROFESSOR", "Professor - Can evaluate applications")));
        user.addRole(profRole);

        userRepository.save(user);
    }

    private String safe(String s) {
        return s == null ? "" : s.trim();
    }
}


