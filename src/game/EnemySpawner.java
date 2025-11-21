package game;

import difficulty.AdaptiveDifficultyEngine;
import difficulty.DifficultyLevel;

import java.util.List;
import java.util.Random;

public class EnemySpawner {

    private final AdaptiveDifficultyEngine difficultyEngine;
    private final Random random = new Random();
    private long lastSpawnTime = 0;

    public EnemySpawner(AdaptiveDifficultyEngine difficultyEngine) {
        this.difficultyEngine = difficultyEngine;
    }

    public void trySpawn(List<Enemy> enemies) {
        long now = System.currentTimeMillis();

        int interval = difficultyEngine.getEnemySpawnIntervalMillis();
        int maxEnemies = difficultyEngine.getMaxActiveEnemies();

        if (now - lastSpawnTime >= interval && enemies.size() < maxEnemies) {

            double x = 20 + random.nextDouble() * (Constants.WIDTH - 40);
            double base = Constants.ENEMY_BASE_SPEED;
            double speed = base * difficultyEngine.getEnemySpeedMultiplier();

            enemies.add(new Enemy(x, 0, speed));

            lastSpawnTime = now;
        }
    }
}
