package nl.saxion.playground.orbisrunner.game.entity;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;

import nl.saxion.playground.orbisrunner.R;
import nl.saxion.playground.orbisrunner.game.OrbisRunnerModel;
import nl.saxion.playground.orbisrunner.lib.Entity;
import nl.saxion.playground.orbisrunner.lib.GameModel;
import nl.saxion.playground.orbisrunner.lib.GameView;
import nl.saxion.playground.orbisrunner.ui.demo.entities.DemoEnemy;

import static nl.saxion.playground.orbisrunner.ui.ControlTestingActivity.Direction.LEFT;
import static nl.saxion.playground.orbisrunner.ui.ControlTestingActivity.Direction.RIGHT;
import static nl.saxion.playground.orbisrunner.ui.ControlTestingActivity.Direction.UP;

public class Player extends Entity {
    private static final String TAG = "Player";

    private static final int LOOKING_RIGHT = 1;
    private static final int LOOKING_LEFT = -1;

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
    private int color;
    private int direction = -1;
    private int lastDirection = -1;
    private int looking;
    private boolean jumpingUp;
    private boolean jumpingDown;
    private boolean dead;
    private OrbisRunnerModel game;
    private AnimationDrawable animationDrawable;
    private Drawable drawable;
    private int frame;
    private int maxFrames;
    private long lastTime;

    public void setGame(OrbisRunnerModel game) {
        this.game = game;
    }

    @Override
    public void draw(GameView gv) {
        if (animationDrawable == null) {
            setXY(degrees);
            animationDrawable = (AnimationDrawable) gv.getContext().getDrawable(R.drawable.walking);
            if (animationDrawable == null) return;
            maxFrames = animationDrawable.getNumberOfFrames();

            width = animationDrawable.getIntrinsicWidth() * 4;
            height = animationDrawable.getIntrinsicHeight() * 4;
        }
        if (frame >= maxFrames) frame = 0;
        int left = (int) (xVal - width / 2);
        int top = (int) (yVal - height / 2);
        if (animationDrawable.getDuration(frame) < System.currentTimeMillis() - lastTime) {
            drawable = animationDrawable.getFrame(frame++);
            lastTime = System.currentTimeMillis();
        }
        if (drawable != null) {
            gv.getCanvas().rotate(angle, left + width / 2, top + height / 2);
            float cW = gv.getCanvas().getWidth();
            float cH = gv.getCanvas().getHeight();
            gv.getCanvas().scale(1, looking, left + width / 2, top + height / 2);
            drawable.setBounds(left, top, left + (int) width, top + (int) height + 40);
            drawable.draw(gv.getCanvas());
        }
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
            }
        }

        if (speed < MAX_SPEED) {
            speed += ACC;
        }
        if (direction == LEFT) {
            looking = LOOKING_LEFT;
            degrees += speed;
            setXY(degrees);
        } else if (direction == RIGHT) {
            looking = LOOKING_RIGHT;
            degrees -= speed;
            setXY(degrees);
        } else if (direction == UP) {
            direction = lastDirection;
            jumpingUp = true;
            jump += JUMP_ACC;
        }
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setDegrees(float degrees) {
        this.degrees = degrees;
    }

    private void setXY(float degrees) {
        float[] xy = game.getXYFromDegrees(degrees, jump);
        this.xVal = xy[0];
        this.yVal = xy[1];
        this.angle = xy[2] - 90;
    }

    @Override
    public int getLayer() {
        return 5;
    }
}
