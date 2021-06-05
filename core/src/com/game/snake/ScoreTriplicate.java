package com.game.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class ScoreTriplicate extends Food{
    protected static Texture imagex3;
    private static int scoreIncrease;
    private static final int scoreDuplication = 3;
    static {
        imagex3 = new Texture(Gdx.files.internal("x3.png"));
        scoreIncrease = 50;
    }

    @Override
    public int getScoreIncrease() {
        return scoreIncrease;
    }
    @Override
    public Texture getImage() {
        return imagex3;
    }

    @Override
    public int getScoreDuplication() {
        return scoreDuplication;
    }
}
