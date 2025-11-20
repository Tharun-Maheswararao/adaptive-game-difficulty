package difficulty;

import datastructures.bst.BalancedBST;
import datastructures.stack.ArrayStack;

import java.util.List;

/**
 * Uses player performance to compute the next difficulty level (EASY, MEDIUM, HARD).
 * Stores past scores in a BalancedBST and difficulty states in an ArrayStack.
 */
public class AdaptiveDifficultyEngine {

    private BalancedBST<Integer> performanceHistory = new BalancedBST<>();
    private ArrayStack<String> difficultyHistory = new ArrayStack<>(30);

    /**
     * Computes a new difficulty level based on recent performance metrics.
     */
    public String computeNewDifficulty(List<PerformanceMetric> metrics) {

        // 1. Aggregate performance stats
        Stats stats = StatsCalculator.computeStats(metrics);

        // 2. Insert score delta into the Balanced BST (history tracking)
        performanceHistory.insert(stats.scoreDelta);

        // 3. Compute difficulty level (your rule-based logic)
        String difficulty;

        if (stats.accuracy > 0.80 && stats.bypassed == 0) {
            difficulty = "HARD";   // Player doing extremely well
        }
        else if (stats.accuracy > 0.50) {
            difficulty = "MEDIUM"; // Player doing okay
        }
        else {
            difficulty = "EASY";   // Player struggling → help them
        }

        // 4. Push difficulty into stack for undo feature
        difficultyHistory.push(difficulty);

        return difficulty;
    }

    /**
     * Undo last difficulty level (if engine decides difficulty correction).
     */
    public String undoDifficulty() {
        if (!difficultyHistory.isEmpty()) {
            return difficultyHistory.pop();
        }
        return "EASY"; // Fallback
    }
}
