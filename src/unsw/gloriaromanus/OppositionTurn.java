package unsw.gloriaromanus;

public class OppositionTurn implements State{
    Turn turn;

    public OppositionTurn(Turn turn){
        this.turn = turn;
    }

    public void endTurn(){
        turn.setState(turn.getPlayersTurn());
    }

    public void startTurn(){
        //AI implementation
    }
    
}
