import java.util.Scanner;

public class Main {

    private static Scanner keyboard;
    private static FileManager fileManager;

    public static void main(String[] args) {
        fileManager = new FileManager();
        keyboard = new Scanner(System.in);
        processingLoop();
    }

    private static void processingLoop(){
        String answers;
        boolean stopProcessingLoop;
        do{
            printMainMenu();
            answers = keyboard.nextLine();

            stopProcessingLoop = answers.equals("2");

            if(!stopProcessingLoop){
                fileManager.loadFile(answers);
            }

        } while(!stopProcessingLoop);
    }

    private static void printMainMenu(){
        System.out.println("\n/////////////////////////");
        System.out.println("/*      Main menu      */");
        System.out.println("/////////////////////////");
        System.out.println("1. Choisir un automate");
        System.out.println("2. Quitter");
        System.out.print("Faites votre choix : ");
    }
}
