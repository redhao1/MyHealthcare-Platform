package com.mhp.notification_service.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public interface NotificationService {
    public void sendNotification(String recipientAddress, String recipientName, String message, LocalDateTime time);
}
