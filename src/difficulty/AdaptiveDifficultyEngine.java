package difficulty;

import datastructures.bst.BalancedBST;
import datastructures.stack.ArrayStack;
import datastructures.queue.CircularQueue;

import java.util.ArrayList;
import java.util.List;

/**
 * AdaptiveDifficultyEngine decides whether the next difficulty level
 * should be EASY, MEDIUM or HARD based on recent performance.
 */
public class AdaptiveDifficultyEngine {

    private BalancedBST<Integer> performanceHistory = new BalancedBST<>();

    private ArrayStack<String> difficultyHistory = new ArrayStack<>(30);

    private CircularQueue<PerformanceMetric> recentMetrics = new CircularQueue<>(10);

    private DifficultyEvaluator evaluator = new DifficultyEvaluator();


    /**
     * Records new metrics, updates internal data structures and
     * returns the new difficulty level.
     *
     * @param metrics list of metrics collected during the last time slice
     * @return difficulty label: "EASY", "MEDIUM" or "HARD"
     */
    public String computeNewDifficulty(List<PerformanceMetric> metrics) {
        if (metrics == null || metrics.isEmpty()) {
            // No new information – default to EASY at the beginning.
            return "EASY";
        }
        for (PerformanceMetric m : metrics) {
            recentMetrics.enqueue(m);
        }
        List<PerformanceMetric> window = toList(recentMetrics);

        Stats stats = StatsCalculator.computeStats(window);

        int performanceScore = computeScore(stats);

        performanceHistory.insert(performanceScore);

        String difficulty = evaluator.evaluate(window);

        difficultyHistory.push(difficulty);

        return difficulty;
    }


    public String undoDifficulty() {
        if (!difficultyHistory.isEmpty()) {
            return difficultyHistory.pop();
        }
        return "EASY"; // Fallback
    }

    /**
     * Helper: copies all elements from a CircularQueue into a new List without
     * permanently removing them from the queue.
     */
    private List<PerformanceMetric> toList(CircularQueue<PerformanceMetric> queue) {
        List<PerformanceMetric> list = new ArrayList<>(queue.size());
        int n = queue.size();

        for (int i = 0; i < n; i++) {
            PerformanceMetric m = queue.dequeue();
            if (m != null) {
                list.add(m);
                queue.enqueue(m); // put it back
            }
        }
        return list;
    }

    private int computeScore(Stats stats) {
        int killsWeight = 10;
        int bypassPenalty = 20;
        int accuracyBonus = (int) (stats.accuracy * 100); // 0..100

        return (stats.kills * killsWeight)
                + stats.scoreDelta
                + accuracyBonus
                - (stats.bypassed * bypassPenalty);
    }

    private String decideDifficulty(Stats stats) {
        // If many enemies bypassed or accuracy is very low → EASY
        if (stats.bypassed >= 3 || stats.accuracy < 0.30) {
            return "EASY";
        }

        // Very good performance → HARD
        if (stats.accuracy >= 0.75 && stats.bypassed == 0 && stats.scoreDelta > 0) {
            return "HARD";
        }

        // Otherwise keep game in MEDIUM
        return "MEDIUM";
    }
}
