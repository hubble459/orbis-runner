package nl.saxion.playground.orbisrunner.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import nl.saxion.playground.orbisrunner.R;

/**
 * Screen with info about the game
 *
 * @author Olaf Sonnemans
 */
public class AboutScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_screen);
    }

    public void back(View view) {
        finish();
    }

    public void sendMail(View view) {
        Intent email = new Intent(Intent.ACTION_SENDTO);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{"orbisrunner@gmail.com"});
        email.putExtra(Intent.EXTRA_SUBJECT, "Orbis Runner");
        startActivity(Intent.createChooser(email, "Choose an Email client :"));
    }
}