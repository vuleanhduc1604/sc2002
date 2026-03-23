package com.game.config;

import com.game.model.combatants.Enemy;
import com.game.model.combatants.Goblin;
import com.game.model.combatants.Wolf;

import java.util.ArrayList;
import java.util.List;

public class LevelConfig {
    private final String difficulty;
    private final List<Enemy> initialEnemies;
    private final List<Enemy> backupEnemies;

    private LevelConfig(String difficulty, List<Enemy> initialEnemies, List<Enemy> backupEnemies) {
        this.difficulty = difficulty;
        this.initialEnemies = initialEnemies;
        this.backupEnemies = backupEnemies;
    }

    // getters
    public String getDifficulty() { return difficulty; }
    public List<Enemy> getInitialEnemies() { return initialEnemies; }
    public List<Enemy> getBackupEnemies() { return backupEnemies; }


    /*
    * Setting the 3 different levels:
    * Easy: Create and return a new Level object with label set to easy,
    * initial array of 3 Goblins objects and empty array for backup
    *
    * Medium: Create and return a new Level object with label set to medium,
    * initial array of 1 Goblin 1 Wolf objects and backup array of 2 Wolves objects
    * 
    * Hard: Create and return a new Level oject with label set to hard,
    * initial array of 2 Goblin objects , backup array of 1 Goblin 2 Wolves objects
    * 
     */
    public static LevelConfig createEasyLevel() {
        List<Enemy> initial = new ArrayList<>();
        initial.add(new Goblin());
        initial.add(new Goblin());
        initial.add(new Goblin());
        
        return new LevelConfig(Difficulty.EASY.name(), initial, new ArrayList<>());
    }

    public static LevelConfig createMediumLevel() {
        List<Enemy> initial = new ArrayList<>();
        initial.add(new Goblin());
        initial.add(new Wolf());
        
        List<Enemy> backup = new ArrayList<>();
        backup.add(new Wolf());
        backup.add(new Wolf());
        
        return new LevelConfig(Difficulty.MEDIUM.name(), initial, backup);
    }

    public static LevelConfig createHardLevel() {
        List<Enemy> initial = new ArrayList<>();
        initial.add(new Goblin());
        initial.add(new Goblin());
        
        List<Enemy> backup = new ArrayList<>();
        backup.add(new Goblin());
        backup.add(new Wolf());
        backup.add(new Wolf());
        
        return new LevelConfig(Difficulty.HARD.name(), initial, backup);
    }
}
