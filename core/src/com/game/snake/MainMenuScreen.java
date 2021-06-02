package com.game.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenuScreen implements Screen {

    final Main game;
    private OrthographicCamera camera;
    private Stage stage;
    private boolean isMathMode = false;
    SwingTableScreen screen;

    public MainMenuScreen(final Main gam) {
        game = gam;
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1000, 680);
        Skin skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));
        CheckBox checkBoxForMathGame = new CheckBox("   Math Game Mode", skin);
        checkBoxForMathGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                isMathMode = !isMathMode;
            }
        });
        checkBoxForMathGame.setSize(100, 100);
        checkBoxForMathGame.setPosition(300, 300);
        stage.addActor(checkBoxForMathGame);
        TextButton button = new TextButton("Begin!", skin);
        button.setSize(200, 100);
        button.setPosition(300, 500);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new ChooseDifficultyScreen(game, isMathMode));
                //!Important!
                MainMenuScreen.this.dispose();
                if(screen != null) screen.dispose();
            }
        });
        stage.addActor(button);
        TextButton scoreBoard = new TextButton("Show score records", skin);
        scoreBoard.setSize(200, 100);
        scoreBoard.setPosition(300, 100);
        scoreBoard.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                screen = new SwingTableScreen(game);
            }
        });
        stage.addActor(scoreBoard);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.font.draw(game.batch, "Welcome to Drop!!! ", 100, 150);
        game.font.draw(game.batch, "Tap anywhere to begin!", 100, 100);
        stage.draw();
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
