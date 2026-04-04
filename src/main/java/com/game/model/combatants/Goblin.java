package com.game.model.combatants;

import com.game.model.core.CombatStats;


public class Goblin extends Enemy {
    static final int maxHp = 55;
    static final int attack = 35;
    static final int defense = 15;
    static final int speed = 25;

    public Goblin(String name) {
        super(name, new CombatStats(maxHp, attack, defense, speed));
    }
    
    @Override
    public String toString() {
        return String.format("Goblin{name='%s', hp=%d/%d}", getName(), getHp(), getMaxHp());
    }
}