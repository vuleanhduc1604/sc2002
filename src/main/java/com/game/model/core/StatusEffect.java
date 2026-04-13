package com.game.model.core;

public abstract class StatusEffect {

    public static final int PERMANENT_DURATION = -1;

    private final String name;
    private int duration;
    private final Combatant target;

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

    public abstract void apply();
    public abstract void remove();

    // tick() is final so subclasses can't break the expiry logic in updateEffects()
    public final void tick() {
        if (duration != PERMANENT_DURATION) {
            duration = Math.max(0, duration - 1);
        }
    }

    // Getters

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
