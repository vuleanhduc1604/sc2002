package com.game.actions;

import com.game.model.core.Action;
import com.game.model.core.Combatant;
import com.game.effects.DefendEffect;
import java.util.List;

public class DefendAction implements Action {

    @Override
    public void execute(Combatant actor, Combatant target, List<Combatant> allEnemies) {
        // Apply the defense bonus effect to the person performing the action
        DefendEffect effect = new DefendEffect(actor);
        actor.applyStatusEffect(effect);
        
        System.out.println(actor.getName() + " takes a defensive stance! Defense increased by 10 for 2 rounds.");
    }

    @Override
    public String getActionName() {
        return "Defend";
    }
}
