package ui;

import difficulty.AdaptiveDifficultyEngine;
import game.GameEngine;
import game.Constants;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class AdaptiveGameApp extends Application {

    private boolean leftPressed;
    private boolean rightPressed;
    private boolean shootPressed;

    @Override
    public void start(Stage stage) {

        Canvas canvas = new Canvas(Constants.WIDTH, Constants.HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        AdaptiveDifficultyEngine diffEngine = new AdaptiveDifficultyEngine();
        GameEngine engine = new GameEngine(diffEngine);
        HUDRenderer hud = new HUDRenderer();
        GameCanvasRenderer renderer = new GameCanvasRenderer();
        EndScreen endScreen = new EndScreen();

        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Adaptive Difficulty Shooter");
        stage.setAlwaysOnTop(true);
        stage.centerOnScreen();
        stage.show();
        
        System.out.println("Window size = " + stage.getWidth() + " x " + stage.getHeight());

        scene.setOnKeyPressed(e -> {
            KeyCode code = e.getCode();
            switch (code) {
                case LEFT -> leftPressed = true;
                case RIGHT -> rightPressed = true;
                case SPACE -> shootPressed = true;
                default -> {}
            }
        });

        scene.setOnKeyReleased(e -> {
            KeyCode code = e.getCode();
            switch (code) {
                case LEFT -> leftPressed = false;
                case RIGHT -> rightPressed = false;
                case SPACE -> shootPressed = false;
                default -> {}
            }
        });

        Timeline loop = new Timeline(new KeyFrame(Duration.millis(16), e -> {
            engine.update(leftPressed, rightPressed, shootPressed);

            renderer.render(gc, engine);
            hud.render(gc,
                    engine.getScore(),
                    engine.getAccuracy(),
                    engine.getBypassed(),
                    engine.getBulletsLeft(),
                    engine.getDifficultyLevel());

            if (engine.isGameOver()) {
                endScreen.render(gc, engine.getStats());
            }
        }));

        loop.setCycleCount(Timeline.INDEFINITE);
        loop.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
