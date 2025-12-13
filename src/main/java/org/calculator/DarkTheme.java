package org.calculator;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.paint.Color;


public class DarkTheme extends GUIAbstractFactory {

    // https://jenkov.com/tutorials/javafx/button.html
    @Override
    public Button createButton() {
        Button button = new Button();
        button.setStyle(
                "-fx-background-color: #1a1919;" +
                        "-fx-text-fill: #ffffff;"
        );
        return button;
    }


    //https://jenkov.com/tutorials/javafx/stage.html
    @Override
    public void setupStage(Stage stage){
        stage.setTitle("Calculator");
        stage.initStyle(StageStyle.DECORATED);// A decorated Stage is a standard window with OS decorations (title bar and minimize / maximize / close buttons), and a white background.
    }

    @Override
    public Scene createScene(Pane root, double width, double height){
        Scene scene = new Scene(root, width, height);
        scene.setFill(Color.web("#202020"));
        return scene;
    }

    @Override
    public VBox createVBox(){
        VBox root = new VBox(10);
        root.setStyle("-fx-background-color: #202020; ");
        return root;
    }
}
