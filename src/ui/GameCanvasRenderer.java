package ui;

import game.Bullet;
import game.Enemy;
import game.GameEngine;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

import java.util.List;

/**
 * Polished game renderer with:
 *  - Gradient background
 *  - Glow bullets
 *  - Soft enemy shadows
 *  - Difficulty flash overlay
 */
public class GameCanvasRenderer {

    private long lastDifficultyFlash = 0;
    private boolean flashing = false;
    private Color flashColor = Color.WHITE;

    // Enemy shadow effect
    private final DropShadow enemyShadow = new DropShadow(10, Color.rgb(0, 0, 0, 0.5));

    // Bullet glow effect
    private final DropShadow bulletGlow = new DropShadow();

    public GameCanvasRenderer() {
        bulletGlow.setBlurType(BlurType.GAUSSIAN);
        bulletGlow.setColor(Color.CYAN);
        bulletGlow.setRadius(12);
    }

    /** Call this when difficulty changes */
    public void triggerDifficultyFlash(String diff) {
        flashing = true;
        lastDifficultyFlash = System.currentTimeMillis();

        switch (diff) {
            case "EASY":
                flashColor = Color.LIGHTGREEN;
                break;
            case "MEDIUM":
                flashColor = Color.GOLD;
                break;
            case "HARD":
                flashColor = Color.RED;
                break;
            default:
                flashColor = Color.WHITE;
        }
    }

    /** Main render function */
    public void render(GraphicsContext gc, GameEngine engine, double width, double height) {
        drawBackground(gc, width, height);
        drawDifficultyFlash(gc, width, height);
        drawEnemies(gc, engine.getEnemies());
        drawBullets(gc, engine.getBullets());
    }

    /** Gradient background using LinearGradient (valid JavaFX API) */
    private void drawBackground(GraphicsContext gc, double width, double height) {
        LinearGradient gradient = new LinearGradient(
                0, 0, 0, 1,        // x1, y1, x2, y2 (0..1 → proportional)
                true,
                CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#0A0F24")),
                new Stop(1, Color.web("#1A2340"))
        );

        gc.setFill(gradient);
        gc.fillRect(0, 0, width, height);
    }

    /** Semi-transparent color overlay when difficulty changes */
    private void drawDifficultyFlash(GraphicsContext gc, double width, double height) {
        if (!flashing) return;

        long now = System.currentTimeMillis();
        long dt = now - lastDifficultyFlash;

        if (dt > 700) {
            flashing = false;
            return;
        }

        double alpha = 0.7 * (1.0 - (double) dt / 700.0);

        gc.setFill(new Color(
                flashColor.getRed(),
                flashColor.getGreen(),
                flashColor.getBlue(),
                alpha
        ));
        gc.fillRect(0, 0, width, height);
    }

    /** Render enemies with soft shadow and rounded corners */
    private void drawEnemies(GraphicsContext gc, List<Enemy> enemies) {
        gc.setEffect(enemyShadow);
        gc.setFill(Color.rgb(255, 70, 70));

        for (Enemy e : enemies) {
            gc.fillRoundRect(
                    e.getX(), e.getY(),
                    e.getWidth(), e.getHeight(),
                    12, 12
            );
        }

        gc.setEffect(null);
    }

    /** Render bullets with glow */
    private void drawBullets(GraphicsContext gc, List<Bullet> bullets) {
        gc.setEffect(bulletGlow);
        gc.setFill(Color.CYAN);

        for (Bullet b : bullets) {
            gc.fillOval(b.getX() - 4, b.getY() - 10, 8, 20);
        }

        gc.setEffect(null);
    }
}
