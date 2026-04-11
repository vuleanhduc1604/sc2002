package com.game.actions;

import com.game.core.Action;
import com.game.model.combatants.Player;

public class SpecialSkillAction implements Action {
    private Player player;

    public SpecialSkillAction(Player player) {
        this.player = player;
    }

    @Override
    public void execute() {
        // Triggers the unique skill logic defined in the Player class
        player.useSpecialSkill();
    }
}
