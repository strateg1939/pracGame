package com.game.snake;

import com.badlogic.gdx.graphics.Texture;

public abstract class Food {
    private Consumable consumable;
    public void setOnConsume(Consumable consume){
        consumable = consume;
    }
    public void consume(){
        consumable.consume();
    }
    public abstract Texture getImage();

    interface Consumable{
        void consume();
    }
}
