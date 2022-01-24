package unsw.gloriaromanus;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Attack {
    List <Unitc> PlayerUnits;
    List <Unitc> OppositionUnits;
    Province PlayerProvince;
    Province OppoProvince;
    Player player;
    Player opposition;

    List <Unitc> playerRouted = new ArrayList<>();
    List <Unitc> oppoRouted = new ArrayList<>();

    public Attack(List<Unitc> PlayerUnits, List<Unitc> OppositionUnits, Province PlayerProvince, Province OppoProvince, Player player, Player opposition){
        this.PlayerProvince = PlayerProvince;
        this.OppoProvince = OppoProvince;
        this.PlayerUnits = PlayerUnits;
        this.OppositionUnits = OppositionUnits;
        this.player = player;
        this.opposition = opposition;
    }

    /**
     * Conducts the battle implementation 
     */
    public String battle(){
        while(true){
            //conduct battle
            //for now print string to system.out.println
            if(OppositionUnits.size() == 0 && PlayerUnits.size() == 0){
                addRouted(PlayerUnits, playerRouted);
                addRouted(OppositionUnits, oppoRouted);
                return "Tied";
            } else if(OppositionUnits.size() == 0){
                addRouted(PlayerUnits, playerRouted);
                //Province capturedProvince = new Province(OppoProvince.getName());
                OppoProvince.changeUnits(PlayerUnits);
                player.addProvince(OppoProvince);
                PlayerProvince.getUnits().removeAll(PlayerUnits);
                opposition.removeProvince(OppoProvince);
                return "Player won battle";
            } else if(PlayerUnits.size() == 0){
                addRouted(OppositionUnits, oppoRouted);
                return "Opposition won battle";
            }
            System.out.println(skirmish());
        }
    }

    /**
     * takes the routed units and adds it to the original list of units 
     * @param originalUnits the original units given in by the player for the battle or the oppoenents units
     * @param routed the list of routed units 
     */
    public void addRouted(List<Unitc> originalUnits, List<Unitc> routed){
        for(Unitc r: routed){
            originalUnits.add(r);
        }
    }
    
    /**
     * implements each skirmish taking in a random unit from both sides
     * @return a String to print out on the front end
     */
    public String skirmish(){
        Random r = new Random();
        int randPlayer = r.nextInt(PlayerUnits.size());
        int randOp = r.nextInt(OppositionUnits.size());
        Unitc playersUnit = PlayerUnits.get(randPlayer);
        Unitc OppoUnit = OppositionUnits.get(randOp);
        if(playersUnit.getIsRanged() == OppoUnit.getIsRanged()){
            if(playersUnit.getIsRanged()){
                while(true){
                    int oppoCasualties = PlayerRangeAttack(playersUnit, OppoUnit);
                    int playerCasualties = PlayerRangeAttack(OppoUnit, playersUnit);

                    int OppoStartNum = OppoUnit.getNumTroops();
                    int playerStartNum = playersUnit.getNumTroops();
                    OppoUnit.setNumTroops(OppoStartNum - oppoCasualties);
                    playersUnit.setNumTroops(playerStartNum - playerCasualties);

                    if(OppoUnit.getNumTroops() == 0 && playersUnit.getNumTroops() == 0){
                        //return a message that OppositionUnits troop is deafeated
                        PlayerProvince.removeUnit(playersUnit);
                        PlayerUnits.remove(playersUnit);
                        OppoProvince.removeUnit(OppoUnit);
                        OppositionUnits.remove(OppoUnit);
                        return "Wiped out both units";
                    } else if(OppoUnit.getNumTroops() == 0){
                        //return a message that OppositionUnits troop is deafeated
                        OppoProvince.removeUnit(OppoUnit);
                        OppositionUnits.remove(OppoUnit);
                        return "Opposition's"+OppoUnit.getType()+"has been defeated";
                    } else if(playersUnit.getNumTroops() == 0){
                        //return a message that players troop is deafeated
                        PlayerProvince.removeUnit(playersUnit);
                        PlayerUnits.remove(playersUnit);
                        return "Players's"+playersUnit.getType()+"has been defeated";
                    }

                    if(Break(OppoUnit, playersUnit, oppoCasualties, OppoStartNum, playerCasualties, playerStartNum)){
                        // return OppositionUnits unit broken
                        return routingRanged(OppoUnit, playersUnit, "OppositionUnits");
                    }

                    if(Break(playersUnit, OppoUnit, playerCasualties, playerStartNum, oppoCasualties, OppoStartNum)){
                        // return PlayerUnits unit has broken
                        return routingRanged(playersUnit, OppoUnit, "PlayerUnits");
                    }
                }
            } else if(!playersUnit.getIsRanged()){
                return MeleeSkirmish(playersUnit, OppoUnit);
            }
        } else if(playersUnit.getIsRanged()){
            //OppositionUnits is melee
            int speedMelee = OppoUnit.getSpeed();
            int speedRanged = OppoUnit.getSpeed();
            double extraMeleeChance = 0.1*(speedMelee - speedRanged);
            double totalMelee = 50 + extraMeleeChance;

            int engagementType = r.nextInt(100); 
            if(engagementType >= (int) totalMelee){
                while(true){
                    int oppoCasualties = PlayerRangeAttack(playersUnit, OppoUnit);
                    //OppositionUnits cant attack as they are melee
                    int OppoStartNum = OppoUnit.getNumTroops();
                    OppoUnit.setNumTroops(OppoStartNum - oppoCasualties);
                    if(OppoUnit.getNumTroops() == 0){
                        //return a message that OppositionUnits troop is deafeated
                        OppoProvince.removeUnit(OppoUnit);
                        OppositionUnits.remove(OppoUnit);
                        return "Opposition's"+OppoUnit.getType()+"has been defeated";
                    }
                    if(Break(OppoUnit, playersUnit, oppoCasualties, OppoStartNum, 0, playersUnit.getNumTroops())){
                        // return OppositionUnits unit broken
                        return routingRanged(OppoUnit, playersUnit, "OppositionUnits");
                    }

                    if(Break(playersUnit, OppoUnit, 0, playersUnit.getNumTroops(), oppoCasualties, OppoStartNum)){
                        // return PlayerUnits unit has broken
                        return routingRanged(playersUnit, OppoUnit, "PlayerUnits");
                    }

                }
            } else if(engagementType < (int) totalMelee){
                return MeleeSkirmish(playersUnit, OppoUnit);
            }
        } else if(!playersUnit.getIsRanged()){
            //OppositionUnits is ranged
            int speedMelee = OppoUnit.getSpeed();
            int speedRanged = OppoUnit.getSpeed();
            double extraMeleeChance = 0.1*(speedMelee - speedRanged);
            double totalMelee = 50 + extraMeleeChance;

            int engagementType = r.nextInt(100);
            if(engagementType >= (int) totalMelee){
                //its a ranged battle
                while(true){
                    int playerCasualties = PlayerRangeAttack(OppoUnit, playersUnit);
                    //OppositionUnits cant attack as they are melee
                    int playerStartNum = playersUnit.getNumTroops();
                    playersUnit.setNumTroops(playerStartNum - playerCasualties);
                    if(playersUnit.getNumTroops() == 0){
                        //return a message that players troop is deafeated
                        PlayerProvince.removeUnit(playersUnit);
                        PlayerUnits.remove(playersUnit);
                        return "Your" + playersUnit.getType() + "have been defeated";
                    }
                    if(Break(playersUnit, OppoUnit, playerCasualties, playerStartNum, 0, OppoUnit.getNumTroops())){
                        // return PlayerUnits unit broken
                        return routingRanged(playersUnit, OppoUnit, "PlayerUnits");
                    }

                    if(Break(OppoUnit, playersUnit, 0, OppoUnit.getNumTroops(), playerCasualties, playerStartNum)){
                        // return OppositionUnits unit has broken
                        return routingRanged(OppoUnit, playersUnit, "OppositionUnits");
                    }

                }
            } else if(engagementType < (int) totalMelee){
                //its a melee battle
                return MeleeSkirmish(playersUnit, OppoUnit);
            }
        }

        return null;
        
    }

    /**
     * Conducts a ranged attack by a unit
     * @param playersUnit the unit thats attacking
     * @param OppoUnit the unit that is getting attacked
     * @return the casualties
     */
    public int PlayerRangeAttack(Unitc playersUnit, Unitc OppoUnit){

        int UnitAttackPlayer = playersUnit.getAttack(); //playersUnit.getUnitRangeAttack
        int UnitEnemySize = OppoUnit.getNumTroops(); 
        int UnitEnemyArmour = OppoUnit.getArmour(); 
        int UnitEnemyShield = OppoUnit.getShieldDefense(); 

        Random ran = new Random();
        double N = ran.nextGaussian();

        double casualties = (UnitEnemySize*0.1)*(UnitAttackPlayer/(UnitEnemyArmour+UnitEnemyShield))*(N+1);

        if(casualties > OppoUnit.getNumTroops()){
            return OppoUnit.getNumTroops();
        } else if(casualties < 1 && casualties > 0){
            return 1;
        } else if(casualties < 0){
            return 0;
        }
        return (int)casualties;
    }
    
    /**
     * conducts a melee attack by a unit
     * @param playersUnit the attacking unit
     * @param OppoUnit the unit defending the attack
     * @return the casualties
     */
    public int PlayerMeleeAttack(Unitc playersUnit, Unitc OppoUnit){
        int UnitAttackPlayer = playersUnit.getAttack(); //playersUnit.getUnitMeleeAttack

        if(playersUnit.getSuperType().equals("cavalry")){
            UnitAttackPlayer += playersUnit.getCharge();
        }

        int UnitEnemySize = OppoUnit.getNumTroops();
        int UnitEnemyArmour = OppoUnit.getArmour(); 
        int UnitEnemyShield = OppoUnit.getShieldDefense(); 
        int UnitEnemyDefense = OppoUnit.getDefenseSkill(); 

        Random ran = new Random();
        double N = ran.nextGaussian();

        double casualties = (UnitEnemySize*0.1)*(UnitAttackPlayer/(UnitEnemyArmour+UnitEnemyShield+UnitEnemyDefense))*(N+1);

        if(casualties > OppoUnit.getNumTroops()){
            return OppoUnit.getNumTroops();
        } else if(casualties < 1 && casualties > 0){
            return 1;
        } else if(casualties < 0){
            return 0;
        }
        return (int)casualties;
    }

    /**
     * Conducts the routing battle in a ranged environment
     * @param fleeingUnit the unit that is tryign to route
     * @param chasingUnit the attacking unit
     * @param fleeing String of the fleeing unit
     * @return the result of the routing battle between units
     */
    public String routingRanged(Unitc fleeingUnit, Unitc chasingUnit, String fleeing){
        while(true){
            Random r = new Random();
            int routingChance = 50 + 10*(fleeingUnit.getSpeed() - chasingUnit.getSpeed());
            if(routingChance > 100){
                routingChance = 100;
            }
            int outcome = r.nextInt(100);
            
            if(outcome < routingChance){
                if(fleeing.equals("PlayerUnits")){
                    PlayerUnits.remove(fleeingUnit);
                    playerRouted.add(fleeingUnit);
                } else {
                    OppositionUnits.remove(fleeingUnit);
                    oppoRouted.add(fleeingUnit);
                }
                return fleeingUnit.getType()+"successfully routed";
            }

            int fleeingCasualties = PlayerRangeAttack(chasingUnit, fleeingUnit);

            int fleeingStartNum = fleeingUnit.getNumTroops();
            fleeingUnit.setNumTroops(fleeingStartNum - fleeingCasualties);

            if(fleeingUnit.getNumTroops() == 0){
                if(fleeing.equals("PlayerUnits")){
                    PlayerProvince.removeUnit(fleeingUnit);
                    PlayerUnits.remove(fleeingUnit);
                    return "Player's"+fleeingUnit.getType()+"has been defeated";
                } else {
                    OppoProvince.removeUnit(fleeingUnit);
                    OppositionUnits.remove(fleeingUnit);
                    return "Opposition's"+fleeingUnit.getType()+"has been defeated";
                }
            }
        }
    }

    /**
     * COnducts the routing battle in a melee environment
     * @param fleeingUnit the unit that is trying to route
     * @param chasingUnit the unit that is attacking
     * @param fleeing String of the fleeing unit
     * @return the result of the routing battle
     */
    public String routingMelee(Unitc fleeingUnit, Unitc chasingUnit, String fleeing){
        while(true){
            Random r = new Random();
            int routingChance = 50 + 10*(fleeingUnit.getSpeed() - chasingUnit.getSpeed());
            if(routingChance > 100){
                routingChance = 100;
            }
            int outcome = r.nextInt(100);
            
            if(outcome < routingChance){
                if(fleeing.equals("PlayerUnits")){
                    PlayerUnits.remove(fleeingUnit);
                    playerRouted.add(fleeingUnit);
                } else {
                    OppositionUnits.remove(fleeingUnit);
                    oppoRouted.add(fleeingUnit);
                }
                return fleeingUnit.getType()+"successfully routed";
            }

            int fleeingCasualties = PlayerMeleeAttack(chasingUnit, fleeingUnit);

            int fleeingStartNum = fleeingUnit.getNumTroops();
            fleeingUnit.setNumTroops(fleeingStartNum - fleeingCasualties);

            if(fleeingUnit.getNumTroops() == 0){
                if(fleeing.equals("PlayerUnits")){
                    PlayerProvince.removeUnit(fleeingUnit);
                    PlayerUnits.remove(fleeingUnit);
                    return "Player's"+fleeingUnit.getType()+"has been defeated";
                } else {
                    OppoProvince.removeUnit(fleeingUnit);
                    OppositionUnits.remove(fleeingUnit);
                    return "Opposition's"+fleeingUnit.getType()+"has been defeated";
                }
            }
        }
    }

    /**
     * COnduct a melee skirmish between 2 units
     * @param playersUnit the current players unit
     * @param OppoUnit the oppositions unit
     * @return String of the result of the skirmish
     */
    public String MeleeSkirmish(Unitc playersUnit, Unitc OppoUnit){
        while(true){
            int oppoCasualties = PlayerMeleeAttack(playersUnit, OppoUnit);
            int playerCasualties = PlayerMeleeAttack(OppoUnit, playersUnit);
            
            int OppoStartNum = OppoUnit.getNumTroops();
            int playerStartNum = playersUnit.getNumTroops();
            OppoUnit.setNumTroops(OppoStartNum - oppoCasualties);
            playersUnit.setNumTroops(playerStartNum - playerCasualties);

            if(OppoUnit.getNumTroops() == 0 && playersUnit.getNumTroops() == 0){
                //return a message that OppositionUnits troop is deafeated
                OppoProvince.removeUnit(OppoUnit);
                OppositionUnits.remove(OppoUnit);
                PlayerProvince.removeUnit(playersUnit);
                PlayerUnits.remove(playersUnit);
                return "Wiped out both units";
            } else if(OppoUnit.getNumTroops() == 0){
                //return a message that OppositionUnits troop is deafeated
                OppoProvince.removeUnit(OppoUnit);
                OppositionUnits.remove(OppoUnit);
                return "Opposition's"+OppoUnit.getType()+"has been defeated";
            } else if(playersUnit.getNumTroops() == 0){
                //return a message that players troop is deafeated
                PlayerProvince.removeUnit(playersUnit);
                PlayerUnits.remove(playersUnit);
                return "Player's"+playersUnit.getType()+"has been defeated";
            }

            if(Break(OppoUnit, playersUnit, oppoCasualties, OppoStartNum, playerCasualties, playerStartNum)){
                // return OppositionUnits unit broken
                return routingMelee(OppoUnit, playersUnit, "OppositionUnits");
            }

            if(Break(playersUnit, OppoUnit, playerCasualties, playerStartNum, oppoCasualties, OppoStartNum)){
                // return PlayerUnits unit has broken
                return routingMelee(playersUnit, OppoUnit, "PlayerUnits");
            }
        }
    }

    /**
     * Checks if the oppositions unit has broken
     * @param OppoUnit the unit that is being broken
     * @param playersUnit the unit that is attacking
     * @param oppoCasualties the casualties of the unit being broken
     * @param OppoStartNum the number on troops in the unit being broken before casualties
     * @param playersCasulaties the casualties of the unit attacking
     * @param playersStartNum the number of troops in the unit attacking before casualties
     * @return true if the unit is broken 
     */
    public boolean Break(Unitc OppoUnit, Unitc playersUnit, int oppoCasualties, int OppoStartNum, int playersCasulaties, int playersStartNum){
        int OppoMorale = OppoUnit.getMorale();
        int initialBreak = 100 - (OppoMorale * 10);
        double addedBreak = 0;
        if(playersCasulaties != 0 && oppoCasualties != 0){
            addedBreak = (oppoCasualties/OppoStartNum)/(playersCasulaties/playersStartNum)*10;
        }
        double OppoBreak = initialBreak + addedBreak;

        if(OppoBreak > 100){
            OppoBreak = 100;
        } else if(OppoBreak < 5){
            OppoBreak = 5;
        }
        Random r = new Random();
        int Unitbreak = r.nextInt(100);
        if(Unitbreak < OppoBreak){
            return true;
        }
        return false;
    }

    /**
     * getter method that retrieves the list of the units routed from the players unit
     * @return the list of units routed
     */
    public List<Unitc> getPlayerRouted(){
        return playerRouted;
    }

    /**
     * getter method that retrieves the list of units routed from the oppositions unit
     * @return the list of units routed
     */
    public List<Unitc> getOppoRouted(){
        return oppoRouted;
    }

    /**
     * getter method that retrieves the list of units that the player gives in to attack the province
     * @return the list of units
     */
    public List<Unitc> getPlayerUnits(){
        return PlayerUnits;
    }

    /**
     * getter method that retrieves the list of units that the opposition has to defend the province
     * @return the list of units
     */
    public List<Unitc> getOppoUnits(){
        return OppositionUnits;
    }
}
