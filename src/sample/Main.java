package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import java.util.ArrayList;

public class Main extends Application {

    /*
        Student Information: 150118014 Can Gök, 150118006 Talha Osman Saraç,
        Integrated Development Environment: Intellij Community Edition,
        Some of the pictures are from https://pngfuel.com

        Description of the Class:

        #   Main is the class, the player will run first.
        #   It has multiple methods for the game, for more information you can look to the comments
     */

    //Create a new Pane
    public static Pane pane;
    //Create a new stage
    public static Stage stage;
    //Create a new scene
    public static Scene scene;
    //Create a new arraylist for storing the Tile objects
    public static ArrayList<Tile> tiles = new ArrayList<>();
    //Create a new GameBoard object for changing the game board
    public static GameBoard gameBoard;
    //Create a new AnimatedCircle object for updating the circle
    public static AnimatedCircle circle;
    //Create a new boolean isLevelFinished
    public static boolean isLevelFinished;
    //Create a new boolean isGameWon
    public static boolean isGameWon = false;
    //Create a new int numberOfMoves for counting the moves
    public static int numberOfMoves = 0;

    @Override
    public void start(Stage primaryStage){
        //Set stage as primary stage
        stage = primaryStage;
        //Display the first level
        displayLevel();
    }

    public static void displayLevel(){
        numberOfMoves = 0;
        //create a text
        GameText text = new GameText("Number of moves: 0", 150, 550, 10, 20);
        //Level isn't finished right now
        isLevelFinished = false;
        //Create a new gameBoard
        gameBoard = new GameBoard();
        //If game has won, don't display the level
        if(isGameWon) {displayGameWon(); return;}
        //Create a new animated circle
        circle = new AnimatedCircle(64, 40, 12.5, Color.YELLOW);
        //Create a new pane
        pane = new Pane();
        //Create a for loop for every tile for setOnMouseDragged event
        for (int a = 0; a < tiles.size(); a++){
            //Store variable a in a final variable
            int finalA = a;
            //Get the rectangle of the Tile object setOnMouseDragged event
            tiles.get(a).getRectangle().setOnMouseDragged(e -> {
                //If the level is finished player cannot move tiles
                if(isLevelFinished) return;
                //First x coordinate of the tile
                int FX = (int) tiles.get(finalA).getRectangle().getX();
                //First y coordinate of the tile
                int FY = (int) tiles.get(finalA).getRectangle().getY();
                //Static method for moving tile
                Tile.moveTile(finalA, e);
                //Static method for controlling if there is coincident tiles
                Tile.controlTiles(finalA, FX, FY);
                //Static method for blocking the cross movement
                Tile.controlCrossMovement(finalA, FX, FY);
                //Static method for controlling the jump
                Tile.controlJump(finalA, FX, FY);
                //Static method for controlling if the tile is static
                Tile.controlStatic(finalA, FX, FY);
                //If the state is changed swap the tiles
                if(Tile.controlMovement(finalA, FX, FY)) Tile.swapTiles(finalA, FX, FY);
                //If the state is changed swap the tiles
                if(Tile.controlMovement(finalA, FX, FY)) text.setText(String.valueOf("Number of moves: " + ++numberOfMoves));
                //If the state is changed look if the level is finished or not
                if(Tile.controlMovement(finalA, FX, FY)) isLevelFinished = GameBoard.controlLevel();
                //If level is finished create a new GameBoard to move to the next level
                if(isLevelFinished) AnimatedCircle.animateCircle();
            });
        }
        //Add rectangles of Tile objects to the pane
        for (Tile tile : tiles) pane.getChildren().add(tile.getRectangle());
        //Add circle to the pane
        pane.getChildren().add(circle.getCircle());
        //Add text to the pane
        pane.getChildren().add(text.getText());
        //Create a new scene
        scene = new Scene(pane, 512, 570);
        //Paint the scene gray color
        scene.setFill(Color.GRAY);
        //Set title for game
        stage.setTitle("Maze Ball");
        //Set scene to the primary stage
        stage.setScene(scene);
        //Show the stage
        stage.show();
    }

    public static void displayGameWon(){
        //Create a new pane
        pane = new Pane();
        //Create a new rectangle
        Rectangle rectangle = new Rectangle(0, 100, 512, 302);
        //Create a new image
        Image rectangleImage = new Image("/congrats.png");
        //Fill rectangle with image
        rectangle.setFill(new ImagePattern(rectangleImage));
        //Add rectangle to the pane
        pane.getChildren().add(rectangle);
        //Create text wonTheGame
        GameText wonTheGame = new GameText("You won the game", 165, 256, 10, 20);
        //Create text developers
        GameText developers = new GameText("Board Game by Can Gök, Talha Osman Saraç", 50, 500, 10, 18);
        //Add wonTheGame text to the pane
        pane.getChildren().add(wonTheGame.getText());
        //Add developers text to the game
        pane.getChildren().add(developers.getText());
        //Create a new scene
        scene = new Scene(pane, 512, 512);
        //Set title for game
        stage.setTitle("Game Won!");
        //Set scene to the primary stage
        stage.setScene(scene);
        //Show the stage
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}