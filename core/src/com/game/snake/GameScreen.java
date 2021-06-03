package com.game.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.LinkedList;
import java.util.Random;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.ArrayList;

public class GameScreen implements Screen {
    public static final int WIDTH_IN_PIXELS = 1000;
    public static final int HEIGHT_IN_PIXELS = 680;
    final Main game;
    //better not change
    private static final int TILE_SIZE_IN_PIXELS = 23;
    //less == faster
    protected int snakeSpeed;
    //speed reduction gradually negated after eating speed reduce
    private int speedDelta;
    //all that is not a simple food
    protected float advancedFoodSpawnChance;
    OrthographicCamera camera;
    private GameDifficulty gameDifficulty;
    //for pause
    protected boolean isPaused;
    protected boolean isOver;
    private Stage pauseStage;
    private Stage finalStage;
    private OrthogonalTiledMapRenderer renderer;
    //amount of rows/columns
    protected int tileColumns;
    protected int tileRows;
    //snake parameters
    private int snakeTailFirstX;
    private int snakeTailFirstY;
    protected static int direction = 0;
    protected Food food;
    //used in creation of food
    protected static List<Class<? extends Food>> advancedFoodClasses =
            Collections.unmodifiableList(Arrays.asList(DoubleStandardFood.class, ReduceSpeedFood.class, ScoreTriplicate.class, ScoreDuplicate.class));
    protected int foodX;
    protected int foodY;
    Random rand = new Random();
    //Snake snake;
    public ArrayList<SnakeTail> snakeTails = new ArrayList<>();
    //!!!size of x and y is 1 bigger than tails
    //to properly add new pieces of snake when it eats
    LinkedList<Integer> X = new LinkedList<>();
    LinkedList<Integer> Y = new LinkedList<>();
    LinkedList<Integer> tailsDirections = new LinkedList<>();
    protected SnakeHead snakeHead;
    //timer on movement
    private long lastSnakeMovement;
    protected IntWrapper score;
    /**
     * score per changing the tile
     */
    private static final int SCORE_PER_TICK = 5;
    //timer for dupli/triplication
    private long lastScoreDuplication;
    //how long in milliseconds score is being dupli/triplicated
    //depends on difficulty
    private int MillisecondsForActiveScoreDuplication;
    private Stage effectsStage;
    protected Label scoreLabel;
    protected IntWrapper scoreMultiplier;
    private Texture labelForReducedSpeed;
    private Texture labelForMultiplication;
    protected BitmapFont fontForExercise;
    private boolean lockedInput;

