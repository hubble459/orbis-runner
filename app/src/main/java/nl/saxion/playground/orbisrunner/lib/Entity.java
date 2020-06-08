package nl.saxion.playground.orbisrunner.lib;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.view.MotionEvent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import nl.saxion.playground.orbisrunner.game.entity.Circle;
import nl.saxion.playground.orbisrunner.game.entity.Player;
import nl.saxion.playground.orbisrunner.levelmaker.EntityItem;
import nl.saxion.playground.orbisrunner.ui.LevelMaker;
import nl.saxion.playground.orbisrunner.ui.demo.entities.DemoEnemy;


abstract public class Entity implements Comparable<Entity>, Serializable {
    // Entity speed
    private static final float SPEED = .3f;

    // Static variable that provides the next `id`.
    private static int count = 0;
    // Used to sort objects on the same layer in the entities tree.
    private int id;

    // All entities have an x, y and an angle.
    protected float xVal, yVal, angle;
    // Start angle used for saving levels
    protected float startAngle;
    // All entities have dimensions.
    protected float width, height;
    // Jump height for player and jumping enemies
    protected float startJump, jump;
    // Used by LevelMaker to change this specific position.
    private boolean selected;
    // Stop moving
    private boolean pause;
    // Level Maker for selecting and deselecting entities.
    private LevelMaker levelMaker;
    // Level Model for getting the x and y positions with degrees from circle;
    private GameModel game;
    // Paint object for drawing outline when selected
    private Paint paint;

    // The constructor assigns an id that is used for ordering draws.
    public Entity() {
        id = ++count;
    }

    /**
     * Override this method to determine the rendering order for this
     * object. Higher numbers get drawn later, overdrawing.
     * The number you return *should be constant* for a specific object.
     * The default layer is 0. Negative layer numbers are allowed.
     * Objects in the same layer are drawn in the order they were created.
     *
     * @return Layer id.
     */
    public int getLayer() {
        return 0;
    }

    public static Entity fromJSON(JSONObject entity) {
        int type = entity.optInt("type");
        Entity e = getFromType(type);
        if (e != null) {
            e.setStartAngle((float) entity.optDouble("angle"));
            e.setStartJump((float) entity.optDouble("jump"));
        }
        return e;
    }

    private static Entity getFromType(int type) {
        switch (type) {
            case EntityItem.DEMO_ENEMY:
                return new DemoEnemy();
        }
        return null;
    }

    /**
     * Called `GameModel::ticksPerSecond()` times per second.
     * The method is to update the game state accordingly.
     */
    public void tick() {
        if (game != null && !pause &&
                !(this instanceof Player) &&
                !(this instanceof Circle)) {
            angle += SPEED;
            if (angle > 360) angle = 0;
            setXYValues(game.getXYFromDegrees(angle, jump, this));
        }
    }

    /**
     * Called up to 60 times per second, system performance allowing.
     * The method is to draw the Entity to the GameView. Entities
     * can be more abstract in nature (CollisionChecker, ObjectSpawner, ..),
     * in which case this method does not need to be overridden.
     *
     * @param gv The `GameView` to draw to.
     */
    public void draw(GameView gv) {
        if (levelMaker != null && selected) {
            if (paint == null) {
                paint = new Paint();
                paint.setColor(Color.CYAN);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(10f);
            }
            drawOutline(gv.getCanvas());
        }
    }

    private void drawOutline(Canvas canvas) {
        canvas.save();
        canvas.rotate(angle, xVal + width / 2, yVal + height / 2);
        canvas.drawCircle(xVal + width / 2, yVal + height / 2, width, paint);
        canvas.restore();
    }

    // Used by the TreeSet to order GameObjects.
    // We order by layer first, and then by id.
    @Override
    public int compareTo(@NonNull Entity o) {
        int prio = getLayer() - o.getLayer();
        return prio == 0 ? id - o.id : prio;
    }

    public void onSwipe(int direction) {
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * Can be overridden if the entity wants to act on touch events.
     *
     * @param touch Information about the touch this event is about.
     * @param event ACTION_DOWN, ACTION_UP or ACTION_MOVE.
     */
    public void handleTouch(GameModel.Touch touch, MotionEvent event) {
        if (levelMaker != null && clickedOnEnemy(touch)) {
            levelMaker.select(this);
        }
    }

    private boolean clickedOnEnemy(GameModel.Touch touch) {
        float x = touch.x;
        float y = touch.y;

        return x + width > xVal && x - width < xVal
                && y + height > yVal && y - height < yVal;
    }

    public void setXYValues(float[] xy) {
        xVal = xy[0];
        yVal = xy[1];
        angle = xy[2];
    }

    public void setLevelMaker(LevelMaker levelMaker) {
        this.levelMaker = levelMaker;
        this.game = null;
    }

    public float getAngle() {
        return angle;
    }

    public void setGame(GameModel game) {
        this.game = game;
        this.levelMaker = null;
    }

    public float getHeight() {
        return height;
    }

    public float getWidth() {
        return width;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject entity = new JSONObject();
        entity.put("angle", startAngle);
        entity.put("jump", Float.isNaN(startJump) ? 0 : startJump);
        entity.put("type", getType());
        return entity;
    }

    private int getType() {
        switch (getClass().getSimpleName().toLowerCase()) {
            case "demoenemy":
                return EntityItem.DEMO_ENEMY;
        }
        return 0;
    }

    public void setScale(float scale) {
        height *= scale;
        width *= scale;
    }

    public float getStartAngle() {
        return startAngle;
    }

    public void setStartAngle(float startAngle) {
        if (Float.isNaN(startAngle)) startAngle = 0;
        this.startAngle = startAngle;
        this.angle = startAngle;
    }

    public float getStartJump() {
        return startJump;
    }

    public void setStartJump(float height) {
        this.startJump = height;
        this.jump = height;
    }

    public float getJump() {
        return jump;
    }

    public float getX() {
        return xVal;
    }

    public float getY() {
        return yVal;
    }

    public void setPaused(boolean pause) {
        this.pause = pause;
    }

    public boolean inHitbox(Entity e) {
        float x = e.getX();
        float y = e.getY();
        float w = e.getWidth();
        float h = e.getHeight();

        float start = x - w / 2;
        float end = x + w / 2;
        float top = y - h / 2;
        float bot = y + h / 2;

        float start2 = xVal - width / 2;
        float end2 = xVal + width / 2;
        float top2 = yVal - height / 2;
        float bot2 = yVal + height / 2;

        if (end >= start2 && start <= end2) {
            return top >= top2 && bot <= bot2
                    || top2 >= top && bot2 <= bot;
        }
        return false;
    }

    public void reset() {
        setStartJump(startJump);
        setStartAngle(startAngle);
    }
}

