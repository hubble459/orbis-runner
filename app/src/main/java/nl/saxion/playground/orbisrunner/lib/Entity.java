package nl.saxion.playground.orbisrunner.lib;

import android.support.annotation.NonNull;
import android.view.MotionEvent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import nl.saxion.playground.orbisrunner.model.game.sprite.Coin;
import nl.saxion.playground.orbisrunner.model.game.sprite.FlyingEnemy;
import nl.saxion.playground.orbisrunner.model.game.sprite.JumpingEnemy;
import nl.saxion.playground.orbisrunner.model.game.sprite.Portal;
import nl.saxion.playground.orbisrunner.model.game.sprite.StaticEnemy;
import nl.saxion.playground.orbisrunner.ui.LevelMaker;


abstract public class Entity implements Comparable<Entity>, Serializable {
    // Static variable that provides the next `id`.
    private static int count = 0;
    // Used to sort objects on the same layer in the entities tree.
    private int id;

    // All entities have an x, y and an angle.
    protected float xVal, yVal, angle;
    // Start angle used for saving levels
    protected float startAngle;
    // Entity scale used in LevelMaker
    protected static float scale = 1;
    // All entities have dimensions.
    protected float width, height, margin;
    // Jump height for player and jumping enemies
    protected float startJump, jump;
    // Used by LevelMaker to change this specific position.
    private boolean selected;
    // Is resetting
    protected boolean reset;
    // Stop moving, and refresh when reset
    protected boolean pause;
    // Level Maker for selecting and deselecting entities.
    protected LevelMaker levelMaker;
    // Level Model for getting the x and y positions with degrees from circle;
    protected GameModel game;

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

    public static void setScale(float scale) {
        Entity.scale = scale;
    }

    public static float getScale() {
        return scale;
    }

    // Used by the TreeSet to order GameObjects.
    // We order by layer first, and then by id.
    @Override
    public int compareTo(@NonNull Entity o) {
        int prio = getLayer() - o.getLayer();
        return prio == 0 ? id - o.id : prio;
    }

    /**
     * Turn an JSONObject into an Entity
     *
     * @param entity JSONObject
     * @return Entity
     */
    public static Entity fromJSON(JSONObject entity) {
        String type = entity.optString("type");
        Entity e = getFromType(type);
        if (e != null) {
            e.setStartAngle((float) entity.optDouble("angle"));
            e.setStartJump((float) entity.optDouble("jump"));
        }
        return e;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * Turn a String into a json
     *
     * @param type Type String
     * @return Entity
     */
    private static Entity getFromType(String type) {
        switch (type) {
            case "StaticEnemy":
                return new StaticEnemy();
            case "JumpingEnemy":
                return new JumpingEnemy();
            case "FlyingEnemy":
                return new FlyingEnemy();
            case "Coin":
                return new Coin();
            case "Portal":
                return new Portal();
            default:
                return null;
        }
    }

    /**
     * Used by the control testing activity
     * Use GameModel#touches instead
     *
     * @param direction the swiping direction
     */
    @Deprecated
    public void onSwipe(int direction) {
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

    /**
     * Called `GameModel::ticksPerSecond()` times per second.
     * The method is to update the game state accordingly.
     */
    public void tick() {
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
    }

    /**
     * Check if an entity is inside this entities hit-box
     *
     * @param e entity needed for dimensions
     * @return true if dead
     */
    public boolean inHitBox(Entity e) {
        float x = e.getX();
        float y = e.getY();
        float w = e.getWidth();
        float h = e.getHeight();

        float start = x;
        float end = x + w;
        float top = y;
        float bot = y + h;

        float start2 = xVal;
        float end2 = xVal + width;
        float top2 = yVal;
        float bot2 = yVal + height;

        return (start < end2 && end > start2 &&
                top < bot2 && bot > top2);
    }

    /**
     * Can be overridden if the entity wants to act on touch events.
     *
     * @param touch Information about the touch this event is about.
     * @param event ACTION_DOWN, ACTION_UP or ACTION_MOVE.
     */
    public void handleTouch(GameModel.Touch touch, MotionEvent event) {
        if (levelMaker != null && clickedOnEntity(touch)) {
            levelMaker.select(this);
        }
    }

    public float getMargin() {
        return margin;
    }

    public void setMargin(float margin) {
        this.margin = margin;
    }

    public boolean onScreen() {
        if (game != null) {
            return xVal >= width && yVal >= height
                    && xVal <= game.getWidth() && yVal <= game.getHeight();
        } else {
            return true;
        }
    }

    /**
     * Used by levelMaker to check if the user clicked on an entity
     *
     * @param touch GameModel.Touch
     * @return clicked
     */
    private boolean clickedOnEntity(GameModel.Touch touch) {
        float x = touch.x;
        float y = touch.y;

        return x + width > xVal && x - width < xVal
                && y + height > yVal && y - height < yVal;
    }

    /**
     * Reset the entity
     */
    public void reset() {
        setStartJump(startJump);
        setStartAngle(startAngle);
        reset = true;
        pause = false;
    }

    /**
     * Resize
     */
    public void resize() {
        width = width * scale;
        height = height * scale;
    }

    /**
     * Turn an Entity into a JSONObject
     *
     * @return JSONObject
     */
    public JSONObject toJSON() throws JSONException {
        JSONObject entity = new JSONObject();
        entity.put("angle", startAngle);
        entity.put("jump", Float.isNaN(startJump) ? 0 : startJump);
        entity.put("type", getClass().getSimpleName());
        return entity;
    }
}

