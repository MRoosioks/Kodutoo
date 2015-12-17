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

/**
 * Main class creates snake game.
 * @author Madis Roosioks
 * @version 1.0, December 2015
 */
public class Main extends Application {

    Stage primaryStage;
    public static final int blockSize = 20;
    public static final int programWidth = 30 * blockSize;
    public static final int programHeight = 25 * blockSize;
    public static final int circleSize = blockSize / 2;
    public int points;
    private Direction direction = Direction.RIGHT;
    private boolean gameOver, newGame, paused, gameMode, music;
    private Timeline timeline = new Timeline();
    private ObservableList<Node> snake;

    /** This method sets Directions for the program. */
    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    /** This block of code makes possible to play two different sound. */
    String pathGameOver = Main.class.getResource("/GAME_OVER.mp3").toString();
    Media gameEnd = new Media(pathGameOver);
    MediaPlayer playerGameEnd = new MediaPlayer(gameEnd);
    String pathBackgroundMusic = Main.class.getResource("/Background_music.mp3").toString();
    Media backgroundMusic = new Media(pathBackgroundMusic);
    MediaPlayer playerBackgroundMusic = new MediaPlayer(backgroundMusic);

    /**
     * Creates layout which consists of VBox. This layout is for welcoming the user of
     * this program.
     * @return returns the layout is defined in the method.
     */
    private Parent firstScene() {

        /** Creates VBoc layout and sets layout's size and position. */
        VBox firstSceneLayout = new VBox(20);
        firstSceneLayout.setAlignment(Pos.CENTER);
        firstSceneLayout.setPrefSize(programWidth, programHeight);

        /** Creates Text for welcoming the user. */
        Text welcomeText = new Text("Teretulemast mängima ussimängu.");
        welcomeText.getStyleClass().addAll("title");

        /** Creates label to tell use which key he/she has to press */
        Label settings = new Label("Mängimiseks vajuta ENTER");
        settings.getStyleClass().add("enterGame");

        /** Creates button for exiting the program and sets style of the button. */
        Button exitBtn1 = new Button("Välju");
        exitBtn1.getStyleClass().addAll("exitButton", "exitButton:hover");
        exitBtn1.setOnAction(e -> System.exit(0));

        firstSceneLayout.getChildren().addAll(welcomeText, settings, exitBtn1);

        return firstSceneLayout;
    }

    /**
     * Creates layout which consists of BorderPane. This layout is meant to let user choose which settings he/she wants
     * to use.
     * @return returns the layout is defined in the method.
     */
    private Parent secondScene() {

        /** Creates layouts */
        VBox settingsGameMode = new VBox(10);
        VBox settingsSound = new VBox(10);
        StackPane playGame = new StackPane();
        StackPane settingsTitle = new StackPane();
        BorderPane secondSceneLayout = new BorderPane();

        /** Creates text and set style to it. */
        Text title = new Text("Mänguseaded");
        title.getStyleClass().add("title");

        /** Creates label and set style to it. */
        Label play = new Label("Mängimiseks vajuta ENTER");
        play.getStyleClass().add("enterGame");

        /** Creates labels and radiobuttons. */
        Label chooseGamemode = new Label("Mängustiil");
        RadioButton withWalls = new RadioButton("Seintega mäng");
        RadioButton withoutWalls = new RadioButton("Seinteta mäng ");
        Label sound = new Label("Heli");
        RadioButton on = new RadioButton("Sees");
        RadioButton off = new RadioButton("Väljas");

        /** Gives value to gameMode or music depending on which on user selects. */
        withWalls.setOnAction(e -> gameMode = true);
        withoutWalls.setOnAction(e -> gameMode = false);
        on.setOnAction(e -> music = true);
        off.setOnAction(e -> music = false);

        /** Sets radiobuttons to the groups. */
        ToggleGroup gameMode = new ToggleGroup();
        withWalls.setToggleGroup(gameMode);
        withoutWalls.setToggleGroup(gameMode);
        ToggleGroup gameSound = new ToggleGroup();
        on.setToggleGroup(gameSound);
        off.setToggleGroup(gameSound);

        /** Sets two radiobuttons selected. */
        withoutWalls.setSelected(true);
        off.setSelected(true);

        /** Gives location to the layouts. */
        secondSceneLayout.setPrefSize(programWidth, programHeight);
        secondSceneLayout.setLeft(settingsSound);
        secondSceneLayout.setRight(settingsGameMode);
        secondSceneLayout.setBottom(playGame);
        secondSceneLayout.setTop(settingsTitle);

        /** Sets margins for the layouts */
        BorderPane.setMargin(settingsSound, new Insets(50, 50, 50, 100));
        BorderPane.setMargin(settingsGameMode, new Insets(50, 100, 50, 50));
        BorderPane.setMargin(playGame, new Insets(50, 0, 50, 0));
        BorderPane.setMargin(settingsTitle, new Insets(50, 0, 50, 0));

        playGame.getChildren().add(play);
        settingsTitle.getChildren().add(title);
        settingsGameMode.getChildren().addAll(chooseGamemode, withoutWalls, withWalls);
        settingsSound.getChildren().addAll(sound, off, on);

        return secondSceneLayout;
    }

