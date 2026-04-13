package com.game.effects;

import com.game.model.core.Combatant;
import com.game.model.core.StatusEffect;

public class StunEffect extends StatusEffect {
    
    // Stun effect for 2 rounds
    public StunEffect(Combatant target) {
        super("Stunned", 2, target);
    }
    @Override
    public void apply() {}

    @Override
    public void remove() {}
}
