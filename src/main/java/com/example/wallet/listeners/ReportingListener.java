package com.example.wallet.listeners;


import com.example.wallet.events.CreateEvents;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ReportingListener {

    @EventListener(CreateEvents.class)
    public void reportUserCreation(CreateEvents event) {
        System.out.println("Increment counter as new data was created: " + event);
    }

    @EventListener(CreateEvents.class)
    public void syncUserToExternalSystem(CreateEvents event) {
        System.out.println("informing other systems about data user: " + event);
    }
}