package nl.saxion.playground;

import android.content.Intent;
import android.graphics.Typeface;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import nl.saxion.playground.orbisrunner.R;
import nl.saxion.playground.orbisrunner.singleton.GameProvider;
import nl.saxion.playground.orbisrunner.ui.ControlTestingActivity;
import nl.saxion.playground.orbisrunner.ui.CustomizationActivity;
import nl.saxion.playground.orbisrunner.ui.DeathScreenActivity;
import nl.saxion.playground.orbisrunner.ui.FinishScreenActivity;
import nl.saxion.playground.orbisrunner.ui.GameActivity;
import nl.saxion.playground.orbisrunner.ui.LevelMaker;
import nl.saxion.playground.orbisrunner.ui.SettingScreenActivity;
import nl.saxion.playground.orbisrunner.ui.SplashScreenActivity;
import nl.saxion.playground.orbisrunner.ui.StartScreenActivity;

/**
 * Starting activity used for testing
 * Click a list item in-app to start an activity
 *
 * @author Quentin Correia
 * @since 13-05-2020
 */
public class TestingMainActivity extends AppCompatActivity {
    private List<Class> activityClasses = new ArrayList<>();

    /**
     * Add the activity class you're testing to the activityClasses list in this method.
     * The name (list item name) will be the same as the class name,
     * so for TemplateActivity.class the name will be TemplateActivity
     */
    private void addActivities() {
        activityClasses.add(SplashScreenActivity.class);
        activityClasses.add(StartScreenActivity.class);
        activityClasses.add(SettingScreenActivity.class);
        activityClasses.add(CustomizationActivity.class);
        activityClasses.add(GameActivity.class);
        activityClasses.add(DeathScreenActivity.class);
        activityClasses.add(LevelMaker.class);
        activityClasses.add(ControlTestingActivity.class);
        activityClasses.add(FinishScreenActivity.class);
    }

    /**
     * Fills the testing list with all activities added in the addActivities() method
     * Instead of the default silkscreen font, a default one is used to make it
     * mare readable
     *
     * @param savedInstanceState unused.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing_main);

        // Should be called in Splash Screen
        GameProvider.getSave(this);

        addActivities();

        ListView list = findViewById(R.id.list);
        list.setAdapter(new ArrayAdapter<Class>(this, android.R.layout.simple_list_item_1, activityClasses) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTypeface(Typeface.DEFAULT);
                tv.setText(Objects.requireNonNull(getItem(position)).getSimpleName());
                return view;
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(TestingMainActivity.this, activityClasses.get(i));
                startActivity(intent);

            }
        });
    }
}
