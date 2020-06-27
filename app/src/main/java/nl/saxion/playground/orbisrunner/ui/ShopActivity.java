package nl.saxion.playground.orbisrunner.ui;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import nl.saxion.playground.orbisrunner.R;
import nl.saxion.playground.orbisrunner.adapter.ItemGridAdapter;
import nl.saxion.playground.orbisrunner.model.ShopItem;
import nl.saxion.playground.orbisrunner.singleton.GameProvider;

public class ShopActivity extends AppCompatActivity {
    private TextView coins;
    private ItemGridAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        coins = findViewById(R.id.coins);
        updateCoins();

        adapter = new ItemGridAdapter(this, GameProvider.getShop().getShopItems()) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                ShopItem item = getItem(position);
                if (item == null) return view;

                TextView price = view.findViewById(R.id.selected);
                price.setVisibility(View.VISIBLE);

                if (item.isUnlocked()) {
                    view.setBackgroundColor(Color.argb(69, 69, 69, 69));
                    price.setText(R.string.bought);
                } else {
                    view.setBackgroundColor(Color.TRANSPARENT);
                    price.setText(String.format(getString(R.string.price_), item.getPrice()));
                }

                return view;
            }
        };

        GridView grid = findViewById(R.id.shopItemGrid);
        grid.setAdapter(adapter);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ShopItem item = (ShopItem) parent.getItemAtPosition(position);
                if (!item.isUnlocked()) {
                    buyItem(item);
                } else {
                    Toast.makeText(ShopActivity.this, R.string.already_bought, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateCoins() {
        coins.setText(String.valueOf(GameProvider.getCoins()));
    }

    private void buyItem(final ShopItem item) {
        if (item.getPrice() > GameProvider.getCoins()) {
            Toast.makeText(this, R.string.insufficient_coins, Toast.LENGTH_SHORT).show();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.buy)
                    .setMessage(String.format(getString(R.string.buy_item_for_price), item.getName(), item.getPrice()))
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            item.setUnlocked(true);
                            for (ShopItem shopItem : GameProvider.getShop().getShopItems()) {
                                if (shopItem.isUnlocked()) {
                                    shopItem.setSelected(false);
                                }
                            }
                            item.setSelected(true);
                            GameProvider.subtractCoins(item.getPrice());
                            updateCoins();
                            adapter.notifyDataSetChanged();
                        }
                    })
                    .setNegativeButton(R.string.cancel, null)
                    .show();
        }
    }

    public void mainMenu(View view) {
        finish();
    }
}