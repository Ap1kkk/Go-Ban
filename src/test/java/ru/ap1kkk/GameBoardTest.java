package ru.ap1kkk;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameBoardTest {
    private final int BOARD_SIZE = 5;

    private GameBoard gameBoard;

    @BeforeEach
    void setUp() {
        gameBoard = new GameBoard(BOARD_SIZE);
    }

    @Test
    void updateSurroundedCells() {
        gameBoard.readBoardFromFile("test_surrounded.txt");
        gameBoard.updateSurroundedCells(gameBoard, Color.BLACK);
        gameBoard.print();
    }
}