package nl.saxion.playground.orbisrunner.sprite;

import nl.saxion.playground.orbisrunner.R;
import nl.saxion.playground.orbisrunner.game.Level;
import nl.saxion.playground.orbisrunner.game.OrbisRunnerModel;
import nl.saxion.playground.orbisrunner.lib.Entity;
import nl.saxion.playground.orbisrunner.singleton.GameProvider;

public class Coin extends Sprite {
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

    @Override
    public boolean inHitBox(Entity e) {
        if (super.inHitBox(e) && game instanceof OrbisRunnerModel) {
            Level level = GameProvider.getCurrentLevel();
            level.collectCoin();
            GameProvider.setCoins(GameProvider.getCoins() + 1);
            game.removeEntity(this);
        }
        return false;
    }
}
