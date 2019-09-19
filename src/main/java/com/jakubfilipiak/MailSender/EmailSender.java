package com.jakubfilipiak.MailSender;

import org.springframework.mail.MailException;

import javax.mail.MessagingException;
import java.io.FileNotFoundException;

public interface EmailSender {

    void sendMessage(MessageParams params) throws IllegalArgumentException, MessagingException, FileNotFoundException, MailException;
}
