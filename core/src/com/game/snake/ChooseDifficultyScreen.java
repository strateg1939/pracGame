package com.game.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;

public class ChooseDifficultyScreen implements Screen {
    final Main game;
    private OrthographicCamera camera;
    private Stage stage;

    public ChooseDifficultyScreen(final Main gam) {
        game = gam;
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        Skin skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));
        //creates buttons for difficulty choosing
        stage.addActor(getDefaultButtonForThisScreen("Easy", 350, GameDifficulty.EASY, skin));
        stage.addActor(getDefaultButtonForThisScreen("Medium", 200, GameDifficulty.MEDIUM, skin));
        stage.addActor(getDefaultButtonForThisScreen("Hard", 50, GameDifficulty.HARD, skin));
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
    }
    //returns TextButton with size 200,100; x= 300, skin from assets and on click start of game
    private Button getDefaultButtonForThisScreen(String buttonText, float positionY, final GameDifficulty gameDifficulty, Skin skin){
        TextButton button = new TextButton(buttonText, skin);
        button.setSize(200, 100);
        button.setPosition(300, positionY);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new GameScreen(game, gameDifficulty));
                //!Important!
                ChooseDifficultyScreen.this.dispose();
            }
        });
        return button;
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        stage.draw();
        game.font.draw(game.batch, "Choose difficulty setting (currently does nothing)", 300, 480);
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
