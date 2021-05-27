package com.game.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class SnakeTail extends Snake{
    private static Texture image;
    static {
        image = new Texture(Gdx.files.internal("snake.png"));
    }

    @Override
    public Texture getImage() {
        return image;
    }
}
