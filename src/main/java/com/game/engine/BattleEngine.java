package com.game.engine;

import com.game.config.LevelConfig;
import com.game.model.combatants.Player;
import com.game.model.core.Action;
import com.game.model.core.BattleResult;
import com.game.model.core.Combatant;
import com.game.model.core.TurnOrderStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * Core engine that manages the flow of the turn-based battle.
 * Handles turn order, action execution, status effects, and win/loss conditions.
 */
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

    /**
     * Constructs a BattleEngine with a player, level configuration, and turn order strategy.
     * @param player   the player character
     * @param level    the level configuration containing enemies and backup enemies
     * @param strategy the strategy used to determine turn order
     */
    public BattleEngine(Player player, LevelConfig level, TurnOrderStrategy strategy) {
        this.player = player;
        this.enemies = new ArrayList<>(level.getInitialEnemies());
        this.backupEnemies = new ArrayList<>(level.getBackupEnemies());
        this.turnOrderStrategy = strategy;
        this.roundCounter = 1;
        this.gameState = GameState.SETUP;
    }

    /**
     * Sets the player's selected action and target for the next turn.
     * @param action the action chosen by the player
     * @param target the target of the action
     */
    public void setPlayerAction(Action action, Combatant target) {
        this.playerAction = action;
        this.playerTarget = target;
    }

    /**
     * Returns the current state of the game.
     * @return the current GameState 
     */
    public GameState getGameState() { return gameState; }

    /**
     * Returns the current round number.
     * @return the current round counter
     */
    public int getRoundCounter() { return roundCounter; }

    /**
     * Returns the player instance.
     * @return the player
     */
    public Player getPlayer() { return player; }
    
    /**
     * Returns the list of current enemies.
     * @return list of active enemies
     */
    public List<Combatant> getEnemies() { return enemies; }

    /**
     * Starts the battle by setting the game state to IN_PROGRESS.
     */    
    public void startBattle() {
        gameState = GameState.IN_PROGRESS;
    }

    /**
     * Executes a full round of combat.
     * Determines turn order, processes each combatant's turn,
     * removes defeated enemies, spawns backups if needed,
     * and checks victory/defeat conditions.
     * @return BattleResult containing logs of all actions in the round
     */
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

        // Apply status effects
        c.updateEffects();

        if (!c.canAct()) {
            result.addLog(c.getName() + " is stunned and skips turn.");
            return;
        }

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
        if (playerAction == null || playerTarget == null) {
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