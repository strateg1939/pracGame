package com.game.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class DoubleStandardFood extends Food{
    private static Texture image;
    static {
        image = new Texture(Gdx.files.internal("appleDouble.png"));
    }


    @Override
    public Texture getImage() {
        return image;
    }
}
