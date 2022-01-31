package sample;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class GameBoard extends Main{

    /*
        Student Information: 150118014 Can Gök, 150118006 Talha Osman Saraç,
        Integrated Development Environment: Intellij Community Edition,
        Some of the pictures are from https://pngfuel.com

        Description of the Class:

        #   GameBoard is a class which has multiple methods for changing the state of the game board or pane,
        #   This class finds the correct way to complete the game and updates this information for animations.
     */

    //Level of the game board, starting level is 1
    private static int level = 1;
    //Map of the completed game board
    private static final ArrayList<Integer> directions = new ArrayList<>();
    //Direction types of the directions
    private static final ArrayList<Integer> directionTypes = new ArrayList<>();
    //Direction ids
    private static final ArrayList<Integer> directionIds = new ArrayList<>();

    public GameBoard(){
        //Read level file and update GameBoard
        this.readFile();
    }

    public void readFile(){
        /*
            Uses the current level of the player,
            to read the contents of the level file.
         */
        //Clear tiles
        tiles.clear();
        File levelFile = new File("src/level" + level++ + ".txt");
        try{
            Scanner levelReader = new Scanner(levelFile);
            do{
                String newLine = levelReader.nextLine();
                if(!newLine.equals("")){
                    String[] parts = newLine.split(",");
                    //Create a new tile object with the information
                    tiles.add(new Tile(Integer.parseInt(parts[0]), parts[1], parts[2]));
                }

            } while(levelReader.hasNextLine());
        } catch(FileNotFoundException e){
            isGameWon = true;
        }
    }

    public static boolean controlLevel(){
        /*
            Settings for direction,
            0 -> top,
            1 -> right,
            2 -> bottom,
            3 -> left,
         */
        //New directions
        GameBoard.directions.clear();
        //New direction types
        GameBoard.directionTypes.clear();
        //New direction ids
        GameBoard.directionIds.clear();
        //Create a new index
        int index = 0;
        //Update index with starter tile index
        for(int i = 0; i < tiles.size(); i++)
            if(tiles.get(i).getType() == 0)
                index = i;
        //Integer value for direction
        Integer direction = null;
        //Create a new loop for looking if the way goes to end
        for(int i = 0; i < 16; i++){
            //Create a new tile  reference
            Tile tile = Tile.findTile(index);
            //If the next tile is end, the level is finished
            if(tile.getType() == 1) return GameBoard.directions.add(direction) && GameBoard.directionTypes.add(tile.getDirection()) && GameBoard.directionIds.add(tile.getId());
            //If the next tile is empty, the level isn't finished
            if(tile.getType() == 2) return false;
            //If the index is out of range, the level isn't finished
            if(!(index >= 0 && index <= 15)) return false;
            //Get the last direction and the type of the tile for defining the new direction
            if(direction != null) direction = GameBoard.findDirection(direction, tile.getDirection());
            //Set it go to bottom if vertical, go to left if horizontal for the starter tile
            if(direction == null) direction = (tile.getDirection() == 0) ? 0 : 3;
            //If direction is 10, level isn't finished
            if(direction == 10) return false;
            //Sets how much the index will change, according to the direction
            int changeIndex = GameBoard.setChangeIndex(direction);
            //Update the index with changeIndex
            index += changeIndex;
            //Update the map level with direction
            GameBoard.directions.add(direction);
            //Update the direction type of the direction
            GameBoard.directionTypes.add(tile.getDirection());
            //Update the direction ids
            GameBoard.directionIds.add(tile.getId());
        }
        //If the loop is finished, level isn't finished
        return false;
    }

    public static int setChangeIndex(Integer direction){
        /*
            Returns the new index according to the direction
         */
        return switch (direction) {
            case 0 -> +4;
            case 1 -> +1;
            case 2 -> -4;
            case 3 -> -1;
            default -> 0;
        };
    }

    public static int findDirection(Integer direction, int directionType){
        /*
            Finds which method will be used to find the direction,
            with looking direction type.
         */
        return (directionType < 3) ? ((directionType == 0) ? findVerticalDirection(direction) : findHorizontalDirection(direction)) : findCurvedDirection(direction, directionType);
    }

    public static int findHorizontalDirection(Integer direction){
        /*
            Static find horizontal direction method
            will look if direction is horizontal or not.
            If it is not horizontal it will return value of 10
            which will finish the control of the level with false value
         */
        return (direction == 1 || direction == 3) ? direction : 10;
    }

    public static int findVerticalDirection(Integer direction){
        /*
            Static find vertical direction method
            will look if direction is vertical or not.
            If it is not vertical it will return value of 10
            which will finish the control of the level with false value
         */
        return (direction == 0 || direction == 2) ? direction : 10;
    }

    public static int findCurvedDirection(Integer direction, int directionType){
        /*
            Static find curved direction method
            will look for the directions coming
            from the both sides of the curved pipe.
            If the direction is not same it will return value of 10
            which will finish the control of the level with false value
         */
        return switch (directionType) {
            case 4 -> (direction == 0) ? 3 : (direction == 1) ? 2 : 10;
            case 5 -> (direction == 0) ? 1 : (direction == 3) ? 2 : 10;
            case 6 -> (direction == 2) ? 3 : (direction == 1) ? 0 : 10;
            case 7 -> (direction == 2) ? 1 : (direction == 3) ? 0 : 10;
            default -> 10;
        };
    }

    public static ArrayList<Integer> getDirections() {
        return directions;
    }

    public static ArrayList<Integer> getDirectionTypes() {
        return directionTypes;
    }

    public static ArrayList<Integer> getDirectionIds() {
        return directionIds;
    }
}
