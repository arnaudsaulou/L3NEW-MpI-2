import java.util.Scanner;

public class Main {

    //region Variables
    private static Scanner keyboard;
    private static FileManager fileManager;
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
        } while (!answers.equals("3"));
    }

    private static void handleUserChoice(String answers) {

        switch (answers){
            case "1":
                printAutomatonSelectionMenu();
                automatonManager.chooseAutomaton(keyboard.nextLine());
                break;
            case "2":
                automatonManager.printAutomaton();
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
        System.out.println("2. Afficher l'automate");
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
