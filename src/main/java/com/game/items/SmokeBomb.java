package com.game.items;

import com.game.model.combatants.Player;
import com.game.engine.BattleEngine;
import com.game.effects.SmokeBombEffect;

public class SmokeBomb implements Item {

    @Override
    public void use(Player player, BattleEngine context) {
        // Create the effect instance and apply it to the player
        SmokeBombEffect effect = new SmokeBombEffect(player);
        player.applyStatusEffect(effect);
        
        System.out.println(player.getName() + " threw a Smoke Bomb and became Invulnerable for 2 rounds!");
    }

    @Override
    public String getName() {
        return "Smoke Bomb";
    }
}
