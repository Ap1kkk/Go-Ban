package ru.ap1kkk;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.ap1kkk.models.enums.Color;
import ru.ap1kkk.records.Move;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

class MinimaxTest {
    private static final int BOARD_SIZE = 5;
    private GameBoard gameBoard;

    @BeforeEach
    void setUp() {
         gameBoard = new GameBoard(BOARD_SIZE);
    }

    @Test
    void calculateMinimax() {
        calculateMinimax("minimax_1.txt");
    }

    void calculateMinimax(String filePath) {
        gameBoard.readBoardFromFile(filePath);

        gameBoard.printCellStates();
        Move bestMove = Minimax.findBestMove(gameBoard, Color.WHITE);
        System.out.printf("Best score: %s%n", Minimax.getBestScore());
        System.out.printf("Best move: %s%n", bestMove);
    }
}