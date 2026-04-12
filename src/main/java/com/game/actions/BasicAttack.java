package com.game.actions;

import com.game.model.core.Action;
import com.game.model.core.Combatant;
import java.util.List;

public class BasicAttack implements Action {

    @Override
    public void execute(Combatant actor, Combatant target, List<Combatant> allEnemies) {
        if (target != null) {
            int damage = Math.max(0, actor.getStats().getAttack() - target.getStats().getDefense());
            target.takeDamage(damage);
            System.out.println(actor.getName() + " attacks " + target.getName() + " for " + damage + " damage!");
        }
    }

    @Override
    public String getActionName() {
        return "Basic Attack";
    }
}
