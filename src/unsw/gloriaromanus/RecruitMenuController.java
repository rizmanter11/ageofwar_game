package unsw.gloriaromanus;

import java.io.IOException;
import java.net.URL;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import com.esri.arcgisruntime.data.Feature;

import org.json.JSONObject;

public class RecruitMenuController extends MenuController{
  @FXML
  private TextField soldiertf;

  @FXML
  private TextField numberofsoldtf;

  @FXML
  private Button enterbtn;

  @FXML
  private TextArea outputta;
  // https://stackoverflow.com/a/30171444
  @FXML
  private URL location; // has to be called location

  @FXML
  void recruitTroops(ActionEvent event) {
      Player player1 = getParent().getPlayer1();
      Feature currentlySelectedHumanProvince = getParent().getCurrentlySelectedHumanProvince();
      if(currentlySelectedHumanProvince == null){
        System.out.println("Go back and select province for player 1");
        outputta.setText("Go back and select province for player 1");
        return;
      }
      Province p = player1.findProvince((String)currentlySelectedHumanProvince.getAttributes().get("name")); 
      int number = 0;
      int tur = 0;
      number = Integer.parseInt(numberofsoldtf.getText());
      if ((isSoldierValid(soldiertf.getText()) && (isNumberOfSoldierValid(number)))){
        p.recruit(soldiertf.getText(), number, player1);             
        for (pendingUnit pend : p.getPendingUnits()){
          System.out.println("p1 unit = "+pend.getUnit().getType()+" release turn = "+pend.getIsAvailable());
          tur = pend.getIsAvailable();
        }  
        outputta.setText(soldiertf.getText()+" Unit is now in training. Will be available at turn "+tur);
      }
  }
  public boolean isSoldierValid(String soldier){
    Unitc u = new Unitc("skirmisher", 1);
    System.out.println("player1 soldier = "+soldiertf.getText());
    JSONObject sold = u.SoldierFromJSON((String)soldiertf.getText());
    if (sold == null){
      System.out.println("Soldier name is incorrect");
      outputta.setText("Soldier name entered is invalid");
      return false;
    }
    System.out.println("Soldier name is valid");
    return true;      
  }
  
  public boolean isNumberOfSoldierValid(int num){
    Player player1 = getParent().getPlayer1();
    int treasury = player1.getTreasury();
    int number = Integer.parseInt(numberofsoldtf.getText());
    if (isSoldierValid(soldiertf.getText())){      
        Unitc u = new Unitc(soldiertf.getText(), number);
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
  public void clickedRecruit(ActionEvent e) throws IOException{
    getParent().recruitSelect(this);
  }
}
