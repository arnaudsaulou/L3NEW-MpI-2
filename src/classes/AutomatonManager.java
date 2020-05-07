package classes;

import exceptions.NonDeterministicTransitionException;
import exceptions.TooMuchInitialStatsException;

import java.util.*;

public class AutomatonManager {

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

    protected void printAutomaton(Automaton automaton) {
        System.out.println(automaton);
    }

    private boolean isAutomatonAsynchronous(Automaton automaton) {
        return automaton.isAsynchronous();
    }

    public void checkIfAutomatonAsynchronous(Automaton automaton) {
        System.out.println("\n-------- Test asynchronite --------\n");

        if (this.isAutomatonAsynchronous(automaton)) {
            System.out.println("L'automate est asynchrone a cause des transitions suivantes :");
            for (Transition asyncTransition : automaton.getAsynchronousTransitions()) {
                System.out.println("\t - " + asyncTransition);
            }
        } else {
            System.out.println("L'automate est synchrone");
        }

    }

    public void checkIfAutomatonDeterministic(Automaton automaton) {
        System.out.println("\n-------- Test determinisation --------\n");

        try {
            if (this.isDeterministic(automaton)) {
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

    public void checkIfAutomatonIsFull(Automaton automaton) {
        System.out.println("\n-------- Test complet --------\n");

        if (this.isFull(automaton)) {
            System.out.println("L'automate est complet");
        } else {
            System.out.println("L'automate n'est pas complet a cause des transitions suivantes :");

            for (State state : automaton.getMissingTransitionToComplete().keySet()) {
                System.out.println("\t - " + state.getStatsName() + automaton.getMissingTransitionToComplete().get(state));
            }
        }
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
                    automaton.addTransition(startingState, transition, endingState);
                    System.out.println("\t - Ajout de la transition : " + startingState + transition + endingState);
                }
            }

            automaton.getMissingTransitionToComplete().clear();
        }
    }

    public void determineAutomaton(Automaton automaton) {

        //Create a new empty automaton based on the automaton
        Automaton deterministAutomaton = new Automaton(automaton.getAlphabet().size());

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


/*
            deterministAutomaton.addState(automaton.getStatsList().get("0"));
            this.determineAutomatonRecursively(automaton.getStatsList().get("0"), automaton, deterministAutomaton, true);*/


        }

        deterministAutomaton.printTransitionTable();
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

    private void determineAutomatonRecursively(State currentState, Automaton automaton, Automaton deterministAutomaton, boolean copyEntry) {

        if (currentState.isNeverVisited()) {
            currentState.setNeverVisited(false);

            //Variable declaration
            ArrayList<State> successorsCreated = new ArrayList<>();

            //For each symbol of the alphabet
            for (Character symbol : automaton.getAlphabet()) {
                this.computeSuccessor(currentState, automaton, deterministAutomaton, successorsCreated, symbol, copyEntry);
            }

            for (State successorCreated : successorsCreated) {
                this.determineAutomatonRecursively(successorCreated, automaton, deterministAutomaton, copyEntry);
            }
        }
    }

    private void computeSuccessor(State currentState, Automaton automaton, Automaton deterministAutomaton,
                                  ArrayList<State> successorsCreated, Character symbol, boolean copyEntry) {

        ComposedState newComposedState;

        //Get all successors of the current state
        ArrayList<State> successors = currentState.getSuccessorWithGivenSymbol(symbol);

        //If there is no successor then do nothing
        if (successors != null) {

            //If there is more then one successor, creation of a ComposedState, else just add the current state
            // to the determinist automaton
            if (successors.size() > 1) {
                newComposedState = this.constructComposedState(automaton, successors);

                //Add the newComposedState to the determinist automaton and to the list of successors created
                deterministAutomaton.addState(newComposedState);
                successorsCreated.add(newComposedState);
            } else {

                //Make sure to remove entries on the newly created ComposedState if a new entry as been created before
                // (see : determineAutomaton() )
                if (!copyEntry) {
                    successors.get(0).setEntry(false);
                }

                //Add the state to the determinist automaton and to the list of successors.
                deterministAutomaton.addState(successors.get(0));
                successorsCreated.add(successors.get(0));
            }
        }
    }

    private ComposedState constructComposedState(Automaton automaton, ArrayList<State> successors) {

        //Create a new ComposedState
        ComposedState newComposedState = new ComposedState();

        //For each successors of the current state with the current symbol
        for (State successor : successors) {
            successor.setComposingState(true);

            //Add the successor to the composing states list of the ComposedSate
            newComposedState.addComposingState(successor);

            //Handle exiting edge og the newComposedState after adding a new composing state
            this.linkExitingEdgeOfComposedState(automaton.getAlphabet(), newComposedState, successor);
        }
        return newComposedState;
    }

    private void linkExitingEdgeOfComposedState(ArrayList<Character> alphabet, ComposedState newComposedState, State successor) {

        //Variable declaration
        ArrayList<State> successorsOfSuccessor;

        //For each symbol of the alphabet
        for (Character symbolOfSuccessor : alphabet) {

            //Get all successors of the state
            successorsOfSuccessor = successor.getSuccessorWithGivenSymbol(symbolOfSuccessor);

            //If there is no successor then do nothing
            if (successorsOfSuccessor != null) {

                //For each successors of the state with the current symbol
                for (State successorOfSuccessor : successorsOfSuccessor) {

                    //Try to add the edge from the newComposedState to the state, with the given symbol
                    try {
                        newComposedState.addExitingEdge(successorOfSuccessor, symbolOfSuccessor);
                    } catch (NonDeterministicTransitionException ignored) {
                    }

                }
            }
        }
    }
    //endregion
}
