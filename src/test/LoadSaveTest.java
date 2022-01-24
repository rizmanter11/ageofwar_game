package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import unsw.gloriaromanus.*;

public class LoadSaveTest{
    String[] provinceList1 = {"Lugdunensis", "Lusitania", "Lycia et Pamphylia", "Macedonia", "Mauretania Caesariensis","Mauretania Tingitana", "Moesia Inferior"};
    String[] provinceList2 = {"Moesia Superior","Narbonensis","Noricum","Numidia","Pannonia Inferior", "Pannonia Superior", "Raetia","Sardinia et Corsica","Sicilia", "Syria", "Tarraconensis", "Thracia", "V","VI","VII", "VIII","X", "XI"};
    List <Province> provinces1 = new ArrayList<>();
    List <Province> provinces2 = new ArrayList<>();

    public void initialiseProvince(String[] provinceList, List<Province> provinces){
        for(String p: provinceList){
            Province newP = new Province(p);
            //add units to province
            provinces.add(newP);
        }
    }

    @Test
    public void simpleLoadSave(){
        initialiseProvince(provinceList1, provinces1);
        initialiseProvince(provinceList2, provinces2);

        Player player1 = new Player("rome", provinces1);
        
        player1.saveData();

        Player player1again = new Player("gaul", provinces2);
        player1again.loadData();

        assertEquals(player1.getVictoryGoals(), player1again.getVictoryGoals());
        assertEquals(player1.getFaction(), player1again.getFaction());
        List<Province> originalProvinces = player1.getProvinces();
        List<Province> loadedProvinces = player1again.getProvinces();
        for(int i = 0; i < player1.getProvinces().size(); i++){
            Province province1 = originalProvinces.get(i);
            Province province2 = loadedProvinces.get(i);

            assertEquals(province1.getName(), province2.getName());
        }
    }

    @Test

    public void moreLoadSave(){
        initialiseProvince(provinceList1, provinces1);
        initialiseProvince(provinceList2, provinces2);

        Player player1 = new Player("rome", provinces1);
        List<Province> playerProvinces = player1.getProvinces();
        for(int turn = 1; turn <= 4; turn++){
            if(turn == 1){
                for(Province prov: playerProvinces){
                    prov.recruit("skirmisher", 3, turn, player1);
                    prov.recruit("heavy infantry", 4, turn, player1);
                }
            }
            for (Province prov: playerProvinces){
                prov.recruitReady(turn);
            }
        }
        
        player1.saveData();

        Player player1again = new Player("gaul", provinces2);
        player1again.loadData();

        assertEquals(player1.getVictoryGoals(), player1again.getVictoryGoals());
        assertEquals(player1.getFaction(), player1again.getFaction());
        assertEquals(player1.getVictoryGoals(), player1again.getVictoryGoals());
        assertEquals(player1.getTreasury(), player1again.getTreasury());
        
        List<Province> originalProvinces = player1.getProvinces();
        List<Province> loadedProvinces = player1again.getProvinces();
        for(int i = 0; i < player1.getProvinces().size(); i++){
            Province province1 = originalProvinces.get(i);
            Province province2 = loadedProvinces.get(i);

            assertEquals(province1.getName(), province2.getName());
            List<Unitc> originalUnits = province1.getUnits();
            List<Unitc> loadedUnits = province2.getUnits();
            for(int j = 0; j < originalUnits.size(); j++){
                Unitc unit1 = originalUnits.get(j);
                Unitc unit2 = loadedUnits.get(j);

                assertEquals(unit1.getType(), unit2.getType());
                assertEquals(unit1.getSuperType(), unit2.getSuperType());
                assertEquals(unit1.getArmour(), unit2.getArmour());
                assertEquals(unit1.getAttack(), unit2.getAttack());
                assertEquals(unit1.getCharge(), unit2.getCharge());
                assertEquals(unit1.getDefenseSkill(), unit2.getDefenseSkill());
                assertEquals(unit1.getSpeed(), unit2.getSpeed());
            }
        }
    }
}