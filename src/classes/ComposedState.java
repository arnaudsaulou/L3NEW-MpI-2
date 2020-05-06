package classes;

import java.util.ArrayList;
import java.util.HashMap;

public class ComposedState extends State {

    private HashMap<String, State> composingStates;

    public ComposedState() {
        super("");
        this.composingStates = new HashMap<>();
    }

    public void addComposingState(State newComposingState) {
        if (!this.statsName.isEmpty()) {
            this.statsName += ",";
        }
        this.statsName += newComposingState.getStatsName();

/*        if(newComposingState.isEntry() && copyEntry){
            this.isEntry = true;
        }*/

        if(newComposingState.isExit()){
            this.isExit = true;
        }

        this.composingStates.put(newComposingState.getStatsName(), newComposingState);
    }

    public HashMap<String, State> getComposingStates() {
        return this.composingStates;
    }

}
