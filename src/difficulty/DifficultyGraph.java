package difficulty;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Set;

/**
 * Encodes allowed transitions between difficulty levels.
 * (e.g., you can't jump from EASY directly to HARD if you don't want to)
 */
public class DifficultyGraph {

    private final EnumMap<DifficultyLevel, Set<DifficultyLevel>> transitions = new EnumMap<>(DifficultyLevel.class);

    public DifficultyGraph() {
        transitions.put(DifficultyLevel.EASY,
                EnumSet.of(DifficultyLevel.EASY, DifficultyLevel.MEDIUM));
        transitions.put(DifficultyLevel.MEDIUM,
                EnumSet.of(DifficultyLevel.EASY, DifficultyLevel.MEDIUM, DifficultyLevel.HARD));
        transitions.put(DifficultyLevel.HARD,
                EnumSet.of(DifficultyLevel.MEDIUM, DifficultyLevel.HARD));
    }

    public DifficultyLevel clampTransition(DifficultyLevel from, DifficultyLevel to) {
        Set<DifficultyLevel> allowed = transitions.get(from);
        return allowed.contains(to) ? to : from;
    }
}
