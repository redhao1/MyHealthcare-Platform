package com.mhp.notification_service.feignClient;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class Fallback implements PatientClient, DoctorClient {

    @Override
    public ResponseEntity<Map<String, Object>> getPatientById(UUID id) {
        Map<String, Object> fallback = new HashMap<>();
        fallback.put("message", "Patient service is unavailable");
        fallback.put("id", id);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(fallback);
    }

    @Override
    public ResponseEntity<Map<String, Object>> getDoctorById(UUID id) {
        Map<String, Object> fallback = new HashMap<>();
        fallback.put("message", "Doctor service is unavailable");
        fallback.put("id", id);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(fallback);
    }
}
