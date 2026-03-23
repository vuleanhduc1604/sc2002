package com.game.model.core;

import java.util.List;

/**
 * Strategy for determining the turn order at the start of each battle round.
 */
public interface TurnOrderStrategy {

    /**
     * Returns a new ordered list of alive combatants for the upcoming round.
     * Dead combatants must be excluded. The input list must not be modified.
     *
     * @param combatants all battle participants; must not be null
     * @return ordered list of alive combatants; never null
     */
    List<Combatant> determineTurnOrder(List<Combatant> combatants);
}