    /**
     * This method is the main method for the program. It creates snake, food, definces collisons and
     * defines snake's movement
     * @return returns the layout is defined in the method. Layout includes snake, food, controls and points.
     */
    private Parent game() {

        playerGameEnd.setVolume(0.5);
        /** Creates the layout for the game and sets layout's size*/
        Pane root = new Pane();
        root.setPrefSize(programWidth, programHeight);

        /** Creates group for snake. */
        Group snakeBody = new Group();
        snake = snakeBody.getChildren();

        /** Creates label for showing points and sets style. */
        Label points = new Label("Punktid: " + this.points);
        points.getStyleClass().add("gameText");
        points.setLayoutX(programWidth - 100);

        /** Creates label for game controls and sets style. */
        Label controls = new Label("Liigu üles: W / Üles nool\n" + "Liigu alla: S / Alla nool\n"
                + "Liigu paremale: D / Paremale nool\n" + "Liigu vasakule: A / Vasakule nool\n"
                + "Mäng pausile / Jätka mängu: P\n" + "Uus mäng: ENTER\n" + "Mängu seaded: Backspace\n"
                + "Välju mängust: ESC");
        controls.getStyleClass().add("gameText");
        controls.setLayoutX(programWidth / 3);
        controls.setLayoutY(programHeight / 3);

        /** Creates food and sets random x and y coordinates to the food. */
        Circle food = new Circle(circleSize, circleSize, circleSize / 1.5, Color.DARKGREEN);
        food.setTranslateX((int) (Math.random() * (programWidth - blockSize)) / blockSize * blockSize);
        food.setTranslateY((int) (Math.random() * (programHeight - blockSize)) / blockSize * blockSize);

        /**  */
        KeyFrame frame = new KeyFrame(Duration.seconds(0.08), event -> {

            controls.setVisible(false);

            boolean toRemove = snake.size() > 1;

            Node tail = toRemove ? snake.remove(snake.size() - 1) : snake.get(0);

            double rememberTailX = tail.getTranslateX();
            double rememberTailY = tail.getTranslateY();

            /** switch statment sets how the snake moves. */
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

            /** when snake collides with itself this block of code will be triggered and game will be over. */
            for (Node circle : snake) {
                if (circle != tail && tail.getTranslateX() == circle.getTranslateX()
                        && tail.getTranslateY() == circle.getTranslateY()) {
                    controls.setVisible(true);
                    if (music)
                        playerGameEnd.play();
                    stopGame();
                    break;
                }
            }
            /** when gameMode is false snake can move trough walls. */
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

            /** when gameMode is true then game will be lost when snake collides against a wall. */
            if ((tail.getTranslateX() < 0 || tail.getTranslateX() >= programWidth
                    || tail.getTranslateY() < 0 || tail.getTranslateY() >= programHeight) && gameMode) {
                controls.setVisible(true);
                if (music)
                    playerGameEnd.play();
                stopGame();
            }

            /**
             * if tail and food coordinates are the same the snake eats food and then snake
             * will grow 3 blocks and player earns points.
             * */
            if (tail.getTranslateX() == food.getTranslateX()
                    && tail.getTranslateY() == food.getTranslateY()) {
                food.setTranslateX((int) (Math.random() * (programWidth - blockSize)) / blockSize * blockSize);
                food.setTranslateY((int) (Math.random() * (programHeight - blockSize)) / blockSize * blockSize);
                this.points += 15;
                points.setText("Punktid: " + this.points);

                Circle addBodyPart1 = new Circle(circleSize, circleSize, circleSize, Color.PURPLE);
                Circle addBodyPart2 = new Circle(circleSize, circleSize, circleSize, Color.PURPLE);
                Circle addBodyPart3 = new Circle(circleSize, circleSize, circleSize, Color.PURPLE);
                addBodyPart1.setTranslateX(rememberTailX);
                addBodyPart1.setTranslateY(rememberTailY);
                addBodyPart2.setTranslateX(rememberTailX);
                addBodyPart2.setTranslateY(rememberTailY);
                addBodyPart3.setTranslateX(rememberTailX);
                addBodyPart3.setTranslateY(rememberTailY);
                snake.addAll(addBodyPart1, addBodyPart2, addBodyPart3);
            }
        });

        timeline.getKeyFrames().add(frame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        root.getChildren().addAll(food, snakeBody, controls, points);

        return root;
    }

    /** This method stops program when this method is called out. */
    private void stopGame() {
        playerBackgroundMusic.stop();
        timeline.stop();
        newGame = false;
        gameOver = true;
        paused = false;
    }

    /** This method starts snake game when this method is called out. */
    private void startGame() {
        Circle head = new Circle(circleSize, circleSize, circleSize, Color.PURPLE);
        Circle startWithBodyPart1 = new Circle(circleSize, circleSize, circleSize, Color.PURPLE);
        Circle startWithBodyPart2 = new Circle(circleSize, circleSize, circleSize, Color.PURPLE);
        Circle startWithBodyPart3 = new Circle(circleSize, circleSize, circleSize, Color.PURPLE);
        snake.addAll(head, startWithBodyPart1, startWithBodyPart2, startWithBodyPart3);
        points = 0;
        playerGameEnd.stop();
        direction = Direction.RIGHT;
        timeline.play();
        newGame = true;
        gameOver = false;
        paused = false;
        if (music)
            playerBackgroundMusic.play();
    }

    /** This method pauses the game. */
    private void pauseGame() {
        playerBackgroundMusic.pause();
        timeline.pause();
        newGame = false;
        gameOver = false;
        paused = true;
    }

    /** This method resumes the game. */
    private void resumeGame() {
        playerBackgroundMusic.play();

        timeline.play();
        newGame = true;
        gameOver = false;
        paused = false;
    }

    /**
     * This method is the main method for calling out all three layout methods for creating scenes.
     * It adds Style.css file with scenes.
     * @param primaryStage This is the Stage for this program.
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setResizable(false);
        Scene gameMenu = new Scene(firstScene());
        Scene settings = new Scene(secondScene());
        Scene game = new Scene(game());

        gameMenu.getStylesheets().add("Style.css");
        settings.getStylesheets().add("Style.css");
        game.getStylesheets().add("Style.css");


        /** This switch adds keys.*/
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
        primaryStage.getIcons().add(new Image("images.jpg"));
    }

    /**
     * This method launches the program.
     * @param args launches args.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /** This method calls popup window which asks user's confirmation. */
    private void sulgeProgramm() {
        pauseGame();
        Boolean answer = Confirmation.display("Kas sa oled kindel?", "Kas sa soovid mängimise lõpetada?");
        if (answer)
            primaryStage.close();
    }
}