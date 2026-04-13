package com.game.items;

import com.game.effects.SmokeBombEffect;
import com.game.model.combatants.Player;
import com.game.model.core.Combatant;
import java.util.List;

public class SmokeBomb implements Item {
    @Override
    public void use(Player player, List<Combatant> enemies) {
        player.applyStatusEffect(new SmokeBombEffect(player));
        System.out.println(player.getName() + " threw a Smoke Bomb and became Invulnerable for 2 turns!");
    }

    @Override
    public String getName() {
        return "Smoke Bomb";
    }
}
