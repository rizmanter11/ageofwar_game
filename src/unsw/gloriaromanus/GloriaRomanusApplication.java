package unsw.gloriaromanus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class GloriaRomanusApplication extends Application {

  private static GloriaRomanusController controller;

  Stage window;
  Scene start, faction_choose, main;

  @Override
  public void start(Stage stage) throws IOException {
    //music();
    // set up the stage
    Parent root = FXMLLoader.load(getClass().getResource("start_menu.fxml"));
    Scene scene = new Scene(root);

    stage.setTitle("Gloria Romanus");
    stage.setWidth(800);
    stage.setHeight(700);
    stage.setScene(scene);
    stage.show();

  }

  
  /*MediaPlayer mediaPlayer;
  public void music(){
    String s = "game.mp3";
    Media h = new Media(new File("game.mp3").toURI().toString());
    mediaPlayer = new MediaPlayer(h);
    mediaPlayer.play();
  }*/
  
  
  /**
   * Stops and releases all resources used in application.
   */
  /*
  @Override
  public void stop() {
    controller.terminate();
  }
  */
  /**
   * Opens and runs application.
   *
   * @param args arguments passed to this application
   */
  public static void main(String[] args) {

    Application.launch(args);
  }
}