import exceptions.NonDeterministicTransition;

import java.util.ArrayList;
import java.util.HashMap;

public class Automaton {

    //region Variables
    private final ArrayList<Character> alphabet;
    private final HashMap<Integer, State> statsList;
    private boolean isAsynchronous;
    private final ArrayList<String> asynchronousTransitions;
    private final ArrayList<String> nonDeterministicTransitions;
    private final ArrayList<State> initialStatsList;
    private final ArrayList<State> terminalStatsList;

    //endregion

    //region Constructor
    public Automaton(int alphabetSize, int numberOfStats) {
        this.alphabet = new ArrayList<>();
        this.statsList = new HashMap<>();
        this.isAsynchronous = false;
        this.asynchronousTransitions = new ArrayList<>();
        this.nonDeterministicTransitions = new ArrayList<>();
        this.initialStatsList = new ArrayList<>();
        this.terminalStatsList = new ArrayList<>();

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
        this.initialStatsList.add(this.statsList.get(stat));
    }

    public void addTerminalStat(int stat) {
        this.statsList.get(stat).setExit(true);
        this.terminalStatsList.add(this.statsList.get(stat));
    }

    public void addTransition(int startingStateInt, char transition, int endingStateInt) {
        State startingState = this.statsList.get(startingStateInt);
        State endingState = this.statsList.get(endingStateInt);

        try {
            startingState.addExitingEdge(endingState, transition);
        } catch (NonDeterministicTransition nonDeterministicTransition) {
            this.nonDeterministicTransitions.add(nonDeterministicTransition.getMessage());
        }

        if (transition == '*') {
            this.isAsynchronous = true;
            this.asynchronousTransitions.add(startingStateInt + String.valueOf(transition) + endingStateInt);
        }

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
    //endregion

    //region Getter
    public ArrayList<String> getAsynchronousTransitions() {
        return this.asynchronousTransitions;
    }

    public boolean isAsynchronous() {
        return this.isAsynchronous;
    }

    public HashMap<Integer, State> getStatsList() {
        return statsList;
    }

    public ArrayList<State> getInitialStatsList() {
        return initialStatsList;
    }

    public ArrayList<State> getTerminalStatsList() {
        return terminalStatsList;
    }

    public ArrayList<String> getNonDeterministicTransitions() {
        return nonDeterministicTransitions;
    }

    public ArrayList<Character> getAlphabet() {
        return alphabet;
    }

    //endregion

    //region Override
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
    //endregion
}
