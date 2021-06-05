package com.game.snake;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import java.util.ArrayList;

/**
 * screen that defines math game mode
 * extends game screen
 * almost all logic is done in overridden createFood
 *
 */
public class MathGameScreen extends GameScreen{
    //time in seconds to solve exercise. Less with increased difficulty
    private static int maxTimerInSeconds = 35;
    //actual timer
    private long timerInMillis;
    private ArrayList<MathFood> answers;
    private Stage mathAnswersStage;
    private static int maxForExercise = 400;
    //min is 0
    private static int maxlengthOfExercise = 5;
    private String exercise;
    private Label exerciseLabel;
    private Label timerLabel;
    int result;
    public MathGameScreen(Main gam, GameDifficulty gameDifficulty) {
        super(gam, gameDifficulty);
        advancedFoodSpawnChance = 1f;
        snakeSpeed = 350;
        switch(gameDifficulty){
            case EASY:
                maxTimerInSeconds = 35;
                break;
            case MEDIUM:
                maxTimerInSeconds = 25;
                break;
            case HARD:
                maxTimerInSeconds = 20;
                break;
        }
        foodX = tileColumns + 10;
        foodY = tileRows + 10;
        timerInMillis = TimeUtils.millis() + maxTimerInSeconds * 1000;
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        //update the time that is left for the player to answer the question
        if(!isOver && !isPaused) {
            timerLabel.setText(Long.toString((timerInMillis - TimeUtils.millis()) / 1000));
            if (timerInMillis <= TimeUtils.millis()) {
                createFinalScreen("Game over! Time is up");
                return;
            }
        }
        game.batch.begin();
        mathAnswersStage.draw();
        showFinalScreen();
        game.batch.end();
    }

    /**
     * overridden to check for multiple food items
     */
    @Override
    protected void checkForFood() {
        for (MathFood food:
             answers) {
            if(snakeHead.x == food.x && snakeHead.y == food.y) {
                food.consume(score, scoreMultiplier);
                if(!isOver)
                    createFood();
                return;
            }
        }
        moveSnake();
    }

    /**
     * creates new exercise and all the random answers for it
     * also creates stage / labels because this method is called before this screen`s constructor
     */
    protected void createFood(){
        if(mathAnswersStage == null) mathAnswersStage = new Stage(new StretchViewport(Main.WORLD_WIDTH, Main.WORLD_HEIGHT));

        mathAnswersStage.clear();
        answers = new ArrayList<>();
        result = generateExercise();
        for (int i = 0; i < rand.nextInt(3) + 3; i++) {
            final MathFood mathFood = new MathFood();
            while (true) {
                mathFood.x = generateRandomInBounds(0, tileColumns);
                mathFood.y = generateRandomInBounds(0, tileRows);
                boolean isOverlapping = false;
                for (int j = 0; j < answers.size(); j++) {
                    if(answers.get(j).x == mathFood.x && answers.get(j).y == mathFood.y) isOverlapping = true;
                }
                if((mathFood.x == snakeHead.x && mathFood.y == snakeHead.y) || isOverlapping) continue;
                break;
            }
            if(i == 0) mathFood.answer = result;
            else mathFood.answer = generateRandomInBounds(result / 2, result * 2 + 3);
            mathFood.setOnConsume(new Food.Consumable() {
                @Override
                public void consume() {
                    if(mathFood.answer != result) {
                        createFinalScreen("Your answer was wrong. Actual answer is " + result);
                        return;
                    }
                    moveSnake();
                }
            });
            answers.add(mathFood);
        }
        Label.LabelStyle style;
        if(gameDifficulty == GameDifficulty.HARD){
             style = new Label.LabelStyle(fontForExercise, Color.WHITE);
        }else {
             style = new Label.LabelStyle(fontForExercise, Color.BLACK);
        }
        exerciseLabel = new Label(exercise, style);
        exerciseLabel.setSize(10, 10);
        exerciseLabel.setPosition(scoreLabel.getMaxWidth() + 300, 660);
        mathAnswersStage.addActor(exerciseLabel);
        for (MathFood answer:
             answers) {
            Label answerLabel = new Label(Integer.toString(answer.answer), style);
            answerLabel.setPosition((answer.x + 0.2f) * Main.WORLD_WIDTH / tileColumns, answer.y * Main.WORLD_HEIGHT / (tileRows + 1));
            mathAnswersStage.addActor(answerLabel);
        }
        timerInMillis = TimeUtils.millis() + maxTimerInSeconds * 1000;
        timerLabel = new Label(Integer.toString(maxForExercise), style);
        timerLabel.setSize(10, 10);
        timerLabel.setPosition(800, 660);
        mathAnswersStage.addActor(timerLabel);

    }

    /**
     * generate random int in bounds (inclusive)
     * @param min
     * @param max
     * @param toExclude number to exclude from generation
     * @return
     */
    private int generateRandomInBounds(int min, int max, int toExclude) {
        int result = toExclude;
        while (result == toExclude){
            result = rand.nextInt(max - min) + min;
        }
        return result;
    }
    private int generateRandomInBounds(int min, int max){
       return generateRandomInBounds(min, max, Integer.MAX_VALUE);
    }

    /**
     * generate math exercise
     * String exercise is changed
     * @return answer to that exercise
     */
    private int generateExercise() {
        int result = rand.nextInt(maxForExercise);
        exercise = result + " ";
        //length is random
        int length = rand.nextInt(maxlengthOfExercise) + 1;
        for (int i = 0; i < length; i++) {
            result = generateNextStep(result);
        }
        exercise += "= ?";
        return result;
    }

    /**
     * procedurally generated next step for exercise
     * @param firstOperand result from previous generations
     * @return result for this generation
     */
    private int generateNextStep(int firstOperand) {
        boolean isPlus = rand.nextBoolean();
        exercise += (isPlus) ? "+ " : "- ";
        int secondOperand;
        if (isPlus)
            secondOperand = rand.nextInt(maxForExercise - firstOperand + 1);
        else
            secondOperand = rand.nextInt(firstOperand + 1);
        exercise += secondOperand + " ";
        return (isPlus) ? firstOperand + secondOperand : firstOperand - secondOperand;
    }

    @Override
    protected void createFinalScreen(String finalMessage) {
        super.createFinalScreen(finalMessage);
    }

    public static void setMaxForExercise(int maxForExercise) {
        MathGameScreen.maxForExercise = maxForExercise;
    }

    public static void setMaxlengthOfExercise(int maxlengthOfExercise) {
        MathGameScreen.maxlengthOfExercise = maxlengthOfExercise;
    }
}
