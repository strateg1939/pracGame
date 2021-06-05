package com.game.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import static com.game.snake.GameScreen.*;

public class ChooseDifficultyScreen implements Screen {
    final Main game;
    private OrthographicCamera camera;
    private Stage stage;
    private boolean isMathMode;
    BitmapFont font12;

    public ChooseDifficultyScreen(final Main gam, boolean isMathMode) {
        this.isMathMode = isMathMode;
        game = gam;
        stage = new Stage(new StretchViewport(Main.WORLD_WIDTH, Main.WORLD_HEIGHT));
        Gdx.input.setInputProcessor(stage);
        Skin skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));
        //creates buttons for difficulty choosing
        stage.addActor(getDefaultButtonForThisScreen("Easy", 350, GameDifficulty.EASY, skin));
        stage.addActor(getDefaultButtonForThisScreen("Medium", 200, GameDifficulty.MEDIUM, skin));
        stage.addActor(getDefaultButtonForThisScreen("Hard", 50, GameDifficulty.HARD, skin));
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Main.WORLD_WIDTH, Main.WORLD_HEIGHT);
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("OpenSans-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 30;
        font12 = generator.generateFont(parameter);
        generator.dispose();
    }
    //returns TextButton with size 200,100; x= 300, skin from assets and on click start of game
    private Button getDefaultButtonForThisScreen(String buttonText, float positionY, final GameDifficulty gameDifficulty, Skin skin){
        TextButton button = new TextButton(buttonText, skin);
        button.setSize(200, 100);
        button.setPosition(400, positionY);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                    GameScreen.buttonS.play();
                    if(gameDifficulty.equals(GameDifficulty.EASY)){
                        music = Gdx.audio.newSound(Gdx.files.internal("dE.mp3"));
                        currentTiles=tiles;
                        currentTiles2=tiles2;
                    }else if(gameDifficulty.equals(GameDifficulty.MEDIUM)){
                        music = Gdx.audio.newSound(Gdx.files.internal("dM.mp3"));
                        currentTiles=tiles_2;
                        currentTiles2=tiles2_2;
                    }else if(gameDifficulty.equals(GameDifficulty.HARD)){
                        music = Gdx.audio.newSound(Gdx.files.internal("dH.mp3"));
                        currentTiles=tiles_3;
                        currentTiles2=tiles2_3;
                    }
                game.setScreen((isMathMode) ? new MathGameScreen(game, gameDifficulty) : new GameScreen(game, gameDifficulty));
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
        font12.draw(game.batch, "Choose difficulty setting", 330, 550);
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
