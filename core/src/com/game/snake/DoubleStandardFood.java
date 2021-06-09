package com.game.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class DoubleStandardFood extends Food{
    private static Texture image;
    private static int scoreIncrease;
    static {
        image = new Texture(Gdx.files.internal("appleDouble.png"));
        scoreIncrease = 150;
    }

    @Override
    public int getScoreIncrease() {
        return scoreIncrease;
    }


    @Override
    public Texture getImage() {
        return image;
    }

    @Override
    public Sound getEatingSound() {
        return Textures.soundDoubleFood;
    }
}
