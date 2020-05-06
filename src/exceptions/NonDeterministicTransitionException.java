package exceptions;

import classes.State;
import classes.Transition;

import java.util.ArrayList;
import java.util.HashSet;

public class NonDeterministicTransitionException extends Exception {

    private final HashSet<Transition> nonDeterministicTransition;

    public NonDeterministicTransitionException(
            State startingState, char transition, State endingState, ArrayList<State> existingEndingStates) {
        this.nonDeterministicTransition = new HashSet<>();
        this.nonDeterministicTransition.add(new Transition(startingState, transition, endingState));
        for (State existingEndingSate : existingEndingStates) {
            if (!existingEndingSate.getStatsName().equals(endingState.getStatsName())) {
                this.nonDeterministicTransition.add(new Transition(startingState, transition, existingEndingSate));
            }
        }
    }

    public HashSet<Transition> getNonDeterministicTransition() {
        return this.nonDeterministicTransition;
    }
}
