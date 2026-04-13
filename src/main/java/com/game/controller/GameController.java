package com.game.controller;

import com.game.config.LevelConfig;
import com.game.engine.BattleEngine;
import com.game.engine.GameState;
import com.game.items.Item;
import com.game.items.Potion;
import com.game.items.PowerStone;
import com.game.items.SmokeBomb;
import com.game.model.combatants.Player;
import com.game.model.combatants.Warrior;
import com.game.model.combatants.Wizard;
import com.game.model.core.Action;
import com.game.model.core.BattleResult;
import com.game.model.core.Combatant;
import com.game.model.core.SpeedBasedTurnOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GameController {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean keepPlaying = true;

        while (keepPlaying) {
            Player player = selectClass(sc);
            selectItems(sc, player);
            LevelConfig level = selectDifficulty(sc);

            BattleEngine engine = new BattleEngine(player, level, new SpeedBasedTurnOrder());
            engine.startBattle();

            // Battle loop
            while (engine.getGameState() == GameState.IN_PROGRESS) {
                displayBattleStatus(engine);

                List<Action> actions = player.getAvailableActions();
                displayActionMenu(actions);

                int choice = readInt(sc, 1, actions.size());
                Action selectedAction = actions.get(choice - 1);

                Combatant target = null;
                if (needsTarget(selectedAction)) {
                    target = pickTarget(sc, engine.getEnemies());
                }

                engine.setPlayerAction(selectedAction, target);
                BattleResult result = engine.executeRound();
                displayRoundResult(result);
            }

            if (engine.getGameState() == GameState.PLAYER_VICTORY) {
                displayVictoryScreen(engine);
                keepPlaying = false;
            } else {
                keepPlaying = displayDefeatScreen(sc, engine);
            }
        }

        sc.close();
    }

    private static Player selectClass(Scanner sc) {
        System.out.println("============================================================");
        System.out.println("           WELCOME TO THE TURN-BASED COMBAT GAME           ");
        System.out.println("============================================================");
        System.out.println();
        System.out.println("Choose your class:");
        System.out.println();
        System.out.println("  1. Warrior");
        System.out.println("     HP: 260  |  ATK: 40  |  DEF: 20  |  SPD: 30");
        System.out.println("     Special Skill: Shield Bash — deals damage and stuns one enemy for 2 turns.");
        System.out.println();
        System.out.println("  2. Wizard");
        System.out.println("     HP: 200  |  ATK: 50  |  DEF: 10  |  SPD: 20");
        System.out.println("     Special Skill: Arcane Blast — hits ALL enemies.");
        System.out.println("                    Each kill permanently grants +10 ATK.");
        System.out.println();

        int choice = readInt(sc, 1, 2);
        System.out.println();

        if (choice == 1) {
            System.out.println("You chose: Warrior");
            return new Warrior("Player");
        } else {
            System.out.println("You chose: Wizard");
            return new Wizard("Player");
        }
    }

    private static void selectItems(Scanner sc, Player player) {
        List<Item> available = new ArrayList<>();
        available.add(new Potion());
        available.add(new SmokeBomb());
        available.add(new PowerStone());

        System.out.println();
        System.out.println("Choose 2 items to bring into battle:");
        System.out.println("  1. Potion       — Restores 100 HP.");
        System.out.println("  2. Smoke Bomb   — Become Invulnerable for 2 turns.");
        System.out.println("  3. Power Stone  — Use your Special Skill without triggering the cooldown.");
        System.out.println();

        for (int slot = 1; slot <= 2; slot++) {
            System.out.print("Select item " + slot + " (1-3): ");
            int choice = readInt(sc, 1, 3);
            player.addItem(available.get(choice - 1));
            System.out.println("Added: " + available.get(choice - 1).getName());
        }
    }

    private static LevelConfig selectDifficulty(Scanner sc) {
        System.out.println();
        System.out.println("Select difficulty:");
        System.out.println("  1. Easy   — 3 Goblins (HP:55 ATK:35 DEF:15 SPD:25). No backup.");
        System.out.println("  2. Medium — 1 Goblin + 1 Wolf. Backup: 2 more Wolves.");
        System.out.println("             Wolf stats: HP:40 ATK:45 DEF:5 SPD:35");
        System.out.println("  3. Hard   — 2 Goblins. Backup: 1 Goblin + 2 Wolves.");
        System.out.println();

        int choice = readInt(sc, 1, 3);
        System.out.println();

        switch (choice) {
            case 1:
                System.out.println("Difficulty: Easy");
                return LevelConfig.createEasyLevel();
            case 2:
                System.out.println("Difficulty: Medium");
                return LevelConfig.createMediumLevel();
            default:
                System.out.println("Difficulty: Hard");
                return LevelConfig.createHardLevel();
        }
    }

    private static void displayBattleStatus(BattleEngine engine) {
        System.out.println();
        System.out.println("------------------------------------------------------------");
        System.out.println("Round: " + engine.getRoundCounter());
        Player p = engine.getPlayer();
        System.out.printf("  %s — HP: %d/%d%s%n",
                p.getName(), p.getHp(), p.getMaxHp(),
                p.getSpecialSkillCooldown() > 0
                        ? "  (Special Skill cooldown: " + p.getSpecialSkillCooldown() + ")" : "");
        System.out.println("  Enemies:");
        List<Combatant> enemies = engine.getEnemies();
        for (int i = 0; i < enemies.size(); i++) {
            Combatant e = enemies.get(i);
            System.out.printf("    %d. %s — HP: %d%n", i + 1, e.getName(), e.getHp());
        }
        System.out.println("------------------------------------------------------------");
    }

    private static void displayActionMenu(List<Action> actions) {
        System.out.println("Choose your action:");
        for (int i = 0; i < actions.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + actions.get(i).getActionName());
        }
    }

    private static boolean needsTarget(Action action) {
        String name = action.getActionName();
        return name.equals("Basic Attack") || name.equals("Special Skill");
    }

    private static Combatant pickTarget(Scanner sc, List<Combatant> enemies) {
        if (enemies.size() == 1) {
            return enemies.get(0);
        }
        System.out.println("Select target:");
        for (int i = 0; i < enemies.size(); i++) {
            System.out.printf("  %d. %s (HP: %d)%n", i + 1, enemies.get(i).getName(), enemies.get(i).getHp());
        }
        int choice = readInt(sc, 1, enemies.size());
        return enemies.get(choice - 1);
    }

    private static void displayRoundResult(BattleResult result) {
        System.out.println();
        for (String log : result.getLogs()) {
            System.out.println("  " + log);
        }
    }

    private static void displayVictoryScreen(BattleEngine engine) {
        System.out.println();
        System.out.println("============================================================");
        System.out.println("  Congratulations, you have defeated all your enemies.");
        System.out.println("============================================================");
        System.out.println("  Statistics:");
        System.out.println("    Remaining HP    : " + engine.getPlayer().getHp());
        System.out.println("    Total Rounds    : " + (engine.getRoundCounter() - 1));
        System.out.println("============================================================");
    }

    private static boolean displayDefeatScreen(Scanner sc, BattleEngine engine) {
        System.out.println();
        System.out.println("============================================================");
        System.out.println("  Defeated. Don't give up, try again!");
        System.out.println("============================================================");
        System.out.println("  Statistics:");
        System.out.println("    Enemies remaining   : " + engine.getEnemies().size());
        System.out.println("    Total Rounds Survived: " + (engine.getRoundCounter() - 1));
        System.out.println("============================================================");
        System.out.println();
        System.out.println("What would you like to do?");
        System.out.println("  1. Play Again");
        System.out.println("  2. Exit");
        int choice = readInt(sc, 1, 2);
        return choice == 1;
    }

    private static int readInt(Scanner sc, int min, int max) {
        while (true) {
            System.out.print("Enter choice (" + min + "-" + max + "): ");
            if (sc.hasNextInt()) {
                int val = sc.nextInt();
                if (val >= min && val <= max) return val;
            } else {
                sc.next(); // discard bad token
            }
            System.out.println("Invalid input. Please enter a number between " + min + " and " + max + ".");
        }
    }
}
