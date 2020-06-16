package nl.saxion.playground.orbisrunner.ui;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import java.util.Random;

import nl.saxion.playground.orbisrunner.R;
import nl.saxion.playground.orbisrunner.lib.Animation;

/**
 * Splash screen shown at startup for extra cool points
 */
public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        init();
    }

    /**
     * Start player walking animation and rotation, so it looks like the player is walking in the circle
     */
    public void init() {
        // Animate Player W A L K I N G
        ImageView player = findViewById(R.id.player);
        ((AnimationDrawable) player.getBackground()).start();
        Animation.walkInCircleSplash(findViewById(R.id.rotateView), 5000, new Random().nextBoolean());
    }
}
