import java.util.ArrayList;
import java.util.HashMap;

public class State {

    //region Variables
    private int statsName;
    private boolean isEntry;
    private boolean isExit;
    private HashMap<Character, ArrayList<State>> exitingEdges;
    //endregion

    //region Constructor
    public State(int statsName) {
        this.statsName = statsName;
        this.exitingEdges = new HashMap<>();
    }
    //endregion

    protected void addExitingEdge(State endingStat, char transition) {
        if(this.exitingEdges.containsKey(transition)){
            this.exitingEdges.get(transition).add(endingStat);
        } else {
            ArrayList<State> newEndingStatesList = new ArrayList<>();
            newEndingStatesList.add(endingStat);
            this.exitingEdges.put(transition, newEndingStatesList);
        }
    }

    public HashMap<Character, ArrayList<State>> getExitingEdges() {
        return exitingEdges;
    }

    //region Setter
    public void setEntry(boolean entry) {
        this.isEntry = entry;
    }

    public void setExit(boolean exit) {
        this.isExit = exit;
    }
    //endregion

    //region Getter
    public int getStatsName() {
        return statsName;
    }

    public boolean isEntry() {
        return isEntry;
    }

    public boolean isExit() {
        return isExit;
    }

    //endregion
}
