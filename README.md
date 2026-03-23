# SC2002 — Person 1: Core Domain Model & Base Abstractions

## What was built

Six files forming the **zero-dependency foundation** of the combat system:

```
src/main/java/com/game/model/core/
├── CombatStats.java          Value Object — stat storage & validation
├── StatusEffect.java         Abstract base — effect lifecycle (Template Method)
├── Combatant.java            Abstract base — combat state management
├── Action.java               Interface — Command pattern for actions
├── TurnOrderStrategy.java    Interface — Strategy pattern for turn order
└── SpeedBasedTurnOrder.java  Concrete strategy — sort by speed descending
```

---

## Key Design Decisions

### 1. Composition over inheritance for stats
`Combatant` holds a `CombatStats` object rather than six individual `int` fields.
**Why:** Adding a new stat (e.g., `critChance`) only touches `CombatStats`; no change to `Combatant`'s API. Person 2 (Player/Enemy) won't need to update their subclasses either.

### 2. StatusEffect owns its own lifecycle
`apply()` and `remove()` are abstract hooks. The effect is responsible for undoing what it did.
**Why:** Keeps `Combatant.updateEffects()` dumb and generic — it just calls `tick()`, checks `isExpired()`, then calls `remove()`. No switch/case on effect types needed.

### 3. `takeDamage` does NOT calculate damage
Damage formula (attack − defense, crits, etc.) lives in concrete `Action` implementations.
**Why:** SRP. Combatant manages state; Action decides how that state changes.

### 4. `tick()` is final in StatusEffect
Subclasses cannot override the duration-decrement logic.
**Why:** Prevents a buggy subclass from silently breaking the expiry mechanism that `Combatant.updateEffects()` relies on.

### 5. `canAct()` checks by effect name ("Stunned")
Currently checks `effect.getName().equals("Stunned")`.
**Why:** Simple and sufficient for now. If more "can't act" effects are needed later, this can be extended to check a marker interface on `StatusEffect` without changing any callers.

---

## SOLID Principles — Specific Examples

| Principle | Where | Evidence |
|-----------|-------|----------|
| **SRP** | `Combatant` | Manages HP/effects only; no damage calc, no UI |
| **SRP** | `CombatStats` | Owns stat data only; no behaviour |
| **OCP** | `StatusEffect` | New effect types extend the class, nothing modified |
| **OCP** | `TurnOrderStrategy` | New ordering rules are new classes, `BattleEngine` unchanged |
| **LSP** | `Combatant` | `Player` and `Enemy` are substitutable everywhere `Combatant` is used |
| **LSP** | `StatusEffect` | All effects processed identically in `updateEffects()` |
| **ISP** | `Action` | Only `execute()` + `getActionName()` — nothing more |
| **ISP** | `TurnOrderStrategy` | Single method interface |
| **DIP** | `BattleEngine` (future) | Will depend on `Combatant`, `Action`, `TurnOrderStrategy` abstractions |

---

## Design Patterns Used

| Pattern | Class | Purpose |
|---------|-------|---------|
| **Value Object** | `CombatStats` | Groups related data, prevents scattered mutation |
| **Template Method** | `StatusEffect` | `tick()` is the invariant; `apply/remove` are hooks |
| **Strategy** | `TurnOrderStrategy` / `SpeedBasedTurnOrder` | Swappable turn-order algorithms |
| **Command** | `Action` | Actions are objects; engine doesn't know their internals |

---

## Integration Guide for Teammates

### Person 2 — Player & Enemy
Extend `Combatant`:
```java
public class Player extends Combatant {
    public Player(String name, int hp, int attack, int defense, int speed) {
        super(name, new CombatStats(hp, attack, defense, speed));
    }

    @Override
    public List<Action> getAvailableActions() {
        return List.of(new BasicAttack(), new DefendAction(), /* ... */);
    }
}
```

### Person 4 — Concrete Actions
Implement `Action`:
```java
public class BasicAttack implements Action {
    @Override
    public void execute(Combatant actor, Combatant target, List<Combatant> allEnemies) {
        int damage = Math.max(1, actor.getAttack() - target.getDefense());
        target.takeDamage(damage);   // Combatant.takeDamage clamps to 0
    }

    @Override
    public String getActionName() { return "Basic Attack"; }
}
```
For AoE (Arcane Blast), iterate `allEnemies` instead of using `target`.

### Person 5 — Concrete StatusEffects
Extend `StatusEffect`. Use `getTarget().getStats().modifyDefense(n)` in `apply()` and `modifyDefense(-n)` in `remove()`:
```java
public class DefendEffect extends StatusEffect {
    private static final int DEFENSE_BONUS = 10;

    public DefendEffect(Combatant target) {
        super("Defending", 1, target);   // lasts 1 turn
    }

    @Override public void apply()  { getTarget().getStats().modifyDefense(DEFENSE_BONUS); }
    @Override public void remove() { getTarget().getStats().modifyDefense(-DEFENSE_BONUS); }
}
```

For `StunEffect`, the name **must** be `"Stunned"` (exact string) for `Combatant.canAct()` to work:
```java
public class StunEffect extends StatusEffect {
    public StunEffect(Combatant target) { super("Stunned", 1, target); }
    @Override public void apply()  { /* no stat change needed */ }
    @Override public void remove() { /* nothing to undo */ }
}
```

### Person 3 — BattleEngine
```java
TurnOrderStrategy strategy = new SpeedBasedTurnOrder();
List<Combatant> turnOrder  = strategy.determineTurnOrder(allCombatants);

for (Combatant c : turnOrder) {
    c.updateEffects();            // tick effects, remove expired ones
    if (!c.canAct()) continue;    // skip if stunned
    // ... let c choose and execute an Action
}
```

---

## Assumptions & Tradeoffs

| Decision | Alternative considered | Reason chosen |
|----------|-----------------------|---------------|
| `canAct()` checks effect name | Marker interface on `StatusEffect` | Simpler now; can be refactored later without breaking the API |
| `takeDamage(negative)` is silently ignored | Throw exception | Callers shouldn't need to guard against heal-via-damage; silent no-op is safer |
| `tick()` is `final` | Overridable | Protects the expiry contract that `updateEffects()` relies on |
| `modifyAttack/Defense/Speed` clamp to 0 | Allow negative stats | Negative stats would require special casing throughout action code |

---

## Running the Demo

```bash
# From project root
javac -d out src/main/java/com/game/model/core/*.java \
             src/test/java/com/game/model/core/CoreDomainDemo.java
java  -cp out com.game.model.core.CoreDomainDemo
```

Expected output covers:
- `CombatStats` constructor validation
- `takeDamage` HP clamping to 0
- `heal` HP capping at maxHp
- `StatusEffect` apply → tick × 2 → auto-remove lifecycle
- `canAct()` returning false while stunned
- `SpeedBasedTurnOrder` excluding dead combatants, sorting by speed descending
- `BasicAttack` applying damage via the `Action` interface
