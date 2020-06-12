package nl.saxion.playground.orbisrunner.lib;

import android.graphics.Canvas;
import android.view.MotionEvent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import static nl.saxion.playground.orbisrunner.ui.ControlTestingActivity.Direction.DOWN;
import static nl.saxion.playground.orbisrunner.ui.ControlTestingActivity.Direction.LEFT;
import static nl.saxion.playground.orbisrunner.ui.ControlTestingActivity.Direction.RIGHT;
import static nl.saxion.playground.orbisrunner.ui.ControlTestingActivity.Direction.UP;


public class GameModel implements Serializable {

    /**
     * The list of current touches. For each update, `Entity.handleTouch` is called.
     * But `Entity.tick` (and others) are free to manually inspect (or even edit)
     * this list as well.
     */
    public transient ArrayList<Touch> touches = new ArrayList<>();
    /**
     * Actual width of the current GameView in pixels. These might change during the game,
     * for instance when changing orientation.
     * These should only be used by your `getWidth()` and `getHeight()` implementations,
     * to derive a virtual resolution that matches the screen aspect ratio.
     */
    protected float actualWidth, actualHeight;
    // The ordered list of active game entities.
    SafeTreeSet<Entity> entities = new SafeTreeSet<>();
    // `true` after `start()` has been called.
    boolean started = false;
    // A list of Entites per class type. Entities are also added to their superclasses (not including Entity itself).
    private HashMap<Class, ArrayList<Entity>> entitiesByType = new HashMap<>();

    /**
     * Override to set ticks per second.
     *
     * @return Number of times per second the `tick` methods should be called.
     * This can be zero (for instance when updates made directly from event handlers,
     * such as may be the case for non-animated board games), in which case
     * `GameModel.event("updated")` should be called when appropriate.
     */
    public int ticksPerSecond() {
        return 180;
    }

    /**
     * Override this to make use of virtual screen sizes.
     *
     * @return The desired virtual width for the game. The `GameView` will scale,
     * translate and crop `draw()` output to make the virtual screen fit exactly
     * within the actual view.
     */
    public float getWidth() {
        return actualWidth;
    }

    /**
     * Override this to make use of virtual screen sizes.
     *
     * @return The desired virtual height for the game. The `GameView` will scale,
     * translate and crop `draw()` output to make the virtual screen fit exactly
     * within the actual view.
     */
    public float getHeight() {
        return actualHeight;
    }

    /**
     * Called just before the first `draw()`. At this point, canvas widths are known, so
     * this may be a good time to create initial `Entity`s.
     *
     * @param canvas canvas
     */
    public void start(Canvas canvas) {
    }

    /**
     * Called right after the first `draw()`. At this point, canvas widths are known, so
     * this may be a good time to create initial `Entity`s.
     *
     * @param canvas canvas
     */
    public void started(Canvas canvas) {
    }


    /**
     * Add a game entity to the list.
     *
     * @param entity The entity to be added.
     */
    public void addEntity(Entity entity) {
        entities.add(entity);
        // For entity class and each super class up till but not including Entity,
        // add the entity to the right entitiesByType array.
        for (Class cls = entity.getClass(); cls != Entity.class; cls = cls.getSuperclass()) {
            ArrayList<Entity> list = entitiesByType.get(cls);
            if (list == null) {
                list = new ArrayList<>();
                entitiesByType.put(cls, list);
            }
            list.add(entity);
        }
    }

    /**
     * Remove a game entity from the list.
     *
     * @param entity The entity to be removed.
     */
    public void removeEntity(Entity entity) {
        entities.remove(entity);
        // For entity class and each super class up til but not including Entity,
        // remove the entity to the right entitiesByType array.
        for (Class cls = entity.getClass(); cls != Entity.class; cls = cls.getSuperclass()) {
            entitiesByType.get(cls).remove(entity);
        }
    }

