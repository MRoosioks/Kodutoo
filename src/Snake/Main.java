package Snake;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
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

    private boolean gameOver, newGame, paused, gameMode, music;

    private Timeline timeline = new Timeline();

    private ObservableList<Node> snake;

    Media gameEnd = new Media("file:///C:/Users/Madis/workspace/Kodutoo/src/GAME_OVER.mp3/");
    MediaPlayer player1 = new MediaPlayer(gameEnd);
    Media backgroundMusic = new Media("file:///C:/Users/Madis/workspace/Kodutoo/src/Background_music.mp3/");
    MediaPlayer player2 = new MediaPlayer(backgroundMusic);

    private Parent firstScene() {

        VBox firstSceneLayout = new VBox(20);
        firstSceneLayout.setAlignment(Pos.CENTER);
        firstSceneLayout.setPrefSize(programWidth, programHeight);

        Text welcomeText = new Text("Teretulemast mängima ussimängu.");
        welcomeText.getStyleClass().addAll("title");

        Label settings = new Label("Mängimiseks vajuta ENTER");
        settings.getStyleClass().add("enterGame");

        Button exitBtn1 = new Button("Välju");
        exitBtn1.getStyleClass().addAll("exitButton", "exitButton:hover");
        exitBtn1.setOnAction(e -> System.exit(0));

        firstSceneLayout.getChildren().addAll(welcomeText, settings, exitBtn1);

        return firstSceneLayout;
    }

    private Parent secondScene() {

        // loon kõik layoutid
        VBox settingsGameMode = new VBox(10);
        VBox settingsSound = new VBox(10);
        StackPane playGame = new StackPane();
        StackPane settingsTitle = new StackPane();
        BorderPane secondSceneLayout = new BorderPane();

        // teen pealkirja ning sean stiili
        Text title = new Text("Mänguseaded");
        title.getStyleClass().add("title");

        // teen teksi, et mängimiseks vajuta enter ning sean ka aukoha ning stiili
        Label play = new Label("Mängimiseks vajuta ENTER");
        play.getStyleClass().add("enterGame");

        // loon labelid  ja radiobuttonid ning annan neile nimed
        Label chooseGamemode = new Label("Mängustiil");
        RadioButton withWalls = new RadioButton("Seintega mäng");
        RadioButton withoutWalls = new RadioButton("Seinteta mäng ");
        Label sound = new Label("Heli");
        RadioButton on = new RadioButton("Sees");
        RadioButton off = new RadioButton("Väljas");

        // annan muutujale difficulty väärtuse olenevalt sellele, mis nuppu vajutatakse
        withWalls.setOnAction(e -> gameMode = true);
        withoutWalls.setOnAction(e -> gameMode = false);
        on.setOnAction(e -> music = true);
        off.setOnAction(e -> music = false);

        // jaotan radiobuttonid gruppidesse
        ToggleGroup gameMode = new ToggleGroup();
        withWalls.setToggleGroup(gameMode);
        withoutWalls.setToggleGroup(gameMode);
        ToggleGroup gameSound = new ToggleGroup();
        on.setToggleGroup(gameSound);
        off.setToggleGroup(gameSound);

        //sean 2 radiobuttonit kohe valituks
        withoutWalls.setSelected(true);
        off.setSelected(true);

        // sean asukohad
        secondSceneLayout.setPrefSize(programWidth, programHeight);
        secondSceneLayout.setLeft(settingsSound);
        secondSceneLayout.setRight(settingsGameMode);
        secondSceneLayout.setBottom(playGame);
        secondSceneLayout.setTop(settingsTitle);

        // sean asukohad layoutidel
        BorderPane.setMargin(settingsSound, new Insets(50, 50, 50, 100));
        BorderPane.setMargin(settingsGameMode, new Insets(50, 100, 50, 50));
        BorderPane.setMargin(playGame, new Insets(50, 0, 50, 0));
        BorderPane.setMargin(settingsTitle, new Insets(50, 0, 50, 0));

        // lisan kõik kokku
        playGame.getChildren().add(play);
        settingsTitle.getChildren().add(title);
        settingsGameMode.getChildren().addAll(chooseGamemode, withoutWalls, withWalls);
        settingsSound.getChildren().addAll(sound, off, on);

        // tagastan teise scene layouti
        return secondSceneLayout;
    }

    private Parent game() {

        player1.setVolume(0.5);

        Pane root = new Pane();
        root.setPrefSize(programWidth, programHeight);

        Group snakeBody = new Group();
        snake = snakeBody.getChildren();

        Label points = new Label("Punktid: " + this.points);
        points.getStyleClass().add("gameText");
        points.setLayoutX(programWidth - 100);
        points.setLayoutY(programHeight - 490);

        Label controls = new Label("Liigu üles: W / Üles nool\n" + "Liigu alla: S / Alla nool\n" + "Liigu paremale: D / Paremale nool\n" +
                "Liigu vasakule: A / Vasakule nool\n" + "Mäng pausile / Jätka mängu: P\n" + "Uus mäng: ENTER\n" +
                "Mängu seaded: Backspace\n" + "Välju mängust: ESC");
        controls.getStyleClass().add("gameText");
        controls.setLayoutX(programWidth / 3);
        controls.setLayoutY(programHeight / 3);

        Circle food = new Circle(circleSize, circleSize, circleSize / 1.5, Color.DARKGREEN);
        food.setTranslateX((int) (Math.random() * (programWidth - blockSize)) / blockSize * blockSize);
        food.setTranslateY((int) (Math.random() * (programHeight - blockSize)) / blockSize * blockSize);

        KeyFrame frame = new KeyFrame(Duration.seconds(0.1), event -> {

            controls.setVisible(false);

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
                    if (music)
                        player1.play();
                    stopGame();
                    break;
                }
            }
            if (!gameMode) {
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
            }

            if (tail.getTranslateX() < 0 || tail.getTranslateX() >= programWidth
                    || tail.getTranslateY() < 0 || tail.getTranslateY() >= programHeight) {
                controls.setVisible(true);
                if (music)
                    player1.play();
                stopGame();
            }

            if (tail.getTranslateX() == food.getTranslateX()
                    && tail.getTranslateY() == food.getTranslateY()) {
                food.setTranslateX((int) (Math.random() * (programWidth - blockSize)) / blockSize * blockSize);
                food.setTranslateY((int) (Math.random() * (programHeight - blockSize)) / blockSize * blockSize);
                this.points += 15;
                points.setText("Punktid: " + this.points);

                Circle addBodyPart1 = new Circle(circleSize, circleSize, circleSize, Color.PURPLE);
                Circle addBodyPart2 = new Circle(circleSize, circleSize, circleSize, Color.PURPLE);
                Circle addBodyPart3 = new Circle(circleSize, circleSize, circleSize, Color.PURPLE);
                addBodyPart1.setTranslateX(tailX);
                addBodyPart1.setTranslateY(tailY);
                addBodyPart2.setTranslateX(tailX);
                addBodyPart2.setTranslateY(tailY);
                addBodyPart3.setTranslateX(tailX);
                addBodyPart3.setTranslateY(tailY);
                snake.addAll(addBodyPart1, addBodyPart2, addBodyPart3);
            }
        });

        timeline.getKeyFrames().add(frame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        root.getChildren().addAll(food, snakeBody, controls, points);

        return root;
    }

    private void stopGame() {
        player2.stop();
        timeline.stop();
        newGame = false;
        gameOver = true;
        paused = false;
    }

    private void startGame() {
        Circle head = new Circle(circleSize, circleSize, circleSize, Color.PURPLE);
        Circle startWithBodyPart1 = new Circle(circleSize, circleSize, circleSize, Color.PURPLE);
        Circle startWithBodyPart2 = new Circle(circleSize, circleSize, circleSize, Color.PURPLE);
        Circle startWithBodyPart3 = new Circle(circleSize, circleSize, circleSize, Color.PURPLE);
        snake.addAll(head, startWithBodyPart1, startWithBodyPart2, startWithBodyPart3);
        points = 0;
        player1.stop();
        direction = Direction.RIGHT;
        timeline.play();
        newGame = true;
        gameOver = false;
        paused = false;
        if (music)
            player2.play();
    }

    private void pauseGame() {
        player2.pause();
        timeline.pause();
        newGame = false;
        gameOver = false;
        paused = true;
    }

    private void resumeGame() {
        player2.play();

        timeline.play();
        newGame = true;
        gameOver = false;
        paused = false;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setResizable(false);
        Scene gameMenu = new Scene(firstScene());
        Scene settings = new Scene(secondScene());
        Scene game = new Scene(game());

        gameMenu.getStylesheets().add("Design.css");
        settings.getStylesheets().add("Design.css");
        game.getStylesheets().add("Design.css");

        game.setOnKeyPressed(event -> {

            switch (event.getCode()) {
                case W:
                case UP:
                    if (direction != Direction.DOWN && newGame)
                        direction = Direction.UP;
                    break;
                case S:
                case DOWN:
                    if (direction != Direction.UP && newGame)
                        direction = Direction.DOWN;
                    break;
                case A:
                case LEFT:
                    if (direction != Direction.RIGHT && newGame)
                        direction = Direction.LEFT;
                    break;
                case D:
                case RIGHT:
                    if (direction != Direction.LEFT && newGame)
                        direction = Direction.RIGHT;
                    break;
                case P:
                    if ((!gameOver || !paused) && newGame)
                        pauseGame();
                    else if (paused && !newGame)
                        resumeGame();
                    break;
                case BACK_SPACE:
                    if ((!newGame && !paused) || (gameOver))
                        primaryStage.setScene(settings);
                    break;
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
                primaryStage.setScene(settings);
        });

        settings.setOnKeyPressed(event -> {
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
        primaryStage.getIcons().add(new Image("file:///C:/Users/Madis/workspace/Kodutoo/src/images.jpg/"));
    }

    private void sulgeProgramm() {
        pauseGame();
        Boolean answer = Confirmation.display("Kas sa oled kindel?", "Kas sa soovid mängimise lõpetada?");
        if (answer)
            primaryStage.close();
    }
}