package com.game.snake;

import com.badlogic.gdx.graphics.Texture;

public abstract class Food {
    private Consumable consumable;
    public void setOnConsume(Consumable consume){
        consumable = consume;
    }
    public abstract int getScoreIncrease();

    /**
     * does what was set on consume
     * and increases score
     *
     * @param score
     */
    public void consume(IntWrapper score){
        consumable.consume();
        score.value += getScoreIncrease();
    }
    public abstract Texture getImage();

    interface Consumable{
        void consume();
    }
}
