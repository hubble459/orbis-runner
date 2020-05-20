package nl.saxion.playground.orbisrunner.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import nl.saxion.playground.orbisrunner.R;

/**
 * Testing activity for player controls
 */
public class ControlTestingActivity extends AppCompatActivity {
    private TextView tv;

    private boolean moved;
    private float oldX;
    private float oldY;
    private long start;


    /**
     * Bind the TextView
     * Make the clear button clear the TextView
     *
     * @param savedInstanceState unused
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_testing);

        tv = findViewById(R.id.textView);

        findViewById(R.id.clearButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv.setText("");
            }
        });
    }

    /**
     * When the screen is touched the point where you touched will be saved in oldX and oldY
     * When you move and release a new X and Y will be compared to the old cords
     * The movement direction can be 'calculated' from this information
     * <p>
     * Prints the direction on the TextView
     *
     * @param event The touch event
     * @return true if the event is consumed
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();

        switch (action) {
            // Screen pressed
            case MotionEvent.ACTION_DOWN:
                // Save cords of press
                oldX = event.getX();
                oldY = event.getY();

                // Save current time
                start = System.currentTimeMillis();
                break;
            // Finger moved
            case MotionEvent.ACTION_MOVE:
                moved = true;
                break;
            // Screen unpressed
            case MotionEvent.ACTION_UP:
                // Check if it's a tap
                if (!moved) {
                    long time = System.currentTimeMillis() - start;
                    tv.append("TAP [" + time + " ms]\n");
                    break;
                }

                // Get the x and y of the release point
                float newX = event.getX();
                float newY = event.getY();

                // Get deltas
                int deltaX = (int) (oldX - newX);
                int deltaY = (int) (oldY - newY);

                // Moved on X or Y
                if (Math.abs(deltaY) > Math.abs(deltaX)) {
                    //Motion in Y direction.
                    if (oldY < newY) {
                        // Moved down
                        tv.append("DOWN\n");
                    } else {
                        // Moved up
                        tv.append("UP\n");
                    }
                } else {
                    // Motion in X direction.
                    if (oldX < newX) {
                        // Moved right
                        tv.append("RIGHT\n");
                    } else {
                        // Moved left
                        tv.append("LEFT\n");
                    }
                }

                // Reset moved to false
                moved = false;
                break;
        }
        return super.onTouchEvent(event);
    }
}
