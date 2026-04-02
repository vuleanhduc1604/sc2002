package com.game.items;

import com.game.model.combatants.Player;
import com.game.engine.BattleEngine;

public class Potion implements Item {
    @Override
    public void use(Player player, BattleEngine context) {
        player.heal(100);
        System.out.println(player.getName() + " used a Potion and recovered 100 HP!");
    }

    @Override
    public String getName() {
        return "Health Potion";
    }
}
