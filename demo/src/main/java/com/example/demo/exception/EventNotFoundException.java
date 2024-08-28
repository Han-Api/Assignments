package com.example.demo.exception;

public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException() {
        super();
    }

    public EventNotFoundException(String message) {
        super(message);
    }
}
