package classes;

import exceptions.NonDeterministicTransitionException;

import java.util.*;

public class State {

    //region Variables
    protected String statsName;
    protected boolean isEntry;
    protected boolean isExit;
    protected boolean isNeverVisited;
    protected boolean isComposingState;
    protected final HashMap<Character, ArrayList<State>> exitingEdges;
    //endregion

    //region Constructor
    public State(String statsName) {
        this.isNeverVisited = true;
        this.statsName = statsName;
        this.exitingEdges = new HashMap<>();
    }
    //endregion

    //region Utils
    protected void addExitingEdge(State endingStat, char symbol) throws NonDeterministicTransitionException {
        //If a transaction already exist with this symbol
        if (this.exitingEdges.containsKey(symbol)) {

            //If the endingStat is not already in the exitingEdges list
            if (!this.exitingEdges.get(symbol).contains(endingStat)) {
                this.exitingEdges.get(symbol).add(endingStat);
                throw new NonDeterministicTransitionException(this, symbol, endingStat, this.exitingEdges.get(symbol));
            }
        } else {
            ArrayList<State> newEndingStatesList = new ArrayList<>();
            newEndingStatesList.add(endingStat);
            this.exitingEdges.put(symbol, newEndingStatesList);
        }
    }

    protected ArrayList<State> getSuccessorWithGivenSymbol(char symbol) {
        if (this instanceof ComposedState) {
            ComposedState composedState = (ComposedState) this;

            Set<State> successors = new HashSet<>();

            ArrayList<State> localSuccessor;
            for (State composingState : composedState.getComposingStates().values()) {
                localSuccessor = composingState.getSuccessorWithGivenSymbol(symbol);
                if(localSuccessor != null) {
                    successors.addAll(localSuccessor);
                }
            }
            return new ArrayList<>(successors);

        } else {
            return this.getExitingEdges().getOrDefault(symbol, null);
        }
    }

    public void reverseExit() {
        this.isExit = !this.isExit;
    }

    //endregion

    //region Setter
    public void setEntry(boolean entry) {
        this.isEntry = entry;
    }

    public void setExit(boolean exit) {
        this.isExit = exit;
    }

    public void setNeverVisited(boolean neverVisited) {
        isNeverVisited = neverVisited;
    }

    public void setComposingState(boolean composingState) {
        isComposingState = composingState;
    }

    //endregion

    //region Getter
    public String getStatsName() {
        return this.statsName;
    }

    public HashMap<Character, ArrayList<State>> getExitingEdges() {
        return this.exitingEdges;
    }

    public boolean isEntry() {
        return this.isEntry;
    }

    public boolean isExit() {
        return this.isExit;
    }

    public boolean isNeverVisited() {
        return this.isNeverVisited;
    }

    public boolean isComposingState() {
        return this.isComposingState;
    }

    //endregion

    //region Override

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof State)) return false;
        State state = (State) o;
        return getStatsName().equals(state.getStatsName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStatsName());
    }

    @Override
    public String toString() {
        return this.statsName;
    }

    //endregion

}
