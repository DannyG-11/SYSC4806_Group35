package org.example.security;

import org.example.models.ApplicationFile;
import org.example.models.Professor;
import org.example.models.User;
import org.example.repositories.ApplicationFileRepository;
import org.example.repositories.UserRepository;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RepositoryEventHandler(Professor.class)
public class ProfessorDeleteHandler {

    private final ApplicationFileRepository applicationFileRepository;
    private final UserRepository userRepository;

    public ProfessorDeleteHandler(ApplicationFileRepository applicationFileRepository,
                                  UserRepository userRepository) {
        this.applicationFileRepository = applicationFileRepository;
        this.userRepository = userRepository;
    }

    @HandleBeforeDelete
    @Transactional
    public void handleBeforeDelete(Professor professor) {
        // Detach professor from all application files
        List<ApplicationFile> apps = applicationFileRepository.findByProfessors_Id(professor.getId());
        for (ApplicationFile app : apps) {
            if (app.getProfessors() != null) {
                app.getProfessors().removeIf(p -> p.getId().equals(professor.getId()));
                applicationFileRepository.save(app);
            }
        }

        // Delete any user accounts mapped to this professor (remove credentials)
        Iterable<User> users = userRepository.findByProfessor_Id(professor.getId());
        for (User u : users) {
            userRepository.delete(u);
        }
    }
}


