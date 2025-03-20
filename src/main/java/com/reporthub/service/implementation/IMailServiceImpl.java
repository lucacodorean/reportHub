package com.reporthub.service.implementation;

import com.reporthub.config.AppConfig;
import com.reporthub.service.IMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class IMailServiceImpl implements IMailService {

    @Autowired private JavaMailSender mailSender;

    @Override
    @Async
    public void sendMail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setFrom(AppConfig.getEmailAddress());
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }
}
