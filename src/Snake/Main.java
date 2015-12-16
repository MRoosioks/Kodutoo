package Snake;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;


public class Main extends Application {

    Stage primaryStage;

    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    public static final int blockSize = 20;
    public static final int programWidth = 30 * blockSize;
    public static final int programHeight = 25 * blockSize;
    public static final int circleSize = blockSize / 2;
    public int points;

    private Direction direction = Direction.RIGHT;

    private boolean running = false;
    private boolean gameOver, newGame, paused;

    private Timeline timeline = new Timeline();

    private ObservableList<Node> snake;

    private Parent firstScene() {

        VBox firstSceneLayout = new VBox(20);
        firstSceneLayout.setStyle("-fx-background-color: #00a3e6;");
        firstSceneLayout.setAlignment(Pos.CENTER);
        firstSceneLayout.setPrefSize(programWidth, programHeight);

        Text welcomeText = new Text("Teretulemast mängima ussimängu.");
        welcomeText.setFont(Font.font("arial", FontWeight.BOLD, FontPosture.ITALIC, 30));
        welcomeText.setFill(Color.AQUAMARINE);

        Label settings = new Label("Mängimiseks vajuta ENTER");
        settings.setStyle("-fx-font: 18 arial;");

        Button exitBtn1 = new Button("Välju");
        exitBtn1.setStyle("-fx-font: 20 arial;");
        exitBtn1.setOnAction(e -> System.exit(0));

        firstSceneLayout.getChildren().addAll(welcomeText, settings, exitBtn1);

        return firstSceneLayout;
    }

    private Parent game() {

        Pane root = new Pane();
        root.setPrefSize(programWidth, programHeight);

        Group snakeBody = new Group();
        snake = snakeBody.getChildren();

        Label points = new Label("Punktid: " + this.points);
        points.setTextFill(Color.WHITE);
        points.setLayoutX(500);
        points.setLayoutY(0);

        Label controls = new Label("Liigu üles: W / Üles nool\n" + "Liigu alla: S / Alla nool\n" + "Liigu paremale: D / Paremale nool\n" +
                "Liigu vasakule: A / Vasakule nool\n" + "Mäng pausile / Jätka mängu: P\n" + "Uus mäng: ENTER\n" +
                "Mängu seaded: Backspace\n" + "Välju mängust: ESC");
        controls.setTextFill(Color.WHITE);
        controls.setLayoutX(programWidth / 3);
        controls.setLayoutY(programHeight / 3);

        Circle food = new Circle(circleSize, circleSize, circleSize / 1.5, Color.DARKGREEN);
        food.setTranslateX((int) (Math.random() * (programWidth - blockSize)) / blockSize * blockSize);
        food.setTranslateY((int) (Math.random() * (programHeight - blockSize)) / blockSize * blockSize);

        KeyFrame frame = new KeyFrame(Duration.seconds(0.1), event -> {

            controls.setVisible(false);

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
                    controls.setVisible(true);
                    stopGame();
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
                this.points += 15;
                points.setText("Punktid: " + this.points);

                Circle addBodyPart1 = new Circle(circleSize, circleSize, circleSize * 1.2, Color.RED);
                Circle addBodyPart2 = new Circle(circleSize, circleSize, circleSize * 1.2, Color.RED);
                Circle addBodyPart3 = new Circle(circleSize, circleSize, circleSize * 1.2, Color.RED);
                addBodyPart1.setTranslateX(tailX);
                addBodyPart1.setTranslateY(tailY);
                addBodyPart2.setTranslateX(tailX);
                addBodyPart2.setTranslateY(tailY);
                addBodyPart3.setTranslateX(tailX);
                addBodyPart3.setTranslateY(tailY);
                snake.addAll(addBodyPart1, addBodyPart2, addBodyPart3);
            }

        });
        root.setStyle("-fx-background-color: #00a3e6;");

        timeline.getKeyFrames().add(frame);
        if (running = true)
            timeline.setCycleCount(Timeline.INDEFINITE);

        root.getChildren().addAll(food, snakeBody, controls, points);

        return root;


    }

    private void stopGame() {
        running = false;
        timeline.stop();
        newGame = false;
        gameOver = true;
        paused = false;
    }

    private void startGame() {
        Circle head = new Circle(circleSize, circleSize, circleSize * 1.2, Color.RED);
        Circle startWithBodyPart1 = new Circle(circleSize, circleSize, circleSize * 1.2, Color.RED);
        Circle startWithBodyPart2 = new Circle(circleSize, circleSize, circleSize * 1.2, Color.RED);
        snake.addAll(head, startWithBodyPart1, startWithBodyPart2);
        this.points = 0;
        direction = Direction.RIGHT;
        running = true;
        timeline.play();
        newGame = true;
        gameOver = false;
        paused = false;
    }

    private void pauseGame() {
        running = true;
        timeline.pause();
        newGame = false;
        gameOver = false;
        paused = true;
    }

    private void resumeGame() {
        running = true;
        timeline.play();
        newGame = true;
        gameOver = false;
        paused = false;
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setResizable(false);

        Scene gameMenu = new Scene(firstScene());

        Scene game = new Scene(game());
        game.setOnKeyPressed(event -> {

            switch (event.getCode()) {
                case W:
                case UP:
                    if (newGame)
                        if (direction != Direction.DOWN)
                            direction = Direction.UP;
                    break;
                case S:
                case DOWN:
                    if (newGame)
                        if (direction != Direction.UP)
                            direction = Direction.DOWN;
                    break;
                case A:
                case LEFT:
                    if (newGame)
                        if (direction != Direction.RIGHT)
                            direction = Direction.LEFT;
                    break;
                case D:
                case RIGHT:
                    if (newGame)
                        if (direction != Direction.LEFT)
                            direction = Direction.RIGHT;
                    break;
                case P:
                    if ((!gameOver || !paused) && newGame)
                        pauseGame();
                    else
                        resumeGame();
                    break;
                case BACK_SPACE:
                    if ((!newGame && !paused) || (gameOver))
                        primaryStage.setScene(gameMenu);
                case ENTER:
                    if ((!newGame && !paused) || (gameOver)) {
                        snake.clear();
                        startGame();
                    }
                    break;
                case ESCAPE:
                    System.exit(0);
            }
        });

        gameMenu.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER)
                primaryStage.setScene(game);
        });

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