package classes;

import exceptions.NonDeterministicTransitionException;
import exceptions.NonExistingStateException;
import exceptions.UnknownTransitionException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Automaton {

    //region Variables
    private final ArrayList<Character> alphabet;
    private final HashMap<String, State> statsList;
    private boolean isAsynchronous;
    private final ArrayList<Transition> asynchronousTransitions;
    private final ArrayList<Transition> nonDeterministicTransitions;
    private final HashMap<State, ArrayList<Character>> missingTransitionToComplete;
    private ArrayList<State> initialStatsList;
    private final ArrayList<State> terminalStatsList;

    //endregion

    //region Constructor
    public Automaton(int alphabetSize, int numberOfStats) {
        this.alphabet = new ArrayList<>();
        this.statsList = new HashMap<>();
        this.isAsynchronous = false;
        this.asynchronousTransitions = new ArrayList<>();
        this.nonDeterministicTransitions = new ArrayList<>();
        this.missingTransitionToComplete = new HashMap<>();
        this.initialStatsList = new ArrayList<>();
        this.terminalStatsList = new ArrayList<>();

        for (int i = 0; i < alphabetSize; i++) {
            this.alphabet.add((char) ('a' + i));
        }

        String stateName;
        for (int i = 0; i < numberOfStats; i++) {
            stateName = String.valueOf(i);
            this.statsList.put(stateName, new State(stateName));
        }
    }

    public Automaton(int alphabetSize) {
        this.alphabet = new ArrayList<>();
        this.statsList = new HashMap<>();
        this.isAsynchronous = false;
        this.asynchronousTransitions = new ArrayList<>();
        this.nonDeterministicTransitions = new ArrayList<>();
        this.missingTransitionToComplete = new HashMap<>();
        this.initialStatsList = new ArrayList<>();
        this.terminalStatsList = new ArrayList<>();

        for (int i = 0; i < alphabetSize; i++) {
            this.alphabet.add((char) ('a' + i));
        }
    }
    //endregion

    //region Utils
    public void addInitialStat(String stateName) throws NonExistingStateException {
        State state = this.statsList.get(stateName);
        if (state != null) {
            state.setEntry(true);
            this.initialStatsList.add(state);
        } else {
            throw new NonExistingStateException("Etat inexistant : " + stateName);
        }
    }

    public void addTerminalStat(String stateName) throws NonExistingStateException {
        State state = this.statsList.get(stateName);
        if (state != null) {
            state.setExit(true);
            this.terminalStatsList.add(state);
        } else {
            throw new NonExistingStateException("Etat inexistant : " + stateName);
        }

    }

    public void addTransition(String startingStateString, char transition, String endingStateSting) throws UnknownTransitionException {
        State startingState = this.statsList.get(startingStateString);
        State endingState = this.statsList.get(endingStateSting);

        if (this.getAlphabet().contains(transition)) {

            try {
                startingState.addExitingEdge(endingState, transition);
            } catch (NonDeterministicTransitionException nonDeterministicTransitionException) {
                this.nonDeterministicTransitions.addAll(
                        nonDeterministicTransitionException.getNonDeterministicTransition()
                );
            }
        } else if (transition == '*') {
            this.isAsynchronous = true;
            this.asynchronousTransitions.add(
                    new Transition(startingState, transition, endingState)
            );
        } else {
            throw new UnknownTransitionException("Transistion inconnue :" + transition);
        }

    }

    private StringBuilder constructStatesList(HashMap<Character, ArrayList<State>> endingStatesHashMap, Character transition) {
        StringBuilder endingStatesListString = new StringBuilder();
        ArrayList<State> endingStates = endingStatesHashMap.get(transition);

        if (endingStates != null) {
            Iterator<State> stateIterator = endingStates.iterator();
            while (stateIterator.hasNext()) {
                endingStatesListString.append(stateIterator.next().getStatsName());
                if (stateIterator.hasNext()) {
                    endingStatesListString.append(",");
                }
            }
        } else {
            endingStatesListString.append(String.format("%9s", "-"));
        }
        return endingStatesListString;
    }

    private void printStateHeader(StringBuilder stringBuilder, String stats) {

        int offset = 8;

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
        stringBuilder.append(String.format("%8s", ""));  //To align columns header with columns

        for (Character transitions : this.alphabet) {
            stringBuilder.append(String.format("%9s", transitions));
        }
    }

    public void addState(State newState) {
        this.statsList.put(newState.getStatsName(), newState);
    }

    public void addTrashState() {
        this.addState(new State("P"));

        for (Character transition : this.alphabet) {
            try {
                this.addTransition("P", transition, "P");
            } catch (UnknownTransitionException ignored) {
            }
        }
    }

    public void printTransitionTable() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("\n-------- Table des transitions --------\n\n");

        this.printAlphabetHeader(stringBuilder);

        //Browse state
        for (String stats : this.statsList.keySet()) {

            this.printStateHeader(stringBuilder, stats);

            HashMap<Character, ArrayList<State>> endingStatesHashMap = this.statsList.get(stats).getExitingEdges();

            for (Character transition : alphabet) {
                StringBuilder endingStatesListString = this.constructStatesList(endingStatesHashMap, transition);
                stringBuilder.append(String.format("%9s", endingStatesListString.toString()));
            }
        }

        System.out.println(stringBuilder.toString());
    }

    //endregion

    //region Getter
    public ArrayList<Transition> getAsynchronousTransitions() {
        return this.asynchronousTransitions;
    }

    public boolean isAsynchronous() {
        return this.isAsynchronous;
    }

    public HashMap<String, State> getStatsList() {
        return this.statsList;
    }

    public ArrayList<State> getInitialStatsList() {
        return this.initialStatsList;
    }

    public ArrayList<State> getTerminalStatsList() {
        return this.terminalStatsList;
    }

    public ArrayList<Transition> getNonDeterministicTransitions() {
        return this.nonDeterministicTransitions;
    }

    public ArrayList<Character> getAlphabet() {
        return this.alphabet;
    }

    public HashMap<State, ArrayList<Character>> getMissingTransitionToComplete() {
        return this.missingTransitionToComplete;
    }

    //endregion

    //region Setter

    public void setInitialStatsList(ArrayList<State> initialStatsList) {
        this.initialStatsList = initialStatsList;
    }

    //endregion

    //region Override
    @Override
    public String toString() {
        return "";
    }

    //endregion
}
