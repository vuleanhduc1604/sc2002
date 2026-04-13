package com.game.model.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Combatant {

    private final String name;
    private final CombatStats stats;
    private final List<StatusEffect> activeStatusEffects;
    private boolean alive;

    protected Combatant(String name, CombatStats stats) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("name must not be null or blank");
        if (stats == null)
            throw new NullPointerException("stats must not be null");

        this.name                = name;
        this.stats               = stats;
        this.activeStatusEffects = new ArrayList<>();
        this.alive               = stats.getHp() > 0;
    }

    public void takeDamage(int damage) {
        if (damage <= 0) return; 
        boolean hasSmokeBomb = activeStatusEffects.stream()
                .anyMatch(e -> "Invulnerable".equals(e.getName()));        
        if (hasSmokeBomb) {
            return;
        }
        stats.setHp(stats.getHp() - damage);
        alive = stats.getHp() > 0;
    }

    public void heal(int amount) {
        if (amount <= 0) return;
        stats.setHp(stats.getHp() + amount);
    }

    public void applyStatusEffect(StatusEffect effect) {
        if (effect == null) throw new NullPointerException("effect must not be null");
        activeStatusEffects.add(effect);
        effect.apply();
    }

    public void removeStatusEffect(StatusEffect effect) {
        if (effect == null) return;
        if (activeStatusEffects.remove(effect)) {
            effect.remove();
        }
    }

    public void updateEffects() {
        List<StatusEffect> snapshot = new ArrayList<>(activeStatusEffects);
        for (StatusEffect effect : snapshot) {
            effect.tick();
            if (effect.isExpired()) {
                activeStatusEffects.remove(effect);
                effect.remove();
            }
        }
    }

    public boolean canAct() {
        return activeStatusEffects.stream()
                .noneMatch(e -> "Stunned".equals(e.getName()));
    }

    // Getters

    public String getName()    { return name; }
    public CombatStats getStats() { return stats; }
    public int getHp()         { return stats.getHp(); }
    public int getMaxHp()      { return stats.getMaxHp(); }
    public int getAttack()     { return stats.getAttack(); }
    public int getDefense()    { return stats.getDefense(); }
    public int getSpeed()      { return stats.getSpeed(); }
    public boolean isAlive()   { return alive; }

    public List<StatusEffect> getActiveStatusEffects() {
        return Collections.unmodifiableList(activeStatusEffects);
    }

    public abstract List<Action> getAvailableActions();

    @Override
    public String toString() {
        return String.format("Combatant{name='%s', %s, alive=%b, effects=%d}",
                name, stats, alive, activeStatusEffects.size());
    }
}
