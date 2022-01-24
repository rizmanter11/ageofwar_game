package unsw.gloriaromanus;

public class PlayersTurn implements State{
    Turn turn;

    public PlayersTurn(Turn turn){
        this.turn = turn;
    }

    public void endTurn(){
        //collect taxes from all provinces
        //add temporary list of invaded provinces to owned provinces
        //check if any of the goals are achieved
        turn.setState(turn.getOppositionTurn());
    }

    public void startTurn(){
        //this is where all the player implementation goes
        int currentTurn = turn.getTurnNumber();
        currentTurn++;
        turn.setTurnNumber(currentTurn);
    }
}
