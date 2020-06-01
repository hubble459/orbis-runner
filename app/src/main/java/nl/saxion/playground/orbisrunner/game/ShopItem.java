package nl.saxion.playground.orbisrunner.game;

public class ShopItem {
    private static int idCount;

    private int id;
    private boolean unlocked;
    private boolean active;

    ShopItem() {
        id = idCount++;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
