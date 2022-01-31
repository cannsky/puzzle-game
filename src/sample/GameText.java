package sample;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class GameText {

    /*
        Student Information: 150118014 Can Gök, 150118006 Talha Osman Saraç,
        Integrated Development Environment: Intellij Community Edition,
        Some of the pictures are from https://pngfuel.com

        Description of the Class:

        #   GameText class is a defined text class for the texts in the game for reusability
        #   It is being placed to panes through getText() and text will be updated via setText
     */

    private Text text;

    public GameText(String text, int x, int y, int width, int height){
        this.text = new Text(width, height, text);
        this.text.setFont(Font.font("Verdana", height));
        this.text.setFill(Color.BLACK);
        this.text.setX(x);
        this.text.setY(y);
    }

    public Text getText() {
        return text;
    }

    public void setText(String text) {
        this.text.setText(text);
    }

}
