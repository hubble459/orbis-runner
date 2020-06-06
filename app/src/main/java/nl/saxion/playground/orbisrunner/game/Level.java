package nl.saxion.playground.orbisrunner.game;

import java.util.ArrayList;

import nl.saxion.playground.orbisrunner.lib.Entity;

public class Level {
    private int number;
    private ArrayList<Entity> entities;

    public Level(int number) {
        this.number = number;
        this.entities = new ArrayList<>();
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public ArrayList<Entity> getEntities() {
        return entities;
    }

    public void setEntities(ArrayList<Entity> entities) {
        this.entities = entities;
    }

    public void addEntity(Entity entity) {
        this.entities.add(entity);
    }
}
