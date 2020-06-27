package nl.saxion.playground.orbisrunner.model.game.sprite;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.widget.TextView;

import nl.saxion.playground.orbisrunner.lib.Entity;
import nl.saxion.playground.orbisrunner.lib.GameModel;
import nl.saxion.playground.orbisrunner.lib.GameView;
import nl.saxion.playground.orbisrunner.model.game.OrbisRunnerModel;
import nl.saxion.playground.orbisrunner.singleton.GameProvider;

/**
 * Tutorial entity
 * <p>
 * Shows when you first play the game to explain that the left side is for ducking, and the right side is for jumping
 */
public class Tutorial extends Entity {
    private Paint paint;
    private TextView duck, jump;

    /**
     * Init Paint object
     *
     * @param game GameModel
     */
    public Tutorial(OrbisRunnerModel game) {
        this.game = game;
        this.paint = new Paint();
        this.paint.setColor(Color.argb(30, 69, 69, 69));
    }

    /**
     * Draw rounded squares on the left and right side
     * Left side says 'Press here to DUCK'
     * Right side says 'Press here to JUMP'
     *
     * @param gv The `GameView` to draw to.
     */
    @Override
    public void draw(GameView gv) {
        if (duck == null && jump == null) {
            duck = new TextView(gv.getContext());
            jump = new TextView(gv.getContext());
            duck.setText("Press here to DUCK");
            jump.setText("Press here to JUMP");
        }

        Canvas c = gv.getCanvas();

        float margin = 10f;
        float left = margin;
        float right = gv.getWidth() / 2f - margin;
        float bottom = gv.getHeight() - margin;
        float xMid = gv.getWidth() * .75f;
        float yMid = gv.getHeight() / 2f;

        // Left side [duck]
        c.drawRoundRect(left, margin, right, bottom, xMid, yMid, paint);
        duck.layout((int) left,
                (int) margin,
                (int) right,
                (int) bottom);
        float textX = (right - left) / (2 * 5);
        c.translate(textX, yMid);
        duck.draw(c);

        left = gv.getWidth() / 2f + margin;
        right = gv.getWidth() - margin;
        xMid = gv.getWidth() * .75f;
        c.translate(-textX, -yMid);


        // Right side [jump]
        c.drawRoundRect(left, margin, right, bottom, xMid, yMid, paint);
        jump.layout((int) left,
                (int) margin,
                (int) right,
                (int) bottom);
        c.translate(xMid * .7f, yMid);
        jump.draw(c);
    }

    /**
     * When screen is touched, remove this entity and set first play to false, to not show this a second time
     *
     * @param touch Information about the touch this event is about.
     * @param event ACTION_DOWN, ACTION_UP or ACTION_MOVE.
     */
    @Override
    public void handleTouch(GameModel.Touch touch, MotionEvent event) {
        game.removeEntity(this);
        GameProvider.setFirstPlay(false);
    }

    /**
     * If player is in this hitBox, do nothing
     *
     * @param e entity needed for dimensions
     * @return false
     */
    @Override
    public boolean inHitBox(Entity e) {
        return false;
    }
}
