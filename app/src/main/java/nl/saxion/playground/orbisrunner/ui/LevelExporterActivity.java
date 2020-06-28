package nl.saxion.playground.orbisrunner.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Locale;

import nl.saxion.playground.orbisrunner.R;
import nl.saxion.playground.orbisrunner.model.Level;
import nl.saxion.playground.orbisrunner.singleton.GameProvider;

/**
 * Used for developing purposes only
 * Handy for sharing levels between group partners
 *
 * @author Quentin Correia
 */
public class LevelExporterActivity extends AppCompatActivity {
    private ClipboardManager clipboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_exporter);

        clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard == null) {
            finish();
        }

        ListView l = findViewById(R.id.levelList);
        ArrayAdapter<Level> adapter = new ArrayAdapter<Level>(this, android.R.layout.simple_list_item_1, GameProvider.getLevels()) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setText(String.format(Locale.ENGLISH, "LVL %d", getItem(position).getNumber()));
                return v;
            }
        };

        l.setAdapter(adapter);
        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Level l = (Level) parent.getItemAtPosition(position);
                try {
                    copy(l.toJSON().toString(3));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void copyAll(View view) {
        try {
            JSONArray array = new JSONArray();
            for (Level level : GameProvider.getLevels()) {
                array.put(level.toJSON());
            }
            copy(array.toString(3));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void copy(String s) {
        ClipData clip = ClipData.newPlainText("LevelJSON", s);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(LevelExporterActivity.this, "Copied!", Toast.LENGTH_SHORT).show();
    }

    public void back(View view) {
        finish();
    }
}