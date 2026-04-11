package com.game.actions;

import com.game.core.Action;
import com.game.items.Item;
import com.game.model.combatants.Player;
import com.game.engine.BattleEngine;

public class UseItemAction implements Action {
    private Player player;
    private Item item;
    private BattleEngine context;

    public UseItemAction(Player player, Item item, BattleEngine context) {
        this.player = player;
        this.item = item;
        this.context = context;
    }

    @Override
    public void execute() {
        if (item != null) {
            item.use(player, context);
            player.getInventory().remove(item);
        }
    }
}
