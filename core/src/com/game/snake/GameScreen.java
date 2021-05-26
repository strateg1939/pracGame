package com.game.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen implements Screen {

    final Main game;
    //better not change
    private static final int TILE_SIZE_IN_PIXELS = 23;
    OrthographicCamera camera;
    //currently does nothing
    private GameDifficulty gameDifficulty;
    //for pause
    private boolean isPaused;
    private Stage pauseStage;
    private OrthogonalTiledMapRenderer renderer;
    //amount of rows/columns
    private int tileRows = 10;
    private int tileColumns = 8;
    private int startX = 4;
    private int startY = 4;
    private int direction = 0;
    Food food;
    Snake snake;

    public GameScreen(final Main gam, GameDifficulty gameDifficulty) {
        this.game = gam;
        this.gameDifficulty = gameDifficulty;
        pauseStage = new Stage();
        System.out.println(gameDifficulty);
        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, tileRows, tileColumns);
        //load map
        TextureRegion[][] splitTilesDark = TextureRegion.split(new Texture(Gdx.files.internal("tiles2.png")), TILE_SIZE_IN_PIXELS, TILE_SIZE_IN_PIXELS);
        TextureRegion[][] splitTilesLight = TextureRegion.split(new Texture(Gdx.files.internal("tiles.png")), TILE_SIZE_IN_PIXELS, TILE_SIZE_IN_PIXELS);
        TiledMap map = new TiledMap();
        MapLayers layers = map.getLayers();

        TiledMapTileLayer layer = new TiledMapTileLayer(tileRows, tileColumns, TILE_SIZE_IN_PIXELS, TILE_SIZE_IN_PIXELS);
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
                if((x+y) % 2 == 0)
                    cell.setTile(new StaticTiledMapTile(splitTilesDark[ty][tx]));
                else cell.setTile(new StaticTiledMapTile(splitTilesLight[ty][tx]));
                layer.setCell(x, tileColumns - 1 - y, cell);
            }
        }
        layers.add(layer);
        float unitScale = 1/(float) TILE_SIZE_IN_PIXELS;
        renderer = new OrthogonalTiledMapRenderer(map, unitScale);
    }

    @Override
    public void render(float delta) {
        if (isPaused) {
            pause();
            return;
        }
        createFood();
        createSnake();
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
        game.batch.draw(food.getImage(), 2,3, 1,1);
        game.batch.draw(snake.getImage(), startX,startY, 1,1);
        //draw smth here


        //snake moves
        if (Gdx.input.isKeyPressed(Keys.UP)) {
            direction = 1;
            startY++;
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            game.batch.draw(snake.getImage(), startX,startY, 1,1);
        }
        if (Gdx.input.isKeyPressed(Keys.DOWN)) {
            direction = 2;
            startY--;
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            game.batch.draw(snake.getImage(), startX,startY, 1,1);
        }
        if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
            direction = 3;
            startX++;
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            game.batch.draw(snake.getImage(), startX,startY, 1,1);
        }
        if (Gdx.input.isKeyPressed(Keys.LEFT)) {
            direction = 4;
            startX--;
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            game.batch.draw(snake.getImage(), startX,startY, 1,1);
        }

/*
    if (Gdx.input.isKeyPressed(Keys.ANY_KEY)) {
        while (direction >= 1 && direction <= 4) {
            if (direction == 1) {
                startY++;
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                game.batch.draw(snake.getImage(), startX, startY, 1, 1);
            } else if (direction == 2) {
                startY--;
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                game.batch.draw(snake.getImage(), startX, startY, 1, 1);
            } else if (direction == 3) {
                startX++;
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                game.batch.draw(snake.getImage(), startX, startY, 1, 1);
            } else {
                startX--;
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                game.batch.draw(snake.getImage(), startX, startY, 1, 1);
            }
        }
    }*/

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
    //creates consumable items
    //call food.consume() when it should be consumed
    private void createFood() {
        food = new StandardFood();
        food.setOnConsume(new Food.Consumable() {
            @Override
            public void consume() {
                System.out.println("I was consumed");
            }
        });
    }

    private void createSnake(){
        snake = new SnakeDesign();
        snake.setOnConsume(new Food.Consumable() {
            @Override
            public void consume() {
                System.out.println("I was consumed");
            }
        });
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
