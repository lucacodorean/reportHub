package com.reporthub.service;

public interface IMailService {
    void sendMail(String to, String subject, String body);
}
