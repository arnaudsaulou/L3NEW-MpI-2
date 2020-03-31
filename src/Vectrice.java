import java.util.ArrayList;
import java.util.HashMap;

public class Vectrice {

    //region Variables
    private int statsName;
    private boolean isEntry;
    private boolean isExit;
    //endregion

    //region Constructor
    public Vectrice(int statsName) {
        this.statsName = statsName;
    }
    //endregion

    //region Setter
    public void setEntry(boolean entry) {
        this.isEntry = entry;
    }

    public void setExit(boolean exit) {
        this.isExit = exit;
    }
    //endregion


}
