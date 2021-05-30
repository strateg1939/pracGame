package com.game.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class SnakeTail extends Snake{
    protected Texture image;
    public SnakeTail(int x, int y){
        image = new Texture(Gdx.files.internal("snakeRight.png"));
    }


    public void setImage(Texture image) {
        this.image = image;
    }

    @Override
    public Texture getImage() {
        return image;
    }
}
