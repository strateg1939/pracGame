package com.game.snake;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class MathGameScreen extends GameScreen{
    private static int maxTimerInSeconds = 35;
    private long timerInMillis;
    private ArrayList<MathFood> answers;
    private Stage mathAnswersStage;
    private static int maxForExercise = 15;
    //min is 0
    private static int maxlengthOfExercise = 5;
    private String exercise;
    private Label exerciseLabel;
    private Label timerLabel;
    public MathGameScreen(Main gam, GameDifficulty gameDifficulty) {
        super(gam, gameDifficulty);
        advancedFoodClasses = Collections.unmodifiableList(Arrays.asList(MathFood.class));
        advancedFoodSpawnChance = 1f;
        snakeSpeed = 350;
        switch(gameDifficulty){
            case EASY:
                maxTimerInSeconds = 45;
                break;
            case MEDIUM:
                maxTimerInSeconds = 35;
                break;
            case HARD:
                maxTimerInSeconds = 25;
                break;
        }
        foodX = tileRows + 10;
        foodY = tileColumns + 10;
        timerInMillis = TimeUtils.millis() + maxTimerInSeconds * 1000;
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        timerLabel.setText(Long.toString((timerInMillis - TimeUtils.millis()) / 1000));
        if(timerInMillis <= TimeUtils.millis()) showFinalScreen();
        game.batch.begin();
        mathAnswersStage.draw();
        game.batch.end();
    }

    @Override
    protected void checkForFood() {
        for (MathFood food:
             answers) {
            if(snakeHead.x == food.x && snakeHead.y == food.y) {
                food.consume(score, scoreMultiplier);
                createFood();
            }
        }
        moveSnake();
    }

    protected void createFood(){
        if(mathAnswersStage == null) mathAnswersStage = new Stage();
        mathAnswersStage.clear();

        answers = new ArrayList<>();
        final int result = generateExercise();
        for (int i = 0; i < rand.nextInt(3) + 3; i++) {
            final MathFood mathFood = new MathFood();
            mathFood.x = rand.nextInt(tileRows);
            mathFood.y = rand.nextInt(tileColumns);
            if(i == 0) mathFood.answer = result;
            else mathFood.answer = generateRandomInBounds(result / 2, result * 2 + 2, result);
            mathFood.setOnConsume(new Food.Consumable() {
                @Override
                public void consume() {
                    moveSnake();
                    if(mathFood.answer != result) showFinalScreen();
                }
            });
            answers.add(mathFood);
        }
        Label.LabelStyle style = new Label.LabelStyle(new BitmapFont(), Color.BLACK);
        exerciseLabel = new Label(exercise, style);
        exerciseLabel.setSize(10, 10);
        exerciseLabel.setPosition(scoreLabel.getMaxWidth() + 200, 650);
        mathAnswersStage.addActor(exerciseLabel);
        for (MathFood answer:
             answers) {
            Label answerLabel = new Label(Integer.toString(answer.answer), style);
            answerLabel.setSize(GameScreen.WIDTH_IN_PIXELS / tileRows, GameScreen.HEIGHT_IN_PIXELS / (tileColumns + 1));
            answerLabel.setPosition(answer.x * GameScreen.WIDTH_IN_PIXELS / tileRows + 10, answer.y * GameScreen.HEIGHT_IN_PIXELS / (tileColumns + 1));
            mathAnswersStage.addActor(answerLabel);
        }
        timerInMillis = TimeUtils.millis() + maxTimerInSeconds * 1000;
        timerLabel = new Label(Integer.toString(maxForExercise), style);
        timerLabel.setSize(10, 10);
        timerLabel.setPosition(800, 650);
        mathAnswersStage.addActor(timerLabel);

    }

    private int generateRandomInBounds(int min, int max, int toExclude) {
        int result = toExclude;
        while (result == toExclude){
            result = rand.nextInt(max - min) + min;
        }
        return result;
    }

    private int generateExercise() {
        int result = rand.nextInt(maxForExercise);
        exercise = result + " ";
        int length = rand.nextInt(maxlengthOfExercise) + 1;
        for (int i = 0; i < length; i++) {
            result = generateNextStep(result);
        }
        exercise += "= ?";
        return result;
    }

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

}
