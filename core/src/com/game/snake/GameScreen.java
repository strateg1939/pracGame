package com.game.snake;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

public class GameScreen implements Screen {

    final Main game;

    OrthographicCamera camera;
    //currently does nothing
    private GameDifficulty gameDifficulty;
    private boolean isPaused;
    private Stage pauseStage;
    private OrthogonalTiledMapRenderer renderer;
    //amount of rows/columns
    private int tileRows = 15;
    private int tileColumns = 10;

    public GameScreen(final Main gam, GameDifficulty gameDifficulty) {
        this.game = gam;
        this.gameDifficulty = gameDifficulty;
        pauseStage = new Stage();
        System.out.println(gameDifficulty);
        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, tileRows, tileColumns);


        Texture tiles = new Texture(Gdx.files.internal("tiles2.png"));
        TextureRegion[][] splitTiles = TextureRegion.split(tiles, 23, 23);
        TiledMap map = new TiledMap();
        MapLayers layers = map.getLayers();

        TiledMapTileLayer layer = new TiledMapTileLayer(tileRows, tileColumns, 23, 23);
        for (int x = 0; x < tileRows; x++) {
            for (int y = 0; y < tileColumns; y++) {
                int tx = 0;
                int ty = 0;
                if(x > 0){
                    tx++;
                    if(x == tileRows - 1) tx++;
                }
                if(y > 0){
                    ty++;
                    if(y == tileColumns - 1) ty++;
                }
                TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                cell.setTile(new StaticTiledMapTile(splitTiles[ty][tx]));
                layer.setCell(x, tileColumns - 1 -y, cell);
            }
        }
        layers.add(layer);
        float unitScale = 1/23f;
        renderer = new OrthogonalTiledMapRenderer(map, unitScale);
    }

    @Override
    public void render(float delta) {
        if (isPaused) {
            pause();
            return;
        }
        ScreenUtils.clear(51f / 255f, 123f / 255f, 250f / 255f, 1f);
        camera.update();
        renderer.setView(camera);
        renderer.render();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        game.batch.setProjectionMatrix(camera.combined);

        // begin a new batch and draw the bucket and
        // all drops
        game.batch.begin();
        //draw smth here
        game.batch.end();

        // process user input
        //set pause on Space
        if (Gdx.input.isKeyPressed(Keys.SPACE)) {
            isPaused = true;
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
        // start the playback of the background music
        // when the screen is shown
        //       rainMusic.play();
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
        if (Gdx.input.isKeyPressed(Keys.SPACE)) {
            pauseStage.clear();
            isPaused = false;
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }
        Gdx.input.setInputProcessor(pauseStage);
        Button resumeButton = getSettingsButton("Resume", 350);
        Button toMainMenuButton = getSettingsButton("To Menu", 200);
        Button exitButton = getSettingsButton("Exit", 50);
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                pauseStage.dispose();
                GameScreen.this.dispose();
            }
        });
        resumeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                isPaused = false;
                pauseStage.clear();
            }
        });
        toMainMenuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenuScreen(game));
                pauseStage.dispose();
                GameScreen.this.dispose();
            }
        });

        pauseStage.addActor(resumeButton);
        pauseStage.addActor(exitButton);
        pauseStage.addActor(toMainMenuButton);
        pauseStage.draw();
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        //       dropSound.dispose();
        //       rainMusic.dispose();
    }

    private Button getSettingsButton(String buttonText, float positionY) {
        Skin skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));
        TextButton button = new TextButton(buttonText, skin);
        button.setSize(200, 100);
        button.setPosition(300, positionY);
        return button;
    }

}
