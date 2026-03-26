package com.game.items;

import com.game.model.combatants.Player;
import com.game.engine.BattleEngine;

public interface Item {
    void use(Player player, BattleEngine context);
    String getName();
}
