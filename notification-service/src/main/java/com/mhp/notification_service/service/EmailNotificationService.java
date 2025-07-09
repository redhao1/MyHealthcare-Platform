package com.mhp.notification_service.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service("EMAIL")
public class EmailNotificationService implements NotificationService{

    @Override
    public void sendNotification(String recipientAddress, String recipientName, String message, LocalDateTime time) {
        log.info("📧 Email sent to address: {} with user: {}, message: {} at {}", recipientAddress, recipientName, message, time);
    }
}
