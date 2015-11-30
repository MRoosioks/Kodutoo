package Snake;

import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

public class Confirmation {


    static boolean vastus;

    public static boolean display(String pealkiri, String s6num) {
        Stage aken = new Stage();
        aken.initModality(Modality.APPLICATION_MODAL);
        aken.setTitle(pealkiri);
        aken.setMinWidth(400);
        aken.setMinHeight(175);
        Label silt = new Label();
        silt.setText(s6num);

        Button jahNupp = new Button("Jah");
        Button eiNupp = new Button("Ei");


        jahNupp.setOnAction(e -> {
            vastus = true;
            aken.close();
        });
        eiNupp.setOnAction(e -> {
            vastus = false;
            aken.close();
        });

        VBox asetus = new VBox(15);

        asetus.getChildren().addAll(silt, jahNupp, eiNupp);
        asetus.setAlignment(Pos.CENTER);
        Scene stseen = new Scene(asetus);
        aken.setScene(stseen);
        aken.showAndWait();

        return vastus;
    }

}