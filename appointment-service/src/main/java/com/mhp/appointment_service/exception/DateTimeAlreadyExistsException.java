package com.mhp.appointment_service.exception;

public class DateTimeAlreadyExistsException extends RuntimeException{

    public DateTimeAlreadyExistsException(String message) {
        super(message);
    }
}
