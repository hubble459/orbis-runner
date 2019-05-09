package nl.saxion.playground.template.spaceshooter;

import java.util.Random;

import nl.saxion.playground.template.lib.Entity;

public class RockCreator extends Entity {

    private Game game;
    private Random random = new Random();
    private long tickCount;
    private long nextRockTickCount;

    RockCreator(Game game) {
        this.game = game;
        calculateNextRockTickCount();
    }

    @Override
    public void tick() {
        // Keep track of game time
        ++tickCount;

        // Should we span one (or more) rocks during this tick?
        while (tickCount >= nextRockTickCount) {
            game.addEntity(new Rock(game));
            calculateNextRockTickCount();
        }
    }

    private void calculateNextRockTickCount() {
        // Calculate how long we've been playing, as average time between rocks
        // should decrease based on this.
        float gameTime = (float)++tickCount / game.ticksPerSecond();

        // Start with one rock every 2s. every 1s after 2m. every 0.66s after 4m. etc.
        float avgTimeBetweenRocks = 2f*120f / (120f+gameTime);

        // The actual until the next rock will be 0 to 2 times the average time.
        float nextRockTime = avgTimeBetweenRocks  * 2f * random.nextFloat();
        nextRockTickCount = tickCount + Math.round(nextRockTime * game.ticksPerSecond());
    }
}

