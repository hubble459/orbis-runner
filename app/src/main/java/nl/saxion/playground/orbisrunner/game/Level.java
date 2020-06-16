package nl.saxion.playground.orbisrunner.game;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import nl.saxion.playground.orbisrunner.lib.Entity;
import nl.saxion.playground.orbisrunner.sprite.Coin;
import nl.saxion.playground.orbisrunner.sprite.StaticEnemy;

/**
 * Level class used by the game model and level maker
 * The level will contains the entity types with positions
 */
public class Level {
    // List of entities in the level
    private ArrayList<Entity> entities;
    // Scale of level circle
    private float scale;
    // Level number. eg. 3 for level 3
    private int number;
    // Counter for the # of deaths
    private int deathCounter;
    // Counter for coins collected
    private int collectedCoins;
    // Check if you already received bonus coins for completing the objectives
    private boolean objectiveClaimed;
    // Is a custom made level
    private boolean custom;

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
        l.setObjectiveClaimed(level.optBoolean("objectiveClaimed"));
        l.setDeathCounter(level.optInt("deaths"));
        l.setCustom(level.optBoolean("custom"));

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

    public void setDeathCounter(int deathCounter) {
        this.deathCounter = deathCounter;
    }

    public void collectCoin() {
        ++collectedCoins;
    }

    public boolean getObjectiveClaimed() {
        return objectiveClaimed;
    }

    public int getDeathCounter() {
        return deathCounter;
    }

    public void death() {
        deathCounter++;
    }

    public void setObjectiveClaimed(boolean objectiveClaimed) {
        this.objectiveClaimed = objectiveClaimed;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public boolean isCustom() {
        return custom;
    }

    public void setCustom(boolean custom) {
        this.custom = custom;
    }

    public int getCollectedCoins() {
        return collectedCoins;
    }

    public void setCollectedCoins(int collectedCoins) {
        this.collectedCoins = collectedCoins;
    }

    public int getCollectibleCoins() {
        int collectibleCoins = 0;
        for (Entity entity : entities) {
            if (entity instanceof Coin)
                collectibleCoins++;
        }
        return collectibleCoins;
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
        level.put("deaths", deathCounter);
        level.put("objectiveClaimed", objectiveClaimed);
        level.put("custom", custom);

        JSONArray entities = new JSONArray();
        for (Entity entity : this.entities) {
            entities.put(entity.toJSON());
        }
        level.put("entities", entities);
        return level;
    }
}
