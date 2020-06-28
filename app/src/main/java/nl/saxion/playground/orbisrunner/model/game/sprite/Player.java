package nl.saxion.playground.orbisrunner.model.game.sprite;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;

import nl.saxion.playground.orbisrunner.R;
import nl.saxion.playground.orbisrunner.lib.Entity;
import nl.saxion.playground.orbisrunner.lib.GameModel;
import nl.saxion.playground.orbisrunner.lib.GameView;
import nl.saxion.playground.orbisrunner.model.game.OrbisRunnerModel;
import nl.saxion.playground.orbisrunner.singleton.GameProvider;

/**
 * MC of Orbis Runner
 * Has a tragic life story but continues to tread on
 *
 * @author Whole group
 */
public class Player extends Sprite {
    private static final float JUMP_ACC = 3f;
    private static final float JUMP_MAX_HEIGHT = 300f;
    private static final float POS = 110f;

    private static final long COOL_DOWN = 2000;

    private float maxJump;
    private float fallingSpeed;
    private float duckInvert;

    private long duckTime;
    private long lastTime;

    private int color;
    private int maxFrames;
    private int frame;

    private boolean falling;
    private boolean jumping;
    private boolean ducking;
    private boolean dead;

    private AnimationDrawable animationDrawable;
    private Drawable drawable;
    private Bitmap hat;
    private Paint paint;
    private Random random;
    private Queue<float[]> dust;

    /**
     * Start angle gets set to 110
     * because when the scale is default (1) 110° is in the perfect place
     */
    public Player() {
        setStartAngle(POS);
        setStartJump(0f);
        color = Color.BLACK;
    }

    /**
     * Reset the player and set game values
     *
     * @param game GameModel
     */
    public void setGame(GameModel game) {
        this.game = game;
        this.maxJump = JUMP_MAX_HEIGHT;
        this.dust = new ArrayDeque<>();
        this.paint = new Paint();
        this.paint.setColor(Color.GRAY);
        this.paint.setStrokeWidth(20f);
        this.paint.setAlpha(69);
        this.random = new Random();
    }

    /**
     * Draw the walking frames and dust clouds at the players feet
     *
     * @param gv The `GameView` to draw to.
     */
    @Override
    public void draw(GameView gv) {
        if (hat == null) {
            int res = GameProvider.getShop().getSelectedRes();
            if (res != -1) {
                hat = gv.getBitmapFromResource(res);
            }
        }

        if (animationDrawable == null) {
            animationDrawable = (AnimationDrawable) gv.getContext().getDrawable(R.drawable.walking);
            if (animationDrawable == null) return;
            maxFrames = animationDrawable.getNumberOfFrames();

            width = animationDrawable.getIntrinsicWidth() * scale;
            height = animationDrawable.getIntrinsicHeight() * scale;

            drawable = animationDrawable.getFrame(0);

            drawable.setTint(color);
            setXY();
        }

        if ((falling && !ducking) || frame >= maxFrames) {
            frame = 0;
        }

        if (animationDrawable.getDuration(frame) < System.currentTimeMillis() - lastTime) {
            drawable = animationDrawable.getFrame(frame++);

            drawable.setTint(color);

            lastTime = System.currentTimeMillis();
        }

        if (drawable != null) {
            // Reset scale if needed
            if (width != drawable.getIntrinsicWidth() * scale) {
                width = drawable.getIntrinsicWidth() * scale;
                height = drawable.getIntrinsicHeight() * scale;
            }

            gv.getCanvas().rotate(angle, xVal, yVal);
            drawable.setBounds(
                    (int) xVal,
                    (int) (yVal - duckInvert),
                    (int) (xVal + width),
                    (int) (yVal + height));
            drawable.draw(gv.getCanvas());

            drawHat(gv);

            drawRunDust(gv);
        }
    }

    /**
     * Draw a hat on the players head
     *
     * @param gv GameView
     */
    private void drawHat(GameView gv) {
        if (game instanceof OrbisRunnerModel && hat != null) {
            gv.getCanvas().save();

            if (duckInvert != 0) {
                gv.getCanvas().scale(1, -1, xVal, yVal);
            }
            gv.drawBitmap(
                    hat,
                    xVal,
                    yVal - hat.getHeight() + 10 + duckInvert,
                    hat.getWidth(),
                    hat.getHeight());
            gv.getCanvas().restore();
        }
    }

    /**
     * If player gets spawned off-screen (110°) it will try to find a new spot
     */
    private void getBestPosition() {
        setStartAngle(--startAngle);
        setXY();
    }

