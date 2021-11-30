/*
Author: Ryan Kelly
Class: Computer Science 4880 - Independent Research Study
School: University of Missouri: Saint Louis
Date: 11/30/2021
Language Used: Java

Description: For my Independent Research Project, I am creating an educational math game.
This program is a variation of the popular game 2048, that uses fibonacci numbers instead of
base 2 numbers. The original program was written following CodeGym's 2048 Game Task
Tutorial. I modified several functions to change how the game is played. As mentioned
previously, I changed the tiles to be fibonacci numbers. The goal of the game now is
to reach a tile with the value 2584. In addition, combos have been added to reward
the player for consecutive moves that merge tiles, leading to higher scores. Finally,
I implemented a special asterisk (*) tile that merges with any block.
*/
package com.codegym.games.game2048;

import com.codegym.engine.cell.*;

public class Game2048 extends Game {
    //Variable declarations
    private static final int SIDE = 4;
    private int[][] gameField = new int[SIDE][SIDE];
    private boolean isGameStopped = false, result = false, curCombo = false;
    private int score = 0, combo = 0;
    //This value is used as it isn't part of the Fibonacci sequence, and just denotes the special * block
    private static int EX = 100;
    //Array of color values for blocks
    private Color[] colors = Color.values();
    private int[] fibValues = new int[]{1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233, 377, 610, 987, 1597, 2584, EX};

    //Initialize is run when the program is started, and creates the game screen.
    @Override
    public void initialize() {
        setScreenSize(SIDE, SIDE);
        createGame();
        drawScene();
    }
    
