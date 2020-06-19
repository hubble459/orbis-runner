package nl.saxion.playground.orbisrunner.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import nl.saxion.playground.orbisrunner.R;
import nl.saxion.playground.orbisrunner.model.Level;
import nl.saxion.playground.orbisrunner.singleton.GameProvider;

/**
 * Adapter class for showing Levels in a grid
 * Used in LevelSelector Activity
 */
public class LevelGridAdapter extends ArrayAdapter<Level> {
    private LayoutInflater inflater;

    public LevelGridAdapter(@NonNull Context context, @NonNull List<Level> entities) {
        super(context, 0, entities);
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.adapter_level_grid, parent, false);
        }

        Level level = getItem(position);
        if (level == null) return convertView;

        TextView levelNumber = convertView.findViewById(R.id.levelNumber);

        if (position <= GameProvider.getMaxLevel()) {
            if (level.getObjectiveClaimed()) {
                levelNumber.setBackgroundColor(Color.argb(69, 124, 252, 0));
            } else {
                if (level.getDeathCounter() != 0) {
                    levelNumber.setBackgroundColor(Color.argb(69, 255, 165, 0));
                } else {
                    levelNumber.setBackgroundColor(Color.TRANSPARENT);
                }

            }
        } else {
            levelNumber.setBackgroundColor(Color.argb(69, 69, 69, 69));
        }

        levelNumber.setText(String.valueOf(level.getNumber()));

        return convertView;
    }
}
