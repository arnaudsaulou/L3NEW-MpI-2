package classes;

import exceptions.NonDeterministicTransitionException;
import exceptions.TooMuchInitialStatsException;
import exceptions.UnknownTransitionException;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.*;

public class AutomatonManager {

    //region Constants
    private static final String EMPTY_TAG = "vide";
    //endregion

    //region Variables
    private final FileManager fileManager;
    //endregion

    //region Constructor
    public AutomatonManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }
    //endregion

    //region Utils
    public Automaton chooseAutomaton(String automatonName) {
        return this.fileManager.loadFile(automatonName);
    }

    public void printTransitionTable(Automaton automaton) {
        automaton.printTransitionTable();
    }

    private boolean isAutomatonAsynchronous(Automaton automaton) {
        return automaton.isAsynchronous();
    }

    public boolean checkIfAutomatonAsynchronous(Automaton automaton) {
        System.out.println("\n-------- Test asynchronite --------\n");

        boolean isAutomatonAsynchronous = this.isAutomatonAsynchronous(automaton);

        if (isAutomatonAsynchronous) {
            System.out.println("L'automate est asynchrone a cause des transitions suivantes :");
            for (Transition asyncTransition : automaton.getAsynchronousTransitions()) {
                System.out.println("\t - " + asyncTransition);
            }
        } else {
            System.out.println("L'automate est synchrone");
        }
        return isAutomatonAsynchronous;
    }

    public boolean checkIfAutomatonDeterministic(Automaton automaton) {
        System.out.println("\n-------- Test determinisation --------\n");

        boolean isAutomatonDeterministic = false;

        try {
            isAutomatonDeterministic = this.isDeterministic(automaton);

            if (isAutomatonDeterministic) {
                System.out.println("L'automate est deterministe");
            } else {
                System.out.println("L'automate n'est pas deterministe a cause des transitions suivantes :");

                for (Transition transitions : automaton.getNonDeterministicTransitions()) {
                    System.out.println("\t - " + transitions);
                }

            }
        } catch (TooMuchInitialStatsException tooMuchInitialStatsException) {
            System.out.print("L'automate n'est pas deterministe car : ");
            System.out.println(tooMuchInitialStatsException.getMessage());
        }

        return isAutomatonDeterministic;

    }

    private boolean isDeterministic(Automaton automaton) throws TooMuchInitialStatsException {

        boolean isDeterministic = true;

        if (!automaton.getStatsList().isEmpty()) {

            if (automaton.getInitialStatsList().size() > 1) {
                throw new TooMuchInitialStatsException();
            } else {
                if (!automaton.getNonDeterministicTransitions().isEmpty()) {
                    isDeterministic = false;
                }
            }
        }

        return isDeterministic;
    }

    public boolean checkIfAutomatonIsFull(Automaton automaton) {
        System.out.println("\n-------- Test complet --------\n");

        boolean isAutomatonFull = this.isFull(automaton);

        if (isAutomatonFull) {
            System.out.println("L'automate est complet");
        } else {
            System.out.println("L'automate n'est pas complet a cause des transitions suivantes :");

            for (State state : automaton.getMissingTransitionToComplete().keySet()) {
                System.out.println("\t - " + state.getStatsName() + automaton.getMissingTransitionToComplete().get(state));
            }
        }

        return isAutomatonFull;
    }

    private boolean isFull(Automaton automaton) {
        boolean isFull = true;

        for (State state : automaton.getStatsList().values()) {
            for (Character symbol : automaton.getAlphabet()) {
                if (state.getExitingEdges().get(symbol) == null) {
                    isFull = false;

                    if (automaton.getMissingTransitionToComplete().containsKey(state) &&
                            !automaton.getMissingTransitionToComplete().get(state).contains(symbol)) {
                        automaton.getMissingTransitionToComplete().get(state).add(symbol);
                    } else {
                        ArrayList<Character> missingTransitionList = new ArrayList<>();
                        missingTransitionList.add(symbol);
                        automaton.getMissingTransitionToComplete().put(state, missingTransitionList);
                    }

                }
            }
        }

        return isFull;
    }

    public void completeAutomaton(Automaton automaton) {
        if (this.isFull(automaton)) {
            System.out.println("Cet automate est deja complet");
        } else {
            System.out.println("\n-------- Completion de l'automate --------\n");

            System.out.println("\t - Ajout de l'état poubelle");
            automaton.addTrashState();

            String startingState, endingState = "P";

            for (State missingTransitionState : automaton.getMissingTransitionToComplete().keySet()) {

                for (Character transition : automaton.getMissingTransitionToComplete().get(missingTransitionState)) {
                    startingState = missingTransitionState.getStatsName();
                    try {
                        automaton.addTransition(startingState, transition, endingState);
                    } catch (UnknownTransitionException ignored) {
                    }
                    System.out.println("\t - Ajout de la transition : " + startingState + transition + endingState);
                }
            }

            automaton.getMissingTransitionToComplete().clear();
        }
    }

    public Automaton determineAutomaton(Automaton automaton) {

        //Create a new empty automaton based on the automaton
        Automaton deterministAutomaton = new Automaton(automaton.getAlphabet().size());
        deterministAutomaton.setInitialStatsList(automaton.getInitialStatsList());

        State entryState = this.checkAndActIfTooMuchEntry(automaton, deterministAutomaton);

        //If a new entry as been created
        if (entryState != null) {
            this.determineAutomatonRecursively(entryState, automaton, deterministAutomaton, false);
        } else {

            //Make sur all states have been checked
            for (State currentState : automaton.getStatsList().values()) {
                if (currentState.isNeverVisited() && !currentState.isComposingState()) {
                    deterministAutomaton.addState(currentState);
                    this.determineAutomatonRecursively(currentState, automaton, deterministAutomaton, true);
                }
            }
        }

        deterministAutomaton.printTransitionTable();

        return deterministAutomaton;
    }

    private State checkAndActIfTooMuchEntry(Automaton automaton, Automaton deterministAutomaton) {

        ArrayList<State> initialStatsList = automaton.getInitialStatsList();
        ComposedState newComposedState = null;

        if (initialStatsList.size() > 1) {
            newComposedState = new ComposedState();

            //For each initial state
            for (State initialState : initialStatsList) {
                newComposedState.addComposingState(initialState);
            }

            for (Character symbol : automaton.getAlphabet()) {
                for (State composingState : newComposedState.getComposingStates().values()) {
                    ArrayList<State> successors = composingState.getSuccessorWithGivenSymbol(symbol);
                    if (successors != null) {
                        for (State successor : successors) {
                            try {
                                newComposedState.addExitingEdge(successor, symbol);
                            } catch (NonDeterministicTransitionException ignored) {
                            }
                        }
                    }
                }
            }

            newComposedState.setEntry(true);

            //Add the new state created
            initialStatsList.clear();
            initialStatsList.add(newComposedState);
            deterministAutomaton.addState(newComposedState);
        }

        return newComposedState;
    }

    private void determineAutomatonRecursively(State currentState, Automaton automaton, Automaton deterministAutomaton,
                                               boolean copyEntry) {

        if (currentState.isNeverVisited()) {
            currentState.setNeverVisited(false);

            //Variable declaration
            ArrayList<State> successorsCreated = new ArrayList<>();
            State successor;

            //For each symbol of the alphabet
            for (Character symbol : automaton.getAlphabet()) {
                successor = this.computeSuccessor(currentState, deterministAutomaton, successorsCreated, symbol, copyEntry);

                if (successor != null) {
                    if (currentState.getExitingEdges().get(symbol) != null) {
                        currentState.getExitingEdges().get(symbol).clear();
                        currentState.getExitingEdges().get(symbol).add(successor);
                    } else {
                        ArrayList<State> successors = new ArrayList<>();
                        successors.add(successor);
                        currentState.getExitingEdges().put(symbol, successors);
                    }
                }
            }

            for (State successorCreated : successorsCreated) {
                if (successorCreated != currentState)
                    this.determineAutomatonRecursively(successorCreated, automaton, deterministAutomaton, copyEntry);
            }
        }
    }

    private State computeSuccessor(State currentState, Automaton deterministAutomaton,
                                   ArrayList<State> successorsCreated, Character symbol, boolean copyEntry) {

        State successor = null;

        //Get all successors of the current state
        ArrayList<State> successors = currentState.getSuccessorWithGivenSymbol(symbol);

        //If there is no successor then do nothing
        if (successors != null) {

            //If there is more then one successor, creation of a ComposedState, else just add the current state
            // to the determinist automaton
            if (successors.size() > 1) {

                //Add the newComposedState to the determinist automaton and to the list of successors created
                successor = this.constructComposedState(successors);

            } else {
                if (!successors.isEmpty()) {

                    //Make sure to remove entries on the newly created ComposedState if a new entry as been created before
                    // (see : determineAutomaton() )
                    if (!copyEntry) {
                        successors.get(0).setEntry(false);
                    }

                    //Add the state to the determinist automaton and to the list of successors.
                    successor = successors.get(0);
                }
            }

            if (successor != null) {

                //If the current state is composed
                if (currentState instanceof ComposedState) {
                    ComposedState currentStateComposed = (ComposedState) currentState;

                    //Prevent the current composed state to loop on itself
                    if (!successors.containsAll(currentStateComposed.getComposingStates().values())) {
                        deterministAutomaton.addState(successor);
                        successorsCreated.add(successor);
                    }
                }
                //Else
                else {
                    deterministAutomaton.addState(successor);
                    successorsCreated.add(successor);
                }
            }

        }
        return successor;
    }

    private ComposedState constructComposedState(ArrayList<State> successors) {

        //Create a new ComposedState
        ComposedState newComposedState = new ComposedState();

        //For each successors of the current state with the current symbol
        for (State successor : successors) {
            successor.setComposingState(true);

            //Add the successor to the composing states list of the ComposedSate
            newComposedState.addComposingState(successor);

            //Handle exiting edge og the newComposedState after adding a new composing state
            //this.linkExitingEdgeOfComposedState(automaton.getAlphabet(), newComposedState, successor);
        }
        return newComposedState;
    }

    public Automaton createComplementaryAutomaton(Automaton automaton) {

        for (State state : automaton.getStatsList().values()) {
            state.reverseExit();
        }

        return automaton;
    }

    public boolean wordRecognition(Automaton automaton, String word) {
        try {
            this.isDeterministic(automaton);
        } catch (TooMuchInitialStatsException tooMuchInitialStatsException) {
            determineAutomaton(automaton);
        }

        boolean recognized = true;
        State start = automaton.getInitialStatsList().get(0);

        CharacterIterator letter = new StringCharacterIterator(word);

        while (letter.current() != CharacterIterator.DONE && recognized) {
            for (char transition : start.getExitingEdges().keySet()) {
                if (letter.current() == transition) {
                    start = start.getExitingEdges().get(letter.current()).get(0);
                    break;
                } else {
                    recognized = false;
                }
            }

            letter.next();
        }

        System.out.println("Etats de sortis :" + start.getStatsName());

        return (start.isExit() && (word.equals(EMPTY_TAG) || !start.isEntry()));
    }

    public void standardization(Automaton automaton) {

        if (automaton.getInitialStatsList().size() > 1) {
            State newState = new State("I");
            automaton.addState(newState);
            newState.setEntry(true);

            for (State initialState : automaton.getInitialStatsList()) {
                initialState.setEntry(false);

                if (initialState.isExit() && !newState.isExit()) {
                    newState.setExit(true);
                    automaton.getTerminalStatsList().remove(initialState);
                    automaton.getTerminalStatsList().add(newState);
                }

                for (char transition : initialState.getExitingEdges().keySet()) {

                    if (newState.getExitingEdges().get(transition) != null) {

                        for (State state : initialState.getExitingEdges().get(transition)) {
                            if (!newState.getExitingEdges().get(transition).contains(state)) {
                                newState.getExitingEdges().get(transition).add(state);
                            }
                        }

                    } else {
                        newState.getExitingEdges().put(transition, new ArrayList<>(
                                initialState.getExitingEdges().get(transition))
                        );
                    }
                }

            }

            automaton.getInitialStatsList().clear();
            automaton.getInitialStatsList().add(newState);

        }
    }


//endregion
}
