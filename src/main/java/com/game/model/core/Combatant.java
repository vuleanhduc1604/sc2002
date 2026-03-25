package com.game.model.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Abstract base for all battle participants (players and enemies).
 * Manages combat state: HP, status effects, and alive status.
 * Damage calculation is the responsibility of Action implementations, not this class.
 */
public abstract class Combatant {

    private final String name;
    private final CombatStats stats;
    private final List<StatusEffect> activeStatusEffects;
    private boolean alive;

    /**
     * @param name  display name; must not be null or blank
     * @param stats combat statistics; must not be null
     */
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

    // Combat state methods

    /**
     * Reduces HP by damage (clamped to 0). Updates alive status.
     * Non-positive values are ignored.
     */
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

    /**
     * Restores HP by amount (capped at maxHp).
     * Non-positive values are ignored.
     */
    public void heal(int amount) {
        if (amount <= 0) return;
        stats.setHp(stats.getHp() + amount);
    }

    /** Adds an effect and immediately calls its apply(). */
    public void applyStatusEffect(StatusEffect effect) {
        if (effect == null) throw new NullPointerException("effect must not be null");
        activeStatusEffects.add(effect);
        effect.apply();
    }

    /** Removes an effect and calls its remove(). No-op if effect is not active. */
    public void removeStatusEffect(StatusEffect effect) {
        if (effect == null) return;
        if (activeStatusEffects.remove(effect)) {
            effect.remove();
        }
    }

    /**
     * Ticks all active effects and removes any that have expired.
     * Should be called at the start of each combatant's turn.
     */
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

    /** @return false if the combatant has an active "Stunned" effect */
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

    /** @return read-only view of active status effects */
    public List<StatusEffect> getActiveStatusEffects() {
        return Collections.unmodifiableList(activeStatusEffects);
    }

    /** @return actions this combatant can choose from on their turn */
    public abstract List<Action> getAvailableActions();

    @Override
    public String toString() {
        return String.format("Combatant{name='%s', %s, alive=%b, effects=%d}",
                name, stats, alive, activeStatusEffects.size());
    }
}
