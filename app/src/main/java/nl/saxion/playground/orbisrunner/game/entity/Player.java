package nl.saxion.playground.orbisrunner.game.entity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;

import nl.saxion.playground.orbisrunner.R;
import nl.saxion.playground.orbisrunner.lib.Entity;
import nl.saxion.playground.orbisrunner.lib.GameModel;
import nl.saxion.playground.orbisrunner.lib.GameView;

public class Player extends Entity {
    private static final float JUMP_ACC = 3f;
    private static final float JUMP_MAX_HEIGHT = 300f;

    private float maxJump;
    private float fallingSpeed;
    private float margin;

    private long lastTime;

    private int color;
    private int frame;
    private int maxFrames;

    private boolean falling;
    private boolean jumping;
    private boolean dead;

    private GameModel game;
    private AnimationDrawable animationDrawable;
    private Drawable drawable;
    private Queue<float[]> dust;
    private Paint paint;
    private Random random;

    public Player() {
        setStartAngle(110);
        setStartJump(0);
    }

    public void setGame(GameModel game) {
        this.game = game;
        this.scale = 1;
        this.maxJump = JUMP_MAX_HEIGHT;
        this.dust = new ArrayDeque<>();
        this.paint = new Paint();
        this.paint.setColor(Color.GRAY);
        this.paint.setStrokeWidth(20f);
        this.paint.setAlpha(69);
        this.random = new Random();
    }

    @Override
    public void draw(GameView gv) {
        if (animationDrawable == null) {
            animationDrawable = (AnimationDrawable) gv.getContext().getDrawable(R.drawable.walking);
            if (animationDrawable == null) return;
            maxFrames = animationDrawable.getNumberOfFrames();

            width = animationDrawable.getIntrinsicWidth() * scale;
            height = animationDrawable.getIntrinsicHeight() * scale;

            drawable = animationDrawable.getFrame(0);
            if (color != Color.BLACK) {
                drawable.setTint(color);
            } else {
                drawable.setTintList(null);
            }
            setXY();
        }

        if (falling || frame >= maxFrames) {
            frame = 0;
        }

        if (animationDrawable.getDuration(frame) < System.currentTimeMillis() - lastTime) {
            drawable = animationDrawable.getFrame(frame++);
            if (color != Color.BLACK) {
                drawable.setTint(color);
            } else {
                drawable.setTintList(null);
            }
            lastTime = System.currentTimeMillis();
        }

        if (drawable != null) {
            gv.getCanvas().rotate(angle, xVal, yVal);
            drawable.setBounds(
                    (int) xVal,
                    (int) yVal,
                    (int) (xVal + width),
                    (int) (yVal + height));
            drawable.draw(gv.getCanvas());

            drawRunDust(gv.getCanvas());
        }
    }

    private void randomDust() {
        while (dust.size() < 10) {
            float[] particle = new float[4];
            particle[0] = xVal + width * .69f - random.nextInt((int) width);
            particle[1] = yVal + height * .75f + random.nextInt(40);
            particle[2] = random.nextInt(30);
            particle[3] = random.nextInt(100) + 69;
            dust.offer(particle);
        }
    }

    private void drawRunDust(Canvas canvas) {
        if (dead || jumping) return;
        randomDust();

        for (float[] point : dust) {
            paint.setStrokeWidth(point[2]);
            paint.setAlpha((int) point[3]);
            canvas.drawPoint(point[0], point[1], paint);
        }
        dust.poll();
    }

    @Override
    public void tick() {
        if (dead) return;

        for (Entity entity : game.getEntities()) {
            if (!(entity instanceof Player)
                    && !(entity instanceof Circle)
                    && entity.onScreen(game.getWidth(), game.getHeight())
                    && entity.inHitbox(this)) {
                dead = true;
                game.dead();
                return;
            }
        }

        for (GameModel.Touch touch : game.touches) {
            // Right side condition
            // if (Math.abs(touch.x) > game.getWidth() / 2) {
            if (falling) {
                float slowdown = .99f * Math.abs(Math.min(touch.getDuration() / 200, 1));
                fallingSpeed = JUMP_ACC / 2 * slowdown;
            } else {
                maxJump = Math.min(JUMP_MAX_HEIGHT / 2 * Math.max(touch.getDuration() / 100, 1), JUMP_MAX_HEIGHT);
                jumping = true;
            }
            // } else {
            //      left side
            // }
        }

        if (jumping) {
            jump();
        }
    }

    private void jump() {
        if (falling) {
            jump -= fallingSpeed == 0 ? JUMP_ACC * .9f : fallingSpeed;
            fallingSpeed = JUMP_ACC * .99f;
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
        if (game == null) {
            Log.i("uwu", "setXY: ewe");
            return;
        }
        float[] xy = game.getXYFromDegrees(startAngle, jump + margin, this);
        setXYValues(xy);
    }

    @Override
    public void setXYValues(float[] xy) {
        super.setXYValues(xy);
        angle -= 90;
    }

    @Override
    public int getLayer() {
        return 5;
    }

    public void setEnabled(boolean enabled) {
        this.dead = !enabled;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public void setMargin(float margin) {
        this.margin = margin;
    }

    @Override
    public void reset() {
        super.reset();
        jumping = false;
        falling = false;
        setDead(false);
        setXY();
    }
}
