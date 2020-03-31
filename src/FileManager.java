import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileManager {

    private Scanner scannerFileReader;

    public FileManager() {

    }

    public void loadFile(String automatonNumber) {
        this.openFile("./res/L3NEW_MpI_2_" + automatonNumber + ".txt");
    }

    private void openFile(String path) {

        try {
            File currentFile = new File(path);
            this.scannerFileReader = new Scanner(currentFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (scannerFileReader.hasNextLine()) {
            System.out.println(scannerFileReader.nextLine());
        }

        this.closeFile();
    }

    private void closeFile() {
        this.scannerFileReader.close();
    }
}
