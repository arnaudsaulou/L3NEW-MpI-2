import exceptions.NonDeterministicTransition;

import java.util.ArrayList;
import java.util.HashMap;

public class State {

    //region Variables
    private final String statsName;
    private boolean isEntry;
    private boolean isExit;
    private final HashMap<Character, ArrayList<State>> exitingEdges;
    //endregion

    //region Constructor
    public State(String statsName) {
        this.statsName = statsName;
        this.exitingEdges = new HashMap<>();
    }
    //endregion

    //region Utils
    protected void addExitingEdge(State endingStat, char transition) throws NonDeterministicTransition {
        if (this.exitingEdges.containsKey(transition)) {
            this.exitingEdges.get(transition).add(endingStat);
            throw new NonDeterministicTransition(this.statsName + transition + endingStat.getStatsName());
        } else {
            ArrayList<State> newEndingStatesList = new ArrayList<>();
            newEndingStatesList.add(endingStat);
            this.exitingEdges.put(transition, newEndingStatesList);
        }
    }

    protected ArrayList<State> getSuccessorWithGivenTransition(char transition) {
        return this.getExitingEdges().get(transition);
    }

    //endregion

    //region Setter
    public void setEntry(boolean entry) {
        this.isEntry = entry;
    }

    public void setExit(boolean exit) {
        this.isExit = exit;
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
    //endregion
}
