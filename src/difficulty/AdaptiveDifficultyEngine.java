package difficulty;

import datastructures.bst.BalancedBST;
import datastructures.stack.ArrayStack;
import datastructures.queue.CircularQueue;

import java.util.ArrayList;
import java.util.List;

/**
 * AdaptiveDifficultyEngine delegates difficulty decisions to DifficultyEvaluator
 * and maintains history using BST + Stack + Queue.
 */
public class AdaptiveDifficultyEngine {

    private final BalancedBST<Integer> performanceHistory = new BalancedBST<>();
    private final ArrayStack<DifficultyLevel> difficultyHistory = new ArrayStack<>(30);
    private final CircularQueue<PerformanceMetric> recentMetrics = new CircularQueue<>(10);

    private final DifficultyEvaluator evaluator = new DifficultyEvaluator();

    private DifficultyLevel currentDifficulty = DifficultyLevel.EASY;

    /** Update difficulty based on recent performance metrics. */
    public DifficultyLevel computeNewDifficulty(List<PerformanceMetric> metrics) {

        if (metrics == null || metrics.isEmpty()) {
            return currentDifficulty;
        }

        // 1. Add metrics to sliding window
        for (PerformanceMetric m : metrics) {
            recentMetrics.enqueue(m);
        }

        List<PerformanceMetric> window = toList(recentMetrics);

        // 2. Compute aggregated stats
        Stats stats = StatsCalculator.computeStats(window);

        // 3. Store performance score in BST
        int score = computeScore(stats);
        performanceHistory.insert(score);

        // 4. Evaluate difficulty
        DifficultyLevel newLevel = evaluator.evaluate(stats);

        currentDifficulty = newLevel;
        difficultyHistory.push(newLevel);

        return newLevel;
    }

    /** Undo the last difficulty update. */
    public DifficultyLevel undoDifficulty() {
        if (!difficultyHistory.isEmpty()) {
            difficultyHistory.pop();
        }
        if (!difficultyHistory.isEmpty()) {
            currentDifficulty = difficultyHistory.peek();
        } else {
            currentDifficulty = DifficultyLevel.EASY;
            difficultyHistory.push(currentDifficulty);
        }
        return currentDifficulty;
    }

    public DifficultyLevel getCurrentDifficultyLevel() {
        return currentDifficulty;
    }

    // =================== GAME ENGINE HELPERS ===================

    public int getEnemySpawnIntervalMillis() {
        return switch (currentDifficulty) {
            case EASY -> 1200;
            case MEDIUM -> 900;
            case HARD -> 600;
        };
    }

    public double getEnemySpeedMultiplier() {
        return switch (currentDifficulty) {
            case EASY -> 1.0;
            case MEDIUM -> 1.35;
            case HARD -> 1.75;
        };
    }

    public int getMaxActiveEnemies() {
        return switch (currentDifficulty) {
            case EASY -> 5;
            case MEDIUM -> 8;
            case HARD -> 12;
        };
    }

    // =================== INTERNAL HELPERS ===================

    private List<PerformanceMetric> toList(CircularQueue<PerformanceMetric> queue) {
        List<PerformanceMetric> list = new ArrayList<>(queue.size());
        int n = queue.size();

        for (int i = 0; i < n; i++) {
            PerformanceMetric m = queue.dequeue();
            if (m != null) {
                list.add(m);
                queue.enqueue(m);
            }
        }
        return list;
    }

    private int computeScore(Stats stats) {
        int killsWeight = 10;
        int bypassPenalty = 20;
        int accuracyBonus = (int) (stats.accuracy * 100);

        return stats.kills * killsWeight
                + stats.scoreDelta
                + accuracyBonus
                - stats.bypassed * bypassPenalty;
    }
}
