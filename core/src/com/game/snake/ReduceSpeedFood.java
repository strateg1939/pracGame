package com.game.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class ReduceSpeedFood extends Food{
    private static Texture image;
    private static int scoreIncrease;
    static {
        image = new Texture(Gdx.files.internal("speed.png"));
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
}
