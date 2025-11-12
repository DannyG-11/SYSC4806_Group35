package org.example.websocketconfig;

import org.example.models.ApplicationFile;
import org.example.repositories.ApplicationFileRepository;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.List;

@Component
public class WebSocketEventListener {

    private final SimpMessageSendingOperations messagingTemplate;
    private final ApplicationFileRepository repo;

    public WebSocketEventListener(ApplicationFileRepository repo, SimpMessageSendingOperations msg) {
        this.repo = repo;
        this.messagingTemplate = msg;
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if (username != null) {

            List<ApplicationFile> files = repo.findByOpenedBy(username);

            for(ApplicationFile file : files) {
                // Clear lock on file
                file.setOpenedBy("");
                file.setOpenedByAdmin(false);
                repo.save(file);

                // Update active users
                messagingTemplate.convertAndSend("/topic/apps/closed",   // everyone updates their UI
                        new ApplicationOpenEvent(file.getId()));
            }
        }
    }
}
