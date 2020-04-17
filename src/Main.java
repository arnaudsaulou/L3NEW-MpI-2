import java.util.Scanner;

public class Main {

    //region Constants
    private final static String EXITING_OPTION_AUTOMATON_NULL = "2";
    private final static String EXITING_OPTION_AUTOMATON_NON_NULL = "7";
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
                printAutomatonSelectionMenu();
                automaton = automatonManager.chooseAutomaton(keyboard.nextLine());
                break;
            case "2":
                if (automaton != null) {
                    automatonManager.printTransitionTable(automaton);
                }
                break;
            case "3":
                automatonManager.checkIfAutomatonAsynchronous(automaton);
                break;

            case "4":
                automatonManager.checkIfAutomatonDeterministic(automaton);
                break;

            case "5":
                automatonManager.checkIfAutomatonIsFull(automaton);
                break;

            case "6":
                automatonManager.completeAutomaton(automaton);
                break;
            default:
                break;
        }
    }

    private static void printMainMenu() {
        System.out.println("\n/////////////////////////");
        System.out.println("/*      Main menu      */");
        System.out.println("/////////////////////////");
        System.out.println("1. Choisir un automate");

        //Change option depending on automaton value
        if (automaton != null) {
            System.out.println("2. Afficher l'automate");
            System.out.println("3. Is async");
            System.out.println("4. Is determinist");
            System.out.println("5. Is full");
            System.out.println("6. Complete");
            System.out.println("7. Quitter");
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

    private static String readWordFromUserInput(){
        System.out.print("Entrer le mot à reconnaitre : ");
        return keyboard.nextLine();
    }
    //endregion
}
