package com.game.actions;

import com.game.model.core.Action;
import com.game.model.core.Combatant;
import com.game.items.Item;
import com.game.model.combatants.Player;
import java.util.List;

public class UseItemAction implements Action {
    private Item item;

    public UseItemAction(Item item) {
        this.item = item;
    }

    @Override
    public void execute(Combatant actor, Combatant target, List<Combatant> allEnemies) {
        if (item != null && actor instanceof Player) {
            // We cast actor to Player because Items specifically require a Player in your Item interface
            item.use((Player) actor, null); 
            ((Player) actor).getInventory().remove(item);
        }
    }

    @Override
    public String getActionName() {
        return "Use Item: " + item.getName();
    }
}
