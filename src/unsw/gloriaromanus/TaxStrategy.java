package unsw.gloriaromanus;

import java.io.Serializable;

public interface TaxStrategy {
    public void collectTax(Province province, Player player);
}
