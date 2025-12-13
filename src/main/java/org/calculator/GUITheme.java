package org.calculator;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class GUITheme {
    private GUIAbstractFactory theme = null;
    private static GUITheme singletonInstance = null;

    // Private constructor so only the class itself can call it
    private GUITheme(){}

    private GUITheme(GUIAbstractFactory theme){
        this.theme = theme;
    }

    public static GUITheme getInstance(GUIAbstractFactory theme){ // static because there must be no need to instance the class in order to call the constructor
        if (singletonInstance == null) {
            singletonInstance = new GUITheme(theme);
        }
        return singletonInstance;
    }

    public Button createButton(){
        return this.theme.createButton();
    }

    public void setupStage(Stage stage){
        this.theme.setupStage(stage);
    }

    public Scene createScene(Pane root, double width, double height){
        return this.theme.createScene(root, width, height);
    }

    public VBox createVBox(){
        return this.theme.createVBox();
    }
}
