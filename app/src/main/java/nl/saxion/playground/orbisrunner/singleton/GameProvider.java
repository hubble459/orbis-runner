package nl.saxion.playground.orbisrunner.singleton;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

import nl.saxion.playground.orbisrunner.R;
import nl.saxion.playground.orbisrunner.model.Level;
import nl.saxion.playground.orbisrunner.model.Shop;
import nl.saxion.playground.orbisrunner.model.ShopItem;
import nl.saxion.playground.orbisrunner.model.game.sprite.Player;

import static android.content.Context.MODE_PRIVATE;

/**
 * Used for saving data and fetching it
 * You can get and set coins, the player, the shop with its items, and the level
 * More could be easily added (Like saving if sound and music should be on or off)
 * <p>
 * The getSave should be called as soon as possible (Splash Screen)
 */
public class GameProvider {
    private static GameProvider instance = new GameProvider();
    private Shop shop;
    private Player player;
    private int coins;
    private int currentLevel;
    private int maxLevel;
    private boolean musicOn;
    private boolean soundOn;
    private boolean firstPlay;

    private ArrayList<Level> levels;

    /**
     * Init the player and shop
     */
    private GameProvider() {
        shop = new Shop();
        player = new Player();
        levels = new ArrayList<>();
        musicOn = true;
        soundOn = true;
        firstPlay = true;
    }

    /**
     * Get the level that you're currently on
     *
     * @return Level
     */
    public static Level getCurrentLevel() {
        if (instance.currentLevel > getLevels().size()) {
            instance.currentLevel = getLevels().size() - 1;
        }
        return getLevels().get(instance.currentLevel);
    }

    public static void setCurrentLevel(int currentLevel) {
        instance.currentLevel = currentLevel;
    }

    /**
     * Returns false if there is no next level
     *
     * @return boolean
     */
    public static boolean nextLevel() {
        if (instance.currentLevel < getLevels().size() - 1) {
            instance.currentLevel += 1;
            return true;
        } else {
            return false;
        }
    }

    public static int getCoins() {
        return instance.coins;
    }

    public static void setCoins(int coins) {
        instance.coins = coins;
    }

    public static void subtractCoins(int amount) {
        instance.coins -= amount;
    }

    public static int getMaxLevel() {
        return instance.maxLevel;
    }

    public static void setMaxLevel(int maxLevel) {
        instance.maxLevel = maxLevel;
    }

    public static ArrayList<Level> getLevels() {
        return instance.levels;
    }

    public static Player getPlayer() {
        return instance.player;
    }

    public static Shop getShop() {
        return instance.shop;
    }

    public static boolean isMusicOn() {
        return instance.musicOn;
    }

    public static void setMusicOn(boolean on) {
        instance.musicOn = on;
    }

    public static boolean isSoundOn() {
        return instance.soundOn;
    }

    public static void setSoundOn(boolean soundOn) {
        instance.soundOn = soundOn;
    }

    public static boolean isFirstPlay() {
        return instance.firstPlay;
    }

    public static void setFirstPlay(boolean firstPlay) {
        instance.firstPlay = firstPlay;
    }

    public static boolean hasLevel(Level level) {
        for (Level l : instance.levels) {
            if (l.getNumber() == level.getNumber()) {
                return true;
            }
        }
        return false;
    }

    private static Level getSameLevel(Level level) {
        for (Level l : getLevels()) {
            if (l.getNumber() == level.getNumber()) {
                return l;
            }
        }
        return null;
    }

