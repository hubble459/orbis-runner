package nl.saxion.playground.orbisrunner.game;

import android.support.annotation.DrawableRes;
/**
 * Shop item for buying, unlocking and using (selecting)
 */
public class ShopItem {
    private static int idCount;

    private String name;
    private int id;
    private int resId;
    private boolean unlocked;
    private boolean active;

    /**
     * Set a unique id to the shop item on create
     */
    ShopItem() {
        id = idCount++;
    }

    public int getId() {
        return id;
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
    }

    public boolean isSelected() {
        return active;
    }

    public void setSelected(boolean active) {
        this.active = active;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(@DrawableRes int resId) {
        this.resId = resId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
