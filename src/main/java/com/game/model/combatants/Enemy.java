package com.game.model.combatants;
 
import com.game.actions.BasicAttack;
import com.game.model.core.Action;
import com.game.model.core.CombatStats;
import com.game.model.core.Combatant;
import java.util.List;

public abstract class Enemy extends Combatant {
 
    protected Enemy(String name, CombatStats stats) {
        super(name, stats);
    }
 

    public Action chooseAction() {
    return new BasicAttack();
    }   

    public Combatant chooseTarget(List<Combatant> validTargets) {
        if (validTargets == null) throw new NullPointerException("validTargets must not be null");
        return validTargets.stream()
                .filter(Combatant::isAlive)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Action> getAvailableActions() {
        return List.of(chooseAction());
    }

}