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
    public void loadFile(String automatonNumber, Automaton automaton) {
        this.openFile("./res/L3NEW_MpI_2_" + automatonNumber + ".txt", automaton);
    }

    public String[] openFolder() {
        File repository = new File("./res");
        return repository.list();
    }

    private void openFile(String path, Automaton automaton) {

        try {
            File currentFile = new File(path);
            this.scannerFileReader = new Scanner(currentFile);

            int lineCounter = 0;
            while (scannerFileReader.hasNextLine()) {
                String line = scannerFileReader.nextLine();

                try {

                    //Extract the number of stats
                    if (lineCounter == 0) {
                        automaton = new Automaton(Integer.parseInt(line));
                    }

                    //Extract initial stats
                    else if (lineCounter == 1) {
                        String[] initialStats = line.split(" ");
                        for (String stat : initialStats) {
                            automaton.addInitialStat(Integer.parseInt(stat));
                        }
                    }

                    //Extract terminal stats
                    else if (lineCounter == 2) {
                        String[] terminalStats = line.split(" ");
                        for (String stat : terminalStats) {
                            automaton.addTerminalStat(Integer.parseInt(stat));
                        }
                    }

                    //Extract transitions
                    else {

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
    }

    private void closeFile() {
        this.scannerFileReader.close();
    }
    //endregion
}
