package com.game.snake;

/**
 * class that allows for int to be passed by reference
 * stupid java doesn't have a proper implementation
 * when even C has
 */
public class IntWrapper {
    public int value;
    public IntWrapper(int value){
        this.value = value;
    }
}
