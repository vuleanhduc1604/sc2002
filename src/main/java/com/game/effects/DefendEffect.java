package com.game.effects;

import com.game.model.core.Combatant;
import com.game.model.core.StatusEffect;

public class DefendEffect extends StatusEffect {
    private static final int DEFENSE_BONUS = 10;

    // Increased defense for two rounds
    public DefendEffect(Combatant target) {
        super("Defending", 2, target);
    }

    @Override
    public void apply() {
        getTarget().getStats().modifyDefense(DEFENSE_BONUS);
    }

    @Override
    public void remove() {
        getTarget().getStats().modifyDefense(-DEFENSE_BONUS);
    }
}
