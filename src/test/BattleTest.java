package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import unsw.gloriaromanus.*;

public class BattleTest {
    String[] provinceList1 = {"Lugdunensis", "Lusitania", "Lycia et Pamphylia", "Macedonia", "Mauretania Caesariensis","Mauretania Tingitana", "Moesia Inferior"};
    String[] provinceList2 = {"Moesia Superior","Narbonensis","Noricum","Numidia","Pannonia Inferior", "Pannonia Superior", "Raetia","Sardinia et Corsica","Sicilia", "Syria", "Tarraconensis", "Thracia", "V","VI","VII", "VIII","X", "XI"};
    List <Province> provinces1 = new ArrayList<>();
    List <Province> provinces2 = new ArrayList<>();

    public void intialiseProvince(String[] provinceList, List<Province> provinces){
        for(String p: provinceList){
            Province newP = new Province(p);
            //add units to province
            provinces.add(newP);
        }
    }

    @Test
    public void basicBattleMelee(){
        intialiseProvince(provinceList1, provinces1);
        intialiseProvince(provinceList2, provinces2);

        
        Player player1 = new Player("rome", provinces1);
        player1.setTreasury(100000);
        List<Province> playerProvinces = player1.getProvinces();
        for(int turn = 1; turn <= 4; turn++){
            if(turn == 1){
                for(Province prov: playerProvinces){
                    prov.recruit("skirmisher", 300, turn, player1);
                    prov.recruit("heavy infantry", 400, turn, player1);
                }
            }
            for (Province prov: playerProvinces){
                prov.recruitReady(turn);
            }
        }

        Player player2 = new Player("gaul", provinces2);
        player2.setTreasury(100000);
        List<Province> playerProvinces2 = player2.getProvinces();
        for(int turn = 1; turn <= 4; turn++){
            if(turn == 1){
                for(Province prov: playerProvinces2){
                    prov.recruit("skirmisher", 300, turn, player2);
                    prov.recruit("heavy infantry", 400, turn, player2);
                }
            }
            for (Province prov: playerProvinces2){
                prov.recruitReady(turn);
            }
        }

        int player1ProvSize = player1.getProvinces().size();
        int player2ProvSize = player2.getProvinces().size();

        Province PlayerProvince = playerProvinces.get(0);
        Province OppoProvince = playerProvinces2.get(0);
        List<Unitc> Play1Units = PlayerProvince.getUnits();
        List<Unitc> Play2Units = OppoProvince.getUnits();
        List<Unitc> PlayerUnits = new ArrayList<Unitc>();
        List<Unitc> OppositionUnits = new ArrayList<Unitc>();

        PlayerUnits.add(Play1Units.get(1));
        OppositionUnits.add(Play2Units.get(0));

        Attack Battle = new Attack(PlayerUnits, OppositionUnits, PlayerProvince, OppoProvince, player1, player2);
        Battle.battle();

        if(Battle.getPlayerRouted().size() == 0 && Battle.getOppoRouted().size() == 0){
            assertEquals(Battle.getOppoUnits().size(), 0);
        } else if(Battle.getPlayerRouted().size() == 1){
            assertEquals(Battle.getPlayerUnits().size(), 0);
            assertEquals(PlayerUnits.size(), 0);
            assertEquals(player1.getProvinces().size(), player1ProvSize);
        } else if(Battle.getOppoRouted().size() == 1){
            assertEquals(Battle.getOppoUnits().size(), 0);
            assertEquals(OppositionUnits.size(), 0);
            assertEquals(player2.getProvinces().size(), (player2ProvSize - 1));
        }
        //assertEquals(); //make sure the losing troop has 0 units left 
    }

