package sample;

import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

    /*
        Integrated Development Environment: Intellij Community Edition,
        Some of the pictures are from https://pngfuel.com

        Description of the Class:

        #   Tile is a class which has multiple methods for changing the state of the tile,
     */

public class Tile extends Main {

    //Create a new variable for storing the id of the Tile
    private int id;
    //Create a new variable for strong the index of the Tile
    private int index;
    //Create a new variable for storing the type of the Tile
    private int type;
    //Create a new variable for storing the direction of the Tile
    private int direction;
    //Create a new Java-Fx rectangle object for the Tile object
    private Rectangle rectangle;
    //Create a new rectangle image
    private Image rectangleImage;
    //Create types for the tiles
    private final static String[] types = { "starter", "end", "empty", "pipe", "pipestatic" };
    //Create directions for the tiles
    private final static String[] directions = { "vertical", "horizontal", "free", "none", "00", "01", "10", "11" };

    public Tile(int id, String type, String direction){
        //Set id
        this.setId(id);
        //Set index
        this.setIndex(id - 1);
        //Set type
        this.setType(type.toLowerCase());
        //Set direction
        this.setDirection(direction.toLowerCase());
        //Create rectangle
        if(this.getDirection() != 2) this.createRectangle();
        else this.rectangle = new Rectangle();
    }

    public void createRectangle(){
        /*
            Create the specific rectangle for the Tile object
            using the id of the Tile object for replacing to the
            location of the rectangle and using the type for filling
            it with the specific image.
         */
        this.setRectangleImage();
        this.rectangle = new Rectangle(128*((this.getId()-1) % 4), 128 * ((this.getId() - 1) / 4), 128, 128);
        this.rectangle.setStroke(Color.BLACK);
        this.rectangle.setFill(new ImagePattern(this.rectangleImage));
    }

    public static void moveTile(int finalA, MouseEvent e){
        /*
            Move rectangles to the new locations,
            Divide to 256 to get new locations coefficient,
            Multiply with 256 to get new location.
        */
        Rectangle rectangle = tiles.get(finalA).getRectangle();
        if((int) e.getX() >= 0 && (int) e.getX() < 512 && (int) e.getY() >= 0 && (int) e.getY() < 512) {
            rectangle.setX((int) (e.getX() / 128) * 128);
            rectangle.setY((int) (e.getY() / 128) * 128);
        }
    }

    public static void controlTiles(int finalA, int FX, int FY){
        /*
            Control rectangles x and y locations,
            If there is a conflict int rectangles,
            Change the rectangle's location to the first location.
        */
        Rectangle rectangle = tiles.get(finalA).getRectangle();
        for(int l = 0; l <  tiles.size(); l++)
            if(l != finalA)
                if(rectangle.getX() == tiles.get(l).getRectangle().getX()
                        && rectangle.getY() == tiles.get(l).getRectangle().getY())
                    if(rectangle != tiles.get(l).getRectangle())
                        Tile.resetRectangle(finalA, FX, FY);
    }

    public static void controlCrossMovement(int finalA, int FX, int FY){
        /*
            Control cross movement for rectangle,
            If both x and y of the rectangle has changed,
            Change the rectangle's location to the first location.
        */
        Rectangle rectangle = tiles.get(finalA).getRectangle();
        boolean movedX = rectangle.getX() < FX || rectangle.getX() > FX;
        boolean movedY = rectangle.getY() < FY || rectangle.getY() > FY;
        if((movedX && movedY) || (!movedX && !movedY)) Tile.resetRectangle(finalA, FX, FY);
    }

    public static void controlJump(int finalA, int FX, int FY){
        /*
            Control jumping for rectangle,
            If rectangle x or y moved more than 256 pixels,
            Change the rectangle's location to the first location.
        */
        Rectangle rectangle = tiles.get(finalA).getRectangle();
        if(Math.abs(rectangle.getX() - FX) > 128
                || Math.abs(rectangle.getY() - FY) > 128) Tile.resetRectangle(finalA, FX, FY);
    }

    public static void controlStatic(int finalA, int FX, int FY){
        /*
            Control if tile is a static tile or not,
            Start and end tiles are also static.
         */
        if(!((tiles.get(finalA).getType() > 1) && (tiles.get(finalA).getType() < 4))) resetRectangle(finalA, FX, FY);
    }

    public static void resetRectangle(int finalA, int FX, int FY){
        /*
            Resets the rectangles location with given FX and FY values.
        */
        Rectangle rectangle = tiles.get(finalA).getRectangle();
        rectangle.setX(FX);
        rectangle.setY(FY);
    }

    public static boolean controlMovement(int finalA, int FX, int FY){
        /*
            Control if a tile is moved or not,
            if the tile is moved, GameBoard object
            will look if the level is ended or not.
         */
        Rectangle rectangle = tiles.get(finalA).getRectangle();
        return rectangle.getX() != FX || rectangle.getY() != FY;
    }

    public static Tile findTile(int index){
        /*
            Find the tile with the given index
         */
        for(int i = 0; i < 16; i++)
            if(tiles.get(i).getIndex() == index)
                return tiles.get(i);
            return tiles.get(0);
    }

    public static void swapTiles(int finalA, int FX, int FY){
        /*
            Control if x or y position changed or not,
            if changed find the swapped tile with calculated index,
            change the both indexes of the swapped objects.
         */
        int index = tiles.get(finalA).getIndex();
        Rectangle rectangle = tiles.get(finalA).getRectangle();
        int swappedIndex = (rectangle.getX() != FX)
                ? ((rectangle.getX() > FX) ? index + 1 : index - 1)
                : ((rectangle.getY() != FY) ? ((rectangle.getY() > FY) ? index + 4 : index - 4) : index);
        Tile swappedTile = Tile.findTile(swappedIndex);
        tiles.get(finalA).setIndex(swappedIndex);
        swappedTile.setIndex(index);
    }

    //Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getType() {
        return type;
    }

    public void setType(String type) {
        //Compare the type and types
        for(int i = 0; i < types.length; i++)
            if(types[i].equals(type))
                this.type = i;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        //Compare the direction and directions
        for(int i = 0; i < directions.length; i++)
            if(directions[i].equals(direction))
                this.direction = i;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void setRectangleImage() {
        this.rectangleImage = new Image("/" + types[this.type] + directions[this.direction] + ".png");
    }
}
