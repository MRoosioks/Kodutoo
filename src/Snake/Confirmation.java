package Snake;

import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

public class Confirmation {


    static boolean answer;

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