package com.game.effects;

import com.game.model.core.Combatant;
import com.game.model.core.StatusEffect;

public class SmokeBombEffect extends StatusEffect {
    
    // Smoke bomb effect for 2 rounds
    public SmokeBombEffect(Combatant target) {
        super("Smoke Bomb", 2, target);
    }

    // Set Invulnerability to true for 2 rounds to negate damage taken before setting it back to false
    @Override
    public void apply() {
        getTarget().setInvulnerable(true);
    }

    @Override
    public void remove() {
        getTarget().setInvulnerable(false);
    }
}