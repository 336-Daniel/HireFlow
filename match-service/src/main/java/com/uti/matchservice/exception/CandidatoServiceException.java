package com.uti.matchservice.exception;

public class CandidatoServiceException extends RuntimeException {
    public CandidatoServiceException(String message) { super(message); }
    public CandidatoServiceException(String message, Throwable cause) { super(message, cause); }
}