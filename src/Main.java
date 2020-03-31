import java.util.Scanner;

public class Main {

    //region Variables
    private static Scanner keyboard;
    private static FileManager fileManager;
    private static Automaton automaton;
    //endregion

    //region Constructor
    public static void main(String[] args) {
        fileManager = new FileManager();
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

            if (answers.equals("1")) {
                printAutomatonSelectionMenu();
                fileManager.loadFile(keyboard.nextLine(),automaton);
            }

        } while (!answers.equals("2"));
    }

    private static void printMainMenu() {
        System.out.println("\n/////////////////////////");
        System.out.println("/*      Main menu      */");
        System.out.println("/////////////////////////");
        System.out.println("1. Choisir un automate");
        System.out.println("2. Quitter");
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
}
