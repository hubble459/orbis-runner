package nl.saxion.playground.orbisrunner.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import nl.saxion.playground.orbisrunner.R;
import nl.saxion.playground.orbisrunner.lib.Entity;
import nl.saxion.playground.orbisrunner.lib.GameModel;
import nl.saxion.playground.orbisrunner.lib.GameView;

/**
 * Testing activity for player controls
 * <p>
 * You can move a white circle on screen by using swipe actions
 * Tapping does nothing but can be seen in the action log nonetheless
 */
public class ControlTestingActivity extends AppCompatActivity {
    private TextView tv, tv2;
    private GameView gameView;
    private GameModel game;

    private boolean moved;
    private float oldX;
    private float oldY;
    private long start;

    /**
     * Make buttons do button stuff and have a TextView show feedback of actions
     * Call the setupGame() method
     *
     * @param savedInstanceState unused
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_testing);

        tv = findViewById(R.id.textView);
        tv2 = findViewById(R.id.speedCounters);
        gameView = findViewById(R.id.gameView);

        setupGame();

        findViewById(R.id.gameView).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.performClick();
                return onTouchEvent(motionEvent);
            }
        });

        tv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String[] lineSplit = editable.toString().split("\n");
                int lines = lineSplit.length;
                if (lines > 5) {
                    StringBuilder newText = new StringBuilder();
                    for (int i = 1; i < lines; i++) {
                        newText.append(lineSplit[i]).append("\n");
                    }
                    tv.setText(newText.toString());
                }
            }
        });

        findViewById(R.id.secretButton).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (game.getEntity(CircleEntity.class).toggleAgario(ControlTestingActivity.this))
                    Toast.makeText(ControlTestingActivity.this, "Agar.io!", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    /**
     * Make a new game from a GameModel
     * Add a CircleEntity (which you control) and a Candy to the game
     * Link the game to the GameView
     */
    private void setupGame() {
        game = new GameModel();
        game.addEntity(new CircleEntity(game, tv2));
        game.addEntity(new Candy(game));
        gameView.setGame(game);
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
        gameView.onTouch(gameView, event);

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

                int direction;

                // Moved on X or Y
                if (Math.abs(deltaY) > Math.abs(deltaX)) {
                    //Motion in Y direction.
                    if (oldY < newY) {
                        // Moved down
                        tv.append("DOWN\n");
                        direction = Direction.DOWN;
                    } else {
                        // Moved up
                        tv.append("UP\n");
                        direction = Direction.UP;
                    }
                } else {
                    // Motion in X direction.
                    if (oldX < newX) {
                        // Moved right
                        tv.append("RIGHT\n");
                        direction = Direction.RIGHT;
                    } else {
                        // Moved left
                        tv.append("LEFT\n");
                        direction = Direction.LEFT;
                    }
                }

                game.getEntity(CircleEntity.class).onSwipe(direction);

                // Reset moved to false
                moved = false;
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * A class for direction constants
     */
    private static class Direction {
        static final int UP = 0;
        static final int RIGHT = 1;
        static final int DOWN = 2;
        static final int LEFT = 3;
    }

    /**
     * CircleEntity class
     */
    private static class CircleEntity extends Entity {
        private final float ACCELERATION = .8f;
        private final float SLOWDOWN = .9999f;
        private final float BOUNCE = .8f;
        private final float TURN_FRICTION = .9f;

        private float width, height;
        private float normalSize;
        private float xVal, yVal;
        private float speed;

        private int points;
        private int direction;
        private int lastDirection;
        private boolean swiped;
        private boolean agario;

        private Bitmap bitmap;
        private GameModel game;
        private TextView tv2;
        private ArrayList<Candy> candies;

        /**
         * Constructor
         *
         * @param game the game (GameModel)
         * @param tv2  TextView to show information (like speed, etc)
         */
        CircleEntity(GameModel game, TextView tv2) {
            super();
            this.game = game;
            this.tv2 = tv2;
            direction = -1;
        }

        /**
         * The first time it gets drawn, the bitmap gets saved to a variable
         * And the candy entities get saved to the candy list
         *
         * @param gv The `GameView` to draw to.
         */
        @Override
        public void draw(GameView gv) {
            if (bitmap == null) {
                bitmap = gv.getBitmap(R.drawable.white_circle);

                width = bitmap.getWidth();
                height = bitmap.getHeight();
                normalSize = width;

                xVal = (float) (int) (Math.random() * (game.getWidth() - width));
                yVal = (float) (int) (Math.random() * (game.getHeight() - width));

                candies = game.getEntities(Candy.class);
            }
            gv.drawBitmap(bitmap, xVal, yVal, width, height, 0);
        }

        /**
         * Make the circle go over the candies
         *
         * @return layer
         */
        @Override
        public int getLayer() {
            return 2;
        }

        /**
         * Every tick the circle will get slowed down (like friction)
         * If you swiped, the speed will increase
         * And the circle will go in the direction you swiped
         * When a wall is hit, the circle will bounce back and lose some speed
         * <p>
         * Information will get printed to the tv2
         * <p>
         * On every tick it checks if you're on a candy, and if you are the candy will be eaten.
         * And it will change from position
         * For every candy one point is granted
         */
        @Override
        public void tick() {
            speed *= SLOWDOWN;

            boolean up, right, down, left;
            up = (direction == Direction.UP);
            down = (direction == Direction.DOWN);
            right = (direction == Direction.RIGHT);
            left = (direction == Direction.LEFT);

            if (swiped) {
                speed += ACCELERATION;

                if (lastDirection != direction) {
                    speed *= TURN_FRICTION;
                }
            }

            if (up) {
                yVal -= speed;
            } else if (down) {
                yVal += speed;
            }

            if (right) {
                xVal += speed;
            } else if (left) {
                xVal -= speed;
            }

            swiped = false;

            // Bounce
            if (xVal < 0) {
                speed *= BOUNCE;
                direction = Direction.RIGHT;
            } else if (xVal >= game.getWidth() - width) {
                speed *= BOUNCE;
                direction = Direction.LEFT;
            }
            if (yVal < 0) {
                speed *= BOUNCE;
                direction = Direction.DOWN;
            } else if (yVal >= game.getHeight() - width) {
                speed *= BOUNCE;
                direction = Direction.UP;
            }

            String info = String.format(Locale.ENGLISH,
                    (agario ? "Size" : "Points") + " %.0f\n" +
                            "Speed %.2f\n" +
                            "X-Val %.0f\n" +
                            "Y-Val %.0f", (agario ? width : points), speed, xVal, yVal);
            tv2.setText(info);

            if (candies != null) {
                for (Candy candy : candies) {
                    float x = candy.getXVal();
                    float y = candy.getYVal();
                    if (x + 10f >= xVal && x + 10f <= xVal + width) {
                        if (y + 10f >= yVal && y <= yVal + height) {
                            candy.changePos();
                            if (agario) {
                                if (width < 1000) {
                                    double scale = (.5 / width) + 1;
                                    width *= scale;
                                    height *= scale;
                                }
                            } else {
                                points++;
                            }
                        }
                    }
                }
            }
        }

        /**
         * Gets called from the onTouchEvent()
         *
         * @param direction the swiping direction
         */
        @Override
        public void onSwipe(int direction) {
            lastDirection = this.direction;
            this.direction = direction;
            swiped = true;
        }

        /**
         * Super secret toggle
         *
         * @return is toggled
         */
        boolean toggleAgario(Context context) {
            agario = !agario;

            if (agario) {
                for (int i = 0; i < 20; i++) {
                    Candy c = new Candy(game, getRandomColour(context));
                    game.addEntity(c);
                }
                candies = game.getEntities(Candy.class);
            } else {
                game.removeEntities(new ArrayList<Entity>(candies));
                game.addEntity(new Candy(game));
                width = normalSize;
                height = normalSize;
            }
            return agario;
        }

        private int getRandomColour(Context context) {
            int rand = (int) (Math.random() * 10);
            switch (rand) {
                case 0:
                    return Color.BLUE;
                case 1:
                    return Color.GREEN;
                case 2:
                    return Color.RED;
                case 3:
                    return Color.CYAN;
                case 4:
                    return Color.MAGENTA;
                case 5:
                    return Color.YELLOW;
                case 6:
                    return Color.LTGRAY;
                case 7:
                    return context.getResources().getColor(R.color.colorAccent);
                case 8:
                    return context.getResources().getColor(android.R.color.holo_orange_light);
                default:
                    return context.getResources().getColor(android.R.color.holo_purple);
            }
        }
    }

    /**
     * Candy Entity class
     */
    private static class Candy extends Entity {
        private float width, height;
        private float xVal, yVal;

        private int colour;

        private Bitmap bitmap;
        private GameModel game;

        Candy(GameModel game) {
            this(game, -1);
        }

        Candy(GameModel game, int colour) {
            super();
            this.game = game;
            this.colour = colour;
        }

        /**
         * onDraw
         *
         * @param gv The `GameView` to draw to.
         */
        @Override
        public void draw(GameView gv) {
            if (bitmap == null) {
                changePos();
                bitmap = gv.getBitmap(R.drawable.candy_shape, colour);

                width = bitmap.getWidth();
                height = bitmap.getHeight();
            }
            gv.drawBitmap(bitmap, xVal, yVal, width, height, 0);
        }

        /**
         * Get the x value of the candy
         *
         * @return x value
         */
        float getXVal() {
            return xVal;
        }

        /**
         * Get the y value of the candy
         *
         * @return y value
         */
        float getYVal() {
            return yVal;
        }

        /**
         * Change from position
         */
        void changePos() {
            yVal = (float) (int) (Math.random() * (game.getHeight() - height * 2));
            xVal = (float) (int) (Math.random() * (game.getWidth() - width * 2));
        }
    }
}
