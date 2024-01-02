package com.komponente.servis3.listener;

import com.komponente.servis3.dto.EmailDto;
import com.komponente.servis3.listener.helper.MessageHelper;
import com.komponente.servis3.service.NotificationService;
import com.komponente.servis3.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;

@Component
public class NotificationListener {

    @Autowired
    NotificationService notificationService;

    public NotificationListener(MessageHelper messageHelper, EmailService emailService) {
        this.messageHelper = messageHelper;
        this.emailService = emailService;
    }

    private MessageHelper messageHelper;
    private EmailService emailService;

    @JmsListener(destination = "${destination.email}", concurrency = "5-10")
    public void onActivationMessage(Message message) throws JMSException {
        System.out.println("Message received: " + message);
        EmailDto email = messageHelper.getMessage(message, EmailDto.class);
        System.out.println("Email: " + email.getType());
        notificationService.createNotification(email);
        emailService.sendEmail(email);
    }
}
