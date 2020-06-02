package nl.saxion.playground.orbisrunner.ui.demo.entities;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import java.util.Locale;

import nl.saxion.playground.orbisrunner.R;
import nl.saxion.playground.orbisrunner.lib.Entity;
import nl.saxion.playground.orbisrunner.lib.GameModel;
import nl.saxion.playground.orbisrunner.lib.GameView;
import nl.saxion.playground.orbisrunner.ui.demo.DemoOrbisGame;

import static nl.saxion.playground.orbisrunner.ui.ControlTestingActivity.Direction.LEFT;
import static nl.saxion.playground.orbisrunner.ui.ControlTestingActivity.Direction.RIGHT;
import static nl.saxion.playground.orbisrunner.ui.ControlTestingActivity.Direction.UP;

public class Player extends Entity {
    private static final String TAG = "Player";

    private static final float BRAKE = .002f;
    private static final float MAX_SPEED = .4f;
    private static final float JUMP_ACC = 3f;
    private static final float ACC = .02f;

    private float maxJump;
    private float speed;
    private float xVal, yVal;
    private float width, height;
    private float angle;
    private float jump;
    private float degrees;
    private int direction = -1;
    private int lastDirection = -1;
    private boolean jumpingUp;
    private boolean jumpingDown;
    private boolean dead;
    private Bitmap bitmap;
    private DemoOrbisGame game;

    public Player(DemoOrbisGame game, float degrees) {
        this.game = game;
        setXY(degrees);
    }

    @Override
    public void draw(GameView gv) {
        if (bitmap == null) {
            bitmap = gv.getBitmap(R.drawable.demo_entity, Color.GREEN);
            width = bitmap.getWidth();
            height = bitmap.getHeight();
            maxJump = bitmap.getHeight() * 3;
        }
        gv.drawBitmap(bitmap, xVal - width / 2, yVal - height / 2, width, height, angle);
    }

    @Override
    public void tick() {
        if (dead) return;

        for (DemoEnemy demoEnemy : game.getEntities(DemoEnemy.class)) {
            if (demoEnemy.inHitbox(xVal, yVal, width, height)) {
                dead = true;
                game.dead();
                return;
            }
        }

        boolean moved = false;
        game.speedometer.setText(String.format(Locale.ENGLISH, "Speed %.2f", speed));
        for (GameModel.Touch touch : game.touches) {
            lastDirection = direction;
            if (Math.abs(touch.deltaY) == Math.abs(touch.deltaX)) {
                if (jumpingDown) break;
                jump();
            } else {
                direction = touch.getSwipeDirection();
            }
            move();
            moved = true;
            game.speedometer.setText(String.format(Locale.ENGLISH, "Speed %.2f", speed));
        }

        if (!moved) {
            move();
            if (jump > 0) {
                jump -= JUMP_ACC;
            } else {
                jumpingDown = false;
            }
        }
    }

    private void jump() {
        if (!jumpingDown) {
            if (jump < maxJump) {
                jump += JUMP_ACC;
            } else {
                jumpingDown = true;
            }
        }
    }

    private void move() {
        if (!jumpingUp && !jumpingDown) {
            while (((direction == LEFT && lastDirection == RIGHT)
                    || (direction == RIGHT && lastDirection == LEFT))
                    && speed > 0.02f) {
                speed -= BRAKE;
                Log.i(TAG, "move: breaking");
            }
        }

        if (speed < MAX_SPEED) {
            speed += ACC;
        }
        if (direction == LEFT) {
            degrees += speed;
            setXY(degrees);
        } else if (direction == RIGHT) {
            degrees -= speed;
            setXY(degrees);
        } else if (direction == UP) {
            direction = lastDirection;
            jumpingUp = true;
            jump += JUMP_ACC;
        }
    }

    private void setXY(float degrees) {
        float[] xy = game.getXYFromDegrees(degrees, jump);
        this.xVal = xy[0];
        this.yVal = xy[1];
        this.angle = xy[2];
    }
}
