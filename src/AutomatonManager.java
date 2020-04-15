public class AutomatonManager {

    //region Variables
    private Automaton automaton;
    private FileManager fileManager;
    //endregion

    //region Constructor
    public AutomatonManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }
    //endregion

    //region Utils
    protected void chooseAutomaton(String automatonName) {
        this.automaton = this.fileManager.loadFile(automatonName);
    }

    protected void printAutomaton() {
        System.out.println(this.automaton);
    }
    //endregion

    //region Getter
    public Automaton getAutomaton() {
        return automaton;
    }
    //endregion

    //region Setter
    public void setAutomaton(Automaton automaton) {
        this.automaton = automaton;
    }
    //endregion
}
