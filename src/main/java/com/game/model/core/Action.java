package com.game.model.core;

import java.util.List;

/**
 * Represents an action a combatant can perform on their turn (attack, defend, skill, etc.).
 */
public interface Action {

    /**
     * Performs the action.
     *
     * @param actor      the combatant performing the action
     * @param target     primary target; may be null for AoE or self-buffs
     * @param allEnemies all opponents; used by AoE actions
     */
    void execute(Combatant actor, Combatant target, List<Combatant> allEnemies);

    /** @return display name shown in the battle UI (e.g. "Basic Attack") */
    String getActionName();
}
