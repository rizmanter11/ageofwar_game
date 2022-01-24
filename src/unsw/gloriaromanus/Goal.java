package unsw.gloriaromanus;

import java.io.Serializable;

public class Goal implements GoalComponent, Serializable{
    private String goal;
    private static final long serialVersionUID = 1234567L;
    
    public Goal(String goal){
        this.goal = goal;
    }

    public String getGoal(){
        return goal;
    }

    @Override
    public String getStringComponent(){
        return "{\"goal:\"" + goal + "}";
    }
}
