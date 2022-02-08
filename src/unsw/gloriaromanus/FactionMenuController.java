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
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class FactionMenuController extends MenuController{
    @FXML
    private StackPane stackPaneMain;

    @FXML
    private TextArea player1_out;

    @FXML 
    private TextArea player2_out;

    @FXML
    private URL location; // has to be called location

    @FXML
    public void Roman1(ActionEvent e) throws IOException, JsonMappingException, JsonParseException{
        getParent().setPlayer1Faction("Roman");
        printMessageToPlayer1("Player 1 Faction: Roman");
    }

    @FXML
    public void Carthignian1(ActionEvent e) throws IOException, JsonMappingException, JsonParseException{
        getParent().setPlayer1Faction("Carthignian");
        printMessageToPlayer1("Player 1 Faction: Carthignian");
    }

    @FXML
    public void Gaul1(ActionEvent e) throws IOException, JsonMappingException, JsonParseException{
        getParent().setPlayer1Faction("Gaul");
        printMessageToPlayer1("Player 1 Faction: Gaul");
    }

    @FXML
    public void Celtic1(ActionEvent e) throws IOException, JsonMappingException, JsonParseException{
        getParent().setPlayer1Faction("Celtic Briton");
        printMessageToPlayer1("Player 1 Faction: Celtic Briton");
    }

    @FXML
    public void Spanish1(ActionEvent e) throws IOException, JsonMappingException, JsonParseException{
        getParent().setPlayer1Faction("Spanish");
        printMessageToPlayer1("Player 1 Faction: Spanish");
    }

    @FXML
    public void Numidian1(ActionEvent e) throws IOException, JsonMappingException, JsonParseException{
        getParent().setPlayer1Faction("Numidian");
        printMessageToPlayer1("Player 1 Faction: Numidian");
    }

    @FXML
    public void Egyptian1(ActionEvent e) throws IOException, JsonMappingException, JsonParseException{
        getParent().setPlayer1Faction("Egyptian");
        printMessageToPlayer1("Player 1 Faction: Egyptian");
    }

    @FXML
    public void Seleucid1(ActionEvent e) throws IOException, JsonMappingException, JsonParseException{
        getParent().setPlayer1Faction("Seleucid Empire");
        printMessageToPlayer1("Player 1 Faction: Seleucid Empire");
    }

    @FXML
    public void Pontus1(ActionEvent e) throws IOException, JsonMappingException, JsonParseException{
        getParent().setPlayer1Faction("Pontus");
        printMessageToPlayer1("Player 1 Faction: Pontus");
    }

    @FXML
    public void Armenian1(ActionEvent e) throws IOException, JsonMappingException, JsonParseException{
        getParent().setPlayer1Faction("Armenian");
        printMessageToPlayer1("Player 1 Faction: Armenian");
    }

    @FXML
    public void Parthian1(ActionEvent e) throws IOException, JsonMappingException, JsonParseException{
        getParent().setPlayer1Faction("Parthian");
        printMessageToPlayer1("Player 1 Faction: Parthian");
    }

    @FXML
    public void Germanic1(ActionEvent e) throws IOException, JsonMappingException, JsonParseException{
        getParent().setPlayer1Faction("Germanic");
        printMessageToPlayer1("Player 1 Faction: Germanic");
    }

    @FXML
    public void Greek1(ActionEvent e) throws IOException, JsonMappingException, JsonParseException{
        getParent().setPlayer1Faction("Greek");
        printMessageToPlayer1("Player 1 Faction: Greek");
    }

    @FXML
    public void Mace1(ActionEvent e) throws IOException, JsonMappingException, JsonParseException{
        getParent().setPlayer1Faction("Macedonian");
        printMessageToPlayer1("Player 1 Faction: Macedonian");
    }

    @FXML
    public void Thracian1(ActionEvent e) throws IOException, JsonMappingException, JsonParseException{
        getParent().setPlayer1Faction("Thracian");
        printMessageToPlayer1("Player 1 Faction: Thracian");
    }

    @FXML
    public void Dacian1(ActionEvent e) throws IOException, JsonMappingException, JsonParseException{
        getParent().setPlayer1Faction("Dacian");
        printMessageToPlayer1("Player 1 Faction: Dacian");
    }

    @FXML
    public void Roman2(ActionEvent e) throws IOException, JsonMappingException, JsonParseException{
        getParent().setPlayer2Faction("Roman");
        printMessageToPlayer2("Player 2 Faction: Roman");
    }

    @FXML
    public void Carthignian2(ActionEvent e) throws IOException, JsonMappingException, JsonParseException{
        getParent().setPlayer2Faction("Carthignian");
        printMessageToPlayer2("Player 2 Faction: Carthignian");
    }

    
    @FXML
    public void Gaul2(ActionEvent e) throws IOException, JsonMappingException, JsonParseException{
        getParent().setPlayer2Faction("Gaul");
        printMessageToPlayer2("Player 2 Faction: Gaul");
    }

    @FXML
    public void Celtic2(ActionEvent e) throws IOException, JsonMappingException, JsonParseException{
        getParent().setPlayer2Faction("Celtic Briton");
        printMessageToPlayer2("Player 2 Faction: Celtic Briton");
    }

    @FXML
    public void Spanish2(ActionEvent e) throws IOException, JsonMappingException, JsonParseException{
        getParent().setPlayer2Faction("Spanish");
        printMessageToPlayer2("Player 2 Faction: Spanish");
    }

    @FXML
    public void Numidian2(ActionEvent e) throws IOException, JsonMappingException, JsonParseException{
        getParent().setPlayer2Faction("Numidian");
        printMessageToPlayer2("Player 2 Faction: Numidian");
    }

    @FXML
    public void Egyptian2(ActionEvent e) throws IOException, JsonMappingException, JsonParseException{
        getParent().setPlayer2Faction("Egyptian");
        printMessageToPlayer2("Player 2 Faction: Egyptian");
    }

    @FXML
    public void Seleucid2(ActionEvent e) throws IOException, JsonMappingException, JsonParseException{
        getParent().setPlayer2Faction("Seleucid Empire");
        printMessageToPlayer2("Player 2 Faction: Seleucid Empire");
    }

    @FXML
    public void Pontus2(ActionEvent e) throws IOException, JsonMappingException, JsonParseException{
        getParent().setPlayer2Faction("Pontus");
        printMessageToPlayer2("Player 2 Faction: Pontus");
    }

    @FXML
    public void Armenian2(ActionEvent e) throws IOException, JsonMappingException, JsonParseException{
        getParent().setPlayer2Faction("Armenian");
        printMessageToPlayer2("Player 2 Faction: Armenian");
    }

    @FXML
    public void Parthian2(ActionEvent e) throws IOException, JsonMappingException, JsonParseException{
        getParent().setPlayer2Faction("Parthian");
        printMessageToPlayer2("Player 2 Faction: Parthian");
    }

    @FXML
    public void German2(ActionEvent e) throws IOException, JsonMappingException, JsonParseException{
        getParent().setPlayer2Faction("Germanic");
        printMessageToPlayer2("Player 2 Faction: Germanic");
    }

    @FXML
    public void Greek2(ActionEvent e) throws IOException, JsonMappingException, JsonParseException{
        getParent().setPlayer2Faction("Greek");
        printMessageToPlayer2("Player 2 Faction: Greek");
    }

    @FXML
    public void Mace2(ActionEvent e) throws IOException, JsonMappingException, JsonParseException{
        getParent().setPlayer2Faction("Macedonain");
        printMessageToPlayer2("Player 2 Faction: Macedonian");
    }

    @FXML
    public void Thracian2(ActionEvent e) throws IOException, JsonMappingException, JsonParseException{
        getParent().setPlayer2Faction("Thracian");
        printMessageToPlayer2("Player 2 Faction: Thracian");
    }

    @FXML
    public void Dacian2(ActionEvent e) throws IOException, JsonMappingException, JsonParseException{
        getParent().setPlayer2Faction("Dacian");
        printMessageToPlayer2("Player 2 Faction: Dacian");
    }

    @FXML 
    public void chosenFactions(ActionEvent e) throws IOException, JsonMappingException, JsonParseException{
        getParent().switchMenu(this);
    }
    

    private void printMessageToPlayer1(String message){
        player1_out.appendText(message + "\n");
    }

    private void printMessageToPlayer2(String message){
        player2_out.appendText(message + "\n");
    }
    
}
