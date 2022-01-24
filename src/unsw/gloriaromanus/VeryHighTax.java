package unsw.gloriaromanus;

import java.io.Serializable;
import java.util.List;

public class VeryHighTax implements TaxStrategy, Serializable{
    private static final long serialVersionUID = 1234567L;
    @Override
    public void collectTax(Province province, Player player){
        int wealth = province.getWealth();
        double tax = wealth * 0.25;
        double newTreasury = player.getTreasury() + tax;
        player.setTreasury((int)newTreasury);
        int newWealth = province.getWealth() - 30;
        province.setWealth(newWealth);
        //decrease morale of all soldiers in the province by 1
        decreaseMorale(province);
    }

    public void decreaseMorale(Province province){
        List<Unitc> troops = province.getUnits();
        for(Unitc u: troops){
            int newMorale = u.getMorale() - 1;
            if(newMorale < 0){
                newMorale = 0;
            }
            u.setMorale(newMorale);
        }
    }
}
