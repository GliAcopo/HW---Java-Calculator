package org.calculator;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * NOTE: The layout of the calculator will be:
 * Stage
 *      Scene
 *          VBox
 *              TextField
 *              GridPane
 *                  Buttons
 */


// Singleton GUILayout class, so no more than one button stage can be created
public class GUILayout {
    private final ArrayList<Button> buttons = new ArrayList<>(); // Arraylist of buttons to be added to the root VBox (internally generated)


    private static GUILayout instance = null; // Singleton instance of the GUILayout class (internally generated)

    private GUITheme theme = null;                      // Theme to be applied to the GUI, the application will use this theme to create the buttons (internally generated)
    private CalculatorController calculatorController = null;   // Instance of the calculator controller (externally given)

    private Stage stage = null; // Stage to be used to display the GUI (externally given)
    private Scene scene = null; // Scene of the GUI, the stage will be set to this scene (internally generated)
    private VBox V1Root = null; //Root VBox of the GUI, all buttons will be added to this VBox (internally generated)
    private GridPane gridPane = null;
    private TextField calculatorDisplay = null;
    /**
     * Dimensions
     */
    private double stageHeight = -1;            // The height of the stage, that is the height of the system window, will be updated when the window is resized
    private double stageWidth = -1;             // The width of the stage, that is the width of the system window, will be updated when the window is resized
    private double stageMinHeight = 300;
    private double stageMinWidth = 200;
    private final double tolerance = 10;        // Value to add to the minimum dimensions to avoid negative values
    private double stageAvailableHeight = -1;   // Helper variables to calculate the available space in the stage
    private double stageAvailableWidth = -1;

    private double textFieldHeight = -1;    // Height of the calculator screen hence the textField
    private double border = 0;             // Border around the screen
    private double Vspacing = 10;            // Vertical border between buttons and also textField (used in VBox and GridPane)
    private double Hspacing = 10;            // Horizontal border between buttons (used in GridPane)

    private int numberOfVerticalButtons = 5;
    private int numberOfHorizontalButtons = 4;

    /**
     * Constructors
     */
    // Private constructors to prevent instantiation of the singleton class
    private GUILayout() {}

    private GUILayout(GUITheme theme, Stage stage, CalculatorController calculatorController) {
        this.theme = theme;
        this.stage = stage;
        this.calculatorController = calculatorController;
    }

    /**
     * Getter for the singleton class
     * @param theme
     * @param stage
     * @return instance of the singleton class
     */
    public static synchronized GUILayout getInstance(GUITheme theme, Stage stage, CalculatorController calculatorController) {
        if(instance == null){
            instance = new GUILayout(theme, stage, calculatorController);
        }
        return instance;
    }

