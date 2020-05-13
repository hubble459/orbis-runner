package nl.saxion.playground.orbisrunner.ui;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import nl.saxion.playground.orbisrunner.R;
import nl.saxion.playground.orbisrunner.lib.Animation;

public class StartScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        ImageView player = findViewById(R.id.player);
        ((AnimationDrawable) player.getDrawable()).start();
        Animation.walkInCircle(player, 4000);
//        Animation.stop(player);
    }
}
