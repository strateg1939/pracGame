package com.game.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class SnakeTail extends Snake{
    protected static Texture image;
    public int xT;
    public int yT;
    public SnakeTail(int x, int y){
        this.xT = x;
        this.yT = y;
    }
    static {
        if (GameScreen.direction==0) {
            image = new Texture(Gdx.files.internal("snakeRight.png"));
        }

    }

    @Override
    public Texture getImage() {
        return image;
    }
}
