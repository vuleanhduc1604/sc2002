package com.game.model.combatants;
 
import com.game.model.core.CombatStats;

public class Wolf extends Enemy {
    static final int maxHp = 40;
    static final int attack = 45;
    static final int defense = 5;
    static final int speed = 35;

    public Wolf(String name) {
        super(name, new CombatStats(maxHp, attack, defense, speed));
    }
    
    @Override
    public String toString() {
        return String.format("Wolf{name='%s', hp=%d/%d}", getName(), getHp(), getMaxHp());
    }
}