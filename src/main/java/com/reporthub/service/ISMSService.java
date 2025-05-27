package com.reporthub.service;

public interface ISMSService {
    void sendSms(String to, String body) throws Exception;
}
