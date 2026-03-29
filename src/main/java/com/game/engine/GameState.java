package com.game.engine;
/**
 * Represents the current state of the game.
 */
public enum GameState {
    /** Initial setup phase before battle starts */
    SETUP,

    /** Battle is currently in progress */
    IN_PROGRESS,

    /** Player has defeated all enemies */
    PLAYER_VICTORY,
    
    /** Player has been defeated */
    PLAYER_DEFEAT
}