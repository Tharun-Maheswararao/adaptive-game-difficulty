package game;

import difficulty.AdaptiveDifficultyEngine;
import difficulty.PerformanceMetric;
import difficulty.DifficultyLevel;

import java.util.ArrayList;
import java.util.List;

public class GameEngine {

    private final Player player;
    private final List<Bullet> bullets = new ArrayList<>();
    private final List<Enemy> enemies = new ArrayList<>();

    private final EnemySpawner spawner;
    private final AdaptiveDifficultyEngine difficultyEngine;
    private final GameStats stats = new GameStats();

    private boolean gameOver = false;
    private long lastDifficultyUpdate = System.currentTimeMillis();
    
    private long lastShotTime = 0;
    private static final long SHOOT_INTERVAL = 250; // fire once every 0.25s

    public GameEngine(AdaptiveDifficultyEngine difficultyEngine) {
        this.difficultyEngine = difficultyEngine;
        this.spawner = new EnemySpawner(difficultyEngine);
        this.player = new Player(Constants.WIDTH / 2.0, Constants.HEIGHT - 60);
    }

    public void update(boolean left, boolean right, boolean shoot) {
        if (gameOver) return;

        // Move player
        if (left) player.moveLeft();
        if (right) player.moveRight();

        // Shoot
        if (shoot) fireBullet();

        // Update bullets
        bullets.forEach(Bullet::update);
        bullets.removeIf(Bullet::isOffScreen);

        // Spawn enemies
        spawner.trySpawn(enemies);

        // Update enemies
        enemies.forEach(Enemy::update);

        // Check bypass
        List<Enemy> toRemove = new ArrayList<>();
        for (Enemy e : enemies) {
            if (e.getY() > Constants.DANGER_LINE_Y) {
                stats.bypassed++;
                stats.score -= 2;
                toRemove.add(e);
                if (stats.bypassed >= Constants.MAX_BYPASSED) {
                    gameOver = true;
                }
            }
        }
        enemies.removeAll(toRemove);

        // Collisions
        int kills = CollisionHandler.handleCollisions(bullets, enemies);
        if (kills > 0) {
            stats.kills += kills;
            stats.score += kills * 10;
        }

        // Time
        stats.timeSeconds += 1.0 / 60.0;

        // Difficulty update every 5s
        long now = System.currentTimeMillis();
        if (now - lastDifficultyUpdate >= 5000) {
            List<PerformanceMetric> window = new ArrayList<>();
            window.add(new PerformanceMetric(kills, stats.shots, stats.bypassed, stats.score));
            difficultyEngine.computeNewDifficulty(window);
            lastDifficultyUpdate = now;
        }
    }

    private void fireBullet() {
        long now = System.currentTimeMillis();

        // only fire if cooldown passed
        if (now - lastShotTime >= SHOOT_INTERVAL) {
            bullets.add(new Bullet(player.getX(), player.getY()));
            stats.shots++;
            lastShotTime = now;
        }
    }


    // ---- GETTERS ----

    public Player getPlayer() { return player; }
    public List<Bullet> getBullets() { return bullets; }
    public List<Enemy> getEnemies() { return enemies; }

    public int getScore() { return stats.score; }
    public double getAccuracy() { return stats.getAccuracy(); }
    public int getBypassed() { return stats.bypassed; }
    public int getBulletsLeft() { return bullets.size(); }
    public int getKills() { return stats.kills; }

    public DifficultyLevel getDifficultyLevel() {
        return difficultyEngine.getCurrentDifficultyLevel();
    }

    public boolean isGameOver() { return gameOver; }

    public GameStats getStats() { return stats; }
}
