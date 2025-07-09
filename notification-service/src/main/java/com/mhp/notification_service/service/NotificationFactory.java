package com.mhp.notification_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class NotificationFactory {
    private final Map<String, NotificationService> notificationServices;

    @Autowired
    public NotificationFactory(List<NotificationService> services) {
        this.notificationServices = services.stream()
                .collect(Collectors.toMap(
                        service -> service.getClass().getAnnotation(Service.class).value(),
                        Function.identity()
                ));
    }

    public NotificationService getService(String channel) {
        return notificationServices.getOrDefault(channel.toUpperCase(), notificationServices.get("EMAIL"));
    }
}
