package com.example.wallet.events;

import org.springframework.context.ApplicationEvent;


public class CreateEvents extends ApplicationEvent {
    public String payload;

    public CreateEvents(Object source, String payload) {
        super(source);
        this.payload = payload;
    }
}