    @Test
    public void basicRangedBattle(){
        intialiseProvince(provinceList1, provinces1);
        intialiseProvince(provinceList2, provinces2);

        
        Player player1 = new Player("rome", provinces1);
        player1.setTreasury(100000);
        List<Province> playerProvinces = player1.getProvinces();
        for(int turn = 1; turn <= 4; turn++){
            if(turn == 1){
                for(Province prov: playerProvinces){
                    prov.recruit("archer", 600, turn, player1);
                }
            }
            for (Province prov: playerProvinces){
                prov.recruitReady(turn);
            }
        }

        Player player2 = new Player("gaul", provinces2);
        player2.setTreasury(100000);
        List<Province> playerProvinces2 = player2.getProvinces();
        for(int turn = 1; turn <= 4; turn++){
            if(turn == 1){
                for(Province prov: playerProvinces2){
                    prov.recruit("onager", 30, turn, player2);
                }
            }
            for (Province prov: playerProvinces2){
                prov.recruitReady(turn);
            }
        }

        int player1ProvSize = player1.getProvinces().size();
        int player2ProvSize = player2.getProvinces().size();

        Province PlayerProvince = playerProvinces.get(0);
        Province OppoProvince = playerProvinces2.get(0);
        List<Unitc> Play1Units = PlayerProvince.getUnits();
        List<Unitc> Play2Units = OppoProvince.getUnits();
        List<Unitc> PlayerUnits = new ArrayList<Unitc>();
        List<Unitc> OppositionUnits = new ArrayList<Unitc>();

        PlayerUnits.add(Play1Units.get(0));
        OppositionUnits.add(Play2Units.get(0));

        Attack Battle = new Attack(PlayerUnits, OppositionUnits, PlayerProvince, OppoProvince, player1, player2);
        Battle.battle();

        if(Battle.getPlayerRouted().size() == 0 && Battle.getOppoRouted().size() == 0){
            assertEquals(Battle.getOppoUnits().size(), 0);
        } else if(Battle.getPlayerRouted().size() == 1){
            assertEquals(Battle.getPlayerUnits().size(), 0);
            assertEquals(PlayerUnits.size(), 0);
            assertEquals(player1.getProvinces().size(), player1ProvSize);
        } else if(Battle.getOppoRouted().size() == 1){
            assertEquals(Battle.getOppoUnits().size(), 0);
            assertEquals(OppositionUnits.size(), 0);
            assertEquals(player2.getProvinces().size(), (player2ProvSize - 1));
        }
    }

    @Test
    public void meleeRangedBattle(){

        intialiseProvince(provinceList1, provinces1);
        intialiseProvince(provinceList2, provinces2);

        
        Player player1 = new Player("rome", provinces1);
        player1.setTreasury(100000);
        List<Province> playerProvinces = player1.getProvinces();
        for(int turn = 1; turn <= 4; turn++){
            if(turn == 1){
                for(Province prov: playerProvinces){
                    prov.recruit("slinger", 50, turn, player1);
                }
            }
            for (Province prov: playerProvinces){
                prov.recruitReady(turn);
            }
        }

        Player player2 = new Player("gaul", provinces2);
        player2.setTreasury(100000);
        List<Province> playerProvinces2 = player2.getProvinces();
        for(int turn = 1; turn <= 4; turn++){
            if(turn == 1){
                for(Province prov: playerProvinces2){
                    prov.recruit("skirmisher", 200, turn, player2);
                }
            }
            for (Province prov: playerProvinces2){
                prov.recruitReady(turn);
            }
        }

        int player1ProvSize = player1.getProvinces().size();
        int player2ProvSize = player2.getProvinces().size();

        Province PlayerProvince = playerProvinces.get(0);
        Province OppoProvince = playerProvinces2.get(0);
        List<Unitc> Play1Units = PlayerProvince.getUnits();
        List<Unitc> Play2Units = OppoProvince.getUnits();
        List<Unitc> PlayerUnits = new ArrayList<Unitc>();
        List<Unitc> OppositionUnits = new ArrayList<Unitc>();

        PlayerUnits.add(Play1Units.get(0));
        OppositionUnits.add(Play2Units.get(0));

        Attack Battle = new Attack(PlayerUnits, OppositionUnits, PlayerProvince, OppoProvince, player1, player2);
        Battle.battle();

        if(Battle.getPlayerRouted().size() == 0 && Battle.getOppoRouted().size() == 0){
            assertEquals(Battle.getOppoUnits().size(), 0);
        } else if(Battle.getPlayerRouted().size() == 1){
            assertEquals(Battle.getPlayerUnits().size(), 0);
            assertEquals(PlayerUnits.size(), 0);
            assertEquals(player1.getProvinces().size(), player1ProvSize);
        } else if(Battle.getOppoRouted().size() == 1){
            assertEquals(Battle.getOppoUnits().size(), 0);
            assertEquals(OppositionUnits.size(), 0);
            assertEquals(player2.getProvinces().size(), (player2ProvSize - 1));
        }
        //assertEquals(); //make sure the losing troop has 0 units left
    }

