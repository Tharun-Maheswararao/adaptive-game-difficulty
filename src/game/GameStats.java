package game;

public class GameStats {
    public int score = 0;
    public int kills = 0;
    public int shots = 0;
    public int bypassed = 0;
    public double timeSeconds = 0.0;

    public double getAccuracy() {
        return shots == 0 ? 0.0 : (double) kills / shots;
    }
}
