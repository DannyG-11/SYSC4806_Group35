package org.example.controllers;

import jakarta.persistence.EntityNotFoundException;
import org.example.models.ApplicationFile;
import org.example.repositories.ApplicationFileRepository;
import org.example.websocketconfig.ApplicationOpenEvent;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ApplicationOpenController {
    private final ApplicationFileRepository repo;
    private final SimpMessagingTemplate msg;

    public ApplicationOpenController(ApplicationFileRepository repo, SimpMessagingTemplate msg) {
        this.repo = repo; this.msg = msg;
    }


    private void setUsername(SimpMessageHeaderAccessor headerAccessor, String username) {
        // Add username in web socket session
        String headerUserName = (String) headerAccessor.getSessionAttributes().get("username");
        if (headerUserName == null) {
            headerAccessor.getSessionAttributes().put("username", username);
        }
    }

    @MessageMapping("/open/{id}")
    public void open(@DestinationVariable Long id, @Payload String openedBy, SimpMessageHeaderAccessor headerAccessor) {
        setUsername(headerAccessor, openedBy);

        ApplicationFile file = repo.findById(id).orElseThrow(EntityNotFoundException::new);
        file.setOpenedByAdmin(true);
        file.setOpenedBy(openedBy);
        repo.save(file);

        msg.convertAndSend("/topic/apps/opened",   // everyone updates their UI
                new ApplicationOpenEvent(id));
    }

    @MessageMapping("/close/{id}")
    public void close(@DestinationVariable Long id, @Payload String openedBy) {

        ApplicationFile file = repo.findById(id).orElseThrow(EntityNotFoundException::new);
        file.setOpenedByAdmin(false);
        file.setOpenedBy("");
        repo.save(file);

        msg.convertAndSend("/topic/apps/closed",   // everyone updates their UI
                new ApplicationOpenEvent(id));
    }
}
