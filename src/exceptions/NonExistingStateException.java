package exceptions;

public class NonExistingStateException extends Exception {

    public NonExistingStateException(String message) {
        super(message);
    }
}
