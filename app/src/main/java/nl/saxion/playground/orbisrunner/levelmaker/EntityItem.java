package nl.saxion.playground.orbisrunner.levelmaker;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import nl.saxion.playground.orbisrunner.R;
import nl.saxion.playground.orbisrunner.ui.demo.entities.DemoEnemy;

public class EntityItem {
    public static final int DEMO_ENEMY = 0;
    private Class entity;
    private int drawableRes;
    private String name;

    public EntityItem(@ItemTypeDef int type) {
        switch (type) {
            case DEMO_ENEMY:
                entity = DemoEnemy.class;
                name = "Demo Enemy";
                drawableRes = R.drawable.demo_entity;
                break;
        }
    }

    public int getDrawableRes() {
        return drawableRes;
    }

    public String getName() {
        return name;
    }

    public Class getEntity() {
        return entity;
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef(value = {DEMO_ENEMY})
    private @interface ItemTypeDef {
    }
}
