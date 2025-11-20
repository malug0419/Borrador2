package exceptions;

// Custom Exception 1 - for invalid coordinates
public class InvalidCoordinateException extends Exception {
    public InvalidCoordinateException(String message) {
        super(message);
    }
}
