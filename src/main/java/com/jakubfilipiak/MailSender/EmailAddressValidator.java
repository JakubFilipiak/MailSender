package com.jakubfilipiak.MailSender;

import com.google.common.base.Preconditions;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class EmailAddressValidator {

    private Pattern emailPattern = Pattern.compile(".+@.+\\..+");

    public boolean isCorrect(String address) throws IllegalArgumentException {
        Preconditions.checkArgument(address != null);
        Matcher emailMatcher = emailPattern.matcher(address);
        return emailMatcher.matches();
    }
}
