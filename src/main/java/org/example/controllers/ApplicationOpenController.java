package org.example.controllers;

import jakarta.persistence.EntityNotFoundException;
import org.example.models.ApplicationFile;
import org.example.repositories.ApplicationFileRepository;
import org.example.websocketconfig.ApplicationOpenEvent;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ApplicationOpenController {
    private final ApplicationFileRepository repo;
    private final SimpMessagingTemplate msg;

    public ApplicationOpenController(ApplicationFileRepository repo, SimpMessagingTemplate msg) {
        this.repo = repo; this.msg = msg;
    }

    @MessageMapping("/open/{id}")
    public void open(@DestinationVariable Long id) {

        ApplicationFile file = repo.findById(id).orElseThrow(EntityNotFoundException::new);
        file.setOpenedByAdmin(true);
        repo.save(file);

        msg.convertAndSend("/topic/apps/opened",   // everyone updates their UI
                new ApplicationOpenEvent(id));
    }

    @MessageMapping("/close/{id}")
    public void close(@DestinationVariable Long id) {

        ApplicationFile file = repo.findById(id).orElseThrow(EntityNotFoundException::new);
        file.setOpenedByAdmin(false);
        repo.save(file);

        msg.convertAndSend("/topic/apps/closed",   // everyone updates their UI
                new ApplicationOpenEvent(id));
    }
}
