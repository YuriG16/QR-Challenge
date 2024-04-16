package it.twentyfive.demoqrcode.utils.Exceptions;

public class InvalidURLException extends RuntimeException {
    public InvalidURLException() {
    }
    public InvalidURLException(String message) {
        super(message);
    }
}

