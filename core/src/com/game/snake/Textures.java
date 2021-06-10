package com.game.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class Textures {
    static Texture snakeStraight = new Texture(Gdx.files.internal("snake\\snakeRight.png"));

    static Texture snakeUpLeft = new Texture(Gdx.files.internal("snake\\snakeDownRight.png"));

    static Texture snakeUpRight = new Texture(Gdx.files.internal("snake\\snakeLeftDown2.png"));

    static Texture snakeHeadLeft = new Texture(Gdx.files.internal("snake\\snakeHeadUp.png"));

    static Texture snakeHeadRightUp = new Texture(Gdx.files.internal("snake\\snakeHeadDownRight2.png"));

    static Texture snakeHeadLeftUp = new Texture(Gdx.files.internal("snake\\snakeHeadDownLeft1.png"));

    static Texture snakeTail = new Texture(Gdx.files.internal("snake\\snakeTailUp.png"));

    //Tiles Texture
    public static Texture tilesEasyDark = new Texture(Gdx.files.internal("tiles2.png"));
    public static Texture tilesEasyLight = new Texture(Gdx.files.internal("tiles.png"));
    public static Texture tilesMediumDark = new Texture(Gdx.files.internal("tiles2_2.png"));
    public static Texture tilesMediumLight = new Texture(Gdx.files.internal("tiles_2.png"));
    public static Texture tilesHardDark = new Texture(Gdx.files.internal("tiles2_3.png"));
    public static Texture tilesHardLight = new Texture(Gdx.files.internal("tiles_3.png"));

    //sounds
    public static Music gameMusic;
    public static Music easyMusic = Gdx.audio.newMusic(Gdx.files.internal("music\\dE.mp3"));
    public static Music mediumMusic = Gdx.audio.newMusic(Gdx.files.internal("music\\dM.mp3"));
    public static Music hardMusic = Gdx.audio.newMusic(Gdx.files.internal("music\\dH.mp3"));
    public static Music pauseMusic = Gdx.audio.newMusic(Gdx.files.internal("music\\pause.mp3"));
    public static Sound soundDoubleFood = Gdx.audio.newSound(Gdx.files.internal("music\\eat1.mp3"));
    public static Sound soundStandardFood = Gdx.audio.newSound(Gdx.files.internal("music\\eat3.mp3"));
    public static Sound buttonS = Gdx.audio.newSound(Gdx.files.internal("music\\button.mp3"));
    public static Sound slider = Gdx.audio.newSound(Gdx.files.internal("music\\pop.mp3"));
    public static Sound fail = Gdx.audio.newSound(Gdx.files.internal("music\\fail.mp3"));
    public static Sound bonus = Gdx.audio.newSound(Gdx.files.internal("music\\bonus.mp3"));
    public static Sound slow = Gdx.audio.newSound(Gdx.files.internal("music\\slow.mp3"));
}
