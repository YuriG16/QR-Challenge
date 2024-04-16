package it.twentyfive.demoqrcode.utils.Exceptions;

public class InvalidNumberException extends RuntimeException {
    public InvalidNumberException() {
    }
    public InvalidNumberException(String message) {
        super(message);
    }
}
