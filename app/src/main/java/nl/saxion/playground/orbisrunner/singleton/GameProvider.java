package nl.saxion.playground.orbisrunner.singleton;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Scanner;

import nl.saxion.playground.orbisrunner.game.Shop;
import nl.saxion.playground.orbisrunner.game.ShopItem;
import nl.saxion.playground.orbisrunner.game.entity.Player;

import static android.content.Context.MODE_PRIVATE;

public class GameProvider {
    private static final GameProvider instance = new GameProvider();
    private Shop shop;
    private Player player;
    private int coins;
    private int level;

    private GameProvider() {
        shop = new Shop();
        player = new Player();
    }

    public static void getSave(Context context) {
        try {
            File file = new File(context.getFilesDir() + "/savedData.json");
            if (!file.exists()) return;

            Scanner sc = new Scanner(file);
            String jsonString = sc.nextLine();

            JSONObject data = new JSONObject(jsonString);

            instance.coins = data.optInt("coins");
            instance.level = data.optInt("level");
            instance.player.setColor(data.optInt("color"));
            instance.shop.activate(data.optInt("active"));

            JSONArray unlocked = data.optJSONArray("unlocked");
            if (unlocked != null) {
                for (int i = 0; i < unlocked.length(); i++) {
                    instance.shop.unlock(unlocked.getInt(i));
                }
            }
        } catch (FileNotFoundException | JSONException e) {
            e.printStackTrace();
        }
    }

    public static void saveData(Context context) {
        try {
            JSONObject savedDataJSON = new JSONObject();

            savedDataJSON.put("coins", instance.coins);
            savedDataJSON.put("level", instance.level);
            savedDataJSON.put("color", instance.player.getColor());
            savedDataJSON.put("active", instance.shop.getActive());

            JSONArray unlockedItems = new JSONArray();
            for (ShopItem shopItem : instance.shop.getShopItems()) {
                if (shopItem.isUnlocked()) {
                    unlockedItems.put(shopItem.getId());
                }
            }
            savedDataJSON.put("unlocked", unlockedItems);

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
            e.printStackTrace();
        }
    }

    public static int getCoins() {
        return instance.coins;
    }

    public static int getLevel() {
        return instance.level;
    }

    public static Player getPlayer() {
        return instance.player;
    }

    public static Shop getShop() {
        return instance.shop;
    }
}
