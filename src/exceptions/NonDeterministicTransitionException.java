package exceptions;

import classes.State;
import classes.Transition;

import java.util.ArrayList;
import java.util.HashSet;

public class NonDeterministicTransitionException extends Exception {

    //region Variables
    private final HashSet<Transition> nonDeterministicTransition;
    //endregion

    //region Constructor
    public NonDeterministicTransitionException(
            State startingState, char symbol, State endingState, ArrayList<State> existingEndingStates) {

        this.nonDeterministicTransition = new HashSet<>();
        this.nonDeterministicTransition.add(new Transition(startingState, symbol, endingState));

        //Foreach ending state already register as ending state
        for (State existingEndingSate : existingEndingStates) {

            //If endingState not already register as ending state
            if (!existingEndingSate.getStatsName().equals(endingState.getStatsName())) {

                //Add the endingState as nonDeterministicTransition with the current symbol
                this.nonDeterministicTransition.add(new Transition(startingState, symbol, existingEndingSate));
            }
        }
    }
    //endregion

    //region Getter
    public HashSet<Transition> getNonDeterministicTransition() {
        return this.nonDeterministicTransition;
    }
    //endregion
}
