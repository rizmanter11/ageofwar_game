package unsw.gloriaromanus;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Unitc implements Serializable{
    private static final long serialVersionUID = 1234567L;
    private String type;
    private String superType; //is unit cavalry, infantry or artillery
    private int numTroops;  // the number of troops in this unit (should reduce based on depletion)
    private int range;  // range of the unit
    private Boolean isRanged = false;
    private int armour;  // armour defense
    private int morale;  // resistance to fleeing
    private int speed;  // ability to disengage from disadvantageous battle
    private int attack;  // can be either missile or melee attack to simplify. Could improve implementation by differentiating!
    private int defenseSkill;  // skill to defend in battle. Does not protect from arrows!
    private int shieldDefense;
    private int charge = 0;
    private int recruitTime; //turns to wait for recruit to appear
    private int movementPoints; //amount of points left to move between provinces
    private int cost;

    public Unitc (String sold, int num){ 
        JSONObject soldier = SoldierFromJSON(sold);
        this.numTroops = num;
        this.type = soldier.getString("soldier");
        this.superType = soldier.getString("unit");
        this.range = soldier.getInt("range");
        if (range > 0) isRanged = true;
        this.armour = soldier.getInt("armour") * num;
        this.morale = soldier.getInt("morale") * num;
        this.speed = soldier.getInt("speed");
        this.attack = soldier.getInt("attack") * num;
        if(this.getSuperType().equals("cavalry")){
            this.movementPoints = 15;
        } else if(this.getSuperType().equals("infantry")){
            this.movementPoints = 10;
        } else if(this.getSuperType().equals("artillery")){
            this.movementPoints = 4;
        }
        this.recruitTime = soldier.getInt("recruitTime"); 
        this.defenseSkill = soldier.getInt("defense");
        this.shieldDefense = soldier.getInt("shield") * num;
        if(this.superType.equals("cavalry")){ this.charge = soldier.getInt("charge");}
        this.recruitTime = soldier.getInt("recruitTime"); 
        this.cost = soldier.getInt("cost") * num;
        
    }

    /**
     * Retrieves JSONObject of soldier from SoldiersData.json file
     * @param soldier the name of the soldier whose data is to be retrieved
     * @return JSONObject containing information on soldier
     */
    public JSONObject SoldierFromJSON (String soldier){
        String str = "{ \"soldier\": \"heavy infantry\", \"unit\": \"infantry\", \"range\": 0, \"attack\": 30, \"defense\": 24, \"morale\": 10, \"armour\": 100, \"shield\": 3, \"speed\": 4, \"recruitTime\": 2, \"cost\": 7}\r\n{ \"soldier\": \"skirmisher\", \"unit\": \"infantry\", \"range\": 0, \"attack\": 20, \"defense\": 20, \"morale\": 8, \"armour\": 70, \"shield\": 3, \"speed\": 7, \"recruitTime\": 1, \"cost\": 5} \r\n{ \"soldier\": \"spearman\", \"unit\": \"infantry\", \"range\": 0, \"attack\": 22, \"defense\": 18, \"morale\": 6, \"armour\": 60, \"shield\": 1, \"speed\": 5, \"recruitTime\": 1, \"cost\": 4}\r\n{ \"soldier\": \"archer\", \"unit\": \"infantry\", \"range\": 15, \"attack\": 20, \"defense\": 10, \"morale\": 9, \"armour\": 80, \"shield\": 2, \"speed\": 5, \"recruitTime\": 1, \"cost\": 6}\r\n{ \"soldier\": \"slinger\", \"unit\": \"infantry\", \"range\": 8, \"attack\": 9, \"defense\": 9, \"morale\": 2, \"armour\": 50, \"shield\": 1, \"speed\": 6, \"recruitTime\": 1, \"cost\": 3}\r\n{ \"soldier\": \"legionary\", \"unit\": \"infantry\", \"range\": 0, \"attack\": 25, \"defense\": 24, \"morale\": 11, \"armour\": 100, \"shield\": 2, \"speed\": 7, \"recruitTime\": 2, \"cost\": 6} \r\n{ \"soldier\": \"beserker\", \"unit\": \"infantry\", \"range\": 0, \"attack\": 35, \"defense\": 30, \"morale\": 18, \"armour\": 150, \"shield\": 5, \"speed\": 3, \"recruitTime\": 3, \"cost\": 11}\r\n{ \"soldier\": \"pikeman\", \"unit\": \"infantry\", \"range\": 0, \"attack\": 22, \"defense\": 10, \"morale\": 10, \"armour\": 70, \"shield\": 2, \"speed\": 5, \"recruitTime\": 1, \"cost\": 3}\r\n{ \"soldier\": \"hoplite\", \"unit\": \"infantry\", \"range\": 0, \"attack\": 23, \"defense\": 26, \"morale\": 14, \"armour\": 110, \"shield\": 2, \"speed\": 4, \"recruitTime\": 1, \"cost\": 4}\r\n{ \"soldier\": \"druid\", \"unit\": \"infantry\", \"range\":18, \"attack\": 30, \"defense\": 6, \"morale\": 6, \"armour\": 70, \"shield\": 2, \"speed\": 7, \"recruitTime\": 2, \"cost\": 10}\r\n{ \"soldier\": \"lancer\", \"unit\": \"cavalry\", \"range\": 0, \"attack\": 22, \"charge\": 10, \"defense\": 20, \"morale\": 11, \"armour\": 125, \"shield\": 3, \"speed\": 3, \"recruitTime\": 2, \"cost\": 9}\r\n{ \"soldier\": \"heavy cavalry\", \"unit\": \"cavalry\", \"range\": 0, \"attack\": 30, \"charge\": 5, \"defense\": 35, \"morale\": 12, \"armour\": 150, \"shield\": 4, \"speed\": 2, \"recruitTime\": 2, \"cost\": 8}\r\n{ \"soldier\": \"elephant\", \"unit\": \"cavalry\", \"range\": 0, \"attack\": 45, \"charge\": 20, \"defense\": 35, \"morale\": 14, \"armour\": 200, \"shield\": 5, \"speed\": 1, \"recruitTime\": 3, \"cost\": 15}\r\n{ \"soldier\": \"chariot\", \"unit\": \"cavalry\", \"range\": 0, \"attack\": 25, \"charge\": 15, \"defense\": 25, \"morale\": 13, \"armour\": 130, \"shield\": 4, \"speed\": 1, \"recruitTime\": 2, \"cost\": 10}\r\n{ \"soldier\": \"horse archer\", \"unit\": \"cavalry\", \"range\": 20, \"attack\": 20, \"charge\": 8, \"defense\": 15, \"morale\": 14, \"armour\": 125, \"shield\": 3, \"speed\": 2, \"recruitTime\": 2, \"cost\": 7}\r\n{ \"soldier\": \"ballista\", \"unit\": \"artillery\", \"range\": 30, \"attack\": 30, \"defense\": 8, \"morale\": 13, \"armour\": 80, \"shield\": 1, \"speed\": 1, \"recruitTime\": 2, \"cost\": 8}\r\n{ \"soldier\": \"onager\", \"unit\": \"artillery\", \"range\": 35, \"attack\": 25, \"defense\": 7, \"morale\": 12, \"armour\": 75, \"shield\": 1, \"speed\": 1, \"recruitTime\": 2, \"cost\": 8}";
        /*
        try {
            FileReader f = new FileReader("bin/unsw/gloriaromanus/SoldiersData.json");
            BufferedReader b =new BufferedReader(f);
            String s;
            while ((s = b .readLine()) != null){
                JSONObject soldierOBJ = new JSONObject(s);                
                if (soldierOBJ.getString("soldier").equals(soldier)) {return soldierOBJ;}
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        return new JSONObject(); // maybe change
        */
        String[] result = str.split("\n", 17);
        for (String s : result){  
            JSONObject obj = new JSONObject(s);
            if (obj.getString("soldier").equals(soldier)){
                return obj;
            }

        }
        return null;
    }
    
    /**
     * Getter method to retrieve the type of the unit
     * @return the type of the unit
     */
    public String getType() {
        return type;
    }

    /**
     * Setter method to set the type of the unit
     * @param type The soldier which makes up the unit
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Getter method to retrieve the number of troops in the unit
     * @return the number of troops in the unit
     */
    public int getNumTroops() {
        return numTroops;
    }

    /**
     * Setter method to set the number of troops in the unit
     * @param numTroops the number of troops in the unit
     */
    public void setNumTroops(int numTroops) {
        this.numTroops = numTroops;
    }

    /**
     * Getter method to to retireve the range of the unit
     * @return the range of the unit
     */
    public int getRange() {
        return range;
    }

    /**
     * Setter method to set the range of the unit
     * @param range the range of the unit
     */
    public void setRange(int range) {
        this.range = range;
    }

    /**
     * Getter method to retireve the armour of the unit
     * @return the armour of the unit
     */
    public int getArmour() {
        return armour;
    }

    /**
     * Setter method to set the armour of the unit
     * @param armour the armour the unit has
     */
    public void setArmour(int armour) {
        this.armour = armour;
    }

    /**
     * Getter mthod to retrieve the moral of the unit
     * @return the moral of the unit
     */
    public int getMorale() {
        return morale;
    }

    /**
     * Setter method to set the morale of the unit
     * @param morale the morale of the unit
     */
    public void setMorale(int morale) {
        this.morale = morale;
    }

    /**
     * Getter method to retrieve the speed of the unit
     * @return The speed of the unit
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Setter method to set the speed of the unit
     * @param speed the speed of the unit
     */
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    /**
     * Getter method to retrieve the attack power of the unit
     * @return the attack power of the unit
     */
    public int getAttack() {
        return attack;
    }

    /**
     * Setter method to set the attack power of the unit
     * @param attack the attack power of the unit
     */
    public void setAttack(int attack) {
        this.attack = attack;
    }

    /**
     * Getter method to retrieve the defense skill of the unit
     * @return the defense skill of the unit
     */
    public int getDefenseSkill() {
        return defenseSkill;
    }

    /**
     * Setter method to set the defense skill of the unit
     * @param defenseSkill the defense skill of the unit
     */
    public void setDefenseSkill(int defenseSkill) {
        this.defenseSkill = defenseSkill;
    }

    /**
     * Getter method to retrieve the shield defense of the unit
     * @return the shield defense of the unit
     */
    public int getShieldDefense() {
        return shieldDefense;
    }

    /**
     * Setter method to set the shield defense of the unit
     * @param shieldDefense the shield defense of the unit
     */
    public void setShieldDefense(int shieldDefense) {
        this.shieldDefense = shieldDefense;
    }

    /**
     * Getter method to retrieve the SuperType of the unit
     * @return if the unit is a cavalry, infantry or artillery
     */
    public String getSuperType() {
        return superType;
    }

    /**
     * Setter method to set the SuperType of the unit
     * @param superType tells if the unit is a cavalry, infantry or artillery
     */
    public void setSuperType(String superType) {
        this.superType = superType;
    }

    /**
     * Getter method to find if unit is ranged
     * @return true if unit is range otherwise false
     */
    public Boolean getIsRanged() {
        return isRanged;
    }

    /**
     * Setter method to set if the unit is ranged or not
     * @param isRanged Tells if unit is ranged or not
     */
    public void setIsRanged(Boolean isRanged) {
        this.isRanged = isRanged;
    }

    /**
     * Getter method to retrieve charge of unit
     * @return the charge of unit
     */
    public int getCharge(){
        return this.charge;
    }

    /**
     * Setter method to set the charge of the unit
     * @param charge the charge of the unit
     */
    public void setCharge(int charge) {
        this.charge = charge;
    }

    /**
     * Getter method to retrieve the recruitTime of the unit
     * @return the recruitTime of the unit
     */
    public int getRecruitTime() {
        return recruitTime;
    }

    /**
     * Setter method to set the recruitTime of the unit
     * @param recruitTime the time the unit takes to be recruited
     */
    public void setRecruitTime(int recruitTime) {
        this.recruitTime = recruitTime;
    }

    /**
     * Reduces the movement point of the unit
     */
    public void decrementMovement(){
        this.movementPoints -= 4;
    } 
    
    /**
     * Getter method to retrieve the cost creating the unit
     * @return the cost required to make the unit
     */
    public int getCost() {
        return cost;
    }

    /**
     * Setter method to set the cost required to make the unit
     * @param cost the cost required to make the unit
     */
    public void setCost(int cost) {
        this.cost = cost;
    }
    
    /**
     * Getter method to retrieve the movementPoints of the unit
     * @return the movementPoints of the unit
     */
    public int getMovement(){
        return movementPoints;
    }
}
