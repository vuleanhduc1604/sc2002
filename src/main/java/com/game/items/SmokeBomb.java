package com.game.items;

import com.game.model.combatants.Player;
import com.game.engine.BattleEngine;

public class SmokeBomb implements Item {

    @Override
    public void use(Player player, BattleEngine context) {
        System.out.println(player.getName() + " uses a Smoke Bomb! Evasion increased.");
        
        // player.applyStatusEffect(new EvasionEffect());
    }

    @Override
    public String getName() {
        return "Smoke Bomb";
    }
}
