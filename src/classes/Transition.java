package classes;

public class Transition {

    //region Variables
    private final State startingState;
    private final char transition;
    private final State endingState;
    //endregion

    //region Constructor
    public Transition(State startingState, char transition, State endingState) {
        this.startingState = startingState;
        this.transition = transition;
        this.endingState = endingState;
    }
    //endregion

    //region Getter
    public State getStartingState() {
        return startingState;
    }

    public char getTransition() {
        return transition;
    }

    public State getEndingState() {
        return endingState;
    }
    //endregion

    //region Override
    @Override
    public String toString() {
        return this.startingState.getStatsName() + this.transition + this.endingState.getStatsName();
    }
    //endregion
}