    public GameScreen(final Main gam, GameDifficulty gameDifficulty) {
        this.game = gam;
        this.gameDifficulty = gameDifficulty;
        score = new IntWrapper(0);
        scoreMultiplier = new IntWrapper(0);
        //difficulty
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
        tileRows = game.tileRows;
        tileColumns = game.tileColumns;
        MillisecondsForActiveScoreDuplication = snakeSpeed * 20;
        lastScoreDuplication = 0;
        speedDelta = 0;
        //labels
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("OpenSans-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        int sizeOfFont = (int)( 30 * Math.pow(1/2.0, (tileRows - 25) / 10.0) );
        parameter.size = (sizeOfFont > 30) ? 30 : sizeOfFont;
        fontForExercise = generator.generateFont(parameter);
        generator.dispose();
        Label.LabelStyle style = new Label.LabelStyle(fontForExercise, Color.BLACK);
        scoreLabel = new Label("Your score is : " + score.value, style);
        scoreLabel.setSize(10,10);
        scoreLabel.setPosition(10,660);
        effectsStage = new Stage();
        effectsStage.addActor(scoreLabel);
        pauseStage = new Stage();
        finalStage = new Stage();
        //snake
        lastSnakeMovement = TimeUtils.millis();
        if(tileRows < 2) throw new RuntimeException("TO FEW COLUMNS");
        snakeHead = new SnakeHead(rand.nextInt(tileColumns), rand.nextInt(tileRows - 1));
        snakeTails.add(new SnakeTail(1,1));
        snakeTailFirstX = snakeHead.x;
        X.add(snakeTailFirstX);
        X.add(snakeTailFirstX);
        snakeTailFirstY = snakeHead.y + 1;
        Y.add(snakeTailFirstY);
        Y.add(snakeTailFirstY + 1);
        direction = 0;
        SnakeHead.setDefaultImage();
        tailsDirections.add(2);
        tailsDirections.add(2);
        //food
        do {
            foodX = rand.nextInt(tileColumns);
            foodY = rand.nextInt(tileRows);
        }
        while ((foodX == snakeHead.x || foodX == snakeTailFirstX) && (foodY == snakeHead.y || foodY == snakeTailFirstY));
        createFood();
        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, tileColumns, tileRows +1);
        //load map
        TextureRegion[][] splitTilesDark = TextureRegion.split(new Texture(Gdx.files.internal("tiles2.png")), TILE_SIZE_IN_PIXELS, TILE_SIZE_IN_PIXELS);
        TextureRegion[][] splitTilesLight = TextureRegion.split(new Texture(Gdx.files.internal("tiles.png")), TILE_SIZE_IN_PIXELS, TILE_SIZE_IN_PIXELS);
        TiledMap map = new TiledMap();
        MapLayers layers = map.getLayers();

        TiledMapTileLayer layer = new TiledMapTileLayer(tileColumns, tileRows, TILE_SIZE_IN_PIXELS, TILE_SIZE_IN_PIXELS);
        for (int x = 0; x < tileColumns; x++) {
            for (int y = 0; y < tileRows; y++) {
                int tx = 0;
                int ty = 0;
                if (x > 0) {
                    tx++;
                    if (x == tileColumns - 1) tx++;
                }
                if (y > 0) {
                    ty++;
                    if (y == tileRows - 1) ty++;
                }
                TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                if ((x + y) % 2 == 0)
                    cell.setTile(new StaticTiledMapTile(splitTilesDark[ty][tx]));
                else cell.setTile(new StaticTiledMapTile(splitTilesLight[ty][tx]));
                layer.setCell(x, tileRows - 1 - y, cell);
            }
        }
        layers.add(layer);
        float unitScale = 1 / (float) TILE_SIZE_IN_PIXELS;
        renderer = new OrthogonalTiledMapRenderer(map, unitScale);
        lockedInput = false;
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
        if(!lockedInput) {
            if (direction != 2 && Gdx.input.isKeyPressed(Keys.UP)) {
                direction = 1;
                lockedInput = true;
            } else if (direction != 1 && Gdx.input.isKeyPressed(Keys.DOWN)) {
                direction = 2;
                lockedInput = true;
            } else if (direction != 4 && Gdx.input.isKeyPressed(Keys.RIGHT)) {
                direction = 3;
                lockedInput = true;
            } else if (direction != 3 && Gdx.input.isKeyPressed(Keys.LEFT)) {
                direction = 4;
                lockedInput = true;
            }
            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                direction = (direction < 3) ? direction + 2 : 5 - direction;
                lockedInput = true;
            }
            if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
                lockedInput = true;
                direction = (direction < 3) ? 5 - direction : -2 + direction;
            }
        }


        if(direction == 1) SnakeHead.image  = new Texture(Gdx.files.internal("snakeHeadDown.png"));
        else if (direction == 2 || direction == 0) SnakeHead.image  = new Texture(Gdx.files.internal("snakeHeadUp.png"));
        else if(direction == 3) SnakeHead.image  = new Texture(Gdx.files.internal("snakeHeadLeft.png"));
        else SnakeHead.image = new Texture(Gdx.files.internal("snakeHeadRight.png"));
        game.batch.begin();

        if(food != null) game.batch.draw(food.getImage(), foodX, foodY, 1, 1);
        game.batch.draw(snakeHead.getImage(), snakeHead.x, snakeHead.y, 1, 1);
        for (int i = 0; i < snakeTails.size(); i++) {
            if(i==0) {
                if (tailsDirections.get(i) == 1) {
                    if (direction == 1 && tailsDirections.get(i+1) == 4) {
                        snakeTails.get(i).setImage(new Texture(Gdx.files.internal("snakeUpRight2.png")));
                    }else if (direction == 1 && tailsDirections.get(i+1) == 3) {
                        snakeTails.get(i).setImage(new Texture(Gdx.files.internal("snakeUpLeft.png")));
                    }else
                    snakeTails.get(i).setImage(new Texture(Gdx.files.internal("snakeLeft.png")));
                } else if (tailsDirections.get(i) == 2) {
                    if (direction == 2 && tailsDirections.get(i+1) == 3) {
                        snakeTails.get(i).setImage(new Texture(Gdx.files.internal("snakeLeftDown2.png")));
                    }else if (direction == 2 && tailsDirections.get(i+1) == 4) {
                        snakeTails.get(i).setImage(new Texture(Gdx.files.internal("snakeDownRight.png")));
                    }else
                        snakeTails.get(i).setImage(new Texture(Gdx.files.internal("snakeRight.png")));
                } else if (tailsDirections.get(i) == 3) {
                    if (direction == 3 && tailsDirections.get(i+1) == 1) {
                        snakeTails.get(i).setImage(new Texture(Gdx.files.internal("snakeDownRight2.png")));
                    }else if (direction == 3 && tailsDirections.get(i+1) == 2) {
                        snakeTails.get(i).setImage(new Texture(Gdx.files.internal("snakeLeftUp.png")));
                    }else
                        snakeTails.get(i).setImage(new Texture(Gdx.files.internal("snakeUp.png")));
                } else {
                    if (direction == 4 && tailsDirections.get(i+1) == 2) {
                        snakeTails.get(i).setImage(new Texture(Gdx.files.internal("snakeLeftUp2.png")));
                    }else if (direction == 4 && tailsDirections.get(i+1) == 1) {
                        snakeTails.get(i).setImage(new Texture(Gdx.files.internal("snakeRightDown.png")));
                    }else
                        snakeTails.get(i).setImage(new Texture(Gdx.files.internal("snakeDown.png")));
                }
            }else{
                if (tailsDirections.get(i) == 1) {
                    if (tailsDirections.get(i-1) == 1 && tailsDirections.get(i+1) == 4) {
                        snakeTails.get(i).setImage(new Texture(Gdx.files.internal("snakeUpRight2.png")));
                    }else if (tailsDirections.get(i-1) == 1 && tailsDirections.get(i+1) == 3) {
                        snakeTails.get(i).setImage(new Texture(Gdx.files.internal("snakeUpLeft.png")));
                    }else if (tailsDirections.get(i-1) == 3 && tailsDirections.get(i+1) == 4) {
                        snakeTails.get(i).setImage(new Texture(Gdx.files.internal("snakeUpRight2.png")));
                    }else if (tailsDirections.get(i-1) == 4 && tailsDirections.get(i+1) == 3) {
                        snakeTails.get(i).setImage(new Texture(Gdx.files.internal("snakeUpLeft.png")));
                    }else if (tailsDirections.get(i-1) == 3 && tailsDirections.get(i+1) == 3) {
                        snakeTails.get(i).setImage(new Texture(Gdx.files.internal("snakeUpLeft.png")));//new//
                    }else if (tailsDirections.get(i-1) == 4 && tailsDirections.get(i+1) == 4) {
                        snakeTails.get(i).setImage(new Texture(Gdx.files.internal("snakeUpRight2.png")));//new//
                    }else
                    snakeTails.get(i).setImage(new Texture(Gdx.files.internal("snakeLeft.png")));
                } else if (tailsDirections.get(i) == 2) {
                    if (tailsDirections.get(i-1) == 2 && tailsDirections.get(i+1) == 3) {
                       snakeTails.get(i).setImage(new Texture(Gdx.files.internal("snakeLeftDown2.png")));
                    }else if (tailsDirections.get(i-1) == 2 && tailsDirections.get(i+1) == 4) {
                        snakeTails.get(i).setImage(new Texture(Gdx.files.internal("snakeDownRight.png")));
                    }else if (tailsDirections.get(i-1) == 4 && tailsDirections.get(i+1) == 3) {
                        snakeTails.get(i).setImage(new Texture(Gdx.files.internal("snakeLeftDown2.png")));
                    }else if (tailsDirections.get(i-1) == 3 && tailsDirections.get(i+1) == 4) {
                        snakeTails.get(i).setImage(new Texture(Gdx.files.internal("snakeDownRight.png")));
                    }else if (tailsDirections.get(i-1) == 3 && tailsDirections.get(i+1) == 3) {
                        snakeTails.get(i).setImage(new Texture(Gdx.files.internal("snakeLeftDown2.png")));//new//
                    }else if (tailsDirections.get(i-1) == 4 && tailsDirections.get(i+1) == 4) {
                        snakeTails.get(i).setImage(new Texture(Gdx.files.internal("snakeDownRight.png")));//new//
                    }else
                    snakeTails.get(i).setImage(new Texture(Gdx.files.internal("snakeRight.png")));
                } else if (tailsDirections.get(i) == 3) {
                    if (tailsDirections.get(i-1) == 3 && tailsDirections.get(i+1) == 1) {
                        snakeTails.get(i).setImage(new Texture(Gdx.files.internal("snakeDownRight2.png")));
                    }else if (tailsDirections.get(i-1) == 3 && tailsDirections.get(i+1) == 2) {
                        snakeTails.get(i).setImage(new Texture(Gdx.files.internal("snakeLeftUp.png")));
                    }else if (tailsDirections.get(i-1) == 1 && tailsDirections.get(i+1) == 2) {
                        snakeTails.get(i).setImage(new Texture(Gdx.files.internal("snakeLeftUp.png")));
                    }else if (tailsDirections.get(i-1) == 2 && tailsDirections.get(i+1) == 1) {
                        snakeTails.get(i).setImage(new Texture(Gdx.files.internal("snakeDownRight2.png")));
                    }else if (tailsDirections.get(i-1) == 1 && tailsDirections.get(i+1) == 1) {
                        snakeTails.get(i).setImage(new Texture(Gdx.files.internal("snakeDownRight2.png")));//new//
                    }else if (tailsDirections.get(i-1) == 2 && tailsDirections.get(i+1) == 2) {
                        snakeTails.get(i).setImage(new Texture(Gdx.files.internal("snakeLeftUp.png")));//new//
                    }else
                    snakeTails.get(i).setImage(new Texture(Gdx.files.internal("snakeUp.png")));
                } else {
                    if (tailsDirections.get(i-1) == 4 && tailsDirections.get(i+1) == 2) {
                        snakeTails.get(i).setImage(new Texture(Gdx.files.internal("snakeLeftUp2.png")));
                    }else if (tailsDirections.get(i-1) == 4 && tailsDirections.get(i+1) == 1) {
                        snakeTails.get(i).setImage(new Texture(Gdx.files.internal("snakeRightDown.png")));
                    }else if (tailsDirections.get(i-1) == 2 && tailsDirections.get(i+1) == 1) {
                        snakeTails.get(i).setImage(new Texture(Gdx.files.internal("snakeRightDown.png")));
                    }else if (tailsDirections.get(i-1) == 1 && tailsDirections.get(i+1) == 2) {
                          snakeTails.get(i).setImage(new Texture(Gdx.files.internal("snakeLeftUp2.png")));
                    }else if (tailsDirections.get(i-1) == 1 && tailsDirections.get(i+1) == 1) {
                        snakeTails.get(i).setImage(new Texture(Gdx.files.internal("snakeRightDown.png")));//new
                    }else if (tailsDirections.get(i-1) == 2 && tailsDirections.get(i+1) == 2) {
                        snakeTails.get(i).setImage(new Texture(Gdx.files.internal("snakeDownRight.png")));//new
                    }else
                    snakeTails.get(i).setImage(new Texture(Gdx.files.internal("snakeDown.png")));
                }
            }
                game.batch.draw(snakeTails.get(i).getImage(), X.get(i), Y.get(i), 1, 1);


        }
        if(labelForMultiplication != null) game.batch.draw(labelForMultiplication, tileColumns - 2, tileRows, 1,1);
        if(labelForReducedSpeed != null) game.batch.draw(labelForReducedSpeed, tileColumns - 1, tileRows, 1,1);
        game.batch.end();
        effectsStage.draw();
        //draw smth here
        //input

        //snake moves
        if (TimeUtils.millis() - lastSnakeMovement > snakeSpeed + speedDelta) {
            scoreLabel.setText("Your score is : " + score.value);
            lockedInput = false;
            lastSnakeMovement = TimeUtils.millis();
            if (direction <= 4 && direction >= 1) {
                if(TimeUtils.millis() - lastScoreDuplication > MillisecondsForActiveScoreDuplication) {
                    lastScoreDuplication = 0;
                    score.value += SCORE_PER_TICK * snakeTails.size();
                    labelForMultiplication = null;
                }
                else{
                    score.value += SCORE_PER_TICK * snakeTails.size() * scoreMultiplier.value;
                }
                if(speedDelta > 0) speedDelta -= 10;
                else labelForReducedSpeed = null;
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
                checkForFood();
            }
            //check if head collides with body
            for(int i = 0; i < snakeTails.size(); i++){
                if(snakeHead.x == X.get(i) && snakeHead.y == Y.get(i)){
                    createFinalScreen("You have collided with yourself");
                }
            }
            //check if head collides with borders
            if(snakeHead.x < 0 || snakeHead.x > tileColumns - 1 || snakeHead.y > tileRows - 1 || snakeHead.y < 0){
                createFinalScreen("You have collided with borders");
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
    /**
    check if snake is eating foor
     */
    protected void checkForFood() {
        if(snakeHead.x == foodX && snakeHead.y == foodY) {
            food.consume(score, scoreMultiplier);
            //to not spawn food in the same tile as snake head
            while (true) {
                foodX = rand.nextInt(tileColumns);
                foodY = rand.nextInt(tileRows);
                if(foodX == snakeHead.x && foodY == snakeHead.y) continue;
                break;
            }
            createFood();
        }
        else{
            //move snake
            moveSnake();
        }
    }

    /**
     * move snake by 1 tile
     */
    protected void moveSnake() {
        X.addFirst(snakeTailFirstX);
        Y.addFirst(snakeTailFirstY);
        Y.removeLast();
        X.removeLast();
        tailsDirections.addFirst(direction);
        tailsDirections.removeLast();
    }

    protected void createFinalScreen(String finalMessage) {
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
                System.exit(0);
            }
        });
        toMainMenuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenuScreen(game));
                finalStage.dispose();
                GameScreen.this.dispose();
                direction = 0;
            }
        });
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("OpenSans-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 30;
        BitmapFont font12 = generator.generateFont(parameter);
        generator.dispose();
        Label.LabelStyle style = new Label.LabelStyle(font12, Color.WHITE);
        Label finalScoreLabel = new Label("Your final score is : " + score.value, style);
        finalScoreLabel.setStyle(style);
        finalScoreLabel.setPosition(300, 450);
        Label finalMessageLabel = new Label(finalMessage, style);
        finalMessageLabel.setPosition(250, 550);
        finalStage.addActor(finalScoreLabel);
        finalStage.addActor(exitButton);
        finalStage.addActor(toMainMenuButton);
        finalStage.addActor(finalMessageLabel);
        finalStage.draw();
        Record record = new Record(score.value, gameDifficulty, "Map`s size was " + tileColumns + "x" + tileRows,
                this instanceof MathGameScreen, game.userName, snakeTails.size() + 1);
        game.records.add(record);
    }
    protected void showFinalScreen(){
        finalStage.draw();
    }
    /**
    creates food
    call food.consume() when it should be consumed
     */
    protected void createFood() {
        if(Math.random() < advancedFoodSpawnChance){
            try {
                food = advancedFoodClasses.get(rand.nextInt(advancedFoodClasses.size())).getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
            if(food instanceof ReduceSpeedFood) food.setOnConsume(getReducedSpeedEffect());
            else if(food instanceof DoubleStandardFood) food.setOnConsume(getDoubleInstanceEffect());
            else if (food instanceof ScoreDuplicate) food.setOnConsume(getScoreDuplicationEffect());
            else if (food instanceof ScoreTriplicate) food.setOnConsume(getScoreTriplicationEffect());
        }
        else {
            food = new StandardFood();
            food.setOnConsume(getStandardEffect());
        }
    }

    //set different effects for different food
    private Food.Consumable getStandardEffect(){
        return new Food.Consumable() {
            @Override
            public void consume() {
                snakeTails.add(new SnakeTail(1,1));
                X.addFirst(snakeTailFirstX);
                Y.addFirst(snakeTailFirstY);
                tailsDirections.addFirst(direction);
            }
        };
    }
    private Food.Consumable getScoreTriplicationEffect() {
        return new Food.Consumable() {
            @Override
            public void consume() {
                moveSnake();
                lastScoreDuplication = TimeUtils.millis();
                labelForMultiplication = new ScoreTriplicate().getImage();
            }
        };
    }

    private Food.Consumable getScoreDuplicationEffect() {
        return new Food.Consumable() {
            @Override
            public void consume() {
                moveSnake();
                lastScoreDuplication = TimeUtils.millis();
                labelForMultiplication = new ScoreDuplicate().getImage();
            }
        };
    }

    private Food.Consumable getDoubleInstanceEffect() {
        return new Food.Consumable() {
            @Override
            public void consume() {
                snakeTails.add(new SnakeTail(1,1));
                X.addFirst(snakeTailFirstX);
                Y.addFirst(snakeTailFirstY);
                snakeTails.add(new SnakeTail(1,1));
                X.addLast(X.getLast() - 1);
                Y.addLast(Y.getLast() - 1);
                tailsDirections.addFirst(direction);
                tailsDirections.addLast(1);
            }
        };
    }

    private Food.Consumable getReducedSpeedEffect() {
        return new Food.Consumable() {
            @Override
            public void consume() {
                speedDelta = 200;
                moveSnake();
                labelForReducedSpeed = new ReduceSpeedFood().getImage();
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
                direction = 0;
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

    /**
     * create button
     * @param buttonText text
     * @param positionY X position is equal for all buttons
     * @return
     */
    private Button getSettingsButton(String buttonText, float positionY) {
        Skin skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));
        TextButton button = new TextButton(buttonText, skin);
        button.setSize(300, 100);
        button.setPosition(300, positionY);
        return button;
    }

}
