package com.game.actions;

import com.game.core.Action;
import com.game.model.combatants.Combatant;

public class BasicAttack implements Action {
    private Combatant actor;
    private Combatant target;

    public BasicAttack(Combatant actor, Combatant target) {
        this.actor = actor;
        this.target = target;
    }

    @Override
    public void execute() {
        int damage = Math.max(0, actor.getStats().getAttack() - target.getStats().getDefense());
        target.takeDamage(damage);
        System.out.println(actor.getName() + " attacks " + target.getName() + " for " + damage + " damage!");
    }
}
