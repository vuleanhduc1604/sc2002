package com.game.effects;

import com.game.model.core.Combatant;
import com.game.model.core.StatusEffect;

public class ArcaneBlastBuff extends StatusEffect {
    private static final int ATK_BONUS = 10;
    
    public ArcaneBlastBuff(Combatant target) {
        super("Arcane Blast", StatusEffect.PERMANENT_DURATION, target);
    }

    // Permentantly increase attack by 10
    @Override
    public void apply() {
        getTarget().getStats().modifyAttack(ATK_BONUS);
    }

    @Override
    public void remove() {}
}