package nl.saxion.playground.orbisrunner.model;

import android.support.annotation.DrawableRes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
     */
    public Shop() {
        this.shopItems = new ArrayList<>();

        shopItems();
    }

    /**
     * Add the items that should be in the store, to this method
     */
    private void shopItems() {
        addItem("Crown", R.drawable.hat_crown, R.drawable.hat_preview_crown, 20);
        addItem("Chicken", R.drawable.hat_chicken, R.drawable.hat_preview_chicken, 10);
        addItem("Marge", R.drawable.hat_marge, R.drawable.hat_preview_marge, 5);
        addItem("Santa", R.drawable.hat_santa, R.drawable.hat_preview_santa, 50);
        addItem("Top Hat", R.drawable.hat_top, R.drawable.hat_preview_top, 69);
        addItem("Louise's Hat", R.drawable.hat_bunny_ears, R.drawable.hat_preview_bunny_ears, 30);
        addItem("Cat Ears", R.drawable.hat_cat_ears, R.drawable.hat_preview_cat_ears, 50);
        addItem("Cat in the Hat", R.drawable.hat_in_cat, R.drawable.hat_preview_in_cat, 80);

        // Sort by name
        Collections.sort(shopItems, new Comparator<ShopItem>() {
            @Override
            public int compare(ShopItem o1, ShopItem o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
    }

    /**
     * Used to make adding items as simple as possible
     *
     * @param itemName the name of the item
     * @param resId    the id pointing towards the drawable
     * @param price    the price for this shop item
     */
    private void addItem(String itemName, @DrawableRes int resId, @DrawableRes int previewId, int price) {
        ShopItem item = new ShopItem();
        item.setName(itemName);
        item.setResId(resId);
        item.setPreviewId(previewId);
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

    /**
     * Get the resource of the currently selected hat
     *
     * @return resID
     */
    public int getSelectedRes() {
        for (ShopItem shopItem : shopItems) {
            if (shopItem.isSelected()) {
                return shopItem.getResId();
            }
        }
        return -1;
    }
}
