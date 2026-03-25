package com.game.effects;

import com.game.model.core.Combatant;
import com.game.model.core.StatusEffect;

public class SmokeBombEffect extends StatusEffect {
    
    // Smoke bomb effect for 2 rounds
    public SmokeBombEffect(Combatant target) {
        super("Invulnerable", 2, target);
    }

    // Nothing to apply/remove since invulnerable effect is checked in Combatant.java takeDamage() method
    @Override
    public void apply() {}

    @Override
    public void remove() {}
}