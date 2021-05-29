package com.game.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class ReduceSpeedFood extends Food{
    private static Texture image;
    static {
        image = new Texture(Gdx.files.internal("speed.png"));
    }

    @Override
    public Texture getImage() {
        return image;
    }
}
