package nl.saxion.playground.orbisrunner.model;

import android.support.annotation.DrawableRes;

import java.util.ArrayList;

import nl.saxion.playground.orbisrunner.R;

/**
 * Model class Shop
 * <p>
 * Has shop items which you can 'unlock' by purchasing it with coins
 * Shop is also able to give you the currently active item (item that will be used by player)
 */
public class Shop {
    /**
     * List with shop items
     */
    private ArrayList<ShopItem> shopItems;

    /**
     * Constructor
     * Initialize the itemList
     * Add all shop items to the list
     * <p>
     * Set all items to 'unlocked' for testing purposes
     * TODO: 10/06/2020 remove auto unlock
     */
    public Shop() {
        this.shopItems = new ArrayList<>();

        shopItems();
    }

    /**
     * Add the items that should be in the store, to this method
     */
    private void shopItems() {
        addItem("Crown", R.drawable.hat_crown, 20);
        addItem("Chicken", R.drawable.hat_chicken, 10);
    }

    /**
     * Used to make adding items as simple as possible
     *
     * @param itemName the name of the item
     * @param resId    the id pointing towards the drawable
     * @param price    the price for this shop item
     */
    private void addItem(String itemName, @DrawableRes int resId, int price) {
        ShopItem item = new ShopItem();
        item.setName(itemName);
        item.setResId(resId);
        item.setPrice(price);
        shopItems.add(item);
    }


    public ArrayList<ShopItem> getShopItems() {
        return shopItems;
    }

    /**
     * Unlock an item with the given id
     *
     * @param id itemId
     */
    public void unlock(int id) {
        for (ShopItem shopItem : shopItems) {
            if (shopItem.getId() == id) {
                shopItem.setUnlocked(true);
                break;
            }
        }
    }

    /**
     * Set an item with the given id to selected
     *
     * @param id itemId
     */
    public void select(int id) {
        for (ShopItem shopItem : shopItems) {
            if (shopItem.getId() == id) {
                shopItem.setSelected(true);
                break;
            }
        }
    }

    /**
     * Get the item that's currently selected
     *
     * @return item id if found, -1 if not
     */
    public int getSelected() {
        for (ShopItem shopItem : shopItems) {
            if (shopItem.isSelected()) {
                return shopItem.getId();
            }
        }
        return -1;
    }

    public int getSelectedRes() {
        for (ShopItem shopItem : shopItems) {
            if (shopItem.isSelected()) {
                return shopItem.getResId();
            }
        }
        return -1;
    }
}
