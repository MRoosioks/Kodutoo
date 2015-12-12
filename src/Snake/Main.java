package Snake;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Created by Madis on 17.11.2015.
 */
public class Main extends Application {

    Stage primaryStage;

    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    public static final int blockSize = 20;
    public static final int programWidth = 30 * blockSize;
    public static final int programHeight = 25 * blockSize;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setResizable(false);

        VBox layout1 = new VBox(20);
        layout1.setStyle("-fx-background-color: #00a3e6;");
        layout1.setAlignment(Pos.CENTER);

        Text welcomeText = new Text("Teretulemast mängima ussimängu.");
        welcomeText.setFont(Font.font("arial", FontWeight.BOLD, FontPosture.ITALIC, 30));
        welcomeText.setFill(Color.AQUAMARINE);

        Label keys = new Label("Liigu üles: W / Up Arrowkey\n" +
                "Liigu alla: S / Down Arrowkey\n" +
                "Liigu paremale: D / Right Arrowkey\n" +
                "Liigu vasakule: A / Left Arrowkey\n" +
                "Mäng pausile / Jätka mängu: P\n" +
                "Uus mäng: ENTER\n" +
                "Välju mängust: ESC");

        Button settingsBtn = new Button("Mängima");
        settingsBtn.setStyle("-fx-font: 24 arial;");

        Button exitBtn1 = new Button("Välju");
        exitBtn1.setStyle("-fx-font: 24 arial;");
        exitBtn1.setOnAction(e -> System.exit(0));
        layout1.getChildren().addAll(welcomeText, settingsBtn, exitBtn1, keys);
        Scene gameMenu = new Scene(layout1, programWidth, programHeight);

        primaryStage.setOnCloseRequest(e -> {
            e.consume();
            sulgeProgramm();
        });

        primaryStage.setScene(gameMenu);
        primaryStage.setTitle("Mäng");
        primaryStage.show();
    }

    private void sulgeProgramm() {
        Boolean vastus = Confirmation.display("Kas sa oled kindel?", "Kas sa soovid mängimise lõpetada?");
        if (vastus)
            primaryStage.close();
    }
}