    /**
     * Sets up the layout of the GUI for the first time
     */
    public void setupLayout() {
        // Get the window dimensions
        this.stageHeight = this.stage.getHeight();
        this.stageWidth = this.stage.getWidth();

        // Set stage minimum size, or else the dimension calculation could go negative value
        if (this.stageMinHeight <= 2 * border + tolerance) {
            this.stageMinHeight = 2 * border + tolerance;
        }
        if (this.stageMinWidth <= 2 * border + tolerance) {
            this.stageMinWidth = 2 * border + tolerance;
        }
        this.stage.setMinHeight(stageMinHeight);
        this.stage.setMinWidth(stageMinWidth);

        // Create variables for the available space in the stage
        this.stageAvailableHeight = this.stageHeight - 2 * border;
        this.stageAvailableWidth = this.stageWidth - 2 * border;

        // Create the first housing for the screen and the GridPane
        // Creates a `VBox` layout with the specified spacing between children. @param spacing the amount of vertical space between each child
        this.V1Root = this.theme.createVBox();
        V1Root.setSpacing(Vspacing);


        // Add the housing to the scene
        try {
            this.scene = this.theme.createScene(this.V1Root, this.stageAvailableWidth, this.stageAvailableHeight);
        }
        catch (NullPointerException e) {
            System.err.println("Error creating scene: stage is null");
            e.printStackTrace();
            System.exit(1);
        }

        // Set the scene to the stage
        this.stage.setScene(this.scene);

        // Create the calculator's screen
        this.calculatorDisplay = new TextField();
        this.calculatorDisplay.setEditable(false);                  // Clearly not a touch screen calculator

        // We let the elements of the screen adapt to the available space so we just need an upper limit
        this.calculatorDisplay.setMaxWidth(Double.MAX_VALUE);

        this.V1Root.getChildren().add(calculatorDisplay);

        // Create the GridPane for the buttons
        this.gridPane = new GridPane();
        //this.gridPane.setHgap(Hspacing);
        //this.gridPane.setVgap(Vspacing);

        // Likewise, for the gridPane dimensions
        this.gridPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        VBox.setVgrow(this.gridPane, Priority.ALWAYS); // Layout area will always try to grow (or shrink), sharing the increase * (or decrease) in space with other layout areas that have a grow * (or shrink) of ALWAYS.

        this.V1Root.getChildren().add(gridPane);

        // rows and columns of the GridPane share space evenly
        for (int j = 0; j < numberOfHorizontalButtons; j++) {
            ColumnConstraints colCos = new ColumnConstraints();
            colCos.setPercentWidth(100.0 / numberOfHorizontalButtons);
            gridPane.getColumnConstraints().add(colCos);
        }
        for (int i = 0; i < numberOfVerticalButtons; i++) {
            RowConstraints rowCos = new RowConstraints();
            rowCos.setPercentHeight(100.0 / numberOfVerticalButtons);
            gridPane.getRowConstraints().add(rowCos);
        }

        // Create the buttons
        for (int i = 0; i < numberOfVerticalButtons; i++) {
            for (int j = 0; j < numberOfHorizontalButtons; j++) {
                Button button = theme.createButton();
                this.buttons.add(button);    // Add button to the list
                // Make each button expand to fill its cell
                button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                this.gridPane.add(button, j, i);
            }
        }

        // Setup the buttons and their behaviors
        // https://jenkov.com/tutorials/javafx/button.html      // Button tutorial
        // https://jenkov.com/tutorials/javafx/textfield.html   // TextField tutorial
        this.buttons.get(0).setText("_7");
        this.buttons.get(0).setOnAction(event -> calculatorDisplay.appendText("7"));
        this.buttons.get(1).setText("_8");
        this.buttons.get(1).setOnAction(event -> calculatorDisplay.appendText("8"));
        this.buttons.get(2).setText("_9");
        this.buttons.get(2).setOnAction(event -> calculatorDisplay.appendText("9"));

        this.buttons.get(3).setText("_/");
        // Append only if not already present and is not empty
        this.buttons.get(3).setOnAction(event -> {if (!(calculatorDisplay.getText().endsWith("/")) && !(calculatorDisplay.getText().isEmpty())){ calculatorDisplay.appendText("/");}});

        this.buttons.get(4).setText("_4");
        this.buttons.get(4).setOnAction(event -> calculatorDisplay.appendText("4"));
        this.buttons.get(5).setText("_5");
        this.buttons.get(5).setOnAction(event -> calculatorDisplay.appendText("5"));
        this.buttons.get(6).setText("_6");
        this.buttons.get(6).setOnAction(event -> calculatorDisplay.appendText("6"));

        this.buttons.get(7).setText("_*");
        this.buttons.get(7).setOnAction(event -> {
            String text = calculatorDisplay.getText();

            boolean lastIsOperand = text.endsWith("*") || text.endsWith("/") || text.endsWith("-") || text.endsWith("+") || text.endsWith(".");
            boolean empty = text.isEmpty();

            if (!lastIsOperand && !empty) {
                calculatorDisplay.appendText("*");
            }
        });

        this.buttons.get(8).setText("_1");
        this.buttons.get(8).setOnAction(event -> calculatorDisplay.appendText("1"));
        this.buttons.get(9).setText("_2");
        this.buttons.get(9).setOnAction(event -> calculatorDisplay.appendText("2"));
        this.buttons.get(10).setText("_3");
        this.buttons.get(10).setOnAction(event -> calculatorDisplay.appendText("3"));

        this.buttons.get(11).setText("_+");
        this.buttons.get(11).setOnAction(event -> {
            String text = calculatorDisplay.getText();

            boolean lastIsOperand = text.endsWith("*") || text.endsWith("/") || text.endsWith("-") || text.endsWith("+") || text.endsWith(".");
            boolean empty = text.isEmpty();

            if (!lastIsOperand && !empty) {
                calculatorDisplay.appendText("+");
            }
        });

        this.buttons.get(12).setText("_0");
        this.buttons.get(12).setOnAction(event -> {
            //boolean empty = calculatorDisplay.getText().isEmpty();
            //calculatorDisplay.appendText(!empty ? "0" : "");
            calculatorDisplay.appendText("0");      // Actually makes sense to append 0 even if the text field is empty
        });

        this.buttons.get(13).setText("_.");
        this.buttons.get(13).setOnAction(event -> {
            String text = calculatorDisplay.getText();

            boolean lastIsOperand = text.endsWith("*") || text.endsWith("/") || text.endsWith("-") || text.endsWith("+") || text.endsWith(".");
            // In a calculator you cannot put more than a dot in the same number
            String[] parts = text.split("[*/+-]");      // Basically a strtok but for Java
            String currentNumber = parts[parts.length - 1];   // I take the last element of the array (the last number)
            boolean alreadyHasDot = currentNumber.contains("."); // Check if the last number already has a dot
            boolean empty = text.isEmpty();

            if (!lastIsOperand && !empty && !alreadyHasDot) {
                calculatorDisplay.appendText(".");
            }
        });
        // Empty button
        this.buttons.get(15).setText("_-"); // The minus sign can be appended at the start even if there is nothing before it (as the first element)
        this.buttons.get(15).setOnAction(event -> {
            String text = calculatorDisplay.getText();

            boolean lastIsOperand = text.endsWith("*") || text.endsWith("/") || text.endsWith("-") || text.endsWith("+") || text.endsWith(".");
            // boolean empty = text.isEmpty(); CAN BE PLACED AS THE FIRST ELEMENT, SO EVEN IF THE CALC IS EMPTY

            if (!lastIsOperand) {
                calculatorDisplay.appendText("-");
            }
        });

        this.buttons.get(16).setText("_AC");
        this.buttons.get(16).setOnAction(event -> calculatorDisplay.clear());
        this.buttons.get(16).setCancelButton(true); // Can be activated by the ESC key

        this.buttons.get(19).setText("_=");
        this.buttons.get(19).setOnAction(event -> this.calculatorController.calculate(this.calculatorDisplay) );
        this.buttons.get(19).setDefaultButton(true); // the "=" button is the default one


        for (Button button : buttons)
        {
            button.setMnemonicParsing(true); // Can be activated by using the ALT key
            button.setWrapText(true); // So that text wraps if the button is too small, not strictly needed for the application but I'm using it for possible future reference for my application
        }
    }


    public void showStage() {
        this.stage.show(); // Show the stage
    }

    public void hideStage() {
        this.stage.hide();
    }



    /**
     * Dimensions getters and setters
     */

    public double getStageMinHeight() {
        return stageMinHeight;
    }

    public void setStageMinHeight(double stageMinHeight) {
        this.stageMinHeight = stageMinHeight;
    }

    public double getStageMinWidth() {
        return stageMinWidth;
    }

    public void setStageMinWidth(double stageMinWidth) {
        this.stageMinWidth = stageMinWidth;
    }

    public double getTextFieldHeight() {
        return textFieldHeight;
    }

    public void setTextFieldHeight(double textFieldHeight) {
        this.textFieldHeight = textFieldHeight;
    }

    public double getBorder() {
        return border;
    }

    public void setBorder(double border) {
        this.border = border;
    }

    public double getVspacing() {
        return Vspacing;
    }

    public void setVspacing(double vspacing) {
        this.Vspacing = vspacing;
    }

    public double getHspacing() {
        return Hspacing;
    }

    public void setHspacing(double hspacing) {
        this.Hspacing = hspacing;
    }

    public TextField getCalculatorDisplay() {
        return calculatorDisplay;
    }

    /**
     * END OF GETTER AND SETTER
     */
}
