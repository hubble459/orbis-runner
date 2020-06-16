package nl.saxion.playground.orbisrunner.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;

import java.util.ArrayList;

import nl.saxion.playground.orbisrunner.R;
import nl.saxion.playground.orbisrunner.adapter.ItemGridAdapter;
import nl.saxion.playground.orbisrunner.game.ShopItem;
import nl.saxion.playground.orbisrunner.singleton.GameProvider;
import nl.saxion.playground.orbisrunner.sprite.Player;

/**
 * @author : Joost Winkelman & Quentin Correia
 * @since 09/06/2020
 */
public class CustomizationActivity extends AppCompatActivity {
    private Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customization);

        player = GameProvider.getPlayer();

        init();
    }

    private void init() {
        final ArrayList<Color> colors = colors();

        Spinner spinner = findViewById(R.id.colorSpinner);
        ArrayAdapter<Color> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, colors);
        spinner.setAdapter(spinnerAdapter);

        spinner.setSelection(colorPos(colors, player.getColor()));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                player.setColor(((Color) parent.getItemAtPosition(position)).color);
                GameProvider.saveData(CustomizationActivity.this);
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        GridView itemGrid = findViewById(R.id.itemGrid);
        final ItemGridAdapter gridAdapter = new ItemGridAdapter(this, unlockedItems());
        itemGrid.setAdapter(gridAdapter);

        itemGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setSelected(position);
                gridAdapter.notifyDataSetChanged();
            }
        });
    }

    private void setSelected(int pos) {
        ArrayList<ShopItem> unlocked = unlockedItems();
        for (int i = 0; i < unlocked.size(); i++) {
            ShopItem si = unlocked.get(i);
            if (i == pos) {
                si.setSelected(true);
                // TODO: 09/06/2020 save selected and use in Player class
            } else {
                si.setSelected(false);
            }
        }
    }

    private ArrayList<ShopItem> unlockedItems() {
        ArrayList<ShopItem> items = new ArrayList<>();
        for (ShopItem item : GameProvider.getShop().getShopItems()) {
            if (item.isUnlocked()) {
                items.add(item);
            }
        }
        return items;
    }

    private int colorPos(ArrayList<Color> colors, int color) {
        for (int i = 0; i < colors.size(); i++) {
            if (colors.get(i).color == color) {
                return i;
            }
        }
        return 0;
    }

    private ArrayList<Color> colors() {
        ArrayList<Color> colors = new ArrayList<>();
        colors.add(new Color("Black", android.graphics.Color.BLACK));
        colors.add(new Color("Red", android.graphics.Color.RED));
        colors.add(new Color("Blue", android.graphics.Color.BLUE));
        colors.add(new Color("Cyan", android.graphics.Color.CYAN));
        colors.add(new Color("Yellow", android.graphics.Color.YELLOW));
        colors.add(new Color("Green", android.graphics.Color.GREEN));
        colors.add(new Color("Magenta", android.graphics.Color.MAGENTA));
        return colors;
    }

    public void mainMenu(View view) {
        finish();
    }

    private static class Color {
        private String name;
        private int color;

        public Color(String name, int color) {
            this.name = name;
            this.color = color;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}