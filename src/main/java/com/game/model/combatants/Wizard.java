package com.game.model.combatants;

import com.game.effects.StunEffect;
import com.game.model.core.Action;
import com.game.model.core.CombatStats;
import com.game.model.core.Combatant;
import java.util.ArrayList;
import java.util.List;

public class Wizard extends Player{
    // Wizards will have the same base stats across all instances
    static final int maxHp = 200;
    static final int attack = 50;
    static final int defense = 10;
    static final int speed = 20;
    private int arcaneBlastKills;

    // Creates an instance of a wizard with the default stats
    public Wizard(String name) {
        super(name, new CombatStats(maxHp, attack, defense, speed));
        this.arcaneBlastKills = 0;
    }

    // Special skill
    @Override
    public void useSpecialSkill(Combatant target, List<Combatant> allEnemies) {
        if (allEnemies == null) throw new NullPointerException("allEnemies must not be null");
        applyArcaneBlast(allEnemies);
        startCooldown();
    }

    // Intentionally does not call startCooldown() when power stone is used
    @Override
    public void useSpecialSkillNoCooldown(Combatant target, List<Combatant> allEnemies) {
        if (allEnemies == null) throw new NullPointerException("allEnemies must not be null");
        applyArcaneBlast(allEnemies);

    }

    // Iterates through all living enemies and permanently increments attack by 10 for enemy kills
    private void applyArcaneBlast(List<Combatant> allEnemies) {
        for (Combatant enemy : allEnemies) {
            if (!enemy.isAlive()) continue;
 
            boolean wasAlive = enemy.isAlive();
            int damage = Math.max(0, getAttack() - enemy.getDefense());
            enemy.takeDamage(damage);
 
            // Award kill bonus if this Arcane Blast reduced the enemy to 0 HP
            if (wasAlive && !enemy.isAlive()) {
                arcaneBlastKills++;
                getStats().modifyAttack(10); // permanent +10 ATK
            }
        }
    }

    /** @return total enemies killed by Arcane Blast this level */
    public int getArcaneBlastKills() {
        return arcaneBlastKills;
    }

    @Override
    public List<Action> getAvailableActions() {
        List<Action> actions = new ArrayList<>();
 
        // TODO (integration): replace stubs with Person 4's concrete actions
        // actions.add(new BasicAttack());
        // actions.add(new DefendAction());
        // if (!getInventory().isEmpty()) actions.add(new UseItemAction(...));
        // if (canUseSpecialSkill())      actions.add(new SpecialSkillAction());
 
        actions.add(stubAction("Basic Attack"));
        actions.add(stubAction("Defend"));
        if (!getInventory().isEmpty())  actions.add(stubAction("Use Item"));
        if (canUseSpecialSkill())       actions.add(stubAction("Arcane Blast"));
 
        return actions;
    }

    // Placeholder
    private static Action stubAction(String name) {
        return new Action() {
            @Override public void execute(Combatant actor, Combatant target,
                                          java.util.List<Combatant> allEnemies) {
                throw new UnsupportedOperationException(
                        "Stub action — replace with Person 4's implementation");
            }
            @Override public String getActionName() { return name; }
        };
    }
    
    // For debugging
    @Override
    public String toString() {
        return String.format("Wizard{name='%s', hp=%d/%d, atk=%d, cooldown=%d, blastKills=%d}",
                getName(), getHp(), getMaxHp(), getAttack(),
                getSpecialSkillCooldown(), arcaneBlastKills);
    }
}