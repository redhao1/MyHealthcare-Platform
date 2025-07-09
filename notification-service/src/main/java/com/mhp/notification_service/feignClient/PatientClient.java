package com.mhp.notification_service.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;
import java.util.UUID;

@FeignClient(name = "patient-service", fallback = Fallback.class)
public interface PatientClient {

    @GetMapping("/patient/{id}")
    ResponseEntity<Map<String, Object>> getPatientById(@PathVariable UUID id);
}