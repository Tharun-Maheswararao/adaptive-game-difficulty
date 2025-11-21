package test;

import difficulty.AdaptiveDifficultyEngine;
//import game.Bullet;
import game.Enemy;
import game.GameEngine;

/**
 * Test file to verify GameEngine logic without launching JavaFX.
 * Runs deterministic updates, prints internal state.
 */
public class TestGameEngine {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("=== TEST STARTED ===");

        AdaptiveDifficultyEngine diff = new AdaptiveDifficultyEngine();
        GameEngine game = new GameEngine(diff);

        // -------------------------------
        // TEST 1: SHOOTING
        // -------------------------------
        System.out.println("\n[TEST] Player shoots 2 bullets");
        // simulate pressing SPACE: game.update(left=false,right=false,shoot=true)
        game.update(false, false, true);
        game.update(false, false, true);

        System.out.println("Bullets after shooting: " + game.getBullets().size());

        // -------------------------------
        // TEST 2: SPAWN ENEMIES
        // -------------------------------
        System.out.println("\n[TEST] Spawning 2 enemies manually");
        game.getEnemies().add(new Enemy(100, 100));
        game.getEnemies().add(new Enemy(200, 200));

        System.out.println("Enemies on screen: " + game.getEnemies().size());

        // -------------------------------
        // TEST 3: RUN 10 FRAMES
        // -------------------------------
        System.out.println("\n=== RUNNING 10 FRAMES ===");

        for (int i = 0; i < 10; i++) {

            // spawn an enemy that instantly bypasses (Y > 600)
            game.getEnemies().add(new Enemy(50, 700));

            // no input this frame
            game.update(false, false, false);

            System.out.println("Frame " + i);
            System.out.println("   Bullets  = " + game.getBullets().size());
            System.out.println("   Enemies  = " + game.getEnemies().size());
            System.out.println("   Kills    = " + game.getKills());
            System.out.println("   Bypassed = " + game.getBypassed());
            System.out.println("   Score    = " + game.getScore());
        }

        // -------------------------------
        // TEST 4: DIFFICULTY UPDATE
        // -------------------------------
        System.out.println("\n=== WAITING 5 SECONDS FOR DIFFICULTY UPDATE ===");
        Thread.sleep(5000);

        game.update(false, false, false);

        System.out.println("\n[DIFFICULTY UPDATE]");
        System.out.println("New difficulty = " + game.getDifficultyLevel());

        // -------------------------------
        // TEST 5: FORCE GAME OVER
        // -------------------------------
        System.out.println("\n=== FORCING GAME OVER ===");

        for (int i = 0; i < 10; i++) {
            game.getEnemies().add(new Enemy(50, 700));  // instantly bypass
            game.update(false, false, false);
            System.out.println("Bypassed = " + game.getBypassed());
        }

        System.out.println("\n=== FINAL GAME STATE ===");
        System.out.println("Score     = " + game.getScore());
        System.out.println("Kills     = " + game.getKills());
        System.out.println("Bypassed  = " + game.getBypassed());
        System.out.println("Difficulty= " + game.getDifficultyLevel());
        System.out.println("GameOver? = " + game.isGameOver());

        System.out.println("\n=== TEST COMPLETE ===");
    }
}
