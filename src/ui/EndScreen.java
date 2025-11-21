package ui;

import game.GameStats;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class EndScreen {

    public void render(GraphicsContext gc, GameStats stats) {
        gc.setFill(Color.color(0, 0, 0, 0.7));
        gc.fillRect(0, 0, 800, 600);

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font(32));
        gc.fillText("Game Over!", 300, 220);

        gc.setFont(Font.font(20));
        gc.fillText("Score: " + stats.score, 320, 270);
        gc.fillText(String.format("Accuracy: %.1f%%", stats.getAccuracy() * 100), 320, 300);
        gc.fillText("Enemies bypassed: " + stats.bypassed, 320, 330);
        gc.fillText(String.format("Time alive: %.1f s", stats.timeSeconds), 320, 360);
    }
}
