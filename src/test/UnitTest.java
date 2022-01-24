package test;

//import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import unsw.gloriaromanus.*;

public class UnitTest{
    
    @Test
    public void blahTest(){
        assertEquals("a", "a");
    }
    
    @Test
    public void blahTest2(){
        Unit u = new Unit();
        assertEquals(u.getNumTroops(), 50);
    }


    public Player setup(){
        List<Province> provinceList = new ArrayList<>();
        Province pro1 = new Province("Lugdunensis");
        Province pro2 = new Province("Lusitania");
        Province pro3 = new Province("Macedonia");
        provinceList.add(pro1);
        provinceList.add(pro2);
        provinceList.add(pro3);
        return new Player ("Rome", provinceList);

    }

    @Test
    public void recruiting(){
        Player p = setup();
        List<Province> playerProvinces = p.getProvinces();
        p.setTreasury(5000);
        for (int turn = 1; turn <= 4; turn++){
            if (turn == 1){
                for (Province pro : playerProvinces){
                    pro.recruit("skirmisher", 3, turn, p);
                    pro.recruit("lancer", 4, turn, p);
                }
            }
            for (Province pro : playerProvinces){
                pro.recruitReady(turn);
                assertEquals(2, pro.getPendingUnits().size());
                if (turn == 1){                    
                    assertEquals(0, pro.getUnits().size());
                }else if (turn == 2){
                    assertEquals(1, pro.getUnits().size());
                    assertEquals("skirmisher", pro.getUnits().get(0).getType());
                    assertEquals( 3, pro.getUnits().get(0).getNumTroops());
                }else if (turn == 3){
                    assertEquals(2, pro.getUnits().size());
                    assertEquals("lancer", pro.getUnits().get(1).getType());
                    assertEquals(4, pro.getUnits().get(1).getNumTroops());
                }else if (turn == 4){
                    assertEquals(2, pro.getUnits().size());
                }
            }
                        
        }
    }

    @Test
    public void recruitingSame(){
        Player p = setup();
        List<Province> playerProvinces = p.getProvinces();
        p.setTreasury(5000);
        for (int turn = 1; turn <= 4; turn++){
            if (turn == 1){
                for (Province pro : playerProvinces){
                    pro.recruit("skirmisher", 3, turn, p);
                    pro.recruit("skirmisher", 4, turn, p);
                }
            }
            for (Province pro : playerProvinces){
                pro.recruitReady(turn);
                assertEquals(2, pro.getPendingUnits().size());
                if (turn == 1){                    
                    assertEquals(0, pro.getUnits().size());
                }else if (turn == 2){
                    assertEquals(1, pro.getUnits().size());
                    assertEquals("skirmisher", pro.getUnits().get(0).getType());
                    assertEquals( 7, pro.getUnits().get(0).getNumTroops());
                }else if (turn == 3){
                    assertEquals(1, pro.getUnits().size());
                    assertEquals( 7, pro.getUnits().get(0).getNumTroops());

                }
            }
                        
        }
    }

    @Test
    public void goal(){
        Player p = setup();
        p.generateVictoryGoals();
        assertEquals((p.isConquestGoal() || p.isTreasuryGoal() || p.isWealthGoal()), true); 
        if(p.isTreasuryGoal()){
            p.setTreasury(100000);
            assertEquals(p.treasuryGoalReached(), true);
        } else if(p.isWealthGoal()){
            for(Province prov: p.getProvinces()){
                prov.setWealth(400000);
            }
            assertEquals(p.wealthGoalReached(), true);
        }
    }

    @Test
    public void lowTaxRate(){
        Player p = setup();
        Province pro = p.getProvinces().get(0);
        LowTax low = new LowTax();
        for (int turn = 0; turn < 6; turn++){
            low.collectTax(pro, p);
            if (turn == 0){
                assertEquals(0, p.getTreasury());
                assertEquals(10, pro.getWealth());
            }else if (turn == 1){
                assertEquals(1, p.getTreasury());
                assertEquals(20, pro.getWealth());
            }else if (turn == 2){
                assertEquals(3, p.getTreasury());
                assertEquals(30, pro.getWealth());
            }else if (turn == 5){
                assertEquals(15, p.getTreasury());
                assertEquals(60, pro.getWealth());
            }
        }     
        assertEquals(60, p.totalWealth());   
    }

    @Test
    public void NormalTaxRate(){
        Player p = setup();
        Province pro = p.getProvinces().get(0);
        normalTax normal = new normalTax();
        for (int turn = 0; turn <= 2; turn++){
            normal.collectTax(pro, p);
            if (turn == 0){
                assertEquals(0, p.getTreasury());
                assertEquals(0, pro.getWealth());
            }
            pro.setWealth(100);
            if (turn == 1){
                assertEquals(15, p.getTreasury());
                assertEquals(100, pro.getWealth());
            }else if (turn == 2){
                assertEquals(30, p.getTreasury());
                assertEquals(100, pro.getWealth());
            }
        }   
    }

    @Test
    public void HighTaxRate(){
        Player p = setup();
        Province pro = p.getProvinces().get(0);
        HighTax high = new HighTax();
        pro.setWealth(100);
        for (int turn = 0; turn <= 2; turn++){
            high.collectTax(pro, p);
            if (turn == 0){
                assertEquals(20, p.getTreasury());
                assertEquals(90, pro.getWealth());
            }else if (turn == 1){
                assertEquals(38, p.getTreasury());
                assertEquals(80, pro.getWealth());
            }else if (turn == 2){
                assertEquals(54, p.getTreasury());
                assertEquals(70, pro.getWealth());
            }
        }  
    }
    @Test
    public void VeryHighTax(){
        Player p = setup();
        Province pro = p.getProvinces().get(0);
        p.setTreasury(15);
        pro.setWealth(100);
        VeryHighTax vhigh = new VeryHighTax();
        for (int turn = 0; turn <= 2; turn++){
            vhigh.collectTax(pro, p);
            if (turn == 0){
                pro.recruit("skirmisher", 3, turn, p);
                assertEquals(25, p.getTreasury());
                assertEquals(70, pro.getWealth());
            }else if (turn == 1){
                assertEquals(42, p.getTreasury());
                assertEquals(40, pro.getWealth());
            }else if (turn == 2){
                assertEquals(52, p.getTreasury());
                assertEquals(10, pro.getWealth());
            }
            pro.recruitReady(turn);
        }  
    }

    @Test

    public void movement(){
        Player p = setup();
        Province pro = p.getProvinces().get(0);
        Province pro1 = p.getProvinces().get(1);
        p.setTreasury(50000);
        for (int turn = 0; turn <= 4; turn++){
            if (turn == 0){
                pro.recruit("skirmisher", 3, turn, p);
            }else if (turn == 1){
                pro.recruitReady(turn);
                assertEquals(1, pro.getUnits().size());
                p.moveTroops(pro.getName(),  pro1.getName(), pro.getUnits().get(0).getType());
                assertEquals(1, pro1.getUnits().size());
            }

        }
    }
}

