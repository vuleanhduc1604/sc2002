package com.game.items;

import com.game.model.combatants.Player;
import com.game.model.core.Combatant;
import java.util.List;

public class PowerStone implements Item {
    @Override
    public void use(Player player, List<Combatant> enemies) {
        Combatant target = enemies.stream()
                .filter(Combatant::isAlive)
                .findFirst()
                .orElse(null);
        player.useSpecialSkillNoCooldown(target, enemies);
        System.out.println(player.getName() + " used a Power Stone to unleash their Special Skill!");
    }

    @Override
    public String getName() {
        return "Power Stone";
    }
}