    /**
     * Add random dust particles to the dust queue
     */
    private void randomDust() {
        if (dust.size() < 10 && !dead && !jumping) {
            float[] particle = new float[4];
            particle[0] = xVal + width * .69f - random.nextInt((int) width);
            particle[1] = yVal + height * .75f + random.nextInt(40);
            particle[2] = random.nextInt(30);
            particle[3] = random.nextInt(100) + 69;
            dust.offer(particle);
        }
    }

    /**
     * Draw the random dust particles
     *
     * @param gv GameView with canvas to draw on
     */
    private void drawRunDust(GameView gv) {
        Canvas canvas = gv.getCanvas();
        randomDust();

        for (float[] point : dust) {
            paint.setStrokeWidth(point[2]);
            paint.setAlpha((int) point[3]);
            canvas.drawPoint(point[0], point[1], paint);
        }

        if (dust.size() >= 10 || dead || jumping) {
            dust.poll();
        }
    }

    /**
     * Every tick it will check for collision
     * And it will respond to the users touches by either jumping or ducking
     */
    @Override
    public void tick() {
        if (dead) return;

        for (Entity entity : game.getEntities()) {
            if (!(entity instanceof Player)
                    && !(entity instanceof Circle)
                    && entity.onScreen()
                    && inHitBox(entity)) {
                dead = true;
                if (game instanceof OrbisRunnerModel) {
                    ((OrbisRunnerModel) game).dead();
                }
                return;
            }
        }

        for (GameModel.Touch touch : game.touches) {
            // Right side condition
            if (Math.abs(touch.x) > game.getWidth() / 2 && !ducking) {
                if (falling) {
                    float slowdown = .99f * Math.abs(Math.min(touch.getDuration() / 200, 1));
                    fallingSpeed = JUMP_ACC / 2 * slowdown;
                } else {
                    maxJump = Math.min(JUMP_MAX_HEIGHT / 2 * Math.max(touch.getDuration() / 100, 1), JUMP_MAX_HEIGHT);
                    jumping = true;
                }
            } else if (!ducking && !jumping) {
                if (duckTime == 0 || duckTime < System.currentTimeMillis() - COOL_DOWN) {
                    ((OrbisRunnerModel) (game)).coolDown(COOL_DOWN);
                    duckTime = System.currentTimeMillis();
                    ducking = true;
                }
            }
        }

        if (jumping) {
            jump();
        }
        if (ducking) {
            duck();
        }
    }

    @Override
    public int getBitmapRes() {
        return R.drawable.walking;
    }

    @Override
    public String getName() {
        return "Player";
    }

    /**
     * Either fall down or continue jumping
     */
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

    /**
     * Walk on the other side of the circle for one second
     */
    private void duck() {
        if (falling && duckTime < System.currentTimeMillis() - 1000) {
            duckInvert += 1;
            if (duckInvert > height * 2) {
                duckInvert = height;
            }
            jump += 3;
        } else if (!falling) {
            duckInvert -= height / 4;
            if (duckInvert < -height * 2) {
                duckInvert = -height * 2;
                falling = true;
            }
            jump -= 10;
            if (jump < -(Circle.STROKE_WIDTH + height)) {
                falling = true;
            }
        }
        if (jump > 0) {
            falling = false;
            duckInvert = 0;
            jumping = false;
            ducking = false;
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

    public void setXY() {
        if (game != null) {
            float[] xy = game.getXYFromDegrees(startAngle, jump, this);
            setXYValues(xy);
            angle -= 90;

            if (!onScreen() && !dead && !pause) {
                getBestPosition();
            }
        }
    }

    @Override
    public void setXYValues(float[] xy) {
        super.setXYValues(xy);
    }

    @Override
    public int getLayer() {
        return 5;
    }

    public void setEnabled(boolean enabled) {
        this.dead = !enabled;
    }

    @Override
    public boolean inHitBox(Entity e) {
        float x = e.getX();
        float y = e.getY();
        float w = e.getWidth();
        float h = e.getHeight();

        float start = x - w / 2;
        float end = x + w / 2;
        float top = y - h / 2 - duckInvert;
        float bot = y + h / 2;

        float start2 = xVal - width / 2;
        float end2 = xVal + width / 2;
        float top2 = yVal - height / 2;
        float bot2 = yVal + height / 2;

        boolean result = (start < end2 && end > start2 &&
                top < bot2 && bot > top2);

        if (result && game instanceof OrbisRunnerModel) {
            if (e instanceof Portal) {
                ((OrbisRunnerModel) game).finish();
                return false;
            }
            if (e instanceof Coin) {
                ((Coin) e).collectCoin();
                return false;
            }
        }


        return result;
    }

    /**
     * Reset player values
     */
    @Override
    public void reset() {
        jumping = false;
        ducking = false;
        falling = false;
        dead = false;
        hat = null;
        duckInvert = 0f;
        super.reset();
        setXY();
    }
}
