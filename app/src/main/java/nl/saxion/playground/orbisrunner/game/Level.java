package nl.saxion.playground.orbisrunner.game;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import nl.saxion.playground.orbisrunner.lib.Entity;
import nl.saxion.playground.orbisrunner.sprite.Player;
import nl.saxion.playground.orbisrunner.sprite.StaticEnemy;

/**
 * Level class used by the game model and level maker
 * The level will contains the entity types with positions
 */
public class Level {
    // Level number. eg. 3 for level 3
    private int number;
    // List of entities in the level
    private ArrayList<Entity> entities;
    // Size of level circle
    private float scale;
    private int collectableCoins;
    private int deathCounter;

    /**
     * Set default scale to 1
     *
     * @param number level number
     */
    public Level(int number) {
        this.scale = 1;
        this.number = number;
        this.entities = new ArrayList<>();
    }

    /**
     * If GameProvider#currentLevel() gets called but there are no levels, you will get this dummy
     *
     * @return a dummy level with one static enemy
     */
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

    public void addEntity(Entity entity) {
        this.entities.add(entity);
    }

    public int getCollectableCoins() {
        for (Entity entity : entities) {
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

    /**
     * Get a Level object from a JSONObject
     *
     * @param level JSONObject
     * @return Level
     */
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

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    /**
     * Save a level object to a JSONObject
     *
     * @return JSONObject with level data
     */
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
}
