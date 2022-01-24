package unsw.gloriaromanus;

import java.io.IOException;
import java.net.URL;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class StartMenuController{

    @FXML
    private StackPane stackPaneMain;

    private static GloriaRomanusController controller;

    @FXML
    public void handlePlay(ActionEvent e) throws IOException, JsonMappingException, JsonParseException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        Stage window = (Stage) ((Node)e.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @FXML
    public void selectLoad(ActionEvent e)throws IOException, JsonMappingException, JsonParseException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        controller.loadPlayer1();
        controller.loadPlayer2();
        controller.setToInvasion();
        Scene scene = new Scene(root);

        Stage window = (Stage) ((Node)e.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}
