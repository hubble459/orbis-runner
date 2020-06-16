package nl.saxion.playground.orbisrunner.game;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import nl.saxion.playground.orbisrunner.lib.Entity;
import nl.saxion.playground.orbisrunner.sprite.Coin;
import nl.saxion.playground.orbisrunner.sprite.StaticEnemy;

public class Level {
    private int number;
    private ArrayList<Entity> entities;
    private float scale;
    private int deathCounter;
    private boolean objectiveClaimed = false;
    private int collectedCoins = 0;

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
        l.setObjectiveClaimed(level.optBoolean("objectiveClaimed"));
        l.setDeathCounter(level.optInt("deaths"));

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

    public int getCollectibleCoins() {
        int collectibleCoins = 0;
        for (Entity entity: entities) {
            if (entity instanceof Coin)
                collectibleCoins++;
        }
        return collectibleCoins;
    }

    private void setDeathCounter(int deathCounter) {
        this.deathCounter = deathCounter;
    }

    public void collectCoin() {
        ++collectedCoins;
        Log.i("OwO", "collectCoin: " + collectedCoins);
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

    public JSONObject toJSON() throws JSONException {
        JSONObject level = new JSONObject();
        level.put("number", number);
        level.put("scale", scale);
        level.put("deaths", deathCounter);
        level.put("objectiveClaimed", objectiveClaimed);

        JSONArray entities = new JSONArray();
        for (Entity entity : this.entities) {
            entities.put(entity.toJSON());
        }
        level.put("entities", entities);
        return level;
    }

    public int getCollectedCoins() {
        return collectedCoins;
    }

    public void setCollectedCoins(int collectedCoins) {
        this.collectedCoins = collectedCoins;
    }
}
