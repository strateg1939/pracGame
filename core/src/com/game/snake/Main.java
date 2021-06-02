package com.game.snake;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Main extends Game {

	SpriteBatch batch;
	BitmapFont font;
	String userName = "player";
	Records records;

	public void create() {
		batch = new SpriteBatch();
		// Use LibGDX's default Arial font.
		font = new BitmapFont();
		this.setScreen(new SetNameScreen(this));
		records = new Records();
	}

	public void render() {
		super.render(); // important!
	}

	public void dispose() {
		records.save();
		batch.dispose();
		font.dispose();
	}

}