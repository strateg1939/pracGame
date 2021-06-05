package com.game.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class MainMenuScreen implements Screen {

    final Main game;
    private OrthographicCamera camera;
    private Stage stage;
    private boolean isMathMode = false;
    SwingTableScreen screen;
    //amount of tiles in game
    private static final int MIN_COUNT = 6;
    private static final int MAX_COUNT = 50;
    private Table mathModeOptions;
    private Table generalTable;
    private Skin skin;
    private static final int MAX_FOR_EXERCISE = 1000;
    private static final int MAX_FOR_EXERCISE_LENGTH = 10;
    private static final int MIN_FOR_EXERCISE_LENGTH = 1;
    private static final int MIN_FOR_EXERCISE = 1;

    public MainMenuScreen(final Main gam) {
        game = gam;
        game.isNoBordersMode = false;
        stage = new Stage(new StretchViewport(Main.WORLD_WIDTH, Main.WORLD_HEIGHT));
        Gdx.input.setInputProcessor(stage);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Main.WORLD_WIDTH, Main.WORLD_HEIGHT);
        skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));
        generalTable = new Table();
        generalTable.setFillParent(true);
        CheckBox checkBoxForMathGame = new CheckBox("   Math Game mode", skin);
        checkBoxForMathGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Textures.buttonS.play();
                isMathMode = !isMathMode;
                if (isMathMode) showMathModeOptions();
                else removeMathModeOptions();
            }
        });
        CheckBox checkBoxForNoBorders = new CheckBox("   No borders mode", skin);
        checkBoxForNoBorders.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Textures.buttonS.play();
                game.isNoBordersMode = !game.isNoBordersMode;
            }
        });
        TextButton beginButton = new TextButton("Begin!", skin);
        beginButton.setSize(200, 50);
        TextButton scoreBoard = new TextButton("Show score board", skin);
        scoreBoard.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Textures.buttonS.play();
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
                Textures.slider.play();
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
                Textures.slider.play();
                columnsValueLabel.setText((int) columnsSlider.getValue());
            }
        });
        generalTable.setPosition(-300, -100);
        generalTable.setTransform(true);
        generalTable.add(rowsLabel);
        generalTable.add(rowsValueSlider);
        generalTable.add(rowsValueLabel);
        generalTable.row().pad(10);
        generalTable.add(columnsLabel, columnsSlider, columnsValueLabel);
        generalTable.row().pad(10);
        generalTable.add(checkBoxForMathGame).padLeft(150);
        generalTable.row().pad(10);
        mathModeOptions = new Table();
        generalTable.add(mathModeOptions).padLeft(100).colspan(3);
        generalTable.row();
        //don`t know why it is 127 when it lines with other checkbox
        generalTable.add(checkBoxForNoBorders).padLeft(127);
        generalTable.row().pad(10);
        generalTable.add(beginButton).size(230, 50).colspan(3);
        generalTable.setScale(1.5f);
        stage.addActor(generalTable);
        beginButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Textures.buttonS.play();
                game.setScreen(new ChooseDifficultyScreen(game, isMathMode));
                game.tileRows = (int) rowsValueSlider.getValue();
                game.tileColumns = (int) columnsSlider.getValue();
                //!Important!
                MainMenuScreen.this.dispose();
                if (screen != null) screen.dispose();
            }
        });
        scoreBoard.setSize(200, 50);
        scoreBoard.setPosition(100, 100);
        stage.addActor(scoreBoard);
        TextButton userScreenButton = new TextButton("Change user", skin);
        userScreenButton.setSize(200, 50);
        userScreenButton.setPosition(700, 100);
        userScreenButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Textures.buttonS.play();
                game.setScreen(new SetNameScreen(game));
                MainMenuScreen.this.dispose();
            }
        });
        stage.addActor(userScreenButton);
        MathGameScreen.setMaxForExercise(MIN_FOR_EXERCISE);
        MathGameScreen.setMaxlengthOfExercise(MIN_FOR_EXERCISE_LENGTH);
    }

    private void removeMathModeOptions() {
        mathModeOptions.clear();
    }

    private void showMathModeOptions() {
        final Label maxNumberLabel = new Label("Max number for exercises", skin);
        final Slider maxValueSlider = new Slider(MIN_FOR_EXERCISE, MAX_FOR_EXERCISE, 1, false, skin);
        final Label maxValueLabel = new Label(Integer.toString(MIN_FOR_EXERCISE), skin);
        maxValueSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Textures.slider.play();
                int value = (int) maxValueSlider.getValue();
                maxValueLabel.setText(value);
                MathGameScreen.setMaxForExercise(value);
            }
        });
        // BPM
        final Label lengthLabel = new Label("Length of an exercise", skin);
        final Slider lengthSlider = new Slider(MIN_FOR_EXERCISE_LENGTH, MAX_FOR_EXERCISE_LENGTH, 1, false, skin);
        final Label lengthValueLabel = new Label(Integer.toString(MIN_FOR_EXERCISE_LENGTH), skin);
        lengthSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Textures.slider.play();
                int value = (int) lengthSlider.getValue();
                lengthValueLabel.setText(value);
                MathGameScreen.setMaxlengthOfExercise(value);
            }
        });
        mathModeOptions.add(maxNumberLabel).padLeft(40);
        mathModeOptions.add(maxValueSlider).padLeft(120);
        mathModeOptions.add(maxValueLabel);
        mathModeOptions.row().pad(10);
        mathModeOptions.add(lengthLabel).padLeft(40);
        mathModeOptions.add(lengthSlider).padLeft(130);
        mathModeOptions.add(lengthValueLabel);
        mathModeOptions.row().pad(10);
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
        stage.getViewport().update(width, height, true);
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
