package nl.saxion.playground.orbisrunner.model.game.sprite;

import nl.saxion.playground.orbisrunner.R;
import nl.saxion.playground.orbisrunner.lib.Entity;
import nl.saxion.playground.orbisrunner.model.Level;
import nl.saxion.playground.orbisrunner.model.game.OrbisRunnerModel;
import nl.saxion.playground.orbisrunner.singleton.GameProvider;

/**
 * Coin sprite
 */
public class Coin extends Sprite {
    /**
     * Set margin on create
     */
    public Coin() {
        setMargin(-20f);
    }

    @Override
    public int getBitmapRes() {
        return R.drawable.sprite_coin;
    }

    @Override
    public String getName() {
        return "Coin";
    }

    /**
     * Collect a coin
     * Will add a current coin to your total coin count
     * Removes this coin entity so it can only be collected once
     * And refresh the coin count text views
     */
    public void collectCoin() {
        Level level = GameProvider.getCurrentLevel();
        level.collectCoin();
        GameProvider.setCoins(GameProvider.getCoins() + 1);
        game.removeEntity(this);
        ((OrbisRunnerModel) game).collectedCoin();
        ((OrbisRunnerModel) game).setCoinCount(GameProvider.getCoins());
    }

    /**
     * When you touch a coin, collectCoin will be called
     *
     * @param e entity needed for dimensions
     * @return true if dead
     */
    @Override
    public boolean inHitBox(Entity e) {
        if (super.inHitBox(e) && game instanceof OrbisRunnerModel) {
            collectCoin();
        }
        return false;
    }
}
