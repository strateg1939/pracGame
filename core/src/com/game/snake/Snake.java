package com.game.snake;

import com.badlogic.gdx.graphics.Texture;

public abstract class Snake {
    private Consumable consumable;
    public void setOnConsume(Consumable consume){
        consumable = consume;
    }
    public void consume(){
        consumable.consume();
    }
    public abstract Texture getImage();

    public void setOnConsume(Food.Consumable i_was_consumed) {
    }

    interface Consumable{
        void consume();
    }
}
