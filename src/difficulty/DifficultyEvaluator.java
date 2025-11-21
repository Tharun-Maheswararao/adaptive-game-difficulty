package difficulty;

import datastructures.heap.MinHeap;

/**
 * Member 2: Difficulty evaluation using weighted scoring + MinHeap smoothing.
 * Converts Stats → DifficultyLevel.
 */
public class DifficultyEvaluator {

    private final MinHeap<Integer> scoreHeap = new MinHeap<>(50);
    private DifficultyLevel lastDifficulty = DifficultyLevel.EASY;

    public DifficultyLevel evaluate(Stats stats) {

        int score = computeScore(stats);
        scoreHeap.add(score);

        // Rolling performance trend (smoothed)
        int trend = scoreHeap.peek(); // the smallest score, smooths spikes

        // --- Bad performance → EASY ---
        if (stats.bypassed >= 3 || stats.accuracy < 0.30 || trend < 0) {
            lastDifficulty = DifficultyLevel.EASY;
            return lastDifficulty;
        }

        // --- Strong performance → HARD ---
        if (stats.accuracy >= 0.75 && stats.bypassed == 0 && stats.scoreDelta > 0 && trend > 50) {
            lastDifficulty = DifficultyLevel.HARD;
            return lastDifficulty;
        }

        // --- Everything in middle → MEDIUM ---
        lastDifficulty = DifficultyLevel.MEDIUM;
        return lastDifficulty;
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
