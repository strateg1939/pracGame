package com.game.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class Textures {
    static Texture snakeUp = new Texture(Gdx.files.internal("snakeUp.png"));
    static Texture snakeLeft = new Texture(Gdx.files.internal("snakeLeft.png"));
    static Texture snakeDown =new Texture(Gdx.files.internal("snakeDown.png"));
    static Texture snakeRight = new Texture(Gdx.files.internal("snakeRight.png"));

    static Texture snakeUpLeft = new Texture(Gdx.files.internal("snakeUpLeft.png"));
    static Texture snakeRightDown =new Texture(Gdx.files.internal("snakeRightDown.png"));
    static Texture snakeDownRight = new Texture(Gdx.files.internal("snakeDownRight.png"));
    static Texture snakeLeftUp = new Texture(Gdx.files.internal("snakeLeftUp.png"));

    static Texture snakeDownRight2 = new Texture(Gdx.files.internal("snakeDownRight2.png"));
    static Texture snakeLeftDown2 =new Texture(Gdx.files.internal("snakeLeftDown2.png"));
    static Texture snakeLeftUp2 = new Texture(Gdx.files.internal("snakeLeftUp2.png"));
    static Texture snakeUpRight2 = new Texture(Gdx.files.internal("snakeUpRight2.png"));

    static Texture snakeHeadLeft = new Texture(Gdx.files.internal("snakeHeadLeft.png"));
    static Texture snakeHeadRight =new Texture(Gdx.files.internal("snakeHeadRight.png"));
    static Texture snakeHeadDown = new Texture(Gdx.files.internal("snakeHeadDown.png"));
    static Texture snakeHeadUp = new Texture(Gdx.files.internal("snakeHeadUp.png"));

    static Texture snakeHeadDownLeft1 = new Texture(Gdx.files.internal("snakeHeadDownLeft1.png"));
    static Texture snakeHeadDownRight1 =new Texture(Gdx.files.internal("snakeHeadDownRight1.png"));
    static Texture snakeHeadLeftUp1 = new Texture(Gdx.files.internal("snakeHeadLeftUp1.png"));
    static Texture snakeHeadRightUp1 = new Texture(Gdx.files.internal("snakeHeadRightUp1.png"));

    static Texture snakeHeadLeftUp2 = new Texture(Gdx.files.internal("snakeHeadLeftUp2.png"));
    static Texture snakeHeadDownRight2 =new Texture(Gdx.files.internal("snakeHeadDownRight2.png"));
    static Texture snakeHeadUpRight2 = new Texture(Gdx.files.internal("snakeHeadUpRight2.png"));
    static Texture snakeHeadRightDown2 = new Texture(Gdx.files.internal("snakeHeadRightDown2.png"));

    static Texture snakeTailUp = new Texture(Gdx.files.internal("snakeTailUp.png"));
    static Texture snakeTailLeft = new Texture(Gdx.files.internal("snakeTailLeft.png"));
    static Texture snakeTailDown =new Texture(Gdx.files.internal("snakeTailDown.png"));
    static Texture snakeTailRight = new Texture(Gdx.files.internal("snakeTailRight.png"));

    //Tiles Texture
    public static Texture currentTiles2 = new Texture(Gdx.files.internal("tiles2.png"));
    public static Texture currentTiles = new Texture(Gdx.files.internal("tiles.png"));
    public static Texture tiles2 = new Texture(Gdx.files.internal("tiles2.png"));
    public static Texture tiles = new Texture(Gdx.files.internal("tiles.png"));
    public static Texture tiles2_2 = new Texture(Gdx.files.internal("tiles2_2.png"));
    public static Texture tiles_2 = new Texture(Gdx.files.internal("tiles_2.png"));
    public static Texture tiles2_3 = new Texture(Gdx.files.internal("tiles2_3.png"));
    public static Texture tiles_3 = new Texture(Gdx.files.internal("tiles_3.png"));

    //sounds
    public static Music gameMusic = Gdx.audio.newMusic(Gdx.files.internal("dE.mp3"));
    public static Music pauseMusic = Gdx.audio.newMusic(Gdx.files.internal("pause.mp3"));
    public static Sound sound1 = Gdx.audio.newSound(Gdx.files.internal("eat1.mp3"));
    public static Sound sound2 = Gdx.audio.newSound(Gdx.files.internal("eat3.mp3"));
    public static Sound buttonS = Gdx.audio.newSound(Gdx.files.internal("button.mp3"));
    public static Sound slider = Gdx.audio.newSound(Gdx.files.internal("pop.mp3"));
    public static Sound fail = Gdx.audio.newSound(Gdx.files.internal("fail.mp3"));
    public static Sound nice = Gdx.audio.newSound(Gdx.files.internal("nice.mp3"));
    public static Sound bonus = Gdx.audio.newSound(Gdx.files.internal("bonus.mp3"));
    public static Sound slow = Gdx.audio.newSound(Gdx.files.internal("slow.mp3"));
}
