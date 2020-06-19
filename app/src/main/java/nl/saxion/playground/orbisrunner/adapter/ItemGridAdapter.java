package nl.saxion.playground.orbisrunner.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import nl.saxion.playground.orbisrunner.R;
import nl.saxion.playground.orbisrunner.model.ShopItem;

/**
 * Adapter class for showing Items in a grid
 * Used in Customization and Shop Activity class
 */
public class ItemGridAdapter extends ArrayAdapter<ShopItem> {
    private LayoutInflater inflater;

    public ItemGridAdapter(@NonNull Context context, @NonNull List<ShopItem> items) {
        super(context, 0, items);
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.adapter_entity_grid, parent, false);
        }

        ShopItem e = getItem(position);
        if (e == null) return convertView;

        if (e.isSelected()) {
            convertView.findViewById(R.id.selected).setVisibility(View.VISIBLE);
        } else {
            convertView.findViewById(R.id.selected).setVisibility(View.GONE);
        }

        TextView name = convertView.findViewById(R.id.name);
        ImageView image = convertView.findViewById(R.id.image);

        name.setText(e.getName());
        image.setImageResource(e.getResId());

        return convertView;
    }
}
