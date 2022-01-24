package unsw.gloriaromanus;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class MoveTroopsController extends MenuController{
    
    @FXML
    private TextField destinationtf;

    @FXML
    private TextField unittf; 

    @FXML
    private Button enterbttn;

    private Player player = getParent().getPlayer1(); //getting player1 TODO make work for both players

    public boolean isDestinationValid(String destination){
        if (player.findProvince(destination) == null){ return false;}
        return true; 
    }

    public boolean isUnitValid(String unit){
        Province p = player.findProvince((String) getParent().getCurrentlySelectedHumanProvince().getAttributes().get("name")); //sus        
        if (p.FindUnit(p.getName()) == null){ return false;}
        return true;
    }

    void moveTroops(ActionEvent event){ 
        Province playerProv = player.findProvince((String) getParent().getCurrentlySelectedHumanProvince().getAttributes().get("name")); //sus        

        if (isDestinationValid(destinationtf.getText()) && isUnitValid(unittf.getText())){
            player.moveTroops(playerProv.getName(), destinationtf.getText(), unittf.getText());
            System.out.println("Troops moved (printstatement to show it worked)");
        }
    }
}
