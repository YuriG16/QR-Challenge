package it.twentyfive.demoqrcode.utils.Exceptions;

public class InvalidInputException extends RuntimeException {
    public InvalidInputException() {
    }
    public InvalidInputException(String message) {
        super(message);
    }
    
}