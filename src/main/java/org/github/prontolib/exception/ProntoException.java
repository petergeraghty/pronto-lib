package org.github.prontolib.exception;

@SuppressWarnings("serial")
public class ProntoException extends RuntimeException {

    public ProntoException(String message) {
        super(message);
    }

    public ProntoException(String message, Throwable cause) {
        super(message, cause);
    }

}
