package com.jakubfilipiak.MailSender;

import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.io.FileNotFoundException;

@RestController
public class EmailController {

    private EmailSender emailSender;

    public EmailController(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    @PostMapping("/messages")
    public ResponseEntity sendMessage(@RequestBody MessageParams messageParams) {
        try {
            emailSender.sendMessage(messageParams);
            return ResponseEntity.status(200).body("OK");
        } catch (MailException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(412).body(e.getMessage());
        } catch (FileNotFoundException e) {
            return ResponseEntity.status(410).body(e.getMessage());
        } catch (MessagingException e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
