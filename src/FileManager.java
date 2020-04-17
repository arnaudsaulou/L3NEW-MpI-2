import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileManager {

    //region Variables
    private Scanner scannerFileReader;
    //endregion

    //region Constructor
    public FileManager() {

    }
    //endregion

    //region Utils
    public Automaton loadFile(String automatonNumber) {
        return this.openFile("./res/L3NEW_MpI_2_" + automatonNumber + ".txt");
    }

    public String[] openFolder() {
        File repository = new File("./res");
        return repository.list();
    }

    private Automaton openFile(String path) {

        Automaton automaton = null;

        try {
            File currentFile = new File(path);
            this.scannerFileReader = new Scanner(currentFile);
            int lineCounter = 0;
            int alphabetSize = 0;

            while (scannerFileReader.hasNextLine()) {
                String line = scannerFileReader.nextLine();

                try {

                    //Extract alphabet's size
                    if (lineCounter == 0) {
                        alphabetSize = Integer.parseInt(line);
                    }

                    //Extract the number of stats
                    else if (lineCounter == 1) {
                        automaton = new Automaton(alphabetSize, Integer.parseInt(line));
                    }

                    //Extract initial stats
                    else if (lineCounter == 2 && automaton != null) {
                        String[] initialStats = line.split(" ");
                        for (String stat : initialStats) {
                            automaton.addInitialStat(Integer.parseInt(stat));
                        }
                    }

                    //Extract terminal stats
                    else if (lineCounter == 3 && automaton != null) {
                        String[] terminalStats = line.split(" ");
                        for (String stat : terminalStats) {
                            automaton.addTerminalStat(Integer.parseInt(stat));
                        }
                    }

                    //Extract transitions
                    else if (automaton != null) {
                        String[] transition = line.split("");
                        automaton.addTransition(
                                Integer.parseInt(transition[0]),
                                transition[1].charAt(0),
                                Integer.parseInt(transition[2]));
                    }

                } catch (NumberFormatException numberFormatException) {
                    System.err.println("Chargement de l'automate impossible, erreur de format de fichier. \n" + numberFormatException.getMessage());
                }

                lineCounter++;
            }

            this.closeFile();

        } catch (FileNotFoundException e) {
            System.err.println("Automate non trouvé");
        }

        return automaton;
    }

    private void closeFile() {
        this.scannerFileReader.close();
    }
    //endregion
}
