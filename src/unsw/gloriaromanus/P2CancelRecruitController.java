package unsw.gloriaromanus;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;

import com.esri.arcgisruntime.data.Feature;

public class P2CancelRecruitController extends MenuController {

    @FXML
    private Button canceltf;

    @FXML
    private Button donebtn;

    @FXML
    private TextField unittf;

    @FXML
    private TextArea labelid;

    @FXML
    private TextArea outputta;

    // https://stackoverflow.com/a/30171444
    @FXML
    private URL location; // has to be called location

    public void showfn(){
        Player player2 = getParent().getPlayer2();
        Feature currentlySelectedEnemyProvince = getParent().getCurrentlySelectedEnemyProvince();
        if (currentlySelectedEnemyProvince == null){
            System.out.println("Go back and select a province for player2");
            outputta.setText("Go back and select a province for player2");
            return;
        }
        Province p = player2.findProvince((String)currentlySelectedEnemyProvince.getAttributes().get("name")); 
        System.out.println("turn = "+p.getTobs().getTurn());
        String units = "Pending units are: ";
        for (pendingUnit u : p.getPendingUnits()){
            units = units+u.getUnit().getType()+", ";
    
        }
        labelid.setText(units);
    }

    public void cancelRecruit (ActionEvent event){
        String selectedItem = unittf.getText();
        Player player2 = getParent().getPlayer2();
        Feature currentlySelectedEnemyProvince = getParent().getCurrentlySelectedEnemyProvince();
        Province p = player2.findProvince((String)currentlySelectedEnemyProvince.getAttributes().get("name")); 
        Iterator<pendingUnit> pend = p.getPendingUnits().iterator();
        while (pend.hasNext()){
            pendingUnit u = pend.next();
            if (u.getUnit().getType().equals(selectedItem)){
                int cost = u.getUnit().getCost();
                player2.setTreasury(player2.getTreasury() + cost / 2);
                pend.remove();
                System.out.println("Unit in training is removed");
                outputta.setText(selectedItem+" unit is now removed from training");
                return;
            }
        }
        outputta.setText(selectedItem+" is an invalid unit. Please select a unit from the pending units above to cancel training that unit");
    }

    @FXML
    public void clickedCancel(ActionEvent e) throws IOException{
        getParent().cancelSelect(this);
    }   
    
}
