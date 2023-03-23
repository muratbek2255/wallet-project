package com.example.wallet.service;


public interface SmsSender {

    public void sendSms(String phoneNumber, String message);

}
