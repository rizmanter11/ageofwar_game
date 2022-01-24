package unsw.gloriaromanus;

import java.io.Serializable;
import java.util.ArrayList;

public class GoalConjunction implements GoalComponent, Serializable{
    ArrayList<GoalComponent> subgoals = new ArrayList<GoalComponent>();
    private static final long serialVersionUID = 1234567L;
    String goal;
    
    public GoalConjunction(String goal){
        this.goal = goal;
    }

    public void addGoal(GoalComponent subgoal){
        subgoals.add(subgoal);
    }

    public String getGoal(){
        return goal;
    }

    @Override
    public String getStringComponent(){
        String answer = "{\"goal:\"" + goal + ",\"subgoals:\"[";
        for(GoalComponent gc: subgoals){
            answer = answer + gc.getStringComponent()+",";
        }
        answer = answer + "]}";
        return answer;
    }

}

