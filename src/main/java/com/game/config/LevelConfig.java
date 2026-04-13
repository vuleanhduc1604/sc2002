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


    public static LevelConfig createEasyLevel() {
        List<Enemy> initial = new ArrayList<>();
        initial.add(new Goblin("Goblin A"));
        initial.add(new Goblin("Goblin B"));
        initial.add(new Goblin("Goblin C"));
        
        return new LevelConfig(Difficulty.EASY.name(), initial, new ArrayList<>());
    }

    public static LevelConfig createMediumLevel() {
        List<Enemy> initial = new ArrayList<>();
        initial.add(new Goblin("Goblin A"));
        initial.add(new Wolf("Wolf A"));
        
        List<Enemy> backup = new ArrayList<>();
        backup.add(new Wolf("Wolf B"));
        backup.add(new Wolf("Wolf C"));
        
        return new LevelConfig(Difficulty.MEDIUM.name(), initial, backup);
    }

    public static LevelConfig createHardLevel() {
        List<Enemy> initial = new ArrayList<>();
        initial.add(new Goblin("Goblin A"));
        initial.add(new Goblin("Goblin B"));
        
        List<Enemy> backup = new ArrayList<>();
        backup.add(new Goblin("Goblin C"));
        backup.add(new Wolf("Wolf A"));
        backup.add(new Wolf("Wolf B"));
        
        return new LevelConfig(Difficulty.HARD.name(), initial, backup);
    }
}
