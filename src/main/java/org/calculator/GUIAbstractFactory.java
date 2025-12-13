package org.calculator;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

// Abstract factory GOF that lets us choose between light and dark mode
public abstract class GUIAbstractFactory {
    public Button createButton(){
        return new Button();
    }

    //https://jenkov.com/tutorials/javafx/stage.html
    public void setupStage(Stage stage){
        stage.setTitle("Calculator");
        stage.initStyle(StageStyle.DECORATED);// A decorated Stage is a standard window with OS decorations (title bar and minimize / maximize / close buttons), and a white background.
    }

    public Scene createScene(Pane root, double width, double height) {
        return new Scene(root, width, height);
    }

    public VBox createVBox(){
        return new VBox();
    }
}
