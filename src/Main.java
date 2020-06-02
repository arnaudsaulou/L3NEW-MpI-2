import classes.Automaton;
import classes.AutomatonManager;
import classes.FileManager;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class Main {

    //region Constants
    private final static String EXITING_OPTION_AUTOMATON_NULL = "2";
    private final static String EXITING_OPTION_AUTOMATON_NON_NULL = "8";
    //endregion

    //region Variables
    private static Scanner keyboard;
    private static FileManager fileManager;
    private static Automaton automaton;
    private static AutomatonManager automatonManager;
    //endregion

    //region Constructor
    public static void main(String[] args) {
        fileManager = new FileManager();
        automatonManager = new AutomatonManager(fileManager);
        keyboard = new Scanner(System.in);
        processingLoop();
    }
    //endregion

    //region Utils
    private static void processingLoop() {
        String answers;

        do {
            printMainMenu();
            answers = keyboard.nextLine();
            handleUserChoice(answers);
        } while (loopDependingOnAutomatonValue(answers));

    }

    private static boolean loopDependingOnAutomatonValue(String answers) {
        return (automaton != null && !answers.equals(EXITING_OPTION_AUTOMATON_NON_NULL)) ||
                (automaton == null && !answers.equals(EXITING_OPTION_AUTOMATON_NULL));
    }

    private static void handleUserChoice(String answers) {

        switch (answers) {
            case "1":
                importAutomatonProcess();
                break;

            case "2":
                if (automaton != null)
                    printTransitionTableProcess();
                break;

            case "3":
                if (automaton != null)
                    determinationAndCompletionProcess();
                break;

            case "4":
                if (automaton != null)
                    minimizationProcess();
                break;

            case "5":
                if (automaton != null)
                    wordRecognitionProcess(automaton);
                break;

            case "6":
                if (automaton != null)
                    complementaryLanguageProcess();
                break;

            case "7":
                if (automaton != null)
                    standardizationProcess();
                break;

            default:
                break;
        }
    }

    //region Process

    private static void minimizationProcess() {
        System.out.println("Coming soon !");
    }

    private static void standardizationProcess() {
        automatonManager.standardization(automaton);
        printTransitionTableProcess();
    }

    private static void wordRecognitionProcess(Automaton automaton) {
        String word;
        do {
            word = readWordFromUserInput();
            System.out.println(automatonManager.wordRecognition(automaton, word) ? "Mot reconnu" : "Mot non-reconnu");
        } while (!word.equals("fin"));
    }

    private static void complementaryLanguageProcess() {
        determinationAndCompletionProcess();
        Automaton complementaryAutomaton = automatonManager.createComplementaryAutomaton(automaton);
        printTransitionTableProcess();
        wordRecognitionProcess(complementaryAutomaton);

        //To reverse the process and go back to the original automaton
        automatonManager.createComplementaryAutomaton(automaton);
    }

    private static void determinationAndCompletionProcess() {
        if (automatonManager.checkIfAutomatonAsynchronous(automaton)) {
            //TODO Maybe one day
        } else {
            if (automatonManager.checkIfAutomatonDeterministic(automaton)) {
                if (!automatonManager.checkIfAutomatonIsFull(automaton)) {
                    automatonManager.completeAutomaton(automaton);
                }
            } else {
                automaton = automatonManager.determineAutomaton(automaton);
                automatonManager.completeAutomaton(automaton);
            }
        }
        automatonManager.printTransitionTable(automaton);
    }

    private static void printTransitionTableProcess() {
        automatonManager.printTransitionTable(automaton);
    }

    private static void importAutomatonProcess() {
        printAutomatonSelectionMenu();
        automaton = automatonManager.chooseAutomaton(keyboard.nextLine());
    }

    //endregion

    //region Menu

    private static void printMainMenu() {
        System.out.println("\n/////////////////////////");
        System.out.println("/*      Main menu      */");
        System.out.println("/////////////////////////");
        System.out.println("1. Choisir un automate");

        //Change option depending on automaton value
        if (automaton != null) {
            System.out.println("2. Affichage de l’automate");
            System.out.println("3. Déterminisation et complétion");
            System.out.println("4. Minimisation");
            System.out.println("5. Reconnaissance de mots");
            System.out.println("6. Langage complémentaire");
            System.out.println("7. Standardisation");
            System.out.println("8. Quitter");
        } else {
            System.out.println("2. Quitter");
        }
        System.out.print("Faites votre choix : ");
    }

    private static void printAutomatonSelectionMenu() {
        System.out.println("\nChoisir un automate parmis la liste disponible :");
        String[] repositoryContent = fileManager.openFolder();

        for (int i = 0; i < repositoryContent.length; i++) {
            System.out.println("\t" + "- " + (i + 1) + ") " + repositoryContent[i].replace(".txt", ""));
        }

        System.out.print("Votre selection : ");
    }

    //endregion

    private static String readWordFromUserInput() {
        System.out.print("Entrer le mot à reconnaitre ('fin' pour quitter) : ");
        return keyboard.nextLine();
    }
    //endregion
}
