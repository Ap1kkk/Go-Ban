package ru.ap1kkk;

import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameBoardTest {
    private final int BOARD_SIZE = 5;

    private GameBoard gameBoard;

    @BeforeEach
    void setUp() {
        gameBoard = new GameBoard(BOARD_SIZE);
    }

    @Test
    void paintSurroundColors() {
        gameBoard.readBoardFromFile("test_surrounded_1.txt");
        gameBoard.paintSurroundColors(Color.WHITE.getOpponent());
    }

    @Test
    void paintStonesToBeEaten() {
        gameBoard.readBoardFromFile("test_surrounded_1.txt");
        PaintedData paintedData = gameBoard.paintSurroundColors(Color.WHITE.getOpponent());
        gameBoard.paintStonesToBeEaten(Color.WHITE, paintedData.playerBoundStones());
    }

    @Test
    void init() {
        gameBoard.readBoardFromFile("test_surrounded_1.txt");
        gameBoard.printCellStates();
    }
}