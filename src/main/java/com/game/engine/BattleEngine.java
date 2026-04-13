package com.game.engine;

import com.game.config.LevelConfig;
import com.game.model.combatants.Player;
import com.game.model.core.Action;
import com.game.model.core.BattleResult;
import com.game.model.core.Combatant;
import com.game.model.core.TurnOrderStrategy;

import java.util.ArrayList;
import java.util.List;

public class BattleEngine {

    private Player player;
    private List<Combatant> enemies;
    private List<Combatant> backupEnemies;
    private TurnOrderStrategy turnOrderStrategy;

    private int roundCounter;
    private GameState gameState;
    private boolean backupSpawned = false;

    private Action playerAction;
    private Combatant playerTarget;

    public BattleEngine(Player player, LevelConfig level, TurnOrderStrategy strategy) {
        this.player = player;
        this.enemies = new ArrayList<>(level.getInitialEnemies());
        this.backupEnemies = new ArrayList<>(level.getBackupEnemies());
        this.turnOrderStrategy = strategy;
        this.roundCounter = 1;
        this.gameState = GameState.SETUP;
    }

    public void setPlayerAction(Action action, Combatant target) {
        this.playerAction = action;
        this.playerTarget = target;
    }

    public GameState getGameState() { return gameState; }
    public int getRoundCounter()    { return roundCounter; }
    public Player getPlayer()       { return player; }
    public List<Combatant> getEnemies() { return enemies; }

    public void startBattle() {
        gameState = GameState.IN_PROGRESS;
    }

    public BattleResult executeRound() {
        BattleResult result = new BattleResult();
        result.addLog("=== Round " + roundCounter + " ===");

        List<Combatant> turnOrder = getAllCombatants();
        turnOrder = turnOrderStrategy.determineTurnOrder(turnOrder);

        for (Combatant c : turnOrder) {
            if (gameState != GameState.IN_PROGRESS) break;
            processTurn(c, result);
        }

        removeDefeated(result);
        checkDefeatCondition();

        if (gameState != GameState.IN_PROGRESS) return result;

        spawnBackupEnemies(result);
        checkVictoryCondition();

        roundCounter++;
        return result;
    }

    private void processTurn(Combatant c, BattleResult result) {
        if (!c.isAlive()) return;

        // Check stun BEFORE ticking so the stun lasts the full 2 turns (current + next)
        if (!c.canAct()) {
            result.addLog(c.getName() + " is stunned and skips turn.");
            c.updateEffects(); // still tick down duration while stunned
            return;
        }

        c.updateEffects();

        if (c instanceof Player) {
            executePlayerTurn((Player) c, result);
        } else {
            executeEnemyTurn(c, result);
        }

        // Cooldown decreases only when player takes a turn
        if (c instanceof Player) {
            ((Player) c).decreaseCooldown();
        }
    }

    private void executePlayerTurn(Player player, BattleResult result) {
        if (playerAction == null) {
            result.addLog("Player skipped turn.");
            return;
        }

        playerAction.execute(player, playerTarget, new ArrayList<>(enemies));
        result.addLog("Player used " + playerAction.getActionName());
        playerAction = null;
        playerTarget = null;
    }

    private void executeEnemyTurn(Combatant enemy, BattleResult result) {
        List<Action> enemyActions = enemy.getAvailableActions();

        if (enemyActions.isEmpty()) return;

        Action action = enemyActions.get(0);
        action.execute(enemy, player, List.of(player));

        result.addLog(enemy.getName() + " used " + action.getActionName());
    }

    private void removeDefeated(BattleResult result) {
        enemies.removeIf(enemy -> {
            if (!enemy.isAlive()) {
                result.addLog(enemy.getName() + " is defeated.");
                return true;
            }
            return false;
        });
    }

    private void spawnBackupEnemies(BattleResult result) {
        if (!backupSpawned && enemies.isEmpty() && !backupEnemies.isEmpty()) {
            enemies.addAll(backupEnemies);
            backupSpawned = true;
            result.addLog("Backup enemies spawned!");
        }
    }

    private void checkVictoryCondition() {
        if (enemies.isEmpty() && (backupSpawned || backupEnemies.isEmpty())) {
            gameState = GameState.PLAYER_VICTORY;
        }
    }

    private void checkDefeatCondition() {
        if (!player.isAlive()) {
            gameState = GameState.PLAYER_DEFEAT;
        }
    }

    private List<Combatant> getAllCombatants() {
        List<Combatant> all = new ArrayList<>();
        all.add(player);
        all.addAll(enemies);
        return all;
    }
}