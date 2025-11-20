package difficulty;

import datastructures.heap.MinHeap;
import datastructures.sort.MergeSort;

import java.util.List;

/**
 * Uses MinHeap + MergeSort to decide the next difficulty level.
 */
public class DifficultyEvaluator {

    private DifficultyGraph graph = new DifficultyGraph();

    public String evaluate(List<PerformanceMetric> metrics) {

        if (metrics == null || metrics.isEmpty()) {
            return "EASY";
        }

        // Sort by performance score
        MergeSort.sort(metrics);

        MinHeap<Integer> heap = new MinHeap<>(metrics.size());

        for (PerformanceMetric m : metrics) {
            // Score formula used for difficulty selection
            int score = (m.getKills() * 10)
                    + (int)(m.getShots() * 0.3)
                    - (m.getBypassed() * 20);

            heap.insert(score);
        }

        Integer worstScore = heap.extractMin(); // Minimum → worst performance

        if (worstScore == null)
            return "EASY";

        // Very weak performance
        if (worstScore < 20)
            return "EASY";

        // Good performance
        if (worstScore >= 60)
            return "HARD";

        // Medium otherwise
        return "MEDIUM";
    }
}
