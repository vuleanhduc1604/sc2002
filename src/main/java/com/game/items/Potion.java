package com.game.items;

import com.game.model.combatants.Player;
import com.game.model.core.Combatant;
import java.util.List;

public class Potion implements Item {
    @Override
    public void use(Player player, List<Combatant> enemies) {
        player.heal(100);
        System.out.println(player.getName() + " used a Potion and recovered 100 HP!");
    }

    @Override
    public String getName() {
        return "Potion";
    }
}
