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
import nl.saxion.playground.orbisrunner.sprite.Sprite;

/**
 * Adapter class for showing Sprites in a grid
 * Used in LevelMaker Activity
 */
public class EntityGridAdapter extends ArrayAdapter<Sprite> {
    private LayoutInflater inflater;

    public EntityGridAdapter(@NonNull Context context, @NonNull List<Sprite> entities) {
        super(context, 0, entities);
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.adapter_entity_grid, parent, false);
        }

        Sprite sprite = getItem(position);
        if (sprite == null) return convertView;

        TextView name = convertView.findViewById(R.id.name);
        ImageView image = convertView.findViewById(R.id.image);

        name.setText(sprite.getName());

        int imageRes = sprite.getBitmapRes();
        image.setImageResource(imageRes);

        return convertView;
    }
}
