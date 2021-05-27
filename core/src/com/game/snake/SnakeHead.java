package com.game.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class SnakeHead extends Snake{
    private static Texture image;
    public int x;
    public int y;
    public SnakeHead(int x, int y){
        this.x = x;
        this.y = y;
    }
    static {
        image = new Texture(Gdx.files.internal("snake.png"));
    }

    @Override
    public Texture getImage() {
        return image;
    }
}