package org.example.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendEvaluationRequests(List<String> professorEmails) {
        for (String email : professorEmails) {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(email);
                message.setSubject("Graduate Admission Request");
                message.setText("A new application has been sent for review.");
                message.setFrom("sysc4806confirmationsg35@gmail.com");

                mailSender.send(message);
            } catch (Exception ex) {
                System.err.println("Failed to send email to " + email + ": " + ex.getMessage());
            }
        }
    }
}