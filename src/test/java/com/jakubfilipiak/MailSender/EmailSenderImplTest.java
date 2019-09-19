package com.jakubfilipiak.MailSender;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.MessagingException;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@RunWith(SpringRunner.class)
@TestPropertySource(locations="classpath:configTest.properties")
public class EmailSenderImplTest {

    @Value("${email.account.username}")
    private String accountUsername;
    @Value("${email.account.password}")
    private String accountPassword;

    @Value("${email.recipient.address}")
    private String recipient;

    @Value("${email.attachment1.path}")
    private String attachment1Path;
    @Value("${email.attachment2.path}")
    private String attachment2Path;

    private JavaMailSender mailSender;
    private EmailAddressValidator addressValidator;
    private EmailSender emailSender;
    private MessageParams params;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
    }

    private JavaMailSender createMailSenderWithCorrectAccountCredentials() {
        return createMailSender(accountUsername, accountPassword);
    }

    private JavaMailSender createMailSenderWithWrongAccountCredentials() {
        return createMailSender("WrongUsername", "WrongPassword");
    }

    private JavaMailSender createMailSender(String accountUsername, String accountPassword) {

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername(accountUsername);
        mailSender.setPassword(accountPassword);

        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.debug", "true");

        return mailSender;
    }

    private Map<String, String> prepareOneAttachment() {
        Map<String, String> attachment = new HashMap<>();
        attachment.put(attachment1Path, "file1.jpg");
        return attachment;
    }

    private Map<String, String> prepareTwoAttachments() {
        Map<String, String> attachments = new HashMap<>();
        attachments.put(attachment1Path, "file1.jpg");
        attachments.put(attachment2Path, "file2.jpg");
        return attachments;
    }

    private Map<String, String> prepareAttachmentWithWrongPath() {
        Map<String, String> attachment = new HashMap<>();
        attachment.put("wrongPath", "file1.jpg");
        return attachment;
    }

    private Map<String, String> prepareAttachmentWithNullName() {
        Map<String, String> attachment = new HashMap<>();
        attachment.put(attachment1Path, null);
        return attachment;
    }

    private Map<String, String> prepareAttachmentWithEmptyName() {
        Map<String, String> attachment = new HashMap<>();
        attachment.put(attachment1Path, "");
        return attachment;
    }

    @Test(expected = MailException.class)
    public void shouldThrowExceptionWhenWrongEmailAccountCredentials() throws FileNotFoundException, MessagingException {
        mailSender = createMailSenderWithWrongAccountCredentials();
        addressValidator = new EmailAddressValidator();
        emailSender = new EmailSenderImpl(mailSender, addressValidator);
        params = MessageParams.builder()
                .recipient(recipient)
                .subject("DummySubject")
                .textContent("DummyText")
                .isHtml(false)
                .build();
        emailSender.sendMessage(params);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenNullRecipient() throws FileNotFoundException, MessagingException {
        mailSender = createMailSenderWithCorrectAccountCredentials();
        addressValidator = new EmailAddressValidator();
        emailSender = new EmailSenderImpl(mailSender, addressValidator);
        params = MessageParams.builder()
                .recipient(null)
                .subject("NullRecipient")
                .textContent("NullRecipient")
                .isHtml(false)
                .build();
        emailSender.sendMessage(params);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenWrongRecipient() throws FileNotFoundException, MessagingException {
        mailSender = createMailSenderWithCorrectAccountCredentials();
        addressValidator = new EmailAddressValidator();
        emailSender = new EmailSenderImpl(mailSender, addressValidator);
        params = MessageParams.builder()
                .recipient("wrongRecipient@gmail")
                .subject("WrongRecipient")
                .textContent("WrongRecipient")
                .isHtml(false)
                .build();
        emailSender.sendMessage(params);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenNullSubject() throws FileNotFoundException, MessagingException {
        mailSender = createMailSenderWithCorrectAccountCredentials();
        addressValidator = new EmailAddressValidator();
        emailSender = new EmailSenderImpl(mailSender, addressValidator);
        params = MessageParams.builder()
                .recipient(recipient)
                .subject(null)
                .textContent("NullSubject")
                .isHtml(false)
                .build();
        emailSender.sendMessage(params);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenEmptySubject() throws FileNotFoundException, MessagingException {
        mailSender = createMailSenderWithCorrectAccountCredentials();
        addressValidator = new EmailAddressValidator();
        emailSender = new EmailSenderImpl(mailSender, addressValidator);
        params = MessageParams.builder()
                .recipient(recipient)
                .subject("")
                .textContent("EmptySubject")
                .isHtml(false)
                .build();
        emailSender.sendMessage(params);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenNullTextContent() throws FileNotFoundException, MessagingException {
        mailSender = createMailSenderWithCorrectAccountCredentials();
        addressValidator = new EmailAddressValidator();
        emailSender = new EmailSenderImpl(mailSender, addressValidator);
        params = MessageParams.builder()
                .recipient(recipient)
                .subject("NullTextContent")
                .textContent(null)
                .isHtml(false)
                .build();
        emailSender.sendMessage(params);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenNullIsHtml() throws FileNotFoundException, MessagingException {
        mailSender = createMailSenderWithCorrectAccountCredentials();
        addressValidator = new EmailAddressValidator();
        emailSender = new EmailSenderImpl(mailSender, addressValidator);
        params = MessageParams.builder()
                .recipient(recipient)
                .subject("NullIsHtml")
                .textContent("NullIsHtml")
                .isHtml(null)
                .build();
        emailSender.sendMessage(params);
    }

    @Test(expected = FileNotFoundException.class)
    public void shouldThrowExceptionWhenWrongAttachmentPath() throws FileNotFoundException, MessagingException {
        mailSender = createMailSenderWithCorrectAccountCredentials();
        addressValidator = new EmailAddressValidator();
        emailSender = new EmailSenderImpl(mailSender, addressValidator);
        Map<String, String> attachment = prepareAttachmentWithWrongPath();
        params = MessageParams.builder()
                .recipient(recipient)
                .subject("PlainTextMessageWithAttachmentWithWrongPath")
                .textContent("PlainTextMessageWithAttachmentWithWrongPath")
                .isHtml(false)
                .attachments(attachment)
                .build();
        emailSender.sendMessage(params);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenNullAttachmentName() throws FileNotFoundException, MessagingException {
        mailSender = createMailSenderWithCorrectAccountCredentials();
        addressValidator = new EmailAddressValidator();
        emailSender = new EmailSenderImpl(mailSender, addressValidator);
        Map<String, String> attachment = prepareAttachmentWithNullName();
        params = MessageParams.builder()
                .recipient(recipient)
                .subject("PlainTextMessageWithNullAttachmentName")
                .textContent("PlainTextMessageWithNullAttachmentName")
                .isHtml(false)
                .attachments(attachment)
                .build();
        emailSender.sendMessage(params);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenEmptyAttachmentName() throws FileNotFoundException, MessagingException {
        mailSender = createMailSenderWithCorrectAccountCredentials();
        addressValidator = new EmailAddressValidator();
        emailSender = new EmailSenderImpl(mailSender, addressValidator);
        Map<String, String> attachment = prepareAttachmentWithEmptyName();
        params = MessageParams.builder()
                .recipient(recipient)
                .subject("PlainTextMessageWithEmptyAttachmentName")
                .textContent("PlainTextMessageWithEmptyAttachmentName")
                .isHtml(false)
                .attachments(attachment)
                .build();
        emailSender.sendMessage(params);
    }

    @Test
    public void shouldCorrectSendPlainTextMessageWithoutAttachmentsWhenEverythingOk() throws FileNotFoundException, MessagingException {
        mailSender = createMailSenderWithCorrectAccountCredentials();
        addressValidator = new EmailAddressValidator();
        emailSender = new EmailSenderImpl(mailSender, addressValidator);
        params = MessageParams.builder()
                .recipient(recipient)
                .subject("PlainTextMessage")
                .textContent("PlainTextMessage")
                .isHtml(false)
                .build();
        emailSender.sendMessage(params);
    }

    @Test
    public void shouldCorrectSendHtmlMessageWithoutAttachmentsWhenEverythingOk() throws FileNotFoundException, MessagingException {
        mailSender = createMailSenderWithCorrectAccountCredentials();
        addressValidator = new EmailAddressValidator();
        emailSender = new EmailSenderImpl(mailSender, addressValidator);
        params = MessageParams.builder()
                .recipient(recipient)
                .subject("HtmlMessage")
                .textContent("<html>\n" +
                        "<head>\n" +
                        "\t<title></title>\n" +
                        "\t<link href=\"https://svc.webspellchecker.net/spellcheck31/lf/scayt3/ckscayt/css/wsc.css\" rel=\"stylesheet\" type=\"text/css\" />\n" +
                        "</head>\n" +
                        "<body aria-readonly=\"false\">\n" +
                        "<h1><span style=\"font-family:verdana,geneva,sans-serif\"><em><u><strong><span style=\"color:#6a8759\">Html</span><span style=\"color:#000080\">Mess</span><span style=\"color:#FF0000\">age</span></strong></u></em></span></h1>\n" +
                        "</body>\n" +
                        "</html>\n")
                .isHtml(true)
                .build();
        emailSender.sendMessage(params);
    }

    @Test
    public void shouldCorrectSendPlainTextMessageWithOneAttachmentWhenEverythingOk() throws FileNotFoundException, MessagingException {
        mailSender = createMailSenderWithCorrectAccountCredentials();
        addressValidator = new EmailAddressValidator();
        emailSender = new EmailSenderImpl(mailSender, addressValidator);
        Map<String, String> oneAttachment = prepareOneAttachment();
        params = MessageParams.builder()
                .recipient(recipient)
                .subject("PlainTextMessageWithOneAttachment")
                .textContent("PlainTextMessageWithOneAttachment")
                .isHtml(false)
                .attachments(oneAttachment)
                .build();
        emailSender.sendMessage(params);
    }

    @Test
    public void shouldCorrectSendPlainTextMessageWithMoreThanOneAttachmentWhenEverythingOk() throws FileNotFoundException, MessagingException {
        mailSender = createMailSenderWithCorrectAccountCredentials();
        addressValidator = new EmailAddressValidator();
        emailSender = new EmailSenderImpl(mailSender, addressValidator);
        Map<String, String> twoAttachments = prepareTwoAttachments();
        params = MessageParams.builder()
                .recipient(recipient)
                .subject("PlainTextMessageWithTwoAttachments")
                .textContent("PlainTextMessageWithTwoAttachments")
                .isHtml(false)
                .attachments(twoAttachments)
                .build();
        emailSender.sendMessage(params);
    }
}