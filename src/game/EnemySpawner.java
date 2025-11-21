package game;

/**
 * Spawns enemies at difficulty-dependent speeds.
 * This class just creates new Enemy objects positioned at random X.
 */
public class EnemySpawner {

    private final double WIDTH = 600;

    public Enemy spawn(String difficulty, double canvasWidth) {
        double x = Math.random() * (canvasWidth - 40);
        double y = -40;
        double speed = 1.8;
    
        switch (difficulty) {
            case "HARD": speed = 3.0; break;
            case "MEDIUM": speed = 2.3; break;
            default: speed = 1.8;
        }
    
        return new Enemy(x, y, speed);
    }
    
}
