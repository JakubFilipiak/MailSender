package com.jakubfilipiak.MailSender;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class MessageParams {

    private final String recipient;
    private final String subject;
    private final String textContent;
    private final Boolean isHtml;
    private final Map<String, String> attachments; // <filePath, fileName>
}