    /**
     * Save all variables to a json file named "savedData.json" in the files dir for this app
     *
     * @param context needed to get the files directory and for the output stream
     */
    public static void saveData(Context context) {
        Log.i("uwu", "saveData: saving");
        try {
            JSONObject savedDataJSON = new JSONObject();

            savedDataJSON.put("coins", instance.coins);
            savedDataJSON.put("level", instance.currentLevel);
            savedDataJSON.put("maxLevel", instance.maxLevel);
            savedDataJSON.put("color", instance.player.getColor());
            savedDataJSON.put("active", instance.shop.getSelected());
            savedDataJSON.put("music", instance.musicOn);
            savedDataJSON.put("sound", instance.soundOn);
            savedDataJSON.put("firstPlay", instance.firstPlay);

            JSONArray unlockedItems = new JSONArray();
            for (ShopItem shopItem : instance.shop.getShopItems()) {
                if (shopItem.isUnlocked()) {
                    unlockedItems.put(shopItem.getId());
                }
            }
            savedDataJSON.put("unlocked", unlockedItems);

            JSONArray levels = new JSONArray();
            for (Level level : instance.levels) {
                levels.put(level.toJSON());
            }
            savedDataJSON.put("levels", levels);

            String name = "savedData.json";
            File path = new File(context.getFilesDir(), name);
            boolean deleted = true;
            if (path.exists()) {
                deleted = context.deleteFile(name);
            }
            if (deleted) {
                OutputStreamWriter out = new OutputStreamWriter(context.openFileOutput(name, MODE_PRIVATE));
                out.write(savedDataJSON.toString());
                out.close();
            }
        } catch (IOException | JSONException e) {
            Log.e("uwu", "saveData: ", e);
        }
    }

    /**
     * Get all saved variables from a json file named "savedData.json" in the files dir for this app
     *
     * @param context needed to get the files directory
     */
    public static void getSave(final Context context) {
        // Get levels
        try {
            Scanner sc = new Scanner(context.getResources().openRawResource(R.raw.levels));
            StringBuilder builder = new StringBuilder();
            while (sc.hasNextLine()) {
                builder.append(sc.nextLine());
            }

            JSONArray levels = new JSONArray(builder.toString());
            for (int i = 0; i < levels.length(); i++) {
                JSONObject level = levels.optJSONObject(i);
                if (level != null) {
                    Level l = Level.fromJSON(level);
                    instance.levels.add(l);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Get data
        try {
            File file = new File(context.getFilesDir() + "/savedData.json");
            if (!file.exists()) return;

            Scanner sc = new Scanner(file);
            String jsonString = sc.nextLine();

            JSONObject data = new JSONObject(jsonString);

            instance.coins = data.optInt("coins");
            instance.currentLevel = data.optInt("level");
            instance.maxLevel = data.optInt("maxLevel");
            instance.player.setColor(data.optInt("color"));
            instance.shop.select(data.optInt("active"));
            instance.musicOn = data.optBoolean("music", true);
            instance.soundOn = data.optBoolean("sound", true);
            instance.firstPlay = data.optBoolean("firstPlay", true);

            JSONArray unlocked = data.optJSONArray("unlocked");
            if (unlocked != null) {
                for (int i = 0; i < unlocked.length(); i++) {
                    instance.shop.unlock(unlocked.getInt(i));
                }
            }

            JSONArray levels = data.optJSONArray("levels");
            if (levels != null) {
                for (int i = 0; i < levels.length(); i++) {
                    JSONObject object = levels.optJSONObject(i);
                    if (object != null) {
                        Level level = Level.fromJSON(object);
                        if (level != null) {
                            if (!hasLevel(level)) {
                                instance.levels.add(level);
                            } else {
                                Level gLevel = getSameLevel(level);
                                if (gLevel != null) {
                                    gLevel.setObjectiveClaimed(level.getObjectiveClaimed());
                                    gLevel.setDeathCounter(level.getDeathCounter());
                                }
                            }
                        }
                    }
                }
                Collections.sort(instance.levels, new Comparator<Level>() {
                    @Override
                    public int compare(Level o1, Level o2) {
                        return String.valueOf(o1.getNumber()).compareTo(String.valueOf(o2.getNumber()));
                    }
                });
            }
        } catch (FileNotFoundException | JSONException e) {
            Log.e("uwu", "getSave: ", e);
        }
    }

    /**
     * Reset this Singleton
     */
    public static void newInstance() {
        instance = new GameProvider();
    }
}


