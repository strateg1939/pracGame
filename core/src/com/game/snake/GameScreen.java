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
import com.badlogic.gdx.utils.TimeUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class GameScreen implements Screen {

    final Main game;
    //better not change
    private static final int TILE_SIZE_IN_PIXELS = 23;
    //less == faster
    private int snakeSpeed;
    private int speedDelta;
    private float advancedFoodSpawnChance;
    OrthographicCamera camera;
    //currently does nothing
    private GameDifficulty gameDifficulty;
    //for pause
    private boolean isPaused;
    private boolean isOver;
    private Stage pauseStage;
    private Stage finalStage;
    private OrthogonalTiledMapRenderer renderer;
    //amount of rows/columns
    private int tileRows = 10;
    private int tileColumns = 12;
    //snake parameters
    private int snakeTailFirstX;
    private int snakeTailFirstY;
    private int direction = 0;
    Food food;
    private static final List<Class<? extends Food>> advancedFoodClasses =
            Collections.unmodifiableList(Arrays.asList(DoubleStandardFood.class, ReduceSpeedFood.class));
    private int foodX = 2;
    private int foodY = 2;
    Random rand = new Random();
    //Snake snake;
    ArrayList<Snake> snakeTails = new ArrayList<>();
    //!!!size of x and y is 1 bigger than tails
    //to properly add new pieces of snake when it eats
    LinkedList<Integer> X = new LinkedList<>();
    LinkedList<Integer> Y = new LinkedList<>();
    SnakeHead snakeHead;
    private long lastSnakeMovement;


    public GameScreen(final Main gam, GameDifficulty gameDifficulty) {
        this.game = gam;
        this.gameDifficulty = gameDifficulty;
        switch(gameDifficulty){
            case EASY:
                snakeSpeed = 500;
                advancedFoodSpawnChance = 0.5f;
                break;
            case MEDIUM:
                snakeSpeed = 350;
                advancedFoodSpawnChance = 0.3f;
                break;
            case HARD:
                snakeSpeed = 200;
                advancedFoodSpawnChance = 0.15f;
                break;
        }
        speedDelta = 0;
        pauseStage = new Stage();
        finalStage = new Stage();
        lastSnakeMovement = TimeUtils.millis();
        if(tileColumns < 2) throw new RuntimeException("TO FEW COLUMNS");
        snakeHead = new SnakeHead(rand.nextInt(tileRows), rand.nextInt(tileColumns - 1));
        System.out.println(snakeHead.x);
        System.out.println(snakeHead.y);
        snakeTails.add(new SnakeTail());
        snakeTailFirstX = snakeHead.x;
        X.add(snakeTailFirstX);
        System.out.println(X);
        X.add(snakeTailFirstX);
        snakeTailFirstY = snakeHead.y + 1;
        Y.add(snakeTailFirstY);
        Y.add(snakeTailFirstY + 1);
        System.out.println(Y);
        createFood();
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
                if (x > 0) {
                    tx++;
                    if (x == tileRows - 1) tx++;
                }
                if (y > 0) {
                    ty++;
                    if (y == tileColumns - 1) ty++;
                }
                TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                if ((x + y) % 2 == 0)
                    cell.setTile(new StaticTiledMapTile(splitTilesDark[ty][tx]));
                else cell.setTile(new StaticTiledMapTile(splitTilesLight[ty][tx]));
                layer.setCell(x, tileColumns - 1 - y, cell);
            }
        }
        layers.add(layer);
        float unitScale = 1 / (float) TILE_SIZE_IN_PIXELS;
        renderer = new OrthogonalTiledMapRenderer(map, unitScale);
    }

    @Override
    public void render(float delta) {
        if (isPaused) {
            pause();
            return;
        }
        if(isOver){
            showFinalScreen();
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

        game.batch.draw(food.getImage(), foodX, foodY, 1, 1);
        game.batch.draw(snakeHead.getImage(), snakeHead.x, snakeHead.y, 1, 1);
        for (int i = 0; i < snakeTails.size(); i++) {
            game.batch.draw(snakeTails.get(i).getImage(), X.get(i), Y.get(i), 1, 1);
        }
        game.batch.draw(food.getImage(), foodX, foodY, 1, 1);
        game.batch.end();
        //draw smth here
        //input
        if (direction != 2) {
            if (Gdx.input.isKeyPressed(Keys.UP)) {
                direction = 1;
                //  snakeTailFirstY++;
            }
        }
        if (direction != 1) {
            if (Gdx.input.isKeyPressed(Keys.DOWN)) {
                direction = 2;
                //  snakeTailFirstY--;
            }
        }
        if (direction != 4) {
            if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
                direction = 3;
                //  snakeTailFirstX++;
            }
        }
        if (direction != 3) {
            if (Gdx.input.isKeyPressed(Keys.LEFT)) {
                direction = 4;
                // snakeTailFirstX--;
            }
        }
        //snake moves
        if (TimeUtils.millis() - lastSnakeMovement > snakeSpeed + speedDelta) {
            if(speedDelta > 0) speedDelta -= 10;
            lastSnakeMovement = TimeUtils.millis();
            if (direction <= 4 && direction >= 1) {
                snakeTailFirstX = snakeHead.x;
                snakeTailFirstY = snakeHead.y;
                if (direction == 1) {
                    snakeHead.y++;
                }
                else if (direction == 2) {
                    snakeHead.y--;
                }
                else if (direction == 3) {
                    snakeHead.x++;
                }
                else{
                    snakeHead.x--;
                }
                //to properly add new snakes first check for food then if no food move
                if(snakeHead.x == foodX && snakeHead.y == foodY) {
                    food.consume();
                    foodX = rand.nextInt(tileRows);
                    foodY = rand.nextInt(tileColumns);
                    createFood();
                }
                else{
                    //move snake
                    moveSnake();
                }
            }
            //check if head collides with body
            for(int i = 0; i < snakeTails.size(); i++){
                if(snakeHead.x == X.get(i) && snakeHead.y == Y.get(i)){
                    System.out.println("Game over");
                    showFinalScreen();
                }
            }
            //check if head collides with borders
            if(snakeHead.x < 0 || snakeHead.x > tileRows - 1 || snakeHead.y > tileColumns - 1 || snakeHead.y < 0){
                System.out.println("Game over");
                System.out.println(snakeHead.x);
                System.out.println(snakeHead.y);
                showFinalScreen();
            }
        }

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

    private void moveSnake() {
        X.addFirst(snakeTailFirstX);
        Y.addFirst(snakeTailFirstY);
        Y.removeLast();
        X.removeLast();
    }

    private void showFinalScreen() {
        isOver = true;
        Gdx.input.setInputProcessor(finalStage);
        Button toMainMenuButton = getSettingsButton("To Menu", 300);
        Button exitButton = getSettingsButton("Exit", 150);
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                finalStage.dispose();
                GameScreen.this.dispose();
                game.dispose();
            }
        });
        toMainMenuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenuScreen(game));
                finalStage.dispose();
                GameScreen.this.dispose();
            }
        });
        finalStage.addActor(exitButton);
        finalStage.addActor(toMainMenuButton);
        finalStage.draw();
    }

    //creates consumable items
    //call food.consume() when it should be consumed
    private void createFood() {
        if(Math.random() < advancedFoodSpawnChance){
            try {
                food = advancedFoodClasses.get(rand.nextInt(advancedFoodClasses.size())).getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
            if(food instanceof ReduceSpeedFood) food.setOnConsume(getReducedSpeedEffect());
            else if(food instanceof DoubleStandardFood) food.setOnConsume(getDoubleInstanceEffect());
        }
        else {
            food = new StandardFood();
            food.setOnConsume(new Food.Consumable() {
                @Override
                public void consume() {
                    snakeTails.add(new SnakeTail());
                    X.addFirst(snakeTailFirstX);
                    Y.addFirst(snakeTailFirstY);
                }
            });
        }
    }

    private Food.Consumable getDoubleInstanceEffect() {
        return new Food.Consumable() {
            @Override
            public void consume() {
                snakeTails.add(new SnakeTail());
                X.addFirst(snakeTailFirstX);
                Y.addFirst(snakeTailFirstY);
                snakeTails.add(new SnakeTail());
                X.addLast(X.getLast() - 1);
                Y.addLast(Y.getLast() - 1);
            }
        };
    }

    private Food.Consumable getReducedSpeedEffect() {
        return new Food.Consumable() {
            @Override
            public void consume() {
                speedDelta = 100;
                moveSnake();
            }
        };
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
        final Button resumeButton = getSettingsButton("Resume", 450);
        Button toMainMenuButton = getSettingsButton("To Menu", 300);
        Button exitButton = getSettingsButton("Exit", 150);
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                pauseStage.dispose();
                GameScreen.this.dispose();
                game.dispose();
            }
        });
        resumeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                pauseStage.clear();
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                isPaused = false;
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
        button.setPosition(400, positionY);
        return button;
    }

}
