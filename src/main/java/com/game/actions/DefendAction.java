package com.game.actions;

import com.game.core.Action;
import com.game.model.combatants.Combatant;

public class DefendAction implements Action {
    private Combatant actor;

    public DefendAction(Combatant actor) {
        this.actor = actor;
    }

    @Override
    public void execute() {
        System.out.println(actor.getName() + " is defending!");
        // actor.applyStatusEffect(new DefendEffect()); 
    }
}
