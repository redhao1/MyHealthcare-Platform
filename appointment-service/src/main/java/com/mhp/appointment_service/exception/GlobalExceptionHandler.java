package com.mhp.appointment_service.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(
                error -> errors.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.badRequest().body(errors);
    }

    // exception to handle the appointment with the same time for current doctor already exists
    @ExceptionHandler(DateTimeAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleDateTimeAlreadyExistsException(
            DateTimeAlreadyExistsException ex) {

        Map<String, String> errors = new HashMap<>();
        errors.put("message", "The appointment with the same date and time and doctor already exists");
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(AppointmentNotFoundException.class)
    public ResponseEntity<Map<String, String>> handlePatientNotFoundException(
            AppointmentNotFoundException ex) {

        Map<String, String> errors = new HashMap<>();
        errors.put("message", "Appointment not found");
        return ResponseEntity.badRequest().body(errors);

    }
}
