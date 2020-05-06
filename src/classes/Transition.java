package classes;

public class Transition {

    private final State startingState;
    private final char transition;
    private final State endingState;

    public Transition(State startingState, char transition, State endingState) {
        this.startingState = startingState;
        this.transition = transition;
        this.endingState = endingState;
    }

    public State getStartingState() {
        return startingState;
    }

    public char getTransition() {
        return transition;
    }

    public State getEndingState() {
        return endingState;
    }

    @Override
    public String toString() {
        return this.startingState.getStatsName() + this.transition + this.endingState.getStatsName();
    }
}
