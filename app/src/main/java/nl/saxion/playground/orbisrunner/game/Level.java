package nl.saxion.playground.orbisrunner.game;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import nl.saxion.playground.orbisrunner.lib.Entity;
import nl.saxion.playground.orbisrunner.ui.demo.entities.DemoEnemy;

public class Level {
    private int number;
    private ArrayList<Entity> entities;

    public Level(int number) {
        this.number = number;
        this.entities = new ArrayList<>();
    }

    public static Level dummy() {
        Level l = new Level(-1);
        l.addEntity(new DemoEnemy());
        return l;
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

    public static Level fromJSON(JSONObject level) {
        if (level == null) return null;

        int number = level.optInt("number");
        Level l = new Level(number);

        JSONArray entities = level.optJSONArray("entities");
        if (entities != null) {
            for (int i = 0; i < entities.length(); i++) {
                Entity e = Entity.fromJSON(entities.optJSONObject(i));
                if (e != null) {
                    l.addEntity(e);
                }
            }
        }

        return l;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject level = new JSONObject();
        level.put("number", number);

        JSONArray entities = new JSONArray();
        for (Entity entity : this.entities) {
            entities.put(entity.toJSON());
        }
        level.put("entities", entities);
        return level;
    }
}