package unsw.gloriaromanus;

import java.io.Serializable;
import java.util.ArrayList;

public class TurnSubject implements Serializable{
    ArrayList<TurnObserver> listObs = new ArrayList<TurnObserver>();
    int turnNum;

    /**
     * Adds an observer to listObs
     * @param obs observer holding the turn for the game
     */
    public void addObs(TurnObserver obs){
        if (! listObs.contains(obs)) {listObs.add(obs);}
    }

    /**
     * Removing an observer from listObs
     * @param obs observer holding the turn for the game
     */
    public void removeObs(TurnObserver obs){
        listObs.remove(obs);
    }

    /**
     * Notifying observer to update it
     */
    public void notfiyObs(){
        for (TurnObserver o : listObs){ o.update(this);}
    }

    /**
     * Getter method to retrieve the turn the game is on
     * @return the turn the game is on
     */
    public int getTurnNum(){
        return turnNum;
    }

    /**
     * Setter method to set the turn the game is on
     * @param turnNum the turn the game is on
     */
    public void setTurnNum(int turnNum){
        this.turnNum = turnNum;
        notfiyObs();
    }
}
