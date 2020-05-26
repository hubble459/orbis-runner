package nl.saxion.playground.orbisrunner.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
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
    private TextView touchFeedback, gameInfo;
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

        touchFeedback = findViewById(R.id.textView);
        gameInfo = findViewById(R.id.speedCounters);
        gameView = findViewById(R.id.gameView);

        setupGame();

        findViewById(R.id.gameView).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.performClick();
                return onTouchEvent(motionEvent);
            }
        });

        touchFeedback.addTextChangedListener(new TextWatcher() {
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
                    touchFeedback.setText(newText.toString());
                }
            }
        });

        findViewById(R.id.secretButton).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (game.getEntity(CircleEntity.class).toggleAgario())
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
        game.addEntity(new CircleEntity(this, game, gameInfo));
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
                    touchFeedback.append("TAP [" + time + " ms]\n");
                    CircleEntity cE = game.getEntity(CircleEntity.class);
                    cE.onSwipe(Direction.TAP);
                    cE.tapDuration(time);
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
                        touchFeedback.append("DOWN\n");
                        direction = Direction.DOWN;
                    } else {
                        // Moved up
                        touchFeedback.append("UP\n");
                        direction = Direction.UP;
                    }
                } else {
                    // Motion in X direction.
                    if (oldX < newX) {
                        // Moved right
                        touchFeedback.append("RIGHT\n");
                        direction = Direction.RIGHT;
                    } else {
                        // Moved left
                        touchFeedback.append("LEFT\n");
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
    public static class Direction {
        public static final int UP = 0;
        public static final int RIGHT = 1;
        public static final int DOWN = 2;
        public static final int LEFT = 3;
        public static final int TAP = 4;
    }

    /**
     * CircleEntity class
     */
    private static class CircleEntity extends Entity {
        private final float ACCELERATION = .8f;
        private final float SLOWDOWN = .9999f;
        private final float BOUNCE = .8f;
        private final float TURN_FRICTION = .9f;

        private float diameter, radius, size;
        private float xVal, yVal;
        private float speed;
        private float scale = 1;

        private int points;
        private int direction;
        private int lastDirection;
        private boolean swiped;
        private boolean agario;
        private long time;

        private Paint paint;
        private GameModel game;
        private TextView gameInfo;
        private ArrayList<Candy> candies;
        private Context context;

        /**
         * Constructor
         *
         * @param context  context
         * @param game     the game (GameModel)
         * @param gameInfo TextView to show information (like speed, etc)
         */
        CircleEntity(Context context, GameModel game, TextView gameInfo) {
            super();
            this.context = context;
            this.game = game;
            this.gameInfo = gameInfo;
            this.radius = 40f;
            this.diameter = radius * 2;
            this.size = diameter;
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
            if (paint == null) {
                paint = new Paint();
                paint.setColor(Color.WHITE);

                xVal = (float) (int) (Math.random() * (game.getWidth() - radius));
                yVal = (float) (int) (Math.random() * (game.getHeight() - radius));

                candies = game.getEntities(Candy.class);
            }
            gv.getCanvas().drawCircle(xVal, yVal, radius, paint);
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
         * The circle will go in the direction you swiped
         * If you swipe the same direction you're going, the speed will increase
         * If swiped to turn, the speed will still increase, but you will also get slowed down a little
         * <p>
         * If the screen is tapped, speed will increase
         * If you hold the screen for longer than 100 milliseconds you will get a super dash
         * <p>
         * When a wall is hit, the circle will bounce back and lose some speed
         * <p>
         * Information will get printed to the gameInfo TextView
         * <p>
         * On every tick it checks if you're on a candy, and if you are, the candy will be eaten
         * and it will change from position
         * For every candy one point is granted
         */
        @Override
        public void tick() {
            speed *= SLOWDOWN;

            boolean up, right, down, left;
            if (direction == Direction.TAP) {
                up = (lastDirection == Direction.UP);
                down = (lastDirection == Direction.DOWN);
                right = (lastDirection == Direction.RIGHT);
                left = (lastDirection == Direction.LEFT);
            } else {
                up = (direction == Direction.UP);
                down = (direction == Direction.DOWN);
                right = (direction == Direction.RIGHT);
                left = (direction == Direction.LEFT);
            }

            if (swiped) {
                if (direction == Direction.TAP && time > 100) {
                    speed += ACCELERATION * (time / 100.);
                } else {
                    speed += ACCELERATION;
                }

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
            if (xVal - radius < 0) {
                speed *= BOUNCE;
                direction = Direction.RIGHT;
            } else if (xVal + radius >= game.getWidth()) {
                speed *= BOUNCE;
                direction = Direction.LEFT;
            }
            if (yVal - radius < 0) {
                speed *= BOUNCE;
                direction = Direction.DOWN;
            } else if (yVal + radius >= game.getHeight()) {
                speed *= BOUNCE;
                direction = Direction.UP;
            }

            String info = String.format(Locale.ENGLISH,
                    (agario ? "Size" : "Points") + " %.0f\n" +
                            "Speed %.2f\n" +
                            "X-Val %.0f\n" +
                            "Y-Val %.0f", (agario ? size : points), speed, xVal, yVal);
            gameInfo.setText(info);

            if (candies != null) {
                for (Candy candy : candies) {
                    float x = candy.getXVal();
                    float y = candy.getYVal();
                    float s = candy.getSize();
                    if (x + s >= xVal - radius && x <= xVal + radius) {
                        if (y + s >= yVal - radius && y <= yVal + radius) {
                            candy.changePos();
                            candy.setColour(getRandomColour());
                            if (agario) {
                                if (diameter >= game.getWidth() * 0.75) {
                                    scale *= 0.75;
                                    diameter *= scale;
                                    scaleCandies();
                                }

                                float scale = (float) ((.5 / radius) + 1);
                                diameter *= scale;
                                size *= scale;
                                radius = diameter / 2;
                            } else {
                                points++;
                            }
                        }
                    }
                }
            }
        }

        private void scaleCandies() {
            for (Candy candy : candies) {
                candy.setScale(scale);
            }
        }

        /**
         * Gets called from the onTouchEvent()
         *
         * @param direction the swiping direction (or tap)
         */
        @Override
        public void onSwipe(int direction) {
            if (this.direction != Direction.TAP) {
                lastDirection = this.direction;
            }
            this.direction = direction;
            swiped = true;
        }

        void tapDuration(long time) {
            this.time = time;
        }

        /**
         * Super secret toggle
         *
         * @return is toggled
         */
        boolean toggleAgario() {
            agario = !agario;

            if (agario) {
                for (int i = 0; i < 20; i++) {
                    Candy c = new Candy(game, getRandomColour());
                    game.addEntity(c);
                }
                candies = game.getEntities(Candy.class);
            } else {
                game.removeEntities(new ArrayList<Entity>(candies));
                game.addEntity(new Candy(game));
                radius = 40f;
            }
            return agario;
        }

        private int getRandomColour() {
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
        private float scale = 1;
        private int colour;

        private Paint paint;
        private GameModel game;

        Candy(GameModel game) {
            this(game, Color.MAGENTA);
        }

        Candy(GameModel game, int colour) {
            super();
            this.game = game;
            this.colour = colour;
            this.paint = new Paint();
            this.width = 40f;
            this.height = 40f;
        }

        /**
         * onDraw
         *
         * @param gv The `GameView` to draw to.
         */
        @Override
        public void draw(GameView gv) {
            if (paint.getColor() != colour) {
                paint.setColor(colour);
                changePos();
            }
            gv.getCanvas().drawRect(xVal, yVal, xVal + width * scale, yVal + height * scale, paint);
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
            xVal = (float) (int) (Math.random() * (game.getWidth() - width));
            yVal = (float) (int) (Math.random() * (game.getHeight() - height));
        }

        void setScale(float scale) {
            this.scale = scale;
        }

        void setColour(int colour) {
            this.colour = colour;
        }

        float getSize() {
            return this.width;
        }
    }
}