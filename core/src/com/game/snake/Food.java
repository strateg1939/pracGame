package com.game.snake;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

/**
 * abstract class that all food is extended from
 * invoke and define setOnConsume in game when creating new food
 */
public abstract class Food {
    private Consumable consumable;
    public void setOnConsume(Consumable consume){
        consumable = consume;
    }
    public abstract int getScoreIncrease();

    /**
     * does what was set on consume
     * and increases score
     * and plays an eating sound
     * @param score
     */
    public void consume(IntWrapper score, IntWrapper scoreMultiplier){
        consumable.consume();
        score.value += getScoreIncrease() * scoreMultiplier.value;
        scoreMultiplier.value = getScoreDuplication();
        getEatingSound().play();
    }
    public abstract Texture getImage();
    public int getScoreDuplication(){return 1;}
    public abstract Sound getEatingSound();

    interface Consumable{
        void consume();
    }
}
