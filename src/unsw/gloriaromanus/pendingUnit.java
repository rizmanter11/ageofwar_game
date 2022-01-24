package unsw.gloriaromanus;

import java.io.Serializable;

public class pendingUnit implements Serializable{
    private Unitc unit;
    private int isAvailable; //on which turn unit is available

    public pendingUnit(Unitc unit, int available){
        this.unit = unit;
        this.isAvailable = available;
    }

    public Unitc getUnit() {
        return unit;
    }

    public void setUnit(Unitc unit) {
        this.unit = unit;
    }

    public int getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(int isAvailable) {
        this.isAvailable = isAvailable;
    }
    
}
