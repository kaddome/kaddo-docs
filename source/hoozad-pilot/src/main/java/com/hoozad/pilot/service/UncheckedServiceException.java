package com.hoozad.pilot.service;


public abstract class UncheckedServiceException extends RuntimeException {
    public UncheckedServiceException(String message) {
        super(message);
    }

    public UncheckedServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public UncheckedServiceException(Throwable cause) {
        super(cause);
    }
}
