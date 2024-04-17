package it.twentyfive.demoqrcode.utils.exceptions;

public class InvalidURLException extends RuntimeException {
    public InvalidURLException() {
    }
    public InvalidURLException(String message) {
        super(message);
    }
}

