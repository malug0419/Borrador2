package exceptions;

// Custom Exception 2 - for invalid game settings
public class InvalidGameSettingsException extends Exception {
    public InvalidGameSettingsException(String message) {
        super(message);
    }
}
