package com.example.wallet.config;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@NoArgsConstructor
public class TwilioConfiguration {

    @Value("${account_sid}")
    private String accountSid;

    @Value("${auth_token}")
    private String authToken;

    @Value("${trial_number}")
    private String trialNumber;
}
