package unsw.gloriaromanus;

import java.io.Serializable;

public class normalTax implements TaxStrategy, Serializable{
    private static final long serialVersionUID = 1234567L;
    @Override
    public void collectTax(Province province, Player player){
        int wealth = province.getWealth();
        double tax = wealth * 0.15;
        double newTreasury = player.getTreasury() + tax;
        player.setTreasury((int)newTreasury);
    }
}
