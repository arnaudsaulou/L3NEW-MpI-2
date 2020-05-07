package exceptions;

public class TooMuchInitialStatsException extends Exception {

    //region Constructor
    public TooMuchInitialStatsException() {
        super("Il existe plus d'un états initial");
    }
    //endregion
}
