package unsw.gloriaromanus;

import java.io.Serializable;

public class TurnObserver implements Serializable{
    private int turn;

    /**
     * Updating the turn in TurnObserver to equal the turn in TurnSubject
     * @param sub The subject holding he turn the game is on
     */
    public void update(TurnSubject sub){
        this.turn = sub.getTurnNum();
    }

    /**
     * Getter method to retrieve the turn the game is on
     * @return the turn the game is on
     */
    public int getTurn(){
        return turn;
    }
}
