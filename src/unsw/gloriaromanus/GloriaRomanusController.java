package unsw.gloriaromanus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.FeatureTable;
import com.esri.arcgisruntime.data.GeoPackage;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.IdentifyLayerResult;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol.HorizontalAlignment;
import com.esri.arcgisruntime.symbology.TextSymbol.VerticalAlignment;
import com.esri.arcgisruntime.data.Feature;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.geojson.FeatureCollection;
import org.geojson.LngLatAlt;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.util.Pair;

public class GloriaRomanusController{

  @FXML
  private MapView mapView;

  @FXML
  private StackPane stackPaneMain;

  // could use ControllerFactory?
  private Player player1;

  private Player player2;

  private ArrayList<Pair<MenuController, VBox>> controllerParentPairs;

  private ArrayList<String> conquerProvinces;

  private ArcGISMap map;

  private Map<String, String> provinceToOwningFactionMap;

  private String player1Faction;
  private String player2Faction;

  private Feature currentlySelectedHumanProvince;
  private Feature currentlySelectedEnemyProvince;

  private FeatureLayer featureLayer_provinces;

  private int turn;
  private TurnSubject turnsub = new TurnSubject();

  @FXML
  private void initialize() throws JsonParseException, JsonMappingException, IOException, InterruptedException {
    
    turn = 0;

    conquerProvinces = new ArrayList<>();
    
    provinceToOwningFactionMap = getProvinceToOwningFactionMap();

    List<Province> p1Provinces = playerProvinces("Player 1");
    List<Province> p2Provinces = playerProvinces("Player 2");

    for (Province p : p1Provinces){
      turnsub.addObs(p.getTobs());
    }
    for (Province p : p2Provinces){
      turnsub.addObs(p.getTobs());
    }
    //player1Faction = "Rome";
    //player2Faction = "Gaul";
    player1 = new Player(player1Faction, p1Provinces);
    player2 = new Player(player2Faction, p2Provinces);

    currentlySelectedHumanProvince = null;
    currentlySelectedEnemyProvince = null;

  String []menus = {"faction_menu.fxml", "goal_menu.fxml", "invasion_menu.fxml", "p2invasion_menu.fxml", "basic_menu.fxml", "setTax1.fxml", "setTax2.fxml", "UnitScene.fxml", "cancelRecruit2.fxml", "P2UnitScene.fxml", "P2CancelRecruit.fxml", "winscene1.fxml", "winscene2.fxml"};
    controllerParentPairs = new ArrayList<Pair<MenuController, VBox>>();
    for (String fxmlName: menus){
      System.out.println(fxmlName);
      FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlName));
      VBox root = (VBox)loader.load();
      MenuController menuController = (MenuController)loader.getController();
      menuController.setParent(this);
      controllerParentPairs.add(new Pair<MenuController, VBox>(menuController, root));
    }

    stackPaneMain.getChildren().add(controllerParentPairs.get(0).getValue());

    initializeProvinceLayers();

  }

  public void clickedInvadeButton(ActionEvent e) throws IOException {
    if (currentlySelectedHumanProvince != null && currentlySelectedEnemyProvince != null){
      String player1ProvinceName = (String)currentlySelectedHumanProvince.getAttributes().get("name");
      String player2ProvinceName = (String)currentlySelectedEnemyProvince.getAttributes().get("name");
      Province player1Province = player1.getProvince(player1ProvinceName);
      Province player2Province = player2.getProvince(player2ProvinceName);
      if (confirmIfProvincesConnected(player1ProvinceName, player2ProvinceName) && confirmNotTakenThisTurn(player1ProvinceName)){
        //all the units attack 
        //player 1 turn
        
        Attack Battle = new Attack(player1Province.getUnits(), player2Province.getUnits(), player1Province, player2Province, player1, player2);
        String result = Battle.battle();
        printMessageToTerminal(result);
        if(result.equals("Player won battle")){
          provinceToOwningFactionMap.put(player2ProvinceName, "Player 1");
          conquerProvinces.add(player2ProvinceName);
        } 
        /*
        Random r = new Random();
        int choice = r.nextInt(2);
        if (choice == 0){
          // human won. Transfer 40% of troops of human over. No casualties by human, but enemy loses all troops
          provinceToOwningFactionMap.put(player2ProvinceName, "Player 1");
          player2.removeProvince(player2Province);
          player1.addProvince(player2Province);
          printMessageToTerminal("Won battle!");
          conquerProvinces.add(player2ProvinceName);
        }
        else{
          // enemy won. Human loses 60% of soldiers in the province
          printMessageToTerminal("Lost battle!");
        }
        */
        resetSelections();  // reset selections in UI
        addAllPointGraphics(); // reset graphics
      } else if(!confirmNotTakenThisTurn(player1ProvinceName)){
        printMessageToTerminal("Can't attack from a province you have conquered this turn");
      } else{
        printMessageToTerminal("Provinces not adjacent, cannot invade!");
      }

    }
  }

  public void P2clickedInvadeButton(ActionEvent e) throws IOException {
    if (currentlySelectedHumanProvince != null && currentlySelectedEnemyProvince != null){
      String player1ProvinceName = (String)currentlySelectedHumanProvince.getAttributes().get("name");
      String player2ProvinceName = (String)currentlySelectedEnemyProvince.getAttributes().get("name");
      Province player1Province = player1.getProvince(player1ProvinceName);
      Province player2Province = player2.getProvince(player2ProvinceName);
      if (confirmIfProvincesConnected(player1ProvinceName, player2ProvinceName) && confirmNotTakenThisTurn(player2ProvinceName)){
        //player 2 turn
        Attack Battle = new Attack(player2Province.getUnits(), player1Province.getUnits(), player2Province, player1Province, player2, player1);
        String result = Battle.battle();
        printMessageToTerminal(result);
        if(result.equals("Player won battle")){
          provinceToOwningFactionMap.put(player1ProvinceName, "Player 2");
          conquerProvinces.add(player1ProvinceName);
        } 
        /*
        Random r = new Random();
        int choice = r.nextInt(2);
        if (choice == 0){
          provinceToOwningFactionMap.put(player1ProvinceName, "Player 2");
          player1.removeProvince(player1Province);
          player2.addProvince(player1Province);
          P2printMessageToTerminal("Won battle!");
          conquerProvinces.add(player1ProvinceName);
        } else{
          // enemy won. Human loses 60% of soldiers in the province
          P2printMessageToTerminal("Lost battle!");
        }
        */
        resetSelections();  // reset selections in UI
        addAllPointGraphics(); // reset graphics
      } else if(!confirmNotTakenThisTurn(player2ProvinceName)){
        P2printMessageToTerminal("Can't attack from a province you have conquered this turn");
      } else{
        P2printMessageToTerminal("Provinces not adjacent, cannot invade!");
      }

    }
  }

  public void setLowTax1(ActionEvent e) throws IOException{
    String player1ProvinceName = (String)currentlySelectedHumanProvince.getAttributes().get("name");
    Province player1Province = player1.getProvince(player1ProvinceName);

    player1Province.changeTaxStrategy(new LowTax());
    Tax1printMessageToTerminal("Set "+player1ProvinceName+" Tax to Low Tax");
  }

  public void setNormalTax1(ActionEvent e) throws IOException{
    String player1ProvinceName = (String)currentlySelectedHumanProvince.getAttributes().get("name");
    Province player1Province = player1.getProvince(player1ProvinceName);

    player1Province.changeTaxStrategy(new normalTax());
    Tax1printMessageToTerminal("Set "+player1ProvinceName+" Tax to Normal Tax");
  }

  public void setHighTax1(ActionEvent e) throws IOException{
    String player1ProvinceName = (String)currentlySelectedHumanProvince.getAttributes().get("name");
    Province player1Province = player1.getProvince(player1ProvinceName);

    player1Province.changeTaxStrategy(new HighTax());
    Tax1printMessageToTerminal("Set "+player1ProvinceName+" Tax to High Tax");
  }

  public void setVeryHighTax1(ActionEvent e) throws IOException{
    String player1ProvinceName = (String)currentlySelectedHumanProvince.getAttributes().get("name");
    Province player1Province = player1.getProvince(player1ProvinceName);

    player1Province.changeTaxStrategy(new VeryHighTax());
    Tax1printMessageToTerminal("Set "+player1ProvinceName+" Tax to Very High Tax");
  }

  public void setLowTax2(ActionEvent e) throws IOException{
    String player2ProvinceName = (String)currentlySelectedEnemyProvince.getAttributes().get("name");
    Province player2Province = player2.getProvince(player2ProvinceName);

    player2Province.changeTaxStrategy(new LowTax());
    Tax2printMessageToTerminal("Set "+player2ProvinceName+" Tax to Low Tax");
  }

  public void setNormalTax2(ActionEvent e) throws IOException{
    String player2ProvinceName = (String)currentlySelectedEnemyProvince.getAttributes().get("name");
    Province player2Province = player2.getProvince(player2ProvinceName);

    player2Province.changeTaxStrategy(new normalTax());
    Tax2printMessageToTerminal("Set "+player2ProvinceName+" Tax to Normal Tax");
  }

  public void setHighTax2(ActionEvent e) throws IOException{
    String player2ProvinceName = (String)currentlySelectedEnemyProvince.getAttributes().get("name");
    Province player2Province = player2.getProvince(player2ProvinceName);

    player2Province.changeTaxStrategy(new HighTax());
    Tax2printMessageToTerminal("Set "+player2ProvinceName+" Tax to High Tax");
  }

  public void setVeryHighTax2(ActionEvent e) throws IOException{
    String player2ProvinceName = (String)currentlySelectedEnemyProvince.getAttributes().get("name");
    Province player2Province = player2.getProvince(player2ProvinceName);

    player2Province.changeTaxStrategy(new VeryHighTax());
    Tax2printMessageToTerminal("Set "+player2ProvinceName+" Tax to Very High Tax");
  }

  /**
   * run this initially to update province owner, change feature in each
   * FeatureLayer to be visible/invisible depending on owner. Can also update
   * graphics initially
   */
  private void initializeProvinceLayers() throws JsonParseException, JsonMappingException, IOException {

    Basemap myBasemap = Basemap.createImagery();
    // myBasemap.getReferenceLayers().remove(0);
    map = new ArcGISMap(myBasemap);
    mapView.setMap(map);

    // note - tried having different FeatureLayers for AI and human provinces to
    // allow different selection colors, but deprecated setSelectionColor method
    // does nothing
    // so forced to only have 1 selection color (unless construct graphics overlays
    // to give color highlighting)
    GeoPackage gpkg_provinces = new GeoPackage("src/unsw/gloriaromanus/provinces_right_hand_fixed.gpkg");
    gpkg_provinces.loadAsync();
    gpkg_provinces.addDoneLoadingListener(() -> {
      if (gpkg_provinces.getLoadStatus() == LoadStatus.LOADED) {
        // create province border feature
        featureLayer_provinces = createFeatureLayer(gpkg_provinces);
        map.getOperationalLayers().add(featureLayer_provinces);

      } else {
        System.out.println("load failure");
      }
    });

    addAllPointGraphics();
  }

  private void addAllPointGraphics() throws JsonParseException, JsonMappingException, IOException {
    mapView.getGraphicsOverlays().clear();

    InputStream inputStream = new FileInputStream(new File("src/unsw/gloriaromanus/provinces_label.geojson"));
    FeatureCollection fc = new ObjectMapper().readValue(inputStream, FeatureCollection.class);

    GraphicsOverlay graphicsOverlay = new GraphicsOverlay();

    for (org.geojson.Feature f : fc.getFeatures()) {
      if (f.getGeometry() instanceof org.geojson.Point) {
        org.geojson.Point p = (org.geojson.Point) f.getGeometry();
        LngLatAlt coor = p.getCoordinates();
        Point curPoint = new Point(coor.getLongitude(), coor.getLatitude(), SpatialReferences.getWgs84());
        PictureMarkerSymbol s = null;
        String province = (String) f.getProperty("name");
        String playerName = provinceToOwningFactionMap.get(province);
        String faction = null;
        TextSymbol t;
        if(playerName.equals("Player 1")){
          faction = player1Faction;
          t = new TextSymbol(10,
          faction + "\n" + province + "\n" + "number of troops: " + player1.getProvince(province).getUnits().size() + "\n" + "wealth: "+ player1.getProvince(province).getWealth(), 0xFFFF0000,
          HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM);
        } else {
          faction = player2Faction;
          t = new TextSymbol(10,
          faction + "\n" + province + "\n" + "number of troops: " + player2.getProvince(province).getUnits().size() + "\n" + "wealth: "+ player2.getProvince(province).getWealth(), 0xFFFF0000,
          HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM);
        }

        switch (playerName) {
          case "Player 2":
            // note can instantiate a PictureMarkerSymbol using the JavaFX Image class - so could
            // construct it with custom-produced BufferedImages stored in Ram
            // http://jens-na.github.io/2013/11/06/java-how-to-concat-buffered-images/
            // then you could convert it to JavaFX image https://stackoverflow.com/a/30970114

            // you can pass in a filename to create a PictureMarkerSymbol...
            s = new PictureMarkerSymbol(new Image((new File("images/Celtic_Druid.png")).toURI().toString()));
            if(player2Faction != null){
              if(player2Faction.equals("Roman")){
                s = new PictureMarkerSymbol(new Image((new File("images/CS2511Sprites_No_Background/Flags/Roman/RomanFlag.png")).toURI().toString()));
              } else if(player2Faction.equals("Carthignian")){
                s = new PictureMarkerSymbol(new Image((new File("images/CS2511Sprites_No_Background/Flags/Carthage/CarthageFlag.png")).toURI().toString()));
              } else if(player2Faction.equals("Gaul")){
                s = new PictureMarkerSymbol(new Image((new File("images/CS2511Sprites_No_Background/Flags/Gallic/GallicFlag.png")).toURI().toString()));
              } else if(player2Faction.equals("Celtic Briton")){
                s = new PictureMarkerSymbol(new Image((new File("images/CS2511Sprites_No_Background/Flags/Celtic/CelticFlag.png")).toURI().toString()));
              } else if(player2Faction.equals("Spanish")){
                s = new PictureMarkerSymbol(new Image((new File("images/CS2511Sprites_No_Background/Flags/Spanish/SpanishFlag.png")).toURI().toString()));
              } else if(player2Faction.equals("Numidian")){
                s = new PictureMarkerSymbol(new Image((new File("images/CS2511Sprites_No_Background/Camel/CamelArcher/CamelArcher_NB.png")).toURI().toString()));
              } else if(player2Faction.equals("Egyptian")){
                s = new PictureMarkerSymbol(new Image((new File("images/CS2511Sprites_No_Background/Flags/Egyptian/EgyptianFlag.png")).toURI().toString()));
              } else if(player2Faction.equals("Seleucid Empire")){
                s = new PictureMarkerSymbol(new Image((new File("images/CS2511Sprites_No_Background/Horse/Horse_Archer/Horse_Archer_NB.png")).toURI().toString()));
              } else if(player2Faction.equals("Pontus")){
                s = new PictureMarkerSymbol(new Image((new File("images/CS2511Sprites_No_Background/Chariot/Chariot_NB.png")).toURI().toString()));
              } else if(player2Faction.equals("Armenian")){
                s = new PictureMarkerSymbol(new Image((new File("images/CS2511Sprites_No_Background/Crossbowman/Crossbowman_NB.png")).toURI().toString()));
              } else if(player2Faction.equals("Parthian")){
                s = new PictureMarkerSymbol(new Image((new File("images/CS2511Sprites_No_Background/Elephant/Alone/Elephant_Alone_NB.png")).toURI().toString()));
              } else if(player2Faction.equals("Germanic")){
                s = new PictureMarkerSymbol(new Image((new File("images/CS2511Sprites_No_Background/Swordsman/Swordsman_NB.png")).toURI().toString()));
              } else if(player2Faction.equals("Greek")){
                s = new PictureMarkerSymbol(new Image((new File("images/CS2511Sprites_No_Background/Hoplite/Hoplite_NB.png")).toURI().toString()));
              } else if(player2Faction.equals("Macedonian")){
                s = new PictureMarkerSymbol(new Image((new File("images/CS2511Sprites_No_Background/Crossbowman/Crossbowman_NB.png")).toURI().toString()));
              } else if(player2Faction.equals("Thracian")){
                s = new PictureMarkerSymbol(new Image((new File("images/CS2511Sprites_No_Background/Slingerman/Slinger_Man_NB.png")).toURI().toString()));
              } else if(player2Faction.equals("Dacian")){
                s = new PictureMarkerSymbol(new Image((new File("images/CS2511Sprites_No_Background/NetFighter/NetFighter_NB.png")).toURI().toString()));
              }
            }
            break;
          case "Player 1":
            // you can also pass in a javafx Image to create a PictureMarkerSymbol (different to BufferedImage)
            s = new PictureMarkerSymbol("images/legionary.png");
            if(player1Faction != null){
              if(player1Faction.equals("Roman")){
                s = new PictureMarkerSymbol(new Image((new File("images/CS2511Sprites_No_Background/Flags/Roman/RomanFlag.png")).toURI().toString()));
              } else if(player1Faction.equals("Carthignian")){
                s = new PictureMarkerSymbol(new Image((new File("images/CS2511Sprites_No_Background/Flags/Carthage/CarthageFlag.png")).toURI().toString()));
              } else if(player1Faction.equals("Gaul")){
                s = new PictureMarkerSymbol(new Image((new File("images/CS2511Sprites_No_Background/Flags/Gallic/GallicFlag.png")).toURI().toString()));
              } else if(player1Faction.equals("Celtic Briton")){
                s = new PictureMarkerSymbol(new Image((new File("images/CS2511Sprites_No_Background/Flags/Celtic/CelticFlag.png")).toURI().toString()));
              } else if(player1Faction.equals("Spanish")){
                s = new PictureMarkerSymbol(new Image((new File("images/CS2511Sprites_No_Background/Flags/Spanish/SpanishFlag.png")).toURI().toString()));
              } else if(player1Faction.equals("Numidian")){
                s = new PictureMarkerSymbol(new Image((new File("images/CS2511Sprites_No_Background/Camel/CamelArcher/CamelArcher_NB.png")).toURI().toString()));
              } else if(player1Faction.equals("Egyptian")){
                s = new PictureMarkerSymbol(new Image((new File("images/CS2511Sprites_No_Background/Flags/Egyptian/EgyptianFlag.png")).toURI().toString()));
              } else if(player1Faction.equals("Seleucid Empire")){
                s = new PictureMarkerSymbol(new Image((new File("images/CS2511Sprites_No_Background/Horse/Horse_Archer/Horse_Archer_NB.png")).toURI().toString()));
              } else if(player1Faction.equals("Pontus")){
                s = new PictureMarkerSymbol(new Image((new File("images/CS2511Sprites_No_Background/Chariot/Chariot_NB.png")).toURI().toString()));
              } else if(player1Faction.equals("Armenian")){
                s = new PictureMarkerSymbol(new Image((new File("images/CS2511Sprites_No_Background/Crossbowman/Crossbowman_NB.png")).toURI().toString()));
              } else if(player1Faction.equals("Parthian")){
                s = new PictureMarkerSymbol(new Image((new File("images/CS2511Sprites_No_Background/Elephant/Alone/Elephant_Alone_NB.png")).toURI().toString()));
              } else if(player1Faction.equals("Germanic")){
                s = new PictureMarkerSymbol(new Image((new File("images/CS2511Sprites_No_Background/Swordsman/Swordsman_NB.png")).toURI().toString()));
              } else if(player1Faction.equals("Greek")){
                s = new PictureMarkerSymbol(new Image((new File("images/CS2511Sprites_No_Background/Hoplite/Hoplite_NB.png")).toURI().toString()));
              } else if(player1Faction.equals("Macedonian")){
                s = new PictureMarkerSymbol(new Image((new File("images/CS2511Sprites_No_Background/Crossbowman/Crossbowman_NB.png")).toURI().toString()));
              } else if(player1Faction.equals("Thracian")){
                s = new PictureMarkerSymbol(new Image((new File("images/CS2511Sprites_No_Background/Slingerman/Slinger_Man_NB.png")).toURI().toString()));
              } else if(player1Faction.equals("Dacian")){
                s = new PictureMarkerSymbol(new Image((new File("images/CS2511Sprites_No_Background/NetFighter/NetFighter_NB.png")).toURI().toString()));
              }
            }
            break;
        }
        t.setHaloColor(0xFFFFFFFF);
        t.setHaloWidth(2);
        Graphic gPic = new Graphic(curPoint, s);
        Graphic gText = new Graphic(curPoint, t);
        graphicsOverlay.getGraphics().add(gPic);
        graphicsOverlay.getGraphics().add(gText);
      } else {
        System.out.println("Non-point geo json object in file");
      }

    }

    inputStream.close();
    mapView.getGraphicsOverlays().add(graphicsOverlay);
  }

  private FeatureLayer createFeatureLayer(GeoPackage gpkg_provinces) {
    FeatureTable geoPackageTable_provinces = gpkg_provinces.getGeoPackageFeatureTables().get(0);

    // Make sure a feature table was found in the package
    if (geoPackageTable_provinces == null) {
      System.out.println("no geoPackageTable found");
      return null;
    }

    // Create a layer to show the feature table
    FeatureLayer flp = new FeatureLayer(geoPackageTable_provinces);

    // https://developers.arcgis.com/java/latest/guide/identify-features.htm
    // listen to the mouse clicked event on the map view
    mapView.setOnMouseClicked(e -> {
      // was the main button pressed?
      if (e.getButton() == MouseButton.PRIMARY) {
        // get the screen point where the user clicked or tapped
        Point2D screenPoint = new Point2D(e.getX(), e.getY());

        // specifying the layer to identify, where to identify, tolerance around point,
        // to return pop-ups only, and
        // maximum results
        // note - if select right on border, even with 0 tolerance, can select multiple
        // features - so have to check length of result when handling it
        final ListenableFuture<IdentifyLayerResult> identifyFuture = mapView.identifyLayerAsync(flp,
            screenPoint, 0, false, 25);

        // add a listener to the future
        identifyFuture.addDoneListener(() -> {
          try {
            // get the identify results from the future - returns when the operation is
            // complete
            IdentifyLayerResult identifyLayerResult = identifyFuture.get();
            // a reference to the feature layer can be used, for example, to select
            // identified features
            if (identifyLayerResult.getLayerContent() instanceof FeatureLayer) {
              FeatureLayer featureLayer = (FeatureLayer) identifyLayerResult.getLayerContent();
              // select all features that were identified
              List<Feature> features = identifyLayerResult.getElements().stream().map(f -> (Feature) f).collect(Collectors.toList());

              if (features.size() > 1){
                printMessageToTerminal("Have more than 1 element - you might have clicked on boundary!");
              }
              else if (features.size() == 1){
                // note maybe best to track whether selected...
                Feature f = features.get(0);
                String province = (String)f.getAttributes().get("name");

                if (provinceToOwningFactionMap.get(province).equals("Player 1")){
                  // province owned by human
                  if (currentlySelectedHumanProvince != null){
                    featureLayer.unselectFeature(currentlySelectedHumanProvince);
                  }
                  currentlySelectedHumanProvince = f;
                  if (controllerParentPairs.get(0).getKey() instanceof InvasionMenuController){
                    ((InvasionMenuController)controllerParentPairs.get(0).getKey()).setInvadingProvince(province);
                  } else if(controllerParentPairs.get(0).getKey() instanceof P2MenuController){
                    ((P2MenuController)controllerParentPairs.get(0).getKey()).setOpponentProvince(province);
                  } else if(controllerParentPairs.get(0).getKey() instanceof TaxMenu1Controller){
                    ((TaxMenu1Controller)controllerParentPairs.get(0).getKey()).setInvadingProvince(province);
                  }

                }
                else{
                  if (currentlySelectedEnemyProvince != null){
                    featureLayer.unselectFeature(currentlySelectedEnemyProvince);
                  }
                  currentlySelectedEnemyProvince = f;
                  if (controllerParentPairs.get(0).getKey() instanceof InvasionMenuController){
                    ((InvasionMenuController)controllerParentPairs.get(0).getKey()).setOpponentProvince(province);
                  }else if(controllerParentPairs.get(0).getKey() instanceof P2MenuController){
                    ((P2MenuController)controllerParentPairs.get(0).getKey()).setInvadingProvince(province);
                  } else if(controllerParentPairs.get(0).getKey() instanceof TaxMenu2Controller){
                    ((TaxMenu2Controller)controllerParentPairs.get(0).getKey()).setInvadingProvince(province);
                  }
                }

                featureLayer.selectFeature(f);                
              }

              
            }
          } catch (InterruptedException | ExecutionException ex) {
            // ... must deal with checked exceptions thrown from the async identify
            // operation
            System.out.println("InterruptedException occurred");
          }
        });
      }
    });
    return flp;
  }

  private Map<String, String> getProvinceToOwningFactionMap() throws IOException {
    String content = Files.readString(Paths.get("src/unsw/gloriaromanus/initial_province_ownership.json"));
    JSONObject ownership = new JSONObject(content);
    Map<String, String> m = new HashMap<String, String>();
    for (String key : ownership.keySet()) {
      // key will be the player name
      JSONArray ja = ownership.getJSONArray(key);
      // value is province name
      for (int i = 0; i < ja.length(); i++) {
        String value = ja.getString(i);
        m.put(value, key);
      }
    }
    return m;
  }

  private List<Province> playerProvinces(String player) throws IOException{
    String content = Files.readString(Paths.get("src/unsw/gloriaromanus/initial_province_ownership.json"));
    JSONObject ownership = new JSONObject(content);
    List<Province> provinces = new ArrayList<>();

    for (String key : ownership.keySet()) {
      // key will be the player name
      JSONArray ja = ownership.getJSONArray(key);
      // value is province name
      for (int i = 0; i < ja.length(); i++) {
        String value = ja.getString(i);
        Province p = new Province(value);
        if(player.equals(key)){
          provinces.add(p);
        } else if(player.equals(key)){
          provinces.add(p);
        }
      }
    }
    return provinces;
  }

  private ArrayList<String> getHumanProvincesList(String faction) throws IOException {
    // https://developers.arcgis.com/labs/java/query-a-feature-layer/

    String content = Files.readString(Paths.get("src/unsw/gloriaromanus/initial_province_ownership.json"));
    JSONObject ownership = new JSONObject(content);
    if(faction.equals(player1Faction)){
      return ArrayUtil.convert(ownership.getJSONArray("Player 1"));
    } else if(faction.equals(player2Faction)){
      return ArrayUtil.convert(ownership.getJSONArray("Player 2"));
    }
    return null; 
  }

  /**
   * returns query for arcgis to get features representing human provinces can
   * apply this to FeatureTable.queryFeaturesAsync() pass string to
   * QueryParameters.setWhereClause() as the query string
   */
  private String getHumanProvincesQuery(String Faction) throws IOException {
    LinkedList<String> l = new LinkedList<String>();
    for (String hp : getHumanProvincesList(player1Faction)) {
      l.add("name='" + hp + "'");
    }
    return "(" + String.join(" OR ", l) + ")";
  }

  private boolean confirmIfProvincesConnected(String province1, String province2) throws IOException {
    String content = Files.readString(Paths.get("src/unsw/gloriaromanus/province_adjacency_matrix_fully_connected.json"));
    JSONObject provinceAdjacencyMatrix = new JSONObject(content);
    return provinceAdjacencyMatrix.getJSONObject(province1).getBoolean(province2);
  }

  private void resetSelections(){
    featureLayer_provinces.unselectFeatures(Arrays.asList(currentlySelectedEnemyProvince, currentlySelectedHumanProvince));
    currentlySelectedEnemyProvince = null;
    currentlySelectedHumanProvince = null;
    if (controllerParentPairs.get(0).getKey() instanceof InvasionMenuController){
      ((InvasionMenuController)controllerParentPairs.get(0).getKey()).setInvadingProvince("");
      ((InvasionMenuController)controllerParentPairs.get(0).getKey()).setOpponentProvince("");
    }
  }

  private void printMessageToTerminal(String message){
    if (controllerParentPairs.get(0).getKey() instanceof InvasionMenuController){
      ((InvasionMenuController)controllerParentPairs.get(0).getKey()).appendToTerminal(message);
    }
  }

  private void P2printMessageToTerminal(String message){
    if (controllerParentPairs.get(0).getKey() instanceof P2MenuController){
      ((P2MenuController)controllerParentPairs.get(0).getKey()).appendToTerminal(message);
    }
  }

  private void Tax1printMessageToTerminal(String message){
    if (controllerParentPairs.get(0).getKey() instanceof TaxMenu1Controller){
      ((TaxMenu1Controller)controllerParentPairs.get(0).getKey()).appendToTerminal(message);
    }
  }

  private void Tax2printMessageToTerminal(String message){
    if (controllerParentPairs.get(0).getKey() instanceof TaxMenu2Controller){
      ((TaxMenu2Controller)controllerParentPairs.get(0).getKey()).appendToTerminal(message);
    }
  }

  /**
   * Stops and releases all resources used in application.
   */
  void terminate() {

    if (mapView != null) {
      mapView.dispose();
    }
  }

  public void switchMenu(MenuController mu) throws JsonParseException, JsonMappingException, IOException {
    System.out.println("trying to switch menu");
    stackPaneMain.getChildren().remove(controllerParentPairs.get(0).getValue());
    int i = 0;
    for(Pair<MenuController, VBox> p: controllerParentPairs){
      if(mu instanceof InvasionMenuController && p.getKey() instanceof P2MenuController){
        checkGoalsMetPlayer1();
        if(WonGame(player1)){
          winScene(mu);
          return;
        }
        Collections.swap(controllerParentPairs, i, 0);
        if(turn == 0){
          ((P2MenuController)p.getKey()).appendGoal();
        }
        conquerProvinces.removeAll(conquerProvinces);
        if(currentlySelectedEnemyProvince != null && currentlySelectedHumanProvince != null){
          resetSelections();
        }
        player1CollectTax();
        turn++;
        turnsub.setTurnNum(turn);
        for (Province prov : player1.getProvinces()){
          prov.recruitReady(turn);
        }
        for (Province prov : player2.getProvinces()){
          prov.recruitReady(turn);
        }
        ((InvasionMenuController)mu).changeYear(turn);
        ((InvasionMenuController)mu).changeTreasury();
        ((P2MenuController)p.getKey()).changeYear(turn);
        addAllPointGraphics();
      } else if(mu instanceof P2MenuController && p.getKey() instanceof InvasionMenuController){
        checkGoalsMetPlayer2();
        if(WonGame(player2)){
          winScene(mu);
          return;
        }
        Collections.swap(controllerParentPairs, i, 0);
        conquerProvinces.removeAll(conquerProvinces);
        if(currentlySelectedEnemyProvince != null && currentlySelectedHumanProvince != null){
          resetSelections();
        }
        player2CollectTax();
        turn++;
        turnsub.setTurnNum(turn);
        for (Province prov : player1.getProvinces()){
          prov.recruitReady(turn);
        }
        for (Province prov : player2.getProvinces()){
          prov.recruitReady(turn);
        }
        ((InvasionMenuController)p.getKey()).changeYear(turn);
        ((P2MenuController)mu).changeYear(turn);
        ((P2MenuController)mu).changeTreasury();
        addAllPointGraphics();
      } else if(mu instanceof FactionMenuController && p.getKey() instanceof GoalMenuController){
        Collections.swap(controllerParentPairs, i, 0);
        addAllPointGraphics(); // reset graphics
      } else if(mu instanceof GoalMenuController && p.getKey() instanceof InvasionMenuController){
        Collections.swap(controllerParentPairs, i, 0);
        ((InvasionMenuController)p.getKey()).appendGoal();
        addAllPointGraphics();
      }
      i++;
    }
    //Collections.reverse(controllerParentPairs);
    stackPaneMain.getChildren().add(controllerParentPairs.get(0).getValue());
  }

  public void winScene(MenuController mu)throws JsonParseException, JsonMappingException, IOException {
    System.out.println("trying to switch to win menu");
    stackPaneMain.getChildren().remove(controllerParentPairs.get(0).getValue());
    int i = 0;
    for(Pair<MenuController, VBox> p: controllerParentPairs){
      if(mu instanceof InvasionMenuController && p.getKey() instanceof WinScene1){
        Collections.swap(controllerParentPairs, i, 0);
      } else if(mu instanceof P2MenuController && p.getKey() instanceof WinScene2){
        Collections.swap(controllerParentPairs, i, 0);
      }
      i++;
    }
    //Collections.reverse(controllerParentPairs);
    stackPaneMain.getChildren().add(controllerParentPairs.get(0).getValue());
  }

  public void checkGoalsMetPlayer1(){
    if(player1.isConquestGoal()){
      if(player1.capturedAllRegions()){
        printMessageToTerminal("Conquest Goal Achieved");
      } 
    }
    if(player1.isTreasuryGoal()){
      if(player1.treasuryGoalReached()){
        printMessageToTerminal("Treasury Goal Achieved");
      }
    }
    if(player1.isWealthGoal()){
      if(player1.wealthGoalReached()){
        printMessageToTerminal("Wealth Goal Achieved");
      }
    }
  }

  public void checkGoalsMetPlayer2(){
    if(player2.isConquestGoal()){
      if(player2.capturedAllRegions()){
        P2printMessageToTerminal("Conquest Goal Achieved");
      } 
    }
    if(player2.isTreasuryGoal()){
      if(player2.treasuryGoalReached()){
        P2printMessageToTerminal("Treasury Goal Achieved");
      }
    }
    if(player2.isWealthGoal()){
      if(player2.wealthGoalReached()){
        P2printMessageToTerminal("Wealth Goal Achieved");
      }
    }
  }

  public boolean WonGame(Player player){
    if(player.isAndConjunction()){
      int count = 0;
      if(player.isConquestGoal()){
        if(player2.capturedAllRegions()){
          count++;
        } 
      }
      if(player.isTreasuryGoal()){
        if(player.treasuryGoalReached()){
          count++;
        }
      }
      if(player.isWealthGoal()){
        if(player.wealthGoalReached()){
          count++;
        }
      }
      if(count == 2){
        return true;
      }
    }
    else if(player.isOrConjunction()){
      if(player.isConquestGoal()){
        if(player.capturedAllRegions()){
          return true;
        } 
      }
      if(player.isTreasuryGoal()){
        if(player.treasuryGoalReached()){
          return true;
        }
      }
      if(player.isWealthGoal()){
        if(player.wealthGoalReached()){
          return true;
        }
      }
    }
    return false;
  }

  public void player1CollectTax(){
    for(Province p: player1.getProvinces()){
      p.taxImplement(player1);
    }
  }

  public void player2CollectTax(){
    for(Province p: player2.getProvinces()){
      p.taxImplement(player2);
    }
  }

  public void chooseTax(MenuController mu) throws JsonParseException, JsonMappingException, IOException {
    stackPaneMain.getChildren().remove(controllerParentPairs.get(0).getValue());
    int i = 0;
    for(Pair<MenuController, VBox> p: controllerParentPairs){
      if(mu instanceof InvasionMenuController && p.getKey() instanceof TaxMenu1Controller){
        Collections.swap(controllerParentPairs, i, 0);
        if(currentlySelectedEnemyProvince != null && currentlySelectedHumanProvince != null){
          resetSelections();
        }
      } else if(mu instanceof TaxMenu1Controller && p.getKey() instanceof InvasionMenuController){
        Collections.swap(controllerParentPairs, i, 0);
        if(currentlySelectedEnemyProvince != null && currentlySelectedHumanProvince != null){
          resetSelections();
        }
      } else if(mu instanceof P2MenuController && p.getKey() instanceof TaxMenu2Controller){
        Collections.swap(controllerParentPairs, i, 0);
        if(currentlySelectedEnemyProvince != null && currentlySelectedHumanProvince != null){
          resetSelections();
        }
      } else if(mu instanceof TaxMenu2Controller && p.getKey() instanceof P2MenuController){
        Collections.swap(controllerParentPairs, i, 0);
        if(currentlySelectedEnemyProvince != null && currentlySelectedHumanProvince != null){
          resetSelections();
        }
      }
      i++;
    }
    stackPaneMain.getChildren().add(controllerParentPairs.get(0).getValue());
  }

  public void recruitSelect(MenuController mu) throws JsonParseException, JsonMappingException, IOException {
    stackPaneMain.getChildren().remove(controllerParentPairs.get(0).getValue());
    int i = 0;
    for(Pair<MenuController, VBox> p: controllerParentPairs){
      if(mu instanceof InvasionMenuController && p.getKey() instanceof RecruitMenuController){
        Collections.swap(controllerParentPairs, i, 0);
      } else if(mu instanceof RecruitMenuController && p.getKey() instanceof InvasionMenuController){
        Collections.swap(controllerParentPairs, i, 0);
      } else if(mu instanceof P2RecruitMenuController && p.getKey() instanceof P2MenuController){
        Collections.swap(controllerParentPairs, i, 0);
      } else if(mu instanceof P2MenuController && p.getKey() instanceof P2RecruitMenuController){
        Collections.swap(controllerParentPairs, i, 0);
      }
      i++;
    }
    stackPaneMain.getChildren().add(controllerParentPairs.get(0).getValue());
  }

  public void cancelSelect(MenuController mu) throws JsonParseException, JsonMappingException, IOException {
    stackPaneMain.getChildren().remove(controllerParentPairs.get(0).getValue());
    int i = 0;
    for(Pair<MenuController, VBox> p: controllerParentPairs){
      if(mu instanceof InvasionMenuController && p.getKey() instanceof CancelRecruitController){
        Collections.swap(controllerParentPairs, i, 0);
      } else if(mu instanceof CancelRecruitController && p.getKey() instanceof InvasionMenuController){
        Collections.swap(controllerParentPairs, i, 0);
      } else if(mu instanceof P2CancelRecruitController && p.getKey() instanceof P2MenuController){
        Collections.swap(controllerParentPairs, i, 0);
      } else if(mu instanceof P2MenuController && p.getKey() instanceof P2CancelRecruitController){
        Collections.swap(controllerParentPairs, i, 0);
      }      
      i++;
    }
    stackPaneMain.getChildren().add(controllerParentPairs.get(0).getValue());
  }

  public void setPlayer1Faction(String faction){
    player1Faction = faction;
  }

  public void setPlayer2Faction(String faction){
    player2Faction = faction;
  }

  public void setGoal(String goal){
    if(goal.equals("wealth")){
      player1.setWealthGoal(true);
      player2.setWealthGoal(true);
    } else if(goal.equals("treasury")){
      player1.setTreasuryGoal(true);
      player2.setTreasuryGoal(true);
    } else if(goal.equals("conquest")){
      player1.setConquestGoal(true);
      player2.setConquestGoal(true);
    }
  }

  public void setConjunction(String con){
    if(con.equals("and")){
      player1.setAndConjunction(true);
      player2.setAndConjunction(true);
    } else if(con.equals("or")){
      player1.setOrConjunction(true);
      player2.setOrConjunction(true);
    }
  }

  public Player getPlayer1(){
    return player1;
  }

  public boolean confirmNotTakenThisTurn(String Province){
    if(conquerProvinces.contains(Province)){
      return false;
    }
    return true;
  }

  public Feature getCurrentlySelectedHumanProvince() {
    return currentlySelectedHumanProvince;
  }

  public void setCurrentlySelectedHumanProvince(Feature currentlySelectedHumanProvince) {
    this.currentlySelectedHumanProvince = currentlySelectedHumanProvince;
  }

  public int getTurn() {
    return turn;
  }

  public void setTurn(int turn) {
    this.turn = turn;
  }

  public Player getPlayer2() {
    return player2;
  }

  public void setPlayer2(Player player2) {
    this.player2 = player2;
  }

  public Feature getCurrentlySelectedEnemyProvince() {
    return currentlySelectedEnemyProvince;
  }

  public void setCurrentlySelectedEnemyProvince(Feature currentlySelectedEnemyProvince) {
    this.currentlySelectedEnemyProvince = currentlySelectedEnemyProvince;
  }
  public void savePlayer1(){
    try {
      ArrayList<Object> data = new ArrayList<Object>();
      data.add(player1);
      data.add(player1Faction);
      data.add(provinceToOwningFactionMap);
      data.add(turn);

      FileOutputStream fileOut = new FileOutputStream("Player1Data.ser");
      ObjectOutputStream out = new ObjectOutputStream(fileOut);
      out.writeObject(data);
      out.close();
      fileOut.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
  }

  public void loadPlayer1(){
    try {
      ArrayList<Object> data = new ArrayList<Object>();

      FileInputStream fileIn = new FileInputStream("Player1Data.ser");
      ObjectInputStream in = new ObjectInputStream(fileIn);
      data = (ArrayList<Object>)in.readObject();
      in.close();
      fileIn.close();

      player1 = (Player)data.get(0);
      player1Faction = (String)data.get(1);
      provinceToOwningFactionMap = (Map<String, String>)data.get(2);
      turn = (int)data.get(3);
    } catch (IOException e) {
        e.printStackTrace();
        System.out.println("IOException");
        return;
    } catch (ClassNotFoundException c){
        c.printStackTrace();
        System.out.println("ClassNotFoundException");
        return;
    }
  }

  public void savePlayer2(){
    try {
      ArrayList<Object> data = new ArrayList<Object>();
      data.add(player2);
      data.add(player2Faction);

      FileOutputStream fileOut = new FileOutputStream("Player2Data.ser");
      ObjectOutputStream out = new ObjectOutputStream(fileOut);
      out.writeObject(data);
      out.close();
      fileOut.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
  }

  public void loadPlayer2(){
    try {
      ArrayList<Object> data = new ArrayList<Object>();

      FileInputStream fileIn = new FileInputStream("Player2Data.ser");
      ObjectInputStream in = new ObjectInputStream(fileIn);
      data = (ArrayList<Object>)in.readObject();
      in.close();
      fileIn.close();

      player2 = (Player)data.get(0);
      player2Faction = (String)data.get(1);

    } catch (IOException e) {
        e.printStackTrace();
        System.out.println("IOException");
        return;
    } catch (ClassNotFoundException c){
        c.printStackTrace();
        System.out.println("ClassNotFoundException");
        return;
    }
  }

  public void setToInvasion() throws JsonParseException, JsonMappingException, IOException{
    stackPaneMain.getChildren().remove(controllerParentPairs.get(0).getValue());
    int i = 0;
    for(Pair<MenuController, VBox> p: controllerParentPairs){
      if(p.getKey() instanceof InvasionMenuController){
        ((InvasionMenuController)p.getKey()).appendGoal();
        ((InvasionMenuController)p.getKey()).changeYear(turn);
        Collections.swap(controllerParentPairs, i, 0);
      } 
      i++;
    }
    stackPaneMain.getChildren().add(controllerParentPairs.get(0).getValue());
    addAllPointGraphics();
  }

  public TurnSubject getTurnsub() {
    return turnsub;
  }

  public void setTurnsub(TurnSubject turnsub) {
    this.turnsub = turnsub;
  }
}
