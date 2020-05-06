package classes;

import exceptions.NonDeterministicTransitionException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class State {

    //region Variables
    protected String statsName;
    protected boolean isEntry;
    protected boolean isExit;
    protected boolean isAlreadyVisited;
    protected final HashMap<Character, ArrayList<State>> exitingEdges;
    //endregion

    //region Constructor
    public State(String statsName) {
        this.statsName = statsName;
        this.exitingEdges = new HashMap<>();
    }
    //endregion

    //region Utils
    protected void addExitingEdge(State endingStat, char transition) throws NonDeterministicTransitionException {
        if (this.exitingEdges.containsKey(transition)) {
            if (!this.exitingEdges.get(transition).contains(endingStat)) {
                this.exitingEdges.get(transition).add(endingStat);
                throw new NonDeterministicTransitionException(this, transition, endingStat, this.exitingEdges.get(transition));
            }
        } else {
            ArrayList<State> newEndingStatesList = new ArrayList<>();
            newEndingStatesList.add(endingStat);
            this.exitingEdges.put(transition, newEndingStatesList);
        }
    }

    protected void removeExitingEdge(char transition) {
        this.exitingEdges.remove(transition);
    }

    protected ArrayList<State> getSuccessorWithGivenSymbol(char symbol) {
        return this.getExitingEdges().getOrDefault(symbol, null);
    }

    protected State clone() {
        State copyState = new State(this.statsName);
        copyState.isExit = this.isExit;
        copyState.isEntry = this.isEntry;
        return copyState;
    }

    //endregion

    //region Setter
    public void setEntry(boolean entry) {
        this.isEntry = entry;
    }

    public void setExit(boolean exit) {
        this.isExit = exit;
    }

    public void setAlreadyVisited() {
        this.isAlreadyVisited = true;
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

    public boolean isAlreadyVisited() {
        return this.isAlreadyVisited;
    }
    //endregion


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


}
