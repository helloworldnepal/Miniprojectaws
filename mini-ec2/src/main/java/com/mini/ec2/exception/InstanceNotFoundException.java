package com.mini.ec2.exception;

public class InstanceNotFoundException extends RuntimeException {
    public InstanceNotFoundException(Integer id) {
        super("Instance not found with id: " + id);
    }

    public InstanceNotFoundException(String message) {
        super(message);
    }

    public InstanceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
