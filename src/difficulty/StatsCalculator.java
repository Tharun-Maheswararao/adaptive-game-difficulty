package difficulty;

import java.util.List;

/**
 * Computes aggregate statistics from a list of PerformanceMetrics.
 * This is used by the AdaptiveDifficultyEngine to determine difficulty updates.
 */
public class StatsCalculator {

    public static Stats computeStats(List<PerformanceMetric> metrics) {
        int totalKills = 0;
        int totalShots = 0;
        int totalBypassed = 0;
        int totalScoreDelta = 0;

        for (PerformanceMetric m : metrics) {
            totalKills += m.getKills();
            totalShots += m.getShots();
            totalBypassed += m.getBypassed();
            totalScoreDelta += m.getScoreDelta();
        }

        double accuracy =
            (totalShots == 0)
            ? 0
            : (double) totalKills / totalShots;

        return new Stats(
            totalKills,
            totalShots,
            totalBypassed,
            accuracy,
            totalScoreDelta
        );
    }
}
