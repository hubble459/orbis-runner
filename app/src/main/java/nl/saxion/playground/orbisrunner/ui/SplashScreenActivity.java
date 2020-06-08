package nl.saxion.playground.orbisrunner.ui;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import java.util.Random;

import nl.saxion.playground.orbisrunner.R;
import nl.saxion.playground.orbisrunner.lib.Animation;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        init();
    }


    public void init() {
        // Animate Player W A L K I N G
        ImageView player = findViewById(R.id.player);
        ((AnimationDrawable) player.getBackground()).start();
        Animation.walkInCircle(findViewById(R.id.rotateView), 7000, new Random().nextBoolean());


    }
}
