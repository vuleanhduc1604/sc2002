package com.game.actions;

import com.game.model.core.Action;
import com.game.model.core.Combatant;
import com.game.model.combatants.Player;
import java.util.List;

public class SpecialSkillAction implements Action {

    @Override
    public void execute(Combatant actor, Combatant target, List<Combatant> allEnemies) {
        // Special skills are unique to the Player class
        if (actor instanceof Player) {
            Player player = (Player) actor;
            player.useSpecialSkill(target, allEnemies);
        } else {
            System.out.println(actor.getName() + " cannot use a special skill.");
        }
    }

    @Override
    public String getActionName() {
        return "Special Skill";
    }
}
