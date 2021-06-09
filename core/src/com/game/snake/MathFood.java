package com.game.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

import java.awt.*;

public class MathFood extends Food{
    public static Texture image;
    public int x;
    public int y;
    public int answer;
    static {
        image = new Texture(Gdx.files.internal("apple.png"));
    }
    @Override
    public int getScoreIncrease() {
        return 100;
    }

    @Override
    public Texture getImage() {
        return image;
    }

    @Override
    public Sound getEatingSound() {
        return Textures.bonus;
    }
}
