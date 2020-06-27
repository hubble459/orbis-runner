package nl.saxion.playground.orbisrunner.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import nl.saxion.playground.orbisrunner.R;

/**
 * Activity to show some more information about this game and its creators
 *
 * @author Olaf Sonnemans
 */
public class AboutScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_screen);
    }
}