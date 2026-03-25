package com.game.effects;

import com.game.model.core.Combatant;
import com.game.model.core.StatusEffect;

public class DefendEffect extends StatusEffect {
    private static final int DEFENSE_BONUS = 10;

    // Increased defense for two rounds
    public DefendEffect(Combatant target) {
        super("Defending", 2, target);
    }

    // Set Defense to increase by 10 for 2 rounds before decreasing it by 10
    @Override
    public void apply() {
        getTarget().getStats().modifyDefense(DEFENSE_BONUS);
    }

    @Override
    public void remove() {
        getTarget().getStats().modifyDefense(-DEFENSE_BONUS);
    }
}
