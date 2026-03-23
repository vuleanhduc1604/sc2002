package com.game.model.core;

/**
 * Abstract base for all status effects (stun, buffs, debuffs).
 * Subclasses override apply() and remove(); tick() handles duration automatically.
 */
public abstract class StatusEffect {

    /** Sentinel value for a permanent (never-expiring) effect. */
    public static final int PERMANENT_DURATION = -1;

    private final String name;
    private int duration;
    private final Combatant target;

    /**
     * @param name     display name; must not be null or blank
     * @param duration turns remaining, or -1 for permanent
     * @param target   combatant this effect is applied to; must not be null
     */
    protected StatusEffect(String name, int duration, Combatant target) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("name must not be null or blank");
        if (duration < PERMANENT_DURATION)
            throw new IllegalArgumentException("duration must be >= -1, got: " + duration);
        if (target == null)
            throw new NullPointerException("target must not be null");

        this.name     = name;
        this.duration = duration;
        this.target   = target;
    }

    // Abstract hooks — subclasses define effect-specific behaviour

    /** Called once when this effect is first applied. */
    public abstract void apply();

    /** Called once when this effect expires or is removed. */
    public abstract void remove();

    // Lifecycle — final to protect the expiry contract

    /** Decrements duration by 1 each turn. Permanent effects are unaffected. */
    public final void tick() {
        if (duration != PERMANENT_DURATION) {
            duration = Math.max(0, duration - 1);
        }
    }

    // Getters

    /** @return true when duration reaches 0 */
    public boolean isExpired()   { return duration == 0; }
    public String getName()      { return name; }
    public int getDuration()     { return duration; }
    public Combatant getTarget() { return target; }

    @Override
    public String toString() {
        String d = (duration == PERMANENT_DURATION) ? "permanent" : duration + " turns";
        return String.format("StatusEffect{name='%s', duration=%s, target='%s'}",
                name, d, target.getName());
    }
}
