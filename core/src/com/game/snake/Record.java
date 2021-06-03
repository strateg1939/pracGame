package com.game.snake;

import java.io.Serializable;

/**
 * class that defines one record that is written in database when a game has ended
 * gameInformation is just general info that isn't meant to be sorted by
 */
public class Record implements Serializable {
    public int score;
    public GameDifficulty gameDifficulty;
    public String gameInformation;
    public boolean isMathMode;
    public String userName;
    public int finalSnakeSize;

    public Record(int score, GameDifficulty gameDifficulty, String gameInformation, boolean isMathMode, String userName,int finalSnakeSize) {
        this.score = score;
        this.gameDifficulty = gameDifficulty;
        this.gameInformation = gameInformation;
        this.isMathMode = isMathMode;
        this.userName = userName;
        this.finalSnakeSize = finalSnakeSize;
    }
}
