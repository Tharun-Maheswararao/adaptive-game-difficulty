package game;

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

        bullets.removeIf(b -> {
            for (Enemy e : enemies) {
                if (isColliding(b, e)) {
                    e.setDestroyed(true);
                    kills++;
                    return true; // remove this bullet
                }
            }
            return false;
        });

        enemies.removeIf(Enemy::isDestroyed);

        return kills;
    }

    /** Simple circular hitbox collision detection */
    private static boolean isColliding(Bullet b, Enemy e) {
        double dx = b.getX() - e.getX();
        double dy = b.getY() - e.getY();
        return Math.sqrt(dx*dx + dy*dy) < 20; // radius-based collision
    }
}
