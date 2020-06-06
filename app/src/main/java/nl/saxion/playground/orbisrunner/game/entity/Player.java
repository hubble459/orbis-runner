package nl.saxion.playground.orbisrunner.game.entity;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import nl.saxion.playground.orbisrunner.R;
import nl.saxion.playground.orbisrunner.lib.Entity;
import nl.saxion.playground.orbisrunner.lib.GameModel;
import nl.saxion.playground.orbisrunner.lib.GameView;
import nl.saxion.playground.orbisrunner.ui.demo.entities.DemoEnemy;

public class Player extends Entity {
    private static final String TAG = "Player";

    private static final float JUMP_ACC = 3f;
    private static final float JUMP_MAX_HEIGHT = 400f;

    private float maxJump;
    private float xVal, yVal;
    private float width, height;
    private float angle;
    private float jump;

    private long lastTime;

    private int color;
    private int frame;
    private int maxFrames;
    private int scale;
    private int marginBottom;

    private boolean falling;
    private boolean jumping;
    private boolean dead;

    private GameModel game;
    private AnimationDrawable animationDrawable;
    private Drawable drawable;

    public void setGame(GameModel game) {
        this.game = game;
        this.scale = 4;
        this.maxJump = JUMP_MAX_HEIGHT;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    @Override
    public void draw(GameView gv) {
        if (animationDrawable == null) {

            setXY();

            animationDrawable = (AnimationDrawable) gv.getContext().getDrawable(R.drawable.walking);
            if (animationDrawable == null) return;
            maxFrames = animationDrawable.getNumberOfFrames();

            width = animationDrawable.getIntrinsicWidth() * scale;
            height = animationDrawable.getIntrinsicHeight() * scale + 40;

            drawable = animationDrawable.getFrame(0);
        }

        if (falling
                || frame >= maxFrames) {
            frame = 0;
        }

        if (animationDrawable.getDuration(frame) < System.currentTimeMillis() - lastTime) {
            drawable = animationDrawable.getFrame(frame++);
            lastTime = System.currentTimeMillis();
        }

        if (drawable != null) {
            int right = (int) (xVal - width / 2);
            int bottom = (int) (yVal - height / 2) - marginBottom;
            gv.getCanvas().rotate(angle, right + width / 2, bottom + height / 2);
            gv.getCanvas().scale(-1, -1, right + width / 2, bottom + height / 2);
            drawable.setBounds(right + (int) width, bottom + (int) height, right, bottom);
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

        for (GameModel.Touch touch : game.touches) {
            if (Math.abs(touch.deltaY) == Math.abs(touch.deltaX)) {
                if (falling) {
                    break;
                } else {
                    Log.i(TAG, "tick: dur = " + touch.getDuration());
                    maxJump = Math.min(JUMP_MAX_HEIGHT / 2 * Math.max(touch.getDuration() / 100, 1), JUMP_MAX_HEIGHT);
                    jumping = true;
                }
            }
        }

        if (jumping) {
            jump();
        }
    }

    private void jump() {
        if (falling) {
            jump -= JUMP_ACC;
        } else {
            jump += JUMP_ACC;
            if (jump > maxJump) {
                falling = true;
            }
        }

        if (jump < 0) {
            falling = false;
            jumping = false;
            jump = 0;
        }

        setXY();
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    private void setXY() {
        float[] xy = game.getXYFromDegrees((float) 110, jump);
        this.xVal = xy[0];
        this.yVal = xy[1];
        this.angle = xy[2] - 90;
    }

    @Override
    public int getLayer() {
        return 5;
    }

    public void setMarginBottom(int marginBottom) {
        this.marginBottom = marginBottom;
    }
}
