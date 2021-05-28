package com.game.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class SnakeTail extends Snake{
    protected static Texture image;
    static {
        if (GameScreen.direction==0) {
            image = new Texture(Gdx.files.internal("snake2.png"));
        }

    }

    @Override
    public Texture getImage() {
        return image;
    }
}
