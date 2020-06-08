package nl.saxion.playground.orbisrunner.game;

import java.util.ArrayList;

import nl.saxion.playground.orbisrunner.R;

public class Shop {
    private ArrayList<ShopItem> shopItems;

    public Shop() {
        this.shopItems = new ArrayList<>();
        // TODO: 09/06/2020 Remove dummies
        addDummies();
    }

    private void addDummies() {
        for (int i = 0; i < 10; i++) {
            ShopItem item = new ShopItem();
            item.setName("item " + i);
            item.setResId(R.drawable.walk_frame_0);
            item.setUnlocked(true);
            shopItems.add(item);
        }

        shopItems.get(0).setSelected(true);
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
                shopItem.setSelected(true);
                break;
            }
        }
    }

    public int getActive() {
        for (ShopItem shopItem : shopItems) {
            if (shopItem.isSelected()) {
                return shopItem.getId();
            }
        }
        return -1;
    }
}
