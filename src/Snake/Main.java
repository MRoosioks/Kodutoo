package Snake;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

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
    private static final int circleSize = blockSize / 2;

    private Direction direction = Direction.RIGHT;

    private boolean running = false;

    private Timeline timeline = new Timeline();

    private ObservableList<Node> snake;

    private Parent game() {

        Pane root = new Pane();
        root.setPrefSize(programWidth, programHeight);

        Group snakeBody = new Group();
        snake = snakeBody.getChildren();

        Circle food = new Circle(circleSize, circleSize, circleSize / 1.5, Color.DARKGREEN);
        food.setTranslateX((int) (Math.random() * (programWidth - blockSize)) / blockSize * blockSize);
        food.setTranslateY((int) (Math.random() * (programHeight - blockSize)) / blockSize * blockSize);

        KeyFrame frame = new KeyFrame(Duration.seconds(0.1), event -> {

            if (!running)
                return;

            boolean toRemove = snake.size() > 1;

            Node tail = toRemove ? snake.remove(snake.size() - 1) : snake.get(0);

            double tailX = tail.getTranslateX();
            double tailY = tail.getTranslateY();

            switch (direction) {
                case UP:
                    tail.setTranslateX(snake.get(0).getTranslateX());
                    tail.setTranslateY(snake.get(0).getTranslateY() - blockSize);
                    break;
                case DOWN:
                    tail.setTranslateX(snake.get(0).getTranslateX());
                    tail.setTranslateY(snake.get(0).getTranslateY() + blockSize);
                    break;
                case LEFT:
                    tail.setTranslateX(snake.get(0).getTranslateX() - blockSize);
                    tail.setTranslateY(snake.get(0).getTranslateY());
                    break;
                case RIGHT:
                    tail.setTranslateX(snake.get(0).getTranslateX() + blockSize);
                    tail.setTranslateY(snake.get(0).getTranslateY());
                    break;

            }

            if (toRemove)
                snake.add(0, tail);

            for (Node circle : snake) {
                if (circle != tail && tail.getTranslateX() == circle.getTranslateX()
                        && tail.getTranslateY() == circle.getTranslateY()) {
                    break;
                }
            }

            if (tail.getTranslateX() <= -blockSize) {
                tail.setTranslateX(programWidth - blockSize);
                tail.getTranslateY();
            } else if (tail.getTranslateX() >= programWidth) {
                tail.setTranslateX(0);
                tail.getTranslateY();
            } else if (tail.getTranslateY() <= -blockSize) {
                tail.getTranslateX();
                tail.setTranslateY(programHeight - blockSize);
            } else if (tail.getTranslateY() >= programHeight) {
                tail.getTranslateX();
                tail.setTranslateY(0);
            }

            if (tail.getTranslateX() == food.getTranslateX()
                    && tail.getTranslateY() == food.getTranslateY()) {
                food.setTranslateX((int) (Math.random() * (programWidth - blockSize)) / blockSize * blockSize);
                food.setTranslateY((int) (Math.random() * (programHeight - blockSize)) / blockSize * blockSize);

                Circle addBodyPart1 = new Circle(circleSize, circleSize, circleSize, Color.RED);
                addBodyPart1.setTranslateX(tailX);
                addBodyPart1.setTranslateY(tailY);

                snake.add(addBodyPart1);
            }

        });
        root.setStyle("-fx-background-color: #00a3e6;");

        timeline.getKeyFrames().add(frame);
        if (running = true)
            timeline.setCycleCount(Timeline.INDEFINITE);

        root.getChildren().addAll(food, snakeBody);

        return root;


    }

    private void startGame() {
        Circle head = new Circle(circleSize, circleSize, circleSize, Color.RED);
        snake.add(head);
        direction = Direction.RIGHT;
        running = true;
        timeline.play();
    }



    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setResizable(false);

        Scene game = new Scene(game());
        game.setOnKeyPressed(event -> {

                    switch (event.getCode()) {
                        case W:
                        case UP:

                            if (direction != Direction.DOWN)
                                direction = Direction.UP;
                            break;
                        case S:
                        case DOWN:

                            if (direction != Direction.UP)
                                direction = Direction.DOWN;
                            break;
                        case A:
                        case LEFT:

                            if (direction != Direction.RIGHT)
                                direction = Direction.LEFT;
                            break;
                        case D:
                        case RIGHT:

                            if (direction != Direction.LEFT)
                                direction = Direction.RIGHT;
                            break;
                        case ENTER:
                                snake.clear();
                                startGame();
                    }
                });

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

            primaryStage.setOnCloseRequest(e -> {
                e.consume();
                sulgeProgramm();
            });

            primaryStage.setScene(game);
            primaryStage.setTitle("Mäng");
            primaryStage.show();
        }

        private void sulgeProgramm () {
            Boolean vastus = Confirmation.display("Kas sa oled kindel?", "Kas sa soovid mängimise lõpetada?");
            if (vastus)
                primaryStage.close();
        }
    }