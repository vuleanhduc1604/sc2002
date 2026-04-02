package com.game.items;

import com.game.model.combatants.Player;
import com.game.engine.BattleEngine;

public class PowerStone implements Item {
    @Override
    public void use(Player player, BattleEngine context) {
        // Triggers special skill immediately, bypassing cooldown logic
        player.useSpecialSkill(); 
        System.out.println(player.getName() + " used a Power Stone to unleash a Special Skill!");
    }

    @Override
    public String getName() {
        return "Power Stone";
    }
}
