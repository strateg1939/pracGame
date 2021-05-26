package com.game.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class SnakeDesign extends Snake{
    private static Texture image;
    static {
        image = new Texture(Gdx.files.internal("snake.png"));
    }

     public SnakeDesign(String name){
        this.name= name;
    }

    @Override
    public Texture getImage() {
        return image;
    }
}
