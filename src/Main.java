//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
/*
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.print("Hello and welcome!");

    }
}
*/

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class Main {

    public static void main(String[] args) {

        Application.launch(MyJavaFXApp.class, args);
    }


    public static class MyJavaFXApp extends Application {

        Label label1;
        Button button1;
        int i = 1;

        @Override
        public void start(Stage stage) throws Exception {
            stage.setTitle("My First Stage Title");

            label1 = new Label("My first Label");

            VBox root = new VBox(10);


            Scene scene = new Scene(root, 300, 200);
            stage.setScene(scene);

            button1 = new Button("My first button");


            button1.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    System.out.println("Hello World!!!");
                    label1.setText("Try " + i);
                    i++;
                }
            });

            root.getChildren().addAll(label1, button1);
            stage.show();
        }
    }
}
