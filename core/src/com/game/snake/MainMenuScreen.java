package com.game.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenuScreen implements Screen {

    final Main game;
    private OrthographicCamera camera;
    private Stage stage;
    private boolean isMathMode = false;
    SwingTableScreen screen;
    private static final int MIN_COUNT = 3;
    private static final int MAX_COUNT = 50;

    public MainMenuScreen(final Main gam) {
        game = gam;
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1000, 680);
        Skin skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));

        Table table = new Table();
        table.setFillParent(true);
        CheckBox checkBoxForMathGame = new CheckBox("   Math Game Mode", skin);
        checkBoxForMathGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                isMathMode = !isMathMode;
            }
        });
        TextButton button = new TextButton("Begin!", skin);

        button.setSize(200, 50);
        TextButton scoreBoard = new TextButton("Show score records", skin);
        scoreBoard.setSize(200, 100);
        scoreBoard.setPosition(300, 100);
        scoreBoard.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                screen = new SwingTableScreen(game);
            }
        });
        final Label rowsLabel = new Label("Rows count", skin);
        rowsLabel.setSize(100, 100);
        final Slider rowsValueSlider = new Slider(MIN_COUNT, MAX_COUNT, 1, false, skin);
        final Label rowsValueLabel = new Label(Integer.toString(MIN_COUNT), skin);
        rowsValueSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float value = rowsValueSlider.getValue();
                rowsValueLabel.setText((int) value);
            }
        });
        // BPM
        final Label columnsLabel = new Label("Columns count", skin);
        final Slider columnsSlider = new Slider(MIN_COUNT, MAX_COUNT, 1, false, skin);
        final Label columnsValueLabel = new Label(Integer.toString(MIN_COUNT), skin);
        columnsSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                columnsValueLabel.setText((int) columnsSlider.getValue());
            }
        });
        table.setPosition(-300,0);
        table.setTransform(true);
        table.add(rowsLabel).uniform();
        table.add(rowsValueSlider);
        table.add(rowsValueLabel);
        table.row().pad(10);
        table.add(columnsLabel ,columnsSlider, columnsValueLabel);
        table.row().pad(10);
        table.add(checkBoxForMathGame).padLeft(150);
        table.row().pad(10);
        table.add(button).padLeft(100).size(150,50);
        table.setScale(1.5f);
        stage.addActor(table);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new ChooseDifficultyScreen(game, isMathMode));
                game.tileRows = (int) rowsValueSlider.getValue();
                game.tileColumns = (int) columnsSlider.getValue();
                //!Important!
                MainMenuScreen.this.dispose();
                if(screen != null) screen.dispose();
            }
        });
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
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
