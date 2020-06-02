package nl.saxion.playground.orbisrunner.game;

import java.util.ArrayList;

import nl.saxion.playground.orbisrunner.lib.Entity;

public class Level {
    private int number;
    private int playerDegrees;
    private ArrayList<Entity> entities;

    public Level() {
        entities = new ArrayList<>();
        playerDegrees = 90; // in degrees
    }

    public int getNumber() {
        return number;
    }

    ArrayList<Entity> getEntities() {
        return entities;
    }

    int getPlayerDegrees() {
        return playerDegrees;
    }
}
