package ui;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Draws the HUD (score, accuracy, bypassed count, difficulty)
 * on top of the game canvas.
 */
public class HUDRenderer {

    public void render(GraphicsContext gc, int score, double accuracy,
                       int bypassed, String difficulty) {

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font(18));

        gc.fillText("Score: " + score, 20, 30);
        gc.fillText("Accuracy: " + String.format("%.1f%%", accuracy * 100), 20, 55);
        gc.fillText("Bypassed: " + bypassed + "/10", 20, 80);
        gc.fillText("Difficulty: " + difficulty, 20, 105);
    }
}
