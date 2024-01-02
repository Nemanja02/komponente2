package com.komponente.servis3.service;

import com.komponente.servis3.dto.EmailDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
@Transactional
public class EmailService {

    @Autowired
    NotificationService notificationService;

    @Value("${mail.smtp.host}")
    private String smtpHost;

    @Value("${mail.smtp.port}")
    private String smtpPort;

    @Value("${mail.smtp.auth}")
    private String smtpAuth;

    @Value("${mail.smtp.starttls.enable}")
    private String startTls;

    @Value("${mail.from}")
    private String fromEmail;

    @Value("${mail.username}")
    private String username;

    @Value("${mail.password}")
    private String password;

    public void sendEmail(EmailDto emailDto) {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", smtpHost);
        properties.put("mail.smtp.port", smtpPort);
        properties.put("mail.smtp.auth", smtpAuth);
        properties.put("mail.smtp.starttls.enable", startTls);

        System.out.println("Sending email to " + emailDto.getEmail());

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailDto.getEmail()));
            message.setSubject(emailDto.getSubject());
            message.setText(emailDto.getBody());

            Transport.send(message);

            System.out.println("Email sent successfully to " + emailDto.getEmail());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
