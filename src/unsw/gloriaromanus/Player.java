package unsw.gloriaromanus;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Player implements Serializable{
    private static final long serialVersionUID = 1234567L;
    private String faction;
    private int treasury;
    private List<Province> provinces;
    private GoalComponent victoryGoals;
    private boolean conquestGoal = false;
    private boolean treasuryGoal = false;
    private boolean wealthGoal = false;
    private boolean andConjunction = false;
    private boolean orConjunction = false;
    private TurnObserver turn;

    public Player(String faction, List<Province> provinces){
        this.faction = faction;
        this.provinces = provinces;
        this.treasury = 0;
    }

    /**
     * add an observer to the turn subject
     * @param sub
     */
    public void addObsToSub(TurnSubject sub){
        sub.addObs(turn);
        setTurnForProvinces();
    } 

    /**
     * setting the turn for all the provinces
     */
    public void setTurnForProvinces(){
        for (Province p : provinces){
            p.setTurn(turn.getTurn());
        }
    }

    /**
     * checks if the player has captured all the regions in a game
     */
    public boolean capturedAllRegions(){
        if(provinces.size() == 53){
            return true;
        }
        return false;
    }

    /**
     * checks if the player has reached the treasury goal
     * @return
     */
    public boolean treasuryGoalReached(){
        if(treasury >= 100000){
            return true;
        }
        return false;
    }

    /**
     * checks if the player has lost all regions
     * @return
     */
    public boolean lostAllRegions(){
        if(provinces.size() == 0){
            return true;
        }
        return false;
    }

    /**
     * checks if the player has reached the wealth goal
     * @return
     */
    public boolean wealthGoalReached(){
        if(totalWealth() >= 400000){
            return true;
        }
        return false;
    }

    /**
     * randomly genrates victory goals by appending one goal to and and the other 2 left to or
     */
    public void generateVictoryGoals(){
        ArrayList<String> goals = new ArrayList<String>();
        goals.add("CONQUEST");
        goals.add("TREASURY");
        goals.add("WEALTH");
        if(isAndConjunction()){
            GoalConjunction and = new GoalConjunction("AND");
            Goal newGoal = null;
            if(isConquestGoal()){
                newGoal = new Goal("CONQUEST");
                goals.remove("CONQUEST");
            } else if(isTreasuryGoal()){
                newGoal = new Goal("TREASURY");
                goals.remove("TREASURY");
            } else if(isWealthGoal()){
                newGoal = new Goal("WEALTH");
                goals.remove("WEALTH");
            }

            Goal otherGoal = null;
            for(String g: goals){
                if(isConquestGoal() && g.equals("CONQUEST")){
                    newGoal = new Goal("CONQUEST");
                } else if(isTreasuryGoal() && g.equals("TREASURY")){
                    newGoal = new Goal("TREASURY");
                } else if(isWealthGoal() && g.equals("WEALTH")){
                    newGoal = new Goal("WEALTH");
                }
            }
            and.addGoal(newGoal);
            and.addGoal(otherGoal);
            victoryGoals = and;
        } else if(isOrConjunction()){
            GoalConjunction or = new GoalConjunction("AND");
            Goal newGoal = null;
            if(isConquestGoal()){
                newGoal = new Goal("CONQUEST");
                goals.remove("CONQUEST");
            } else if(isTreasuryGoal()){
                newGoal = new Goal("TREASURY");
                goals.remove("TREASURY");
            } else if(isWealthGoal()){
                newGoal = new Goal("WEALTH");
                goals.remove("WEALTH");
            }

            Goal otherGoal = null;
            for(String g: goals){
                if(isConquestGoal() && g.equals("CONQUEST")){
                    newGoal = new Goal("CONQUEST");
                } else if(isTreasuryGoal() && g.equals("TREASURY")){
                    newGoal = new Goal("TREASURY");
                } else if(isWealthGoal() && g.equals("WEALTH")){
                    newGoal = new Goal("WEALTH");
                }
            }

            or.addGoal(newGoal);
            or.addGoal(otherGoal);
            victoryGoals = or;
        }
    }

    /**
     * returns total wealth of all the provinces in the players faction
     * @return int indictaing wealth
     */
    public int totalWealth(){
        int totalWealth = 0;
        for(Province p: provinces){
            totalWealth += p.getWealth();
        }
        return totalWealth;
    }

    /**
     * returns treasury of the players faction
     * @return int inicating treasury levels
     */
    public int getTreasury(){
        return treasury;
    }

    /**
     * setter method that sets the treasury
     * @param treasury
     */
    public void setTreasury(int treasury){
        this.treasury = treasury;
    }

    /**
     * getter method that retrives the players Faction
     * @return String indicating the players faction
     */
    public String getFaction(){
        return faction;
    } 

    /**
     * adds a province obejct to the provinces list for the player
     */
    public void addProvince(Province province){
        provinces.add(province);
    }

    /**
     * removes a province from the players province list
     * @param province
     */
    public void removeProvince(Province province){
        provinces.remove(province);
    }

    /**
     * finds a province using the string identifying the provinces name
     * @param provinceName
     * @return a province object
     */
    public Province findProvince(String provinceName){
        for(Province p: provinces){
            if(p.getName().equals(provinceName)){
                return p;
            }
        }
        return null;
    }

    /**
     * moves the troops between provinces
     * @param provinceInitial
     * @param provinceAfter
     * @param unitName
     */
    public void moveTroops(String provinceInitial, String provinceAfter, String unitName){
        Province inital = findProvince(provinceInitial);
        Province after = findProvince(provinceAfter);
        Unitc unit = inital.FindUnit(unitName);
        if(unit.getMovement() < 4){
            System.out.println("Can't move"+unit.getType());
            return;
        }
        unit.decrementMovement();
        inital.removeUnit(unit);
        after.addUnit(unit);
    }

    /**
     * saves the players data
     */
    public void saveData(){
        ArrayList<Object> data = new ArrayList<Object>();
        data.add(faction);
        data.add(treasury);
        data.add(provinces);
        data.add(victoryGoals);
        data.add(conquestGoal);
        data.add(treasuryGoal);
        data.add(wealthGoal);
        data.add(turn);

        try {
            FileOutputStream fileOut = new FileOutputStream("PlayerData.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(data);
            out.close();
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * loads the players data
     */
    public void loadData(){
        ArrayList<Object> data = new ArrayList<Object>();

        try {
            FileInputStream fileIn = new FileInputStream("PlayerData.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            data = (ArrayList<Object>)in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IOException");
            return;
        } catch (ClassNotFoundException c){
            c.printStackTrace();
            System.out.println("ClassNotFoundException");
            return;
        }

        faction = (String)data.get(0);
        treasury = (int)data.get(1);
        provinces = (List<Province>)data.get(2);
        victoryGoals = (GoalComponent)data.get(3);
        conquestGoal = (boolean)data.get(4);
        treasuryGoal = (boolean)data.get(5);
        wealthGoal = (boolean)data.get(6);
        turn = (TurnObserver)data.get(7);

    }

    /**
     * getter method that retrieves the playrs provinces list
     * @return list of provinces
     */
    public List<Province> getProvinces() {
        return provinces;
    }

    /**
     * sets the provinces list to the province list given
     * @param provinces
     */
    public void setProvinces(List<Province> provinces) {
        this.provinces = provinces;
    }

    /**
     * getter method that returns the victory goals for the front end to display
     * @return
     */
    public GoalComponent getVictoryGoals() {
        return victoryGoals;
    }

    /**
     * sets vistory goals to the given vistory goals object
     * @param victoryGoals
     */
    public void setVictoryGoals(GoalComponent victoryGoals) {
        this.victoryGoals = victoryGoals;
    }

    /**
     * checks if the player has to achieve a conquest goal
     * @return  true if the players has to achieve the goal
     */
    public boolean isConquestGoal() {
        return conquestGoal;
    }

    /**
     * sets that conquestgoal boolean to the given boolean indicating whether they have to achieve th goal or not
     * @param conquestGoal
     */
    public void setConquestGoal(boolean conquestGoal) {
        this.conquestGoal = conquestGoal;
    }

    /**
     * checks if the player has to achieve a treasury goal
     * @return  true if the players has to achieve the goal
     */
    public boolean isTreasuryGoal() {
        return treasuryGoal;
    }

    /**
     * sets that treasurygoal boolean to the given boolean indicating whether they have to achieve the goal or not
     * @param conquestGoal
     */
    public void setTreasuryGoal(boolean treasuryGoal) {
        this.treasuryGoal = treasuryGoal;
    }

    /**
     * checks if the player has to achieve a wealth goal
     * @return  true if the players has to achieve the goal
     */
    public boolean isWealthGoal() {
        return wealthGoal;
    }

    /**
     * sets that wealthgoal boolean to the given boolean indicating whether they have to achieve th goal or not
     * @param conquestGoal
     */
    public void setWealthGoal(boolean wealthGoal) {
        this.wealthGoal = wealthGoal;
    }

    public void setAndConjunction(boolean and){
        this.andConjunction = and;
    }

    public void setOrConjunction(boolean or){
        this.orConjunction = or;
    }

    public Province getProvince(String provinceName){
        for(Province p: provinces){
            if(p.getName().equals(provinceName)){
                return p;
            }
        }
        return null;
    }

    public boolean isAndConjunction(){
        return andConjunction;
    }

    public boolean isOrConjunction(){
        return orConjunction;
    }
    
}