    /**
     * Remove game entities from the list.
     *
     * @param entityList The entityList to be removed.
     */
    public void removeEntities(ArrayList<Entity> entityList) {
        for (Entity entity : entityList) {
            entities.remove(entity);

        }
        // For entityList class and each super class up til but not including Entity,
        // remove the entityList to the right entitiesByType array.
        for (Entity entity : entityList) {
            for (Class cls = entity.getClass(); cls != Entity.class; cls = cls.getSuperclass()) {
                entitiesByType.get(cls).remove(entity);
            }
        }
    }

    /**
     * Get an `ArrayList` of `Entity`s of the specified type. Eg:
     * `ArrayList<MyHero> heroes = game.getEntities(MyHero.class);`
     *
     * @param type A class object to search for in the list of entities.
     * @return The list of entities of the specified class.
     */
    public <T extends Entity> ArrayList<T> getEntities(Class<T> type) {
        @SuppressWarnings("unchecked")
        ArrayList<T> list = (ArrayList<T>) entitiesByType.get(type);
        return list == null ? new ArrayList<T>() : list;
    }

    public SafeTreeSet<Entity> getEntities() {
        return entities;
    }

    /**
     * Get an `Entity` of the specified type.
     *
     * @param type A class object to search for in the list of entities.
     * @return A single entity (the first) of the specified type, or null (if none are found).
     */
    public <T extends Entity> T getEntity(Class<T> type) {
        @SuppressWarnings("unchecked")
        ArrayList<T> list = (ArrayList<T>) entitiesByType.get(type);
        if (list.size() == 0) return null;
        return list.get(0);
    }

    // See if any game objects are interested in handling this click immediately.
    // They may also choose to scan for ongoing touches in their tick() methods.
    void handleTouch(MotionEvent me) {
        Touch touch = getTouch(me);
        for (Entity go : entities) {
            go.handleTouch(touch, me);
        }
    }

    // Helper method to match the incoming touch event to an ongoing touch we
    // have in are `touches` array. If not found, create one.
    private Touch getTouch(MotionEvent me) {
        int action = me.getActionMasked();
        int actionIndex = me.getActionIndex();
        int pointerId = me.getPointerId(actionIndex);
        float x = me.getX(actionIndex);
        float y = me.getY(actionIndex);

        if (action == MotionEvent.ACTION_POINTER_DOWN) action = MotionEvent.ACTION_DOWN;
        if (action == MotionEvent.ACTION_POINTER_UP) action = MotionEvent.ACTION_UP;

        for (Touch touch : touches) {
            if (touch.pointerId == pointerId) {
                touch.setXY(x, y);
                touch.lastAction = action;
                if (action == MotionEvent.ACTION_UP) {
                    touches.remove(touch);
                }
                return touch;
            }
        }

        // New touch
        Touch touch = new Touch(pointerId, x, y, action);
        touches.add(touch);
        return touch;
    }

    public void dead() {
    }

    public float[] getXYFromDegrees(float degrees, float jump, Entity e) {
        return new float[3];
    }

    /**
     * Helper class that contains information about an ongoing touch.
     */
    public static class Touch {
        public int pointerId;
        public float x, y;
        public float startX, startY;
        public float deltaY, deltaX;
        public long startTime;
        public int lastAction;

        Touch(int pointerId, float startX, float startY, int lastAction) {
            this.pointerId = pointerId;
            this.lastAction = lastAction;
            this.startTime = System.currentTimeMillis();
            this.x = this.startX = startX;
            this.y = this.startY = startY;
        }

        public long getDuration() {
            return System.currentTimeMillis() - startTime;
        }

        void setXY(float x, float y) {
            this.deltaX = x - this.x;
            this.x = x;
            this.deltaY = y - this.y;
            this.y = y;
        }

        public int getSwipeDirection() {
            if (Math.abs(deltaY) > Math.abs(deltaX)) {
                if (startY < y) {
                    // Moved down
                    // Roll
                    return DOWN;
                } else {
                    // Moved up
                    // Do nothing
                    return UP;
                }
            } else {
                // Motion in X direction.
                if (startX < x) {
                    // Moved right
                    return RIGHT;
                } else {
                    // Moved left
                    return LEFT;
                }
            }
        }
    }
}
