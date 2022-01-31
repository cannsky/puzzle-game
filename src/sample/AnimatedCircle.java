package sample;

import javafx.animation.PathTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.Duration;

public class AnimatedCircle extends Main {

    /*
        Student Information: 150118014 Can Gök, 150118006 Talha Osman Saraç,
        Integrated Development Environment: Intellij Community Edition,
        Some of the pictures are from https://pngfuel.com

        Description of the Class:

        #   AnimatedCircle is a class which has multiple methods for changing the state of the circle,
        #   This class has a variety of methods for creating a circle, creating an animation path and displaying it.
        #   All methods of the AnimatedCircle affects the circle in the JavaFX node, which is an object created by that class.
     */

    //Circle's x value
    private static double x;
    //Circle's y value
    private static double y;
    //Circle's radius
    private static double radius;
    //Circle's color
    private static Color color;
    //Circle object
    private static Circle circle;
    //Create a new index
    private static int index = 0;
    //Animation path
    private static final Path animationPath = new Path();
    //Animation counter
    private static int counter = 0;
    //rectangle's x and y coordinates
    private static double[] rectangleCoordinate = new double[2];

    public AnimatedCircle(double x, double y, double radius, Color color){
        //Set x
        this.setX(x);
        //Set y
        this.setY(y);
        //Set radius
        this.setRadius(radius);
        //Set color
        this.setColor(color);
        //Create a new circle object
        this.setCircle();
        //Set counter to zero
        AnimatedCircle.counter = 0;
        //Set index to zero
        AnimatedCircle.index = 0;
        //Clear animationPath
        AnimatedCircle.animationPath.getElements().clear();
        //Update animationPath
        AnimatedCircle.animationPath.getElements().add(new MoveTo(AnimatedCircle.x, AnimatedCircle.y));
    }

    public static void animateCircle(){
        /*
            Note: animateCircle will be called recursively until the index is equal to the number of tiles.
         */
        //If the end tile has been read, display the animation
        if(GameBoard.getDirections().size() == index){ displayAnimation(); return; }
        //Increase the counter by 1
        AnimatedCircle.counter++;
        //Animate the circle according to the direction type and direction
        updateAnimationPath(GameBoard.getDirections().get(index), index++);
    }

    public static void updateAnimationPath(int direction, int index){
        /*
            Draws the animation path according to the direction, counter and index
            Then the animation will be displayed via displayAnimation method
         */
        //Update the x and y values of the current y to the rectangleCoordinate array
        rectangleCoordinate[0] = tiles.get(GameBoard.getDirectionIds().get(index) - 1).getRectangle().getX();
        rectangleCoordinate[1] = tiles.get(GameBoard.getDirectionIds().get(index) - 1).getRectangle().getY();
        //Calls animateEnd, animateVertical, animateHorizontal and animateCurved according to the state
        if (GameBoard.getDirections().size() == index + 1) animateEnd();
        else if ((GameBoard.getDirectionTypes().get(index) < 4))
            if ((direction == 0 || direction == 2)) animateVertical(direction);
            else animateHorizontal(direction);
        else animateCurved(direction, GameBoard.getDirectionTypes().get(index));
    }

    public static void animateVertical(int direction){
        /*
            Animates the circle in vertical direction,
            If the given direction is bottom (0) it will move the circle 256px to bottom,
            otherwise it will move the circle 256 px to top.
         */
        AnimatedCircle.animationPath.getElements().add(new VLineTo((direction == 0) ? rectangleCoordinate[1] + 128 : rectangleCoordinate[1]));
        animateCircle();
    }

    public static void animateHorizontal(int direction){
        /*
            Animates the circle in horizontal direction,
            If the given direction is right (1) it will move the circle 256px to right,
            otherwise it will move the circle 256 px to left.
         */
        AnimatedCircle.animationPath.getElements().add(new HLineTo((direction == 1) ? rectangleCoordinate[0] + 128  : rectangleCoordinate[0]));
        animateCircle();
    }

    public static void animateCurved(int direction, int directionType){
        /*
            Animates the circle in horizontal direction,
            Animation path will be drawn with an ArcTo object.
            ArcTo ->
                radius: 128px both x and y (Circle)
                x and y coordinates will change according to the direction type and direction
                Direction affects the way the animated circle goes and the type affects which coordinates
                the animated circle will go.
            animateCurved uses an array rectangleCoordinate for storing the x and y coordinates of the current tile.
         */
        //Create a new arcTo object
        ArcTo arcTo = new ArcTo();
        //Set radius x to 128
        arcTo.setRadiusX(100);
        //Set radius y to 128
        arcTo.setRadiusY(100);
        //Update rectangleCoordinate according to the direction type and direction
        rectangleCoordinate = (directionType == 4)
                ? ((direction == 3) ? new double[] {rectangleCoordinate[0], rectangleCoordinate[1] + 64}
                : new double[] {rectangleCoordinate[0] + 64, rectangleCoordinate[1]})
                : (directionType == 5) ? ((direction == 1) ? new double[] {rectangleCoordinate[0] + 128, rectangleCoordinate[1] + 64}
                : new double[] {rectangleCoordinate[0], rectangleCoordinate[1] + 64})
                : (directionType == 6) ? ((direction == 0) ? new double[] {rectangleCoordinate[0] + 64, rectangleCoordinate[1] + 128}
                : new double[] {rectangleCoordinate[0], rectangleCoordinate[1] + 64})
                : (directionType == 7) ? ((direction == 0) ? new double[] {rectangleCoordinate[0] + 64, rectangleCoordinate[1] + 128}
                : new double[] {rectangleCoordinate[0] + 128, rectangleCoordinate[1] + 64}) : new double[] {rectangleCoordinate[0], rectangleCoordinate[1]};
        //Update arcTo x
        arcTo.setX(rectangleCoordinate[0]);
        //Update arcTo y
        arcTo.setY(rectangleCoordinate[1]);
        //Update animation path
        AnimatedCircle.animationPath.getElements().add(arcTo);
        //Animate circle
        animateCircle();
    }

    public static void animateEnd(){
        /*
            An improved and mixed version of animateVertical and animateHorizontal methods,
            which creates an animation path for end tile.
         */
        AnimatedCircle.animationPath.getElements().add(
                (GameBoard.getDirectionTypes().get(GameBoard.getDirectionTypes().size() - 1) == 0)
                        ? new VLineTo(rectangleCoordinate[1] + 50)
                        : new HLineTo(rectangleCoordinate[0] + 90));
        animateCircle();
    }

    public static void displayAnimation(){
        /*
            Animation will be displayed according to the animation path
            Animation time is total tile count in seconds
            Animated node is AnimatedCircle object
            When animation is finished display next level
         */
        PathTransition pathTransition = new PathTransition(Duration.seconds(AnimatedCircle.counter), AnimatedCircle.animationPath, AnimatedCircle.circle);
        pathTransition.play();
        pathTransition.setOnFinished(e -> displayLevel());
    }

    //Getters and setters

    public void setX(double x) {
        AnimatedCircle.x = x;
    }

    public void setY(double y) {
        AnimatedCircle.y = y;
    }

    public void setRadius(double radius) {
        AnimatedCircle.radius = radius;
    }

    public void setColor(Color color) {
        AnimatedCircle.color = color;
    }

    public Circle getCircle() {
        return circle;
    }

    public void setCircle() {
        AnimatedCircle.circle = new Circle(AnimatedCircle.x, AnimatedCircle.y, AnimatedCircle.radius, AnimatedCircle.color);
    }
}
