package nl.saxion.playground.orbisrunner.sprite;

import nl.saxion.playground.orbisrunner.R;
import nl.saxion.playground.orbisrunner.game.OrbisRunnerModel;

public class JumpingEnemy extends Sprite {
    private static final float MAX_HEIGHT = 200f;
    private static final float JUMP_ACC = 2f;
    private float maxHeight;
    private boolean up;

    public JumpingEnemy() {
        setMargin(-15f);
        maxHeight = getRandomHeight();
    }

    @Override
    public int getBitmapRes() {
        return R.drawable.sprite_koopa;
    }

    @Override
    public String getName() {
        return "Jumping Enemy";
    }

    @Override
    public void tick() {
        super.tick();

        if (game instanceof OrbisRunnerModel) {
            if (up) {
                jump += JUMP_ACC;
                if (jump > maxHeight) {
                    up = false;
                }
            } else {
                jump -= JUMP_ACC;
                if (jump <= 0) {
                    up = true;
                    maxHeight = getRandomHeight();
                }
            }
        }
    }

    private float getRandomHeight() {
        return MAX_HEIGHT + (int) (Math.random() * 100) - 50;
    }
}
