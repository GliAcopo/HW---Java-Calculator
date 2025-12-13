package org.calculator;

import javafx.application.Application;
import javafx.stage.Stage;

import java.util.List;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        Application.launch(CalculatorApp.class, args);
    }

    public static class CalculatorApp extends Application {
        List<String> args = null;
        GUITheme theme = null;

        // NOTE: start() is called only once in a JavaFX application.
        // This method will be launched as a main
        @Override
        public void start(Stage stage) throws Exception {
            boolean dark = true;

            args = getParameters().getRaw(); // Get the main arguments

            // Polymorphism
            // @warning: List.getFirst() is Java 21 specific, get(0) is better
            // @warning: Launching the application without main arguments and accessing the first element via args.getFirst() or args.get(0) will throw a NoSuchElementException or IndexOutOfBoundsException, crashing the app immediately on startup. So we need to check if the list is empty first.
            if ((!args.isEmpty() && args.get(0).equals("dark")) || dark) {
                theme = GUITheme.getInstance(new DarkTheme());
            }
            else if (!args.isEmpty() && Objects.equals(args.get(0), "light")) {  // Two ways to access the first element of the list
                theme = GUITheme.getInstance(new LightTheme());
            }
            else { // default theme
                theme = GUITheme.getInstance(new LightTheme());
            }
            CalculatorController calculatorController = new CalculatorController();
            GUILayout.getInstance(theme, stage, calculatorController).setupLayout();
            GUILayout.getInstance(theme, stage, calculatorController).showStage();

        }
    }
}


/**
 * NOTES:
 * The Stage is the window itself:
 * The title bar
 * The close / minimize / maximize buttons
 * The native OS window
 *
 * Scene = the content inside the window
 * You put a Scene inside a Stage, the scene contains all the elements and windows. But not raw, you organize them in Boxes or other things:
 *
 * VBox = a vertical layout node inside the scene
 * It organizes the elements you put inside of it vertically.
 */
