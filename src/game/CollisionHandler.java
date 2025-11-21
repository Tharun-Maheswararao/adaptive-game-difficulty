package game;

import java.util.Iterator;
import java.util.List;

public class CollisionHandler {

    public static int handleCollisions(List<Bullet> bullets, List<Enemy> enemies) {

        int kills = 0;

        Iterator<Bullet> bit = bullets.iterator();
        while (bit.hasNext()) {
            Bullet b = bit.next();

            Iterator<Enemy> eit = enemies.iterator();
            boolean bulletRemoved = false;

            while (eit.hasNext()) {
                Enemy e = eit.next();

                if (isColliding(b, e)) {
                    eit.remove();
                    bit.remove();
                    kills++;
                    bulletRemoved = true;
                    break;
                }
            }

            if (bulletRemoved) {
                // already removed in loop
            }
        }
        return kills;
    }

    private static boolean isColliding(Bullet b, Enemy e) {
        double dx = b.getX() - e.getX();
        double dy = b.getY() - e.getY();
        double distSq = dx * dx + dy * dy;
        double radius = 18;
        return distSq <= radius * radius;
    }
}
