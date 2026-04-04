package com.game.model.combatants;
 
import com.game.effects.StunEffect;
import com.game.model.core.Action;
import com.game.model.core.CombatStats;
import com.game.model.core.Combatant;
import java.util.ArrayList;
import java.util.List;

public class Warrior extends Player {
    // Warriors will have the same base stats across all instances
    static final int maxHp = 260;
    static final int attack = 40;
    static final int defense = 20;
    static final int speed = 30;

    // Creates an instance of a warrior with the default stats
    public Warrior(String name) {
        super(name, new CombatStats(maxHp, attack, defense, speed));
    }

    //Special skill
    @Override
    public void useSpecialSkill(Combatant target, List<Combatant> allEnemies) {
        if (target == null) throw new NullPointerException("target must not be null");
        applyShieldBash(target);
        startCooldown();
    }

    // Intentionally does not call startCooldown() when power stone is used
    @Override
    public void useSpecialSkillNoCooldown(Combatant target, List<Combatant> allEnemies) {
        if (target == null) throw new NullPointerException("target must not be null");
        applyShieldBash(target);
    }

    private void applyShieldBash(Combatant target) {
        int damage = Math.max(0, getAttack() - target.getDefense());
        target.takeDamage(damage);
        target.applyStatusEffect(new StunEffect(target));
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
        if (!getInventory().isEmpty())   actions.add(stubAction("Use Item"));
        if (canUseSpecialSkill())        actions.add(stubAction("Shield Bash"));
 
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
        return String.format("Warrior{name='%s', hp=%d/%d, cooldown=%d}",
                getName(), getHp(), getMaxHp(), getSpecialSkillCooldown());
    }
}