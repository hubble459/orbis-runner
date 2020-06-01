package nl.saxion.playground.orbisrunner.game;

import java.util.ArrayList;

public class Shop {
    private ArrayList<ShopItem> shopItems;

    public Shop() {
        ArrayList<ShopItem> shopItems = new ArrayList<>();
        shopItems.add(new ShopItem()); // Hat 1
        shopItems.add(new ShopItem()); // Hat 2
        shopItems.add(new ShopItem()); // Hat 3
        shopItems.add(new ShopItem()); // Hat 4
        this.shopItems = shopItems;
    }

    public ArrayList<ShopItem> getShopItems() {
        return shopItems;
    }

    public void unlock(int id) {
        for (ShopItem shopItem : shopItems) {
            if (shopItem.getId() == id) {
                shopItem.setUnlocked(true);
                break;
            }
        }
    }

    public void activate(int id) {
        for (ShopItem shopItem : shopItems) {
            if (shopItem.getId() == id) {
                shopItem.setActive(true);
                break;
            }
        }
    }

    public int getActive() {
        for (ShopItem shopItem : shopItems) {
            if (shopItem.isActive()) {
                return shopItem.getId();
            }
        }
        return -1;
    }
}
