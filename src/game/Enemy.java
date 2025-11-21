package game;

public class Enemy {

    private double x, y;
    private double speed;
    private boolean destroyed = false;

    // Visual size of the enemy (for rendering & collision)
    private static final double WIDTH = 30;
    private static final double HEIGHT = 30;

    // Main constructor (used by EnemySpawner with difficulty-based speed)
    public Enemy(double x, double y, double speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
    }

    // Convenience constructor (used by tests and manual spawning)
    public Enemy(double x, double y) {
        this(x, y, 3.0);
    }

    public void update() {
        y += speed;
    }

    public boolean isOffScreen() {
        return y > Constants.HEIGHT;
    }

    // --- Getters for position and size ---

    public double getX() { return x; }
    public double getY() { return y; }

    public double getWidth() { return WIDTH; }
    public double getHeight() { return HEIGHT; }

    // --- Destroy state (not strictly required but OK to keep) ---

    public boolean isDestroyed() { return destroyed; }
    public void setDestroyed(boolean destroyed) { this.destroyed = destroyed; }
}
