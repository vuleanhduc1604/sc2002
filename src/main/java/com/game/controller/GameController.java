import com.game.config.LevelConfig;
import com.game.engine.BattleEngine;
import com.game.engine.GameState;
import com.game.model.combatants.Player;
import com.game.model.combatants.Warrior;

import com.game.model.core.Action;
import com.game.model.core.BattleResult;
import com.game.model.core.Combatant;
import com.game.model.core.SpeedBasedTurnOrder;

import java.util.List;
import java.util.Scanner;

/**
 * Entry point of the game.
 * Handles user input/output and controls the game loop.
 */
public class GameController {
    
    /**
     * Main method that starts the game.
     * Initializes player, level, and battle engine, then runs the game loop.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        displayLoadingScreen();

        Player player = new Warrior(); // simplify for now
        LevelConfig level = LevelConfig.createEasyLevel();

        BattleEngine engine = new BattleEngine(
                player,
                level,
                new SpeedBasedTurnOrder()
        );

        engine.startBattle();

        while (engine.getGameState() == GameState.IN_PROGRESS) {

            displayBattleStatus(engine);

            List<Action> actions = player.getAvailableActions();
            displayActionMenu(actions);

            int choice = sc.nextInt();
            if (choice < 1 || choice > actions.size()) {
                System.out.println("Invalid choice, try again.");
                continue;
            }
            Action selectedAction = actions.get(choice - 1);

            Combatant target = null;
            if (!engine.getEnemies().isEmpty()) {
                target = engine.getEnemies().get(0); // simple targeting
            }

            engine.setPlayerAction(selectedAction, target);

            BattleResult result = engine.executeRound();
            displayRoundResult(result);
        }

        if (engine.getGameState() == GameState.PLAYER_VICTORY) {
            displayVictoryScreen(engine);
        } else {
            displayDefeatScreen(engine);
        }

        sc.close();
    }

    private static void displayLoadingScreen() {
        System.out.println("=== Turn-Based Combat Game ===");
    }

    private static void displayBattleStatus(BattleEngine engine) {
        System.out.println("\nRound: " + engine.getRoundCounter());
        System.out.println("Player HP: " + engine.getPlayer().getHp());
        System.out.println("Enemies remaining: " + engine.getEnemies().size());
    }

    private static void displayActionMenu(List<Action> actions) {
        System.out.println("Choose action:");
        for (int i = 0; i < actions.size(); i++) {
            System.out.println((i + 1) + ". " + actions.get(i).getActionName());
        }
    }

    private static void displayRoundResult(BattleResult result) {
        for (String log : result.getLogs()) {
            System.out.println(log);
        }
    }

    private static void displayVictoryScreen(BattleEngine engine) {
        System.out.println("\nVictory!");
        System.out.println("Remaining HP: " + engine.getPlayer().getHp());
        System.out.println("Total Rounds: " + engine.getRoundCounter());
    }

    private static void displayDefeatScreen(BattleEngine engine) {
        System.out.println("\nDefeat!");
        System.out.println("Rounds Survived: " + engine.getRoundCounter());
    }
}