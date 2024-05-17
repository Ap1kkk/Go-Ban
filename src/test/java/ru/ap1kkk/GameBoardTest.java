package ru.ap1kkk;

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
}