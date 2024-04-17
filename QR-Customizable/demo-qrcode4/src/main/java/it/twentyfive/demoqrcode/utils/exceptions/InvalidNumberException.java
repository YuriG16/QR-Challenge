package it.twentyfive.demoqrcode.utils.exceptions;

public class InvalidNumberException extends RuntimeException {
    public InvalidNumberException() {
    }
    public InvalidNumberException(String message) {
        super(message);
    }
}