    @Test
    public void sameUnitBattle(){
        //test with multipole units passed in for each player
        intialiseProvince(provinceList1, provinces1);
        intialiseProvince(provinceList2, provinces2);

        
        Player player1 = new Player("rome", provinces1);
        player1.setTreasury(100000);
        List<Province> playerProvinces = player1.getProvinces();
        for(int turn = 1; turn <= 4; turn++){
            if(turn == 1){
                for(Province prov: playerProvinces){
                    prov.recruit("elephant", 300, turn, player1);
                }
            }
            for (Province prov: playerProvinces){
                prov.recruitReady(turn);
            }
        }

        Player player2 = new Player("gaul", provinces2);
        player2.setTreasury(100000);
        List<Province> playerProvinces2 = player2.getProvinces();
        for(int turn = 1; turn <= 4; turn++){
            if(turn == 1){
                for(Province prov: playerProvinces2){
                    prov.recruit("elephant", 300, turn, player2);
                }
            }
            for (Province prov: playerProvinces2){
                prov.recruitReady(turn);
            }
        }

        int player1ProvSize = player1.getProvinces().size();
        int player2ProvSize = player2.getProvinces().size();

        Province PlayerProvince = playerProvinces.get(0);
        Province OppoProvince = playerProvinces2.get(0);
        List<Unitc> Play1Units = PlayerProvince.getUnits();
        List<Unitc> Play2Units = OppoProvince.getUnits();
        List<Unitc> PlayerUnits = new ArrayList<Unitc>();
        List<Unitc> OppositionUnits = new ArrayList<Unitc>();

        PlayerUnits.add(Play1Units.get(0));
        OppositionUnits.add(Play2Units.get(0));

        Attack Battle = new Attack(PlayerUnits, OppositionUnits, PlayerProvince, OppoProvince, player1, player2);
        Battle.battle();

        if(Battle.getPlayerRouted().size() == 0 && Battle.getOppoRouted().size() == 0){
            assertEquals(Battle.getOppoUnits().size(), 0);
        } else if(Battle.getPlayerRouted().size() == 1){
            assertEquals(Battle.getPlayerUnits().size(), 0);
            assertEquals(PlayerUnits.size(), 0);
            assertEquals(player1.getProvinces().size(), player1ProvSize);
        } else if(Battle.getOppoRouted().size() == 1){
            assertEquals(Battle.getOppoUnits().size(), 0);
            assertEquals(OppositionUnits.size(), 0);
            assertEquals(player2.getProvinces().size(), (player2ProvSize - 1));
        }

        //assertEquals(); //make sure the losing troop has 0 units left
    }

    @Test
    public void rangedMeleeBattle(){
        //test with multipole units passed in for each player
        intialiseProvince(provinceList1, provinces1);
        intialiseProvince(provinceList2, provinces2);

        
        Player player1 = new Player("rome", provinces1);
        player1.setTreasury(100000);
        List<Province> playerProvinces = player1.getProvinces();
        for(int turn = 1; turn <= 4; turn++){
            if(turn == 1){
                for(Province prov: playerProvinces){
                    prov.recruit("beserker", 500, turn, player1);
                }
            }
            for (Province prov: playerProvinces){
                prov.recruitReady(turn);
            }
        }

        Player player2 = new Player("gaul", provinces2);
        player2.setTreasury(100000);
        List<Province> playerProvinces2 = player2.getProvinces();
        for(int turn = 1; turn <= 4; turn++){
            if(turn == 1){
                for(Province prov: playerProvinces2){
                    prov.recruit("druid", 300, turn, player2);
                }
            }
            for (Province prov: playerProvinces2){
                prov.recruitReady(turn);
            }
        }

        int player1ProvSize = player1.getProvinces().size();
        int player2ProvSize = player2.getProvinces().size();

        Province PlayerProvince = playerProvinces.get(0);
        Province OppoProvince = playerProvinces2.get(0);
        List<Unitc> Play1Units = PlayerProvince.getUnits();
        List<Unitc> Play2Units = OppoProvince.getUnits();
        List<Unitc> PlayerUnits = new ArrayList<Unitc>();
        List<Unitc> OppositionUnits = new ArrayList<Unitc>();

        PlayerUnits.add(Play1Units.get(0));
        OppositionUnits.add(Play2Units.get(0));

        Attack Battle = new Attack(PlayerUnits, OppositionUnits, PlayerProvince, OppoProvince, player1, player2);
        Battle.battle();

        if(Battle.getPlayerRouted().size() == 0 && Battle.getOppoRouted().size() == 0){
            assertEquals(Battle.getOppoUnits().size(), 0);
        } else if(Battle.getPlayerRouted().size() == 1){
            assertEquals(Battle.getPlayerUnits().size(), 0);
            assertEquals(PlayerUnits.size(), 0);
            assertEquals(player1.getProvinces().size(), player1ProvSize);
        } else if(Battle.getOppoRouted().size() == 1){
            assertEquals(Battle.getOppoUnits().size(), 0);
            assertEquals(OppositionUnits.size(), 0);
            assertEquals(player2.getProvinces().size(), (player2ProvSize - 1));
        }

        //assertEquals(); //make sure the losing troop has 0 units left
    }
}

