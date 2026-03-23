package com.game.model.core;

import java.util.Arrays;
import java.util.List;

/**
 * Stand-alone demo that exercises every class in {@code com.game.model.core} without
 * any external test framework.  Run {@link #main} to see console output.
 *
 * <p>This file exists purely as a compilation and smoke-test aid for Person 1.
 * Replace with proper JUnit tests once the team adds a test dependency.
 *
 * @author Person 1
 * @version 1.0
 */
public class CoreDomainDemo {

    /** Minimal Combatant stub — just enough to run demos. */
    static class TestCombatant extends Combatant {
        TestCombatant(String name, CombatStats stats) {
            super(name, stats);
        }

        @Override
        public List<Action> getAvailableActions() {
            return List.of(); // not the focus of this demo
        }
    }

    /** Minimal StatusEffect stub that tracks apply/remove calls. */
    static class TestEffect extends StatusEffect {
        boolean applyCalled  = false;
        boolean removeCalled = false;
        final int defenseBonus;

        TestEffect(String name, int duration, Combatant target, int defenseBonus) {
            super(name, duration, target);
            this.defenseBonus = defenseBonus;
        }

        @Override
        public void apply() {
            applyCalled = true;
            getTarget().getStats().modifyDefense(defenseBonus);
            System.out.printf("  [%s] applied → defense +%d%n", getName(), defenseBonus);
        }

        @Override
        public void remove() {
            removeCalled = true;
            getTarget().getStats().modifyDefense(-defenseBonus);
            System.out.printf("  [%s] removed → defense -%d%n", getName(), defenseBonus);
        }
    }

    /** StunEffect stub whose name matches what Combatant.canAct() checks. */
    static class StunEffect extends StatusEffect {
        StunEffect(int duration, Combatant target) {
            super("Stunned", duration, target);
        }

        @Override public void apply()  { System.out.println("  [Stunned] applied"); }
        @Override public void remove() { System.out.println("  [Stunned] removed"); }
    }

    /** Minimal Action stub for compile verification. */
    static class BasicAttack implements Action {
        @Override
        public void execute(Combatant actor, Combatant target, List<Combatant> allEnemies) {
            int rawDamage = actor.getAttack() - target.getDefense();
            int damage    = Math.max(1, rawDamage); // at least 1 damage
            target.takeDamage(damage);
            System.out.printf("  %s attacks %s for %d damage (%s)%n",
                    actor.getName(), target.getName(), damage, target.getStats());
        }

        @Override
        public String getActionName() { return "Basic Attack"; }
    }

    // =========================================================================
    // Main demo
    // =========================================================================

    public static void main(String[] args) {
        System.out.println("=== CombatStats validation ===");
        demoCombatStats();

        System.out.println("\n=== takeDamage / heal clamping ===");
        demoTakeDamageAndHeal();

        System.out.println("\n=== StatusEffect lifecycle ===");
        demoStatusEffectLifecycle();

        System.out.println("\n=== canAct() with StunEffect ===");
        demoStunEffect();

        System.out.println("\n=== SpeedBasedTurnOrder ===");
        demoTurnOrder();

        System.out.println("\n=== BasicAttack action ===");
        demoAction();

        System.out.println("\nAll demos completed.");
    }

    // -------------------------------------------------------------------------

    static void demoCombatStats() {
        CombatStats stats = new CombatStats(100, 20, 10, 15);
        System.out.println("Created: " + stats);

        stats.modifyAttack(5);
        System.out.println("After +5 attack: " + stats);

        stats.modifyDefense(-20); // should clamp to 0
        System.out.println("After -20 defense (clamped): " + stats);

        // Validate constructor throws on invalid input
        try {
            new CombatStats(-1, 10, 5, 5);
            System.out.println("FAIL: should have thrown");
        } catch (IllegalArgumentException e) {
            System.out.println("Correctly rejected maxHp=-1: " + e.getMessage());
        }
    }

    static void demoTakeDamageAndHeal() {
        Combatant c = new TestCombatant("Hero", new CombatStats(50, 10, 5, 8));
        System.out.println("Initial: " + c);

        c.takeDamage(40);
        System.out.printf("After 40 damage: HP=%d, alive=%b%n", c.getHp(), c.isAlive());

        c.takeDamage(999); // should clamp to 0
        System.out.printf("After 999 damage: HP=%d, alive=%b%n", c.getHp(), c.isAlive());

        c.heal(5); // dead combatant can still be healed (revivals)
        System.out.printf("After healing 5: HP=%d, alive=%b%n", c.getHp(), c.isAlive());

        // Healing beyond maxHp should cap
        Combatant full = new TestCombatant("FullHealth", new CombatStats(100, 10, 5, 5));
        full.heal(999);
        System.out.printf("Heal beyond maxHp: HP=%d (expected 100)%n", full.getHp());
    }

    static void demoStatusEffectLifecycle() {
        Combatant c = new TestCombatant("Warrior", new CombatStats(80, 15, 10, 7));
        System.out.println("Defense before effect: " + c.getDefense());

        TestEffect defend = new TestEffect("Defending", 2, c, 20);
        c.applyStatusEffect(defend);
        System.out.println("Defense after apply:   " + c.getDefense() + " (expected 30)");

        // Tick 1 — duration 2 → 1
        c.updateEffects();
        System.out.println("After tick 1: duration=" + defend.getDuration()
                + ", expired=" + defend.isExpired());

        // Tick 2 — duration 1 → 0 → expired, remove() called automatically
        c.updateEffects();
        System.out.println("After tick 2: defense=" + c.getDefense()
                + " (expected 10), effects active=" + c.getActiveStatusEffects().size());

        System.out.println("apply called:  " + defend.applyCalled
                + ", remove called: " + defend.removeCalled);
    }

    static void demoStunEffect() {
        Combatant c = new TestCombatant("Rogue", new CombatStats(60, 18, 5, 20));
        System.out.println("canAct before stun: " + c.canAct()); // true

        StunEffect stun = new StunEffect(1, c);
        c.applyStatusEffect(stun);
        System.out.println("canAct while stunned: " + c.canAct()); // false

        c.updateEffects(); // stun expires
        System.out.println("canAct after stun expires: " + c.canAct()); // true
    }

    static void demoTurnOrder() {
        Combatant fast  = new TestCombatant("Fast",  new CombatStats(100, 10, 5, 30));
        Combatant mid   = new TestCombatant("Mid",   new CombatStats(100, 15, 8, 20));
        Combatant slow  = new TestCombatant("Slow",  new CombatStats(100, 20, 10, 10));
        Combatant dead  = new TestCombatant("Dead",  new CombatStats(100, 5,  2,  25));
        dead.takeDamage(100); // kill it

        TurnOrderStrategy strategy = new SpeedBasedTurnOrder();
        List<Combatant> ordered = strategy.determineTurnOrder(
                Arrays.asList(slow, dead, fast, mid));

        System.out.print("Turn order (dead excluded): ");
        ordered.forEach(c -> System.out.print(c.getName() + "(" + c.getSpeed() + ") "));
        System.out.println();
        System.out.println("Expected: Fast(30) Mid(20) Slow(10)");
    }

    static void demoAction() {
        Combatant attacker = new TestCombatant("Knight",  new CombatStats(100, 25, 10, 12));
        Combatant defender = new TestCombatant("Goblin",  new CombatStats(40,  10,  5, 8));

        Action attack = new BasicAttack();
        System.out.println("Action name: " + attack.getActionName());
        attack.execute(attacker, defender, List.of(defender));
        System.out.printf("Goblin HP after attack: %d, alive=%b%n",
                defender.getHp(), defender.isAlive());
    }
}
