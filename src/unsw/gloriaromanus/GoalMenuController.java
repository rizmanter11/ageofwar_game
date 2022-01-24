package unsw.gloriaromanus;

import java.io.IOException;
import java.net.URL;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class GoalMenuController extends MenuController{
    @FXML
    private CheckBox wealth;

    @FXML
    private CheckBox treasury;

    @FXML
    private CheckBox prov;

    @FXML
    private CheckBox and;

    @FXML
    private CheckBox or;

    // https://stackoverflow.com/a/30171444
    @FXML
    private URL location; // has to be called location

    public void checked(ActionEvent e) throws Exception{
        if(wealth.isSelected()){
            getParent().setGoal("wealth");
        } else if(treasury.isSelected()){
            getParent().setGoal("treasury");
        } else if(prov.isSelected()){
            getParent().setGoal("conquest");
        }
    }

    public void conjunctionChecked(ActionEvent e) throws Exception{
        if(and.isSelected()){
            getParent().setConjunction("and");
        } else if(or.isSelected()){
            getParent().setConjunction("or");
        }
    }

    public void chosenGoals(ActionEvent e) throws Exception{
        getParent().switchMenu(this);
    }
}
