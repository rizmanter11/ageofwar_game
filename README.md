# romancivilisation_game

A extended project built upon the framework of a course project. The game implements more extensions that enhances the game imcluding implementing immersive background music,having a multiplayer campaign rather than a one player vs CP and the ability to choose your path to campaign vistory. It implements various OOP design patterns and uses javafx to create an interactive front-end to the project. 

![image](https://user-images.githubusercontent.com/73875509/150711108-bced4bb3-88d9-46af-91eb-edb1e549107f.png)

### Preliminary client requirements

The project is a grand-strategy game, set in the time of Ancient Rome, where the player(s) can play as a faction of the time (such as the Romans, Carthaginians, Gauls, Celtic Britons, Spanish, Numidians, Egyptians, Seleucid Empire, Pontus, Amenians, Parthians, Germanics, Greek City States, Macedonians, Thracians, Dacians), with the overall goal of conquering all provinces in the game map (or succeeding at another grand victory objective).

![image](https://user-images.githubusercontent.com/73875509/150711462-351981e1-d7b6-4fb4-a104-91e329e310fc.png)


The game is a turn-based military/economic strategy game - each faction engages in actions during sequential "turns", where they perform all actions to manage their armies and provinces for a single year in their turn, before clicking "End turn", which will result in all other factions performing their turns sequentially.

The basic unit of currency in the game will be "Gold". Each unit of troops has a price which must be paid in gold upfront, without going into debt.

![image](https://user-images.githubusercontent.com/73875509/150716713-8138c403-694a-45ca-badf-50b3541e2f68.png)

### Basic Campaign Game Interactions

Upon starting/loading a game, the user is presented with a game map and a menu/menus to perform actions. The game map displays a map of the world, split into historically accurate, clickable provinces. Each province should be allocated to a faction, and display the flag of the faction, the province wealth, the number of soldiers, and the faction name.

The user is able to view the current game year and turn on the screen whilst playing the game. The user is able to select to finish the turn by clicking an "End turn" button.

The user is able to save their campaign game at any time (so that they can load it and resume gameplay later).
![image](https://user-images.githubusercontent.com/73875509/150716878-20abc340-c88a-4dd6-b512-192a5e24fcf1.png)

### Campaign Victory

Basic goals in the game include:

* Conquering all territories (CONQUEST goal)
* Accumulating a treasury balance of 100,000 gold (TREASURY goal)
* Building all possible infrastructure across all settlements (INFRASTRUCTURE goal)
* Accumulating faction wealth of 400,000 gold (WEALTH goal)

When starting a campaign game, a logical conjunction(AND)/disjunction(OR) of basic goals, or a conjunction/disjunction of other conjunctions/disjunctions can be chosen randomly (a uniformly random choice from all possible goal expressions where each basic goal exists in the expression at most once). For example:

{ "goal": "AND", "subgoals":
  [ { "goal": "TREASURY" },
    { "goal": "OR", "subgoals":
      [ {"goal": "CONQUEST" },
        {"goal": "WEALTH" }
      ]
    }
  ]
}

![image](https://user-images.githubusercontent.com/73875509/150711585-d6680440-6e33-4553-80cd-4651211fd35f.png)

This example of a victory condition would allow victory in either of the following scenarios:

* TREASURY and CONQUEST goals achieved
* TREASURY and WEALTH goals achieved

Upon reaching the victory condition, the user presented with an interface congratulating them on their victory. 

### Source of Map Data

The map data was obtained from: https://github.com/klokantech/roman-empire

The project uses the following files in this repository:
* *data/provinces.geojson* (province borders and province names)
* *data/provinces/label.geojson* (province label points and province names; the locations to place images/text for each province in the starter code are generated from here)

You can interact with an online version of the map at: https://klokantech.github.io/roman-empire/#4.01/41.957/19.646/0/8

The GeoJSON files downloaded from the repository were converted to the newer format for GeoJSON by applying the "Right hand rule" using: http://mapster.me/right-hand-rule-geojson-fixer/

This tool was then used to convert the GeoJSON files to GeoPackage files: https://mygeodata.cloud/converter/geojson-to-geopackage

GeoJSON files have the advantage of being JSON data and therefore easy to read using a text editor, or process with Java code using the provided `geojson-jackson` library (or you could use a standard JSON library such as `JSON-Java` (also provided, this is the JSON library you used in assignment 1)). However, it is inconvenient to load GeoJSON data directly into ArcGIS FeatureLayers because the ArcGIS runtime SDK for Java lacks a class to directly load in GeoJSON files. You can also view a GeoJSON map visually by using the VSCode extension "VSCode Map Preview".

Whilst GeoPackage files are hard to read using a text editor, it is easy to load GeoPackage files directly into ArcGIS using the `GeoPackage` class. This has been done in the starter code. The documentation for the `GeoPackage` class is at:

https://developers.arcgis.com/java/latest/api-reference/reference/com/esri/arcgisruntime/data/GeoPackage.html
