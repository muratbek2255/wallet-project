package com.example.wallet.service;

import com.example.wallet.dto.SmsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class NewService {

    private final SmsSender smsSender;

    @Autowired
    public NewService(@Qualifier("twilio") SmsSenderImpl smsSender) {
        this.smsSender = smsSender;
    }

    public void sendSms(String phoneNumber, String message) {
        smsSender.sendSms(phoneNumber, message);
    }
}
