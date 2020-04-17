import exceptions.TooMuchInitialStatsException;

import java.util.ArrayList;
import java.util.HashMap;

public class AutomatonManager {

    //region Variables
    private final FileManager fileManager;
    private final HashMap<State, ArrayList<Character>> missingTransitionToComplete;
    //endregion

    //region Constructor
    public AutomatonManager(FileManager fileManager) {
        this.fileManager = fileManager;
        this.missingTransitionToComplete = new HashMap<>();
    }
    //endregion

    //region Utils
    protected Automaton chooseAutomaton(String automatonName) {
        return this.fileManager.loadFile(automatonName);
    }

    protected void printTransitionTable(Automaton automaton) {
        automaton.printTransitionTable();
    }

    protected void printAutomaton(Automaton automaton) {
        System.out.println(automaton);
    }

    private boolean isAutomatonAsynchronous(Automaton automaton) {
        return automaton.isAsynchronous();
    }

    protected void checkIfAutomatonAsynchronous(Automaton automaton) {
        System.out.println("\n-------- Test asynchronite --------\n");

        if (this.isAutomatonAsynchronous(automaton)) {
            System.out.println("L'automate est asynchrone a cause des transitions suivantes :");
            for (String asyncTransition : automaton.getAsynchronousTransitions()) {
                System.out.println("\t - " + asyncTransition);
            }
        } else {
            System.out.println("L'automate est synchrone");
        }

    }

    protected void checkIfAutomatonDeterministic(Automaton automaton) {
        System.out.println("\n-------- Test determinisation --------\n");

        try {
            if (this.isDeterministic(automaton)) {
                System.out.println("L'automate est deterministe");
            } else {
                System.out.println("L'automate n'est pas deterministe a cause des transitions suivantes :");

                for (String transitions : automaton.getNonDeterministicTransitions()) {
                    System.out.println("\t - " + transitions);
                }

            }
        } catch (TooMuchInitialStatsException tooMuchInitialStatsException) {
            System.out.print("L'automate est deterministe car : ");
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

    protected void checkIfAutomatonIsFull(Automaton automaton) {
        System.out.println("\n-------- Test complet --------\n");

        if (this.isFull(automaton)) {
            System.out.println("L'automate est complet");
        } else {
            System.out.println("L'automate n'est pas complet a cause des transitions suivantes :");

            for (State state : this.missingTransitionToComplete.keySet()) {
                System.out.println("\t - " + String.valueOf(state.getStatsName()) + this.missingTransitionToComplete.get(state));
            }
        }
    }

    private boolean isFull(Automaton automaton) {
        boolean isFull = true;

        for (State state : automaton.getStatsList().values()) {
            for (Character symbol : automaton.getAlphabet()) {
                if (state.getExitingEdges().get(symbol) == null) {
                    isFull = false;

                    if (this.missingTransitionToComplete.containsKey(state) &&
                            !this.missingTransitionToComplete.get(state).contains(symbol)) {
                        this.missingTransitionToComplete.get(state).add(symbol);
                    } else {
                        ArrayList<Character> missingTransitionList = new ArrayList<>();
                        missingTransitionList.add(symbol);
                        this.missingTransitionToComplete.put(state, missingTransitionList);
                    }

                }
            }
        }

        return isFull;
    }

    protected void completeAutomaton(Automaton automaton) {
        if (this.isFull(automaton)) {
            System.out.println("Cet automate est deja complet");
        } else {
            System.out.println("\n-------- Completion de l'automate --------\n");

            System.out.println("\t - Ajout de l'état poubelle");
            automaton.addTrashState();

            String startingState, endingState = "P";

            for (State missingTransitionState : this.missingTransitionToComplete.keySet()) {

                for (Character transition : this.missingTransitionToComplete.get(missingTransitionState)) {
                    startingState = missingTransitionState.getStatsName();
                    automaton.addTransition(startingState, transition, endingState);
                    System.out.println("\t - Ajout de la transition : " + startingState + transition + endingState);
                }
            }

            this.missingTransitionToComplete.clear();
        }
    }
    //endregion
}
