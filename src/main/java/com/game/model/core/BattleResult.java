package com.game.model.core;

import java.util.ArrayList;
import java.util.List;

public class BattleResult {

    private List<String> logs;

    public BattleResult() {
        logs = new ArrayList<>();
    }

    public void addLog(String log) {
        logs.add(log);
    }

    public List<String> getLogs() {
        return logs;
    }
}