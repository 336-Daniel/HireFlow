package com.uti.matchservice.exception;

public class VacanteServiceException extends RuntimeException {
    public VacanteServiceException(String message) { super(message); }
    public VacanteServiceException(String message, Throwable cause) { super(message, cause); }
}