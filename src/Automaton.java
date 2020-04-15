import java.util.ArrayList;
import java.util.HashMap;

public class Automaton {

    //region Variables
    private int numberOfStats;
    private ArrayList<Character> alphabet;
    private HashMap<Integer, State> statsList;
    //endregion

    //region Constructor
    public Automaton(int alphabetSize, int numberOfStats) {
        this.alphabet = new ArrayList<>();
        this.statsList = new HashMap<>();
        this.numberOfStats = numberOfStats;

        for (int i = 0; i < alphabetSize; i++) {
            this.alphabet.add((char) ('a' + i));
        }

        for (int i = 0; i < numberOfStats; i++) {
            this.statsList.put(i, new State(i));
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

    public void addTransition(int startingStateInt, char transition, int endingStateInt) {
        State startingState = this.statsList.get(startingStateInt);
        State endingState = this.statsList.get(endingStateInt);
        startingState.addExitingEdge(endingState, transition);
    }
    //endregion


    public int getNumberOfStats() {
        return numberOfStats;
    }

    public void setNumberOfStats(int numberOfStats) {
        this.numberOfStats = numberOfStats;
    }

    public HashMap<Integer, State> getStatsList() {
        return statsList;
    }

    public void setStatsList(HashMap<Integer, State> statsList) {
        this.statsList = statsList;
    }

    @Override
    public String toString() {

        System.out.println("\n-------- Table des transitions --------\n");

        StringBuilder stringBuilder = new StringBuilder();

        this.printAlphabetHeader(stringBuilder);

        //Browse state
        for (Integer stats : this.statsList.keySet()) {

            this.printStateHeader(stringBuilder, stats);

            HashMap<Character, ArrayList<State>> endingStatesHashMap = this.statsList.get(stats).getExitingEdges();

            for (Character transition : alphabet) {
                StringBuilder endingStatesListString = this.constructStatesList(endingStatesHashMap, transition);
                stringBuilder.append(String.format("%7s", endingStatesListString.toString()));
            }
        }

        return stringBuilder.toString();
    }

    private StringBuilder constructStatesList(HashMap<Character, ArrayList<State>> endingStatesHashMap, Character transition) {
        StringBuilder endingStatesListString = new StringBuilder();
        ArrayList<State> endingStates = endingStatesHashMap.get(transition);

        if (endingStates != null) {
            for (State endingState : endingStates) {
                endingStatesListString.append(endingState.getStatsName());
            }
        } else {
            endingStatesListString.append(String.format("%7s", "-"));
        }
        return endingStatesListString;
    }

    private void printStateHeader(StringBuilder stringBuilder, Integer stats) {

        int offset = 4;

        stringBuilder.append("\n\n");

        if (this.statsList.get(stats).isEntry()) {
            stringBuilder.append("E");
            offset--;
        }

        if (this.statsList.get(stats).isExit()) {
            stringBuilder.append("S");
            offset--;
        }

        stringBuilder.append(String.format("%" + offset + "s", stats));
    }

    private void printAlphabetHeader(StringBuilder stringBuilder) {
        stringBuilder.append(String.format("%4s", ""));  //To align columns header with columns

        for (Character transitions : this.alphabet) {
            stringBuilder.append(String.format("%7s", transitions));
        }
    }
}
