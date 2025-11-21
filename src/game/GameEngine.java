package game;

import difficulty.AdaptiveDifficultyEngine;
import difficulty.PerformanceMetric;
import difficulty.Stats;
import difficulty.StatsCalculator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class GameEngine {

    private List<Bullet> bullets = new ArrayList<>();
    private List<Enemy> enemies = new ArrayList<>();

    private int score = 0;
    private int kills = 0;
    private int bypassed = 0;
    private int shots = 0;
    private int lastScore = 0;

    private String difficulty = "EASY";

    private AdaptiveDifficultyEngine difficultyEngine;
    private long lastDifficultyUpdate;

    private static final long DIFFICULTY_INTERVAL = 5000; // 5 seconds
    private static final int MAX_BYPASS = 10;              // game over

    public GameEngine(AdaptiveDifficultyEngine engine) {
        this.difficultyEngine = engine;
        this.lastDifficultyUpdate = System.currentTimeMillis();
    }

    public void shoot(double x, double y) {
        bullets.add(new Bullet(x, y));
        shots++; // ✅ count shots for accuracy
    }
    

    /** Called every frame by AnimationTimer */
    public void update() {
        updateBullets();
        updateEnemies();
        checkDifficultyUpdate();
    }

    private void updateBullets() {
        Iterator<Bullet> iterator = bullets.iterator();

        while (iterator.hasNext()) {
            Bullet b = iterator.next();
            b.update();

            if (b.isOffScreen()) {
                iterator.remove();
            }
        }
    }

    private void updateEnemies() {
        Iterator<Enemy> iterator = enemies.iterator();

        while (iterator.hasNext()) {
            Enemy e = iterator.next();
            e.update();

            boolean enemyHit = false;

            // Check if a bullet hits this enemy
            Iterator<Bullet> bIt = bullets.iterator();
            while (bIt.hasNext()) {
                Bullet b = bIt.next();

                if (isColliding(b, e)) {
                    kills++;
                    score += 10;
                    enemyHit = true;
                    bIt.remove(); // remove the bullet
                    break;        // one bullet is enough
                }
            }

            if (enemyHit) {
                iterator.remove(); // remove enemy
                continue;
            }

            // Remove if enemy bypassed bottom of screen (temporary height 600)
            if (e.getY() > 600) {
                bypassed++;
                score -= 2;
                iterator.remove();
            }
        }
    }


    private boolean isColliding(Bullet b, Enemy e) {
        return (b.getX() >= e.getX() &&
                b.getX() <= e.getX() + e.getWidth() &&
                b.getY() >= e.getY() &&
                b.getY() <= e.getY() + e.getHeight());
    }

    /**
     * Updates difficulty every 5 seconds based on recent performance.
     */
    private void checkDifficultyUpdate() {
        long now = System.currentTimeMillis();

        if (now - lastDifficultyUpdate >= DIFFICULTY_INTERVAL) {

            // Score difference in last interval
            int scoreDelta = score - lastScore;
            lastScore = score;

            PerformanceMetric metric = new PerformanceMetric(
                    kills,
                    shots,
                    bypassed,
                    scoreDelta
            );

            List<PerformanceMetric> metrics = new ArrayList<>();
            metrics.add(metric);

            difficulty = difficultyEngine.computeNewDifficulty(metrics);

            lastDifficultyUpdate = now;
        }
    }

    public int getScore()     { return score; }
    public int getKills()     { return kills; }
    public int getBypassed()  { return bypassed; }
    public String getDifficulty() { return difficulty; }

    public List<Bullet> getBullets() { return bullets; }
    public List<Enemy> getEnemies() { return enemies; }

    /** Game ends when 10 enemies bypass */
    public boolean isGameOver() {
        return bypassed >= MAX_BYPASS;
    }

    /** Adds a new enemy from the spawner */
    public void spawnEnemy(Enemy e) {
        enemies.add(e);
    }
}
