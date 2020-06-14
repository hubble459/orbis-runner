package nl.saxion.playground.orbisrunner.game;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import nl.saxion.playground.orbisrunner.lib.Entity;
import nl.saxion.playground.orbisrunner.sprite.Player;
import nl.saxion.playground.orbisrunner.sprite.StaticEnemy;

public class Level {
    private int number;
    private ArrayList<Entity> entities;
    private float scale;
    private int collectableCoins;
    private int deathCounter;

    public Level(int number) {
        this.scale = 1;
        this.number = number;
        this.entities = new ArrayList<>();
    }

    public static Level dummy() {
        Level l = new Level(-1);
        l.addEntity(new StaticEnemy());
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
        l.setScale((float) level.optDouble("scale"));

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

    public int getCollectableCoins() {
        for (Entity entity: entities) {
            if (entity instanceof Player)//Change player to coin when coin object is created.
                collectableCoins++;
        }
        return collectableCoins;
    }

    public int getDeathCounter() {
        return deathCounter;
    }

    public void death() {
        deathCounter++;
    }

    public void setDeathCounter(int deathCounter) {
        this.deathCounter = deathCounter;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject level = new JSONObject();
        level.put("number", number);
        level.put("scale", scale);

        JSONArray entities = new JSONArray();
        for (Entity entity : this.entities) {
            entities.put(entity.toJSON());
        }
        level.put("entities", entities);
        return level;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
}
