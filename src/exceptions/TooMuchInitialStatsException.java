package exceptions;

public class TooMuchInitialStatsException extends Exception {

    public TooMuchInitialStatsException() {
        super("Il existe plus d'un états initial");
    }
}