    //Initializes the gamefield with 0s, create 2 random tiles, and display the starting message.
    private void createGame() {
        score = 0;
        combo = 0;
        setScore(score);
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                gameField[y][x] = 0;
            }
        }
        createNewNumber();
        createNewNumber();
        Color cell = Color.GREEN;
        Color text = Color.BLACK;
        int textSize = 25;
        String message = "Try to reach 2584!" + "\n Press the I Key to check Instructions!";
        showMessageDialog(cell, message, text, textSize);
    }
    
    //Update gamefield
    private void drawScene() {
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                setCellColoredNumber(x, y, gameField[y][x]);
            }
        }
    }
    
    //Adds a new tile to the gamefield, having either the value 1, 2, or the special * blocks
    //The * is only a 1% chance, 1 is an 89% chance, and 2 is a 10% chance
    //Everytime createNewNumber is called (which is every move) it calls 
    //getMaxTileValue to see if the player meets the win condition
    private void createNewNumber() {
        int x = getRandomNumber(SIDE);
        int y = getRandomNumber(SIDE);
        if ( gameField[y][x] == 0 ) {
            int number = getRandomNumber(100);
            if (number == 99) {
                gameField[y][x] = fibValues[18];
            }
            else if (number < 99 && number > 10) {
                gameField[y][x] = fibValues[1];
            }
            else {
                gameField[y][x] = fibValues[2];
            }
        }
        else {
            createNewNumber();
        }
        int max = getMaxTileValue();
        if (max == 2584)
        {
            win();
        }
    }
    
    //Sets a tiles value, as well as it's color
    private void setCellColoredNumber(int x, int y, int value) {
        Color color = getColorByValue(value);
        String str;
        if (value == 0) {
            str = "";
        }
        else if (value == EX) {
            str = "*";
        }
        else {
            str = "" + value + "";
        }
        setCellValueEx(x, y, color, str);
    }
    
    //Determines which color to make the cell by it's value
    private Color getColorByValue(int value) {
        if (value == 1) {
            return Color.PLUM;
        }
        else if (value == 2) {
            return Color.SLATEBLUE;
        }
        else if (value == 3) {
            return Color.DODGERBLUE;
        }
        else if (value == 5) {
            return Color.DARKTURQUOISE;
        }
        else if (value == 8) {
            return Color.MEDIUMSEAGREEN;
        }
        else if (value == 13) {
            return Color.LIMEGREEN;
        }
        else if (value == 21) {
            return Color.DARKORANGE;
        }
        else if (value == 34) {
            return Color.SALMON;
        }
        else if (value == 55) {
            return Color.ORANGERED;
        }
        else if (value == 89) {
            return Color.DEEPPINK;
        }
        else if (value == 144) {
            return Color.MEDIUMVIOLETRED;
        }
        else if (value == 233) {
            return Color.PURPLE;
        }
        else if (value == 377) {
            return colors[7];
        }
        else if (value == 610) {
            return Color.DARKRED;
        }
        else if (value == 987) {
            return colors[73];
        }
        else if (value == 1597) {
            return colors[13];
        }
        else if (value == 2584) {
            return colors[89];
        }
        else if (value == EX) {
            return Color.YELLOW;
        }
        else {
            return Color.WHITE;
        }
    }
    
    //Attempted function to animate tile movements
    //public static void wait(int ms)
    //{
    //    try
    //    {
    //        Thread.sleep(ms);
    //    }
    //    catch(InterruptedException ex)
    //    {
    //        Thread.currentThread().interrupt();
    //    }
    //}
    
    //Function that moves tiles in the direction the user presses
    private boolean compressRow(int[] row) {
        int insertPos = 0;
        boolean result = false;
        for (int x = 0; x < SIDE; x++) {
            if (row[x] > 0) {
                if (x != insertPos) {
                    row[insertPos] = row[x];
                    row[x] = 0;
                    result = true;
                }
                insertPos++;
            }
        }
        return result;
    }
    
    //Function that combines tiles if they are next to each other on the fibonacci sequence
    private boolean mergeRow(int[] row) {
        result = false;
        
        for (int x = 0; x < SIDE - 1; x++) {
            for(int i = 1; i < fibValues.length - 1; i++) {
                if (row[x] != 0 && row[x] == fibValues[i] && row[x + 1] == fibValues[i + 1] && curCombo == false) {
                    row[x] += row[x + 1];
                    row[x + 1] = 0;
                    result = true;
                    curCombo = true;
                    combo = combo + 1;
                    score += row[x];
                    setScore(score);
                }
                else if (row[x] != 0 && row[x] == fibValues[i] && row[x + 1] == fibValues[i - 1] && curCombo == false) {
                    row[x] += row[x + 1];
                    row[x + 1] = 0;
                    result = true;
                    curCombo = true;
                    combo = combo + 1;
                    score += row[x];
                    setScore(score);
                }
                else if (row[x] != 0 && row[x] == fibValues[i] && row[x + 1] == fibValues[i + 1] && curCombo == true) {
                    row[x] += row[x + 1];
                    row[x + 1] = 0;
                    result = true;
                    curCombo = true;
                    combo = combo + 1;
                    score = score + (row[x] * combo);
                    setScore(score);
                }
                else if (row[x] != 0 && row[x] == fibValues[i] && row[x + 1] == fibValues[i - 1] && curCombo == true) {
                    row[x] += row[x + 1];
                    row[x + 1] = 0;
                    result = true;
                    curCombo = true;
                    combo = combo + 1;
                    score = score + (row[x] * combo);
                    setScore(score);
                }
                else if (row[x] != 0 && row[x] == fibValues[i] && row[x + 1] == fibValues[18] && curCombo == false) {
                    row[x] += fibValues[i - 1];
                    row[x + 1] = 0;
                    result = true;
                    curCombo = true;
                    combo = combo + 1;
                    score += row[x];
                    setScore(score);
                }
                else if (row[x] != 0 && row[x] == fibValues[i] && row[x + 1] == fibValues[18] && curCombo == true) {
                    row[x] += fibValues[i - 1];
                    row[x + 1] = 0;
                    result = true;
                    curCombo = true;
                    combo = combo + 1;
                    score += row[x];
                    setScore(score);
                }
            }
        }
        return result;
    }
    
    //Reads user input. If the game is over and the user presses space, start a new game.
    //If the game is running and the user presses space, display score.
    //If the user presses an arrow key, check if a move is possible and either move, or give a game over.
    @Override
    public void onKeyPress(Key key) {
        boolean movePossible = canUserMove();
        if (movePossible == false) {
            gameOver();
            //return;
        }
        
        if (key == Key.SPACE && isGameStopped == true) {
            isGameStopped = false;
            createGame();
            drawScene();
        }
        
        if (key == Key.UNKNOWN && isGameStopped == false) {
            Color cell = Color.GREEN;
            Color text = Color.BLACK;
            int textSize = 15;
            String message = "Goal: Make a tile with 2584 points!\nControls: Arrow Keys shift tiles. \nSpace displays score and restarts on a game over.\n The I Key shows instructions!";
            showMessageDialog(cell, message, text, textSize);
        }
        
        if (key == Key.LEFT && isGameStopped == false) {
            moveLeft();
        }
        else if (key == Key.RIGHT && isGameStopped == false) {
            moveRight();
        }
        else if (key == Key.UP && isGameStopped == false) {
            moveUp();
        }
        else if (key == Key.DOWN && isGameStopped == false) {
            moveDown();
        }
        else if (key == Key.SPACE && isGameStopped == false) {
            Color cell = Color.LIGHTBLUE;
            Color text = Color.WHITE;
            int textSize = 25;
            String message = "Score: " + score + " Combo: " + combo;
            showMessageDialog(cell, message, text, textSize);
        }
        else {
            return;
        }
        drawScene();
    }
    
    //Function that checks if you need to compress, merge, or both based off your input
    //If a merge doesn't occur, combo returns to 0
    private void moveLeft() {
        boolean moved = false;
        boolean mergeThisMove = false;
        
        for (int[] row : gameField) {
            boolean compressed = compressRow(row);
            boolean merged = mergeRow(row);
            if (merged) {
                compressRow(row);
                mergeThisMove = true;
            }
            if (compressed || merged) {
                moved = true;
            }
        }
        if (moved) {
            createNewNumber();
        }
        if(!mergeThisMove)
        {
            curCombo = false;
            combo = 0;
        }
    }
    
    //By rotating the gameField, we can create the same result as a moveRight function using moveLeft
    private void moveRight() {
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
    }
    
    private void moveUp() {
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
    }
    
    private void moveDown() {
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
    }
    
    //Rather than coding a moveRight, moveUp, and moveDown, we can 
    //rotate the gamefield until moveLeft will produce the intended results
    private void rotateClockwise() {
        int[][] rotatedGameField = new int[SIDE][SIDE];
        
        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {
                rotatedGameField[j][SIDE - 1 - i] = gameField[i][j];
            }
        }
        gameField = rotatedGameField;
    }
    
    //Check gamefield for the largest current value
    private int getMaxTileValue() {
        int maxValue = 0;
        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {
                if (maxValue < gameField[i][j]) {
                    maxValue = gameField[i][j];
                }
            }
        }
        return maxValue;
    }
    
    //When called, win stops the game and displays "You win!" plus your score.
    private void win() {
        isGameStopped = true;
        Color cell = Color.LIGHTBLUE;
        Color text = Color.WHITE;
        int textSize = 25;
        String message = "You win! Score: " + score;
        showMessageDialog(cell, message, text, textSize);
    }
    
    //Function to check if a move is possible. If not possible, return false
    //This leads to a gameOver
    private boolean canUserMove() {
        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {
                for (int x = 1; x < fibValues.length; x++) {
                    if (gameField[j][i] == 0) {
                        return true;
                    }
                    else if (j < SIDE - 1 && gameField[j][i] == fibValues[x] && gameField[j + 1][i] == fibValues[x + 1]) {
                        return true;
                    }
                    else if (j < SIDE - 1 && gameField[j][i] == fibValues[x] && gameField[j + 1][i] == fibValues[x - 1]) {
                        return true;
                    }
                    else if (i < SIDE - 1 && gameField[j][i] == fibValues[x] && gameField[j][i + 1] == fibValues[x + 1]) {
                        return true;
                    }
                    else if (i < SIDE - 1 && gameField[j][i] == fibValues[x] && gameField[j][i + 1] == fibValues[x - 1]) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    //Function to stop the game. Displays "You lose!" plus your final score.
    private void gameOver() {
        isGameStopped = true;
        Color cell = Color.RED;
        Color text = Color.GREY;
        int textSize = 25;
        String message = "You lose! Score: " + score;
        showMessageDialog(cell, message, text, textSize);
    }
    
}
