package classes;

import exceptions.NonExistingStateException;
import exceptions.UnknownTransitionException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Comparator;
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
        return this.openFile("./res/L3New_MpI_2_" + automatonNumber + ".txt");
    }

    public String[] openFolder() {
        File repository = new File("./res");
        String[] result = repository.list();

        Arrays.sort(result, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                o1 = o1.replace("L3New_MpI_2_", "");
                o1 = o1.replace(".txt", "");
                o2 = o2.replace("L3New_MpI_2_", "");
                o2 = o2.replace(".txt", "");

                return Integer.valueOf(o1).compareTo(Integer.valueOf(o2));
            }
        });

        return result;
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
                            automaton.addInitialStat(stat);
                        }
                    }

                    //Extract terminal stats
                    else if (lineCounter == 3 && automaton != null) {
                        String[] terminalStats = line.split(" ");
                        for (String stat : terminalStats) {
                            automaton.addTerminalStat(stat);
                        }
                    }

                    //Extract transitions
                    else if (automaton != null) {
                        String[] transition = line.split("");

                        automaton.addTransition(
                                transition[0],
                                transition[1].charAt(0),
                                transition[2]);
                    }

                } catch (NumberFormatException | NonExistingStateException | UnknownTransitionException | ArrayIndexOutOfBoundsException exception) {
                    System.err.println("Chargement de l'automate impossible, erreur de format de fichier. \n" +
                            exception.getMessage() + "\n");
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
