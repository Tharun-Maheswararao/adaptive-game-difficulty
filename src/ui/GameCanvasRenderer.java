package ui;

import game.Bullet;
import game.Constants;
import game.Enemy;
import game.GameEngine;
import game.Player;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class GameCanvasRenderer {

    public void render(GraphicsContext gc, GameEngine engine) {

        // background
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, Constants.WIDTH, Constants.HEIGHT);

        // danger line
        gc.setStroke(Color.DARKRED);
        gc.strokeLine(0, Constants.DANGER_LINE_Y, Constants.WIDTH, Constants.DANGER_LINE_Y);

        // player
        Player p = engine.getPlayer();
        gc.setFill(Color.CYAN);
        gc.fillRect(p.getX() - p.getWidth()/2, p.getY() - p.getHeight()/2,
                p.getWidth(), p.getHeight());

        // bullets
        gc.setFill(Color.YELLOW);
        for (Bullet b : engine.getBullets()) {
            gc.fillOval(b.getX() - 3, b.getY() - 6, 6, 12);
        }

        // enemies
        gc.setFill(Color.ORANGE);
        for (Enemy e : engine.getEnemies()) {
            gc.fillOval(e.getX() - 12, e.getY() - 12, 24, 24);
        }
    }
}
