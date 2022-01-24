package unsw.gloriaromanus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Province implements Serializable{
    private static final long serialVersionUID = 1234567L;
    private String name;
    private List<Unitc> units;
    private int wealth;
    private TaxStrategy strategy;
    private List<pendingUnit> pendingUnits = new ArrayList<>();
    private int turn;
    private TurnObserver tobs = new TurnObserver();

    public Province(String name){
        this.name = name;
        this.units = new ArrayList<Unitc>();
        this.wealth = 0; //initialise wealth
        this.strategy = new LowTax();
    }

    /**
     * Method to recruit a unit of soldiers
     * @param category the type of unit to be recruited
     * @param num the number of soldiers to be trained when unit is constructed
     * @param p class containing information on user 
     */
    public void recruit(String category, int num, Player p){
        Unitc recruitUnit = new Unitc(category, num);
        if (p.getTreasury() >= recruitUnit.getCost()){
            pendingUnits.add(new pendingUnit(recruitUnit, recruitUnit.getRecruitTime() + tobs.getTurn())); 
            p.setTreasury(p.getTreasury() - recruitUnit.getCost());
        }
    }

    /**
     * Method to check if unit is ready to be allowed to be used by the player
     * @param turnNumber the turn the game is on
     */
    public void recruitReady(int turnNumber){
        Iterator<pendingUnit> p = pendingUnits.iterator();

        while (p.hasNext()){
            pendingUnit pen = p.next();
            if (pen.getIsAvailable() == tobs.getTurn()){
                addUnitc(pen.getUnit());
                p.remove();
            }
        }
        
    }
    
    /**
     * Adding the recruited unit to the list of units for the province so player can use them
     * @param unit the recruited group of soldiers that is to be made available to the player
     */
    public void addUnitc (Unitc unit){
        for (Unitc u : units){
            if (unit.getType().equals(u.getType())){
                u.setNumTroops(u.getNumTroops() + unit.getNumTroops());
                u.setArmour(u.getArmour() + unit.getArmour());
                u.setMorale(u.getMorale() + unit.getMorale());
                u.setAttack(u.getAttack() + unit.getAttack());
                u.setShieldDefense(u.getShieldDefense() + unit.getShieldDefense());
                return;
            }    
        }
        units.add(unit);
    }

    /**
     * Getter method to retrieve the wealth the province has
     * @return the wealth the province has
     */
    public int getWealth(){
        return wealth;
    }

    /**
     * Getter method to retrieve the units available in the province to be used by the player
     * @return the units available in the province to be used by the player
     */
    public List<Unitc> getUnits(){
        return units;
    }

    /**
     * Getter method to retrieve the name of the province
     * @return the name of the province
     */
    public String getName(){
        return name;
    }

    /**
     * Method to find if a unit from its string name exists in the list if units available in the province
     * @param unit the name of the unit that is checked to be available in the province
     * @return the unit if name of unit exists in list of units available to the province, otherwise null
     */
    public Unitc FindUnit(String unit){
        for(Unitc u: units){
            if(u.getType().equals(unit)){
                return u;
            }
        }
        return null;
    }

    /**
     * Removes unit from the available units for the province
     * @param unit the unit that is to be removed
     */
    public void removeUnit(Unitc unit){
        units.remove(unit);
    }

    /**
     * Adds unit to the available units for the province
     * @param unit the unit to be added
     */
    public void addUnit(Unitc unit){
        units.add(unit);
    }

    /**
     * TODO 
     * @param WonUnits
     */
    public void changeUnits(List<Unitc> WonUnits){
        this.units = WonUnits;
    }

    /**
     * Setter method to set the wealth of the province
     * @param newWealth the wealth of the province
     */
    public void setWealth(int newWealth){
        this.wealth = newWealth;
    }

    /**
     * Method to change the tax strategy implemented by the province
     */
    public void changeTaxStrategy(TaxStrategy newStrat){
        this.strategy = newStrat;
    }

    /**
     * TODO
     * @param player
     */
    public void taxImplement(Player player){
        strategy.collectTax(this, player);
    }

    /**
     * Getter method to retrieve the list of units that are still in training for the province
     * @return the list of units that are still in training for the province
     */
    public List<pendingUnit> getPendingUnits() {
        return pendingUnits;
    }

    /**
     * Setter method to set the list of units that are still in training for the province
     * @param pendingUnits the list of units that are stil in training for the province
     */
    public void setPendingUnits(List<pendingUnit> pendingUnits) {
        this.pendingUnits = pendingUnits;
    }

    /**
     * Getter method to retrieve the turn the game is on
     * @return the turn the game is on
     */
    public int getTurn() {
        return turn;
    }

    /**
     * Setter method to set the turn the game is on
     * @param turn the turn the game is on
     */
    public void setTurn(int turn) {
        this.turn = turn;
    }

    public TurnObserver getTobs() {
        return tobs;
    }

    public void setTobs(TurnObserver tobs) {
        this.tobs = tobs;
    }
    
}
