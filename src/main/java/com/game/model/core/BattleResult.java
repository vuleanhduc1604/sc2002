package com.game.model.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores the results of a battle round.
 * Contains logs describing actions taken during the round.
 */
public class BattleResult {

    private List<String> logs;

    public BattleResult() {
        logs = new ArrayList<>();
    }

    /**
     * Adds a log entry to the battle result.
     *
     * @param log the message describing an action or event
     */
    public void addLog(String log) {
        logs.add(log);
    }

    /**
     * Returns all log messages from the battle round.
     *
     * @return list of log strings
     */
    public List<String> getLogs() {
        return logs;
    }
}