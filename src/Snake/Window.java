package Snake;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Created by Madis on 17.11.2015.
 */
public class Window extends Application {

    Stage aken;
    Scene stseen1, stseen2, stseen3;

    public static void main(String[] args) {

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        aken = primaryStage;


        StackPane root = new StackPane();
        Scene stseen1 = new Scene(root, 300, 300);
        Button nupp1 = new Button("Mängi!");
        nupp1.setOnAction(e -> aken.setScene(stseen2));
        VBox asetus1 = new VBox(20);
        root.getChildren().addAll(nupp1);
        asetus1.setAlignment(Pos.CENTER);

        Button nupp2 = new Button("Mängi uuesti!");
        nupp2.setOnAction(e -> aken.setScene(stseen1));
        VBox asetus2 = new VBox(20);
        stseen2 = new Scene(asetus2, 300, 300);
        asetus2.getChildren().add(nupp2);
        asetus2.setAlignment(Pos.CENTER);

        aken.setOnCloseRequest(e -> {
            e.consume();
            sulgeProgramm();
        });


        Button nupp3 = new Button("Välju!");
        nupp3.setOnAction(e -> Platform.exit());
        asetus2.getChildren().add(nupp3);

        aken.setScene(stseen1);
        aken.setTitle("Ussimäng");
        aken.show();
    }

    private void sulgeProgramm() {
        Boolean vastus = Confirmation.display("Kas sa oled kindel?", "                Kas sa soovid mängimise lõpetada?               ");
        if (vastus)
            aken.close();
    }
}