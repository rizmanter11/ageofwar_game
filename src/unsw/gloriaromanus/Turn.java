
package unsw.gloriaromanus;

public class Turn {

    State playersTurn;
    State oppositionTurn;
    int turnNumber;

    State state = playersTurn;

    public Turn(){
        this.playersTurn = new PlayersTurn(this);
        this.oppositionTurn = new OppositionTurn(this);
        this.turnNumber = 1;
    }

    public void startTurn(){
        state.startTurn();
    }

    public void endTurn(){
        state.endTurn();
    }

    public void setState(State state){
        this.state = state;
    }

    public void setTurnNumber(int turnNo){
        this.turnNumber = turnNo;
    }

    public State getState() {
        return state;
    }

    public State getPlayersTurn(){
        return playersTurn;
    }

    public State getOppositionTurn(){
        return oppositionTurn;
    }

    public int getTurnNumber(){
        //use to get turn number in frontend and use in player.java
        return turnNumber;
    }
}

