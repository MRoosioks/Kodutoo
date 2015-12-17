package Snake;

import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

/**
 * This class is for creating popup window so that the user can't close the program instantly.
 * @author Madis Roosioks
 * @version 1.0, December 2015
 */
public class Confirmation {


    static boolean answer;

    /**
     * This class creates popup window which has two buttons
     * @param title Title of the popup window which will be displayed to the user
     * @param message Message what will be displayed to the user.
     * @return returns the boolean answer either true or false.
     */
    public static boolean display(String title, String message) {
        Stage primaryStage = new Stage();
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.setTitle(title);
        primaryStage.setMinWidth(400);
        primaryStage.setMinHeight(175);
        Label label = new Label();
        label.setText(message);

        Button yBtn = new Button("Jah");
        Button nBtn = new Button("Ei");

        yBtn.setOnAction(e -> {
            answer = true;
            System.exit(0);
        });
        nBtn.setOnAction(e -> {
            answer = false;
            primaryStage.close();
        });

        VBox layout = new VBox(15);

        layout.getChildren().addAll(label, yBtn, nBtn);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        primaryStage.setScene(scene);
        primaryStage.showAndWait();

        return answer;
    }
}