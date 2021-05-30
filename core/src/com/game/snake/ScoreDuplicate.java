package com.game.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class ScoreDuplicate extends Food{
    private static Texture image;
    private static int scoreIncrease;
    private static final int scoreDuplication = 2;
    static {
        image = new Texture(Gdx.files.internal("x2.png"));
        scoreIncrease = 50;
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
    public int getScoreDuplication() {
        return scoreDuplication;
    }
}