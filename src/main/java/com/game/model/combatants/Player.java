package com.game.model.combatants;

import com.game.model.core.Action;
import com.game.model.core.CombatStats;
import com.game.model.core.Combatant;
import com.game.engine.BattleEngine;
import com.game.items.Item;

import java.util.List;
import java.util.Collections;
import java.util.ArrayList;


public abstract class Player extends Combatant{
    
    public static final int SPECIAL_SKILL_COOLDOWN = 3;
    private final List<Item> inventory;
    private int specialSkillCooldown;

    protected Player(String name, CombatStats stats) {
        super(name, stats);
        this.inventory = new ArrayList<>();
        this.specialSkillCooldown = 0;
    }

    // Inventory management
    public void addItem(Item item) {
        if (item == null) throw new NullPointerException("item must not be null");
        inventory.add(item);
    }

    public void useItem(Item item, BattleEngine context) {
        if (!inventory.contains(item)) throw new IllegalArgumentException("Item not in inventory: " + item.getName());
        item.use(this, context);
        inventory.remove(item);
    }

    public List<Item> getInventory() {
        return Collections.unmodifiableList(inventory);
    }
   
    // Special skill cooldown management
    public boolean canUseSpecialSkill() {
        return specialSkillCooldown == 0;
    }

    protected void startCooldown() {
        specialSkillCooldown = SPECIAL_SKILL_COOLDOWN;
    }

    public void decreaseCooldown() {
        if (specialSkillCooldown > 0) {
            specialSkillCooldown--;
        }
    }

    public int getSpecialSkillCooldown() {
        return specialSkillCooldown;
    }

    // Special skills will be implemented by Warrior and Wizard subclasses
    public abstract void useSpecialSkill(Combatant target, List<Combatant> allEnemies);
 
    public abstract void useSpecialSkillNoCooldown(Combatant target, List<Combatant> allEnemies);    
}