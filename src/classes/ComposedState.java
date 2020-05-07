package classes;

import java.util.HashMap;

public class ComposedState extends State {

    //region Variables
    private final HashMap<String, State> composingStates;
    //endregion

    //region Constructor
    public ComposedState() {
        super("");
        this.composingStates = new HashMap<>();
    }
//endregion

    //region Utils
    public void addComposingState(State newComposingState) {
        if (!this.statsName.isEmpty()) {
            this.statsName += ",";
        }
        this.statsName += newComposingState.getStatsName();

        if (newComposingState.isExit()) {
            this.isExit = true;
        }

        this.composingStates.put(newComposingState.getStatsName(), newComposingState);
    }
    //endregion

    //region Getter
    public HashMap<String, State> getComposingStates() {
        return this.composingStates;
    }
    //endregion
}
