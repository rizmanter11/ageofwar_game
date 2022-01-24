package unsw.gloriaromanus;

import java.io.Serializable;

public class HighTax implements TaxStrategy, Serializable{
    private static final long serialVersionUID = 1234567L;
    @Override
    public void collectTax(Province province, Player player){
        int wealth = province.getWealth();
        double tax = wealth * 0.2;
        double newTreasury = player.getTreasury() + tax;
        player.setTreasury((int)newTreasury);
        int newWealth = province.getWealth() - 10;
        province.setWealth(newWealth);
    }
}
