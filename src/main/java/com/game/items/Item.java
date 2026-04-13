package com.game.items;

import com.game.model.combatants.Player;
import com.game.model.core.Combatant;
import java.util.List;

public interface Item {
    void use(Player player, List<Combatant> enemies);
    String getName();
}
