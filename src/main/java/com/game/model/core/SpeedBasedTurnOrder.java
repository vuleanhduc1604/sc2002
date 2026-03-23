package com.game.model.core;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Turn order strategy that sorts combatants by speed, highest first.
 * Equal-speed combatants retain their original relative order (stable sort).
 */
public class SpeedBasedTurnOrder implements TurnOrderStrategy {

    private static final Comparator<Combatant> SPEED_DESCENDING =
            (a, b) -> Integer.compare(b.getSpeed(), a.getSpeed());

    /** Filters out dead combatants, then sorts the rest by speed descending. */
    @Override
    public List<Combatant> determineTurnOrder(List<Combatant> combatants) {
        if (combatants == null) throw new NullPointerException("combatants must not be null");

        return combatants.stream()
                .filter(Combatant::isAlive)
                .sorted(SPEED_DESCENDING)
                .collect(Collectors.toList());
    }
}
