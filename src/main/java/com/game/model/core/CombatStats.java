package com.game.model.core;

public class CombatStats {

    private static final int MIN_STAT_VALUE = 0;

    private int hp;
    private final int maxHp;
    private int attack;
    private int defense;
    private int speed;

    public CombatStats(int maxHp, int attack, int defense, int speed) {
        if (maxHp <= 0)        
            throw new IllegalArgumentException("maxHp must be > 0, got: " + maxHp);
        if (attack < 0)        
            throw new IllegalArgumentException("attack must be >= 0, got: " + attack);
        if (defense < 0)       
            throw new IllegalArgumentException("defense must be >= 0, got: " + defense);
        if (speed < 0)         
            throw new IllegalArgumentException("speed must be >= 0, got: " + speed);

        this.maxHp = maxHp;
        this.hp = maxHp;
        this.attack = attack;
        this.defense = defense;
        this.speed = speed;
    }

    // Getters
    public int getHp()      { return hp; }
    public int getMaxHp()   { return maxHp; }
    public int getAttack()  { return attack; }
    public int getDefense() { return defense; }
    public int getSpeed()   { return speed; }

    // Setters / modifiers

    public void setHp(int hp) {
        this.hp = Math.max(MIN_STAT_VALUE, Math.min(hp, maxHp));
    }

    public void modifyAttack(int delta) {
        this.attack = Math.max(MIN_STAT_VALUE, this.attack + delta);
    }

    public void modifyDefense(int delta) {
        this.defense = Math.max(MIN_STAT_VALUE, this.defense + delta);
    }

    public void modifySpeed(int delta) {
        this.speed = Math.max(MIN_STAT_VALUE, this.speed + delta);
    }

    @Override
    public String toString() {
        return String.format("CombatStats{hp=%d/%d, atk=%d, def=%d, spd=%d}",
                hp, maxHp, attack, defense, speed);
    }
}
