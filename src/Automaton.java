import java.util.HashMap;

public class Automaton {

    //region Variables
    private int numberOfStats;
    private HashMap<Integer, Vectrice> statsList;
    //endregion

    //region Constructor
    public Automaton(int numberOfStats) {
        this.statsList = new HashMap<>();
        this.numberOfStats = numberOfStats;

        for (int i = 0; i < numberOfStats; i++) {
            this.statsList.put(i, new Vectrice(i));
        }
    }
    //endregion

    //region Utils
    public void addInitialStat(int stat) {
        this.statsList.get(stat).setEntry(true);
    }

    public void addTerminalStat(int stat) {
        this.statsList.get(stat).setExit(true);
    }

    public void addTransition(int startingStatInt, char symbol, int endingStatInt) {
        Vectrice startingStat = this.statsList.get(startingStatInt);
        Vectrice endingStat = this.statsList.get(endingStatInt);
    }
    //endregion
}
