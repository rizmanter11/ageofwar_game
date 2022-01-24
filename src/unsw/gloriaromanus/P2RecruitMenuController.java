package unsw.gloriaromanus;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;

import com.esri.arcgisruntime.data.Feature;

import org.json.JSONObject;

public class P2RecruitMenuController extends MenuController {
  @FXML
  private TextField soldiertf2;

  @FXML
  private TextField numberofsoldtf2;

  @FXML
  private Button enterbtn2;

  @FXML
  private TextArea outputta;

  // https://stackoverflow.com/a/30171444
  @FXML
  private URL location; // has to be called location

  @FXML
  void p2recruitTroops(ActionEvent event) {
      Player player2 = getParent().getPlayer2();
      Feature currentlySelectedEnemyProvince = getParent().getCurrentlySelectedEnemyProvince();
      if(currentlySelectedEnemyProvince == null){
        System.out.println("Go back and select province for player 2");
        outputta.setText("Go back and select province for player 2");
        return;
      }
      Province p = player2.findProvince((String)currentlySelectedEnemyProvince.getAttributes().get("name")); 
      int number = 0;
      int tur = 0;
      number = Integer.parseInt(numberofsoldtf2.getText());
      if ((isSoldierValid(soldiertf2.getText()) && (isNumberOfSoldierValid(number)))){
        p.recruit(soldiertf2.getText(), number, player2);   
        for (pendingUnit pend : p.getPendingUnits()){
          System.out.println("p2 unit = "+pend.getUnit().getType()+" release turn = "+pend.getIsAvailable());
          tur = pend.getIsAvailable();
        }           
        outputta.setText(soldiertf2.getText()+" Unit is now in training. Will be available at turn "+tur);
      }      
  }
  public boolean isSoldierValid(String soldier){
    Unitc u = new Unitc("skirmisher", 1);
    System.out.println("player2 soldier = "+soldiertf2.getText());
    JSONObject sold = u.SoldierFromJSON((String)soldiertf2.getText());
    if (sold == null){
      System.out.println("Soldier name is incorrect");
      outputta.setText("Soldier name entered is invalid");
      return false;
    }
    System.out.println("Soldier name is valid");
    return true;      
  }
  
  public boolean isNumberOfSoldierValid(int num){
    Player player2 = getParent().getPlayer2();
    int treasury = player2.getTreasury();
    int number = Integer.parseInt(numberofsoldtf2.getText());
    if (isSoldierValid(soldiertf2.getText())){      
        Unitc u = new Unitc(soldiertf2.getText(), number);
      if (treasury < u.getCost()){
        System.out.println("Invalid number of troops: Can't afford");
        outputta.setText("Invalid number of troops: Can't afford");
        return false;        
      }      
    }
    System.out.println("Valid number of troops");
    return true;   
  }

  @FXML
  public void p2clickedRecruit(ActionEvent e) throws IOException{
    getParent().recruitSelect(this);
  }
}
