package unsw.gloriaromanus;

import java.io.IOException;
import java.net.URL;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class InvasionMenuController extends MenuController{
    @FXML
    private TextField invading_province;
    @FXML
    private TextField opponent_province;
    @FXML
    private TextArea output_terminal;
    @FXML
    private TextArea goal;
    @FXML
    private Label year;
    @FXML
    private Label treasury;

    // https://stackoverflow.com/a/30171444
    @FXML
    private URL location; // has to be called location

    public void changeYear(int turn){
        String text = turn + " AD";
        year.setText(text);
    }

    public void changeTreasury(){
        String text = "Treasury: " + getParent().getPlayer1().getTreasury();
        treasury.setText(text);
    }

    public void appendGoal(){
        Player p1 = getParent().getPlayer1();
        boolean con = false;
        boolean trea = false;
        boolean wealth = false;
        //String text = p1.getVictoryGoals().getStringComponent();
        if(p1.isConquestGoal()){
            goal.appendText("CONQUEST ");
            con = true;
        } else if(p1.isTreasuryGoal()){
            goal.appendText("TREASURY ");
            trea = true;
        } else if(p1.isWealthGoal()){
            goal.appendText("WEALTH ");
            wealth = true;
        } 

        if(p1.isAndConjunction()){
            goal.appendText("AND ");
        } else if(p1.isOrConjunction()){
            goal.appendText("OR ");
        }

        if(p1.isConquestGoal() && !con){
            goal.appendText("CONQUEST");
        } else if(p1.isTreasuryGoal() && !trea){
            goal.appendText("TREASURY");
        } else if(p1.isWealthGoal() && !wealth){
            goal.appendText("WEALTH");
        }
    }

    public void setInvadingProvince(String p) {
        invading_province.setText(p);
    }

    public void setOpponentProvince(String p) {
        opponent_province.setText(p);
    }

    public void appendToTerminal(String message) {
        output_terminal.appendText(message + "\n");
    }

    @FXML
    public void clickedInvadeButton(ActionEvent e) throws IOException {
        getParent().clickedInvadeButton(e);
    }

    @FXML
    public void clickedTaxMenu(ActionEvent e) throws IOException {
        getParent().chooseTax(this);
    }

    @FXML
    public void clickedRecruit(ActionEvent e) throws IOException{
        getParent().recruitSelect(this);
    }

    @FXML
    public void clickedCancel(ActionEvent e) throws IOException{
        getParent().cancelSelect(this);
    }

    @FXML
    public void selectSave(ActionEvent e) throws IOException{
        getParent().savePlayer1();
        getParent().savePlayer2();
    }
}
