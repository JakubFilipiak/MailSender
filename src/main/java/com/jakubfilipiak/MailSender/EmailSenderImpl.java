package com.jakubfilipiak.MailSender;

import com.google.common.base.Preconditions;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

@Service
public class EmailSenderImpl implements EmailSender {

    private JavaMailSender mailSender;
    private EmailAddressValidator addressValidator;

    public EmailSenderImpl(JavaMailSender mailSender, EmailAddressValidator addressValidator) {
        this.mailSender = mailSender;
        this.addressValidator = addressValidator;
    }

    @Override
    public void sendMessage(MessageParams params) throws IllegalArgumentException, MessagingException, FileNotFoundException, MailException {
        mailSender.send(composeMessage(params));
    }

    private MimeMessage composeMessage(MessageParams params) throws IllegalArgumentException, MessagingException, FileNotFoundException {
        MimeMessage message = mailSender.createMimeMessage();
        if (isEachRequiredParamPresent(params)) {
            boolean isMessageWithAttachments = isMessageWithAttachments(params);
            MimeMessageHelper helper = new MimeMessageHelper(message, isMessageWithAttachments);
            helper.setTo(params.getRecipient());
            helper.setSubject(params.getSubject());
            helper.setText(params.getTextContent(), params.getIsHtml());
            if (isMessageWithAttachments)
                addAttachments(helper, params.getAttachments());
        }
        return message;
    }

    private boolean isEachRequiredParamPresent(MessageParams params) throws IllegalArgumentException {
        Preconditions.checkArgument(params.getRecipient() != null && addressValidator.isCorrect(params.getRecipient()),  "Wrong recipient!");
        Preconditions.checkArgument(params.getSubject() != null && !params.getSubject().isEmpty(), "Wrong subject!");
        Preconditions.checkArgument(params.getTextContent() != null,  "Wrong textContent!");
        Preconditions.checkArgument(params.getIsHtml() != null,  "Wrong isHtml!");
        return true;
    }

    private boolean isMessageWithAttachments(MessageParams params) {
        return params.getAttachments() != null && !params.getAttachments().isEmpty();
    }

    private void addAttachments(MimeMessageHelper helper, Map<String, String> attachments) throws MessagingException, FileNotFoundException, IllegalArgumentException {
        for (String path : attachments.keySet()) {
            File file = new File(path);
            String name = attachments.get(path);
            if (file.exists()) {
                if (name != null && !name.isEmpty())
                    helper.addAttachment(attachments.get(path), new FileSystemResource(path));
                else throw new IllegalArgumentException();
            } else throw new FileNotFoundException();
        }
    }
}
