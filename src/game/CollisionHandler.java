package game;

import java.util.Iterator;
import java.util.List;

/**
 * Handles collisions between bullets and enemies.
 */
public class CollisionHandler {

    /**
     * Detects bullet-enemy collisions, removes hit enemies,
     * removes corresponding bullets, and returns kill count.
     */
    public static int handleBulletEnemyCollisions(
            List<Bullet> bullets,
            List<Enemy> enemies) {

        int kills = 0;


        Iterator<Bullet> iterator = bullets.iterator();

        while (iterator.hasNext()) {
            Bullet b = iterator.next();

            for (Enemy e : enemies) {
                if (isColliding(b, e)) {
                    kills++;
                    e.isDestroyed();
                    iterator.remove(); // remove bullet
                    break; // stop checking this bullet
                }
            }
        }

        return kills;
    }

    /** Simple circular hitbox collision detection */
    private static boolean isColliding(Bullet b, Enemy e) {
        double dx = b.getX() - e.getX();
        double dy = b.getY() - e.getY();
        return Math.sqrt(dx*dx + dy*dy) < 20; // radius-based collision
    }
}
