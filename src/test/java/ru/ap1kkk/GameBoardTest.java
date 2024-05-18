package ru.ap1kkk;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.ap1kkk.models.enums.Color;
import ru.ap1kkk.records.PaintedData;

class GameBoardTest {
    private final int BOARD_SIZE = 5;

    private GameBoard gameBoard;

    @BeforeEach
    void setUp() {
        gameBoard = new GameBoard(BOARD_SIZE);
    }

    @Test
    void paintSurroundColors_1() {
        paintSurroundColors("test_surrounded_1.txt");
    }

    @Test
    void paintSurroundColors_2() {
        paintSurroundColors("test_surrounded_2.txt");
    }

    @Test
    void paintSurroundColors_3() {
        paintSurroundColors("test_surrounded_3.txt");
    }

    @Test
    void paintStonesToBeEaten_1() {
        paintStonesToBeEaten("test_eaten_1.txt");
    }

    @Test
    void paintStonesToBeEaten_2() {
        paintStonesToBeEaten("test_eaten_2.txt");
    }

    @Test
    void paintStonesToBeEaten_3() {
        paintStonesToBeEaten("test_eaten_3.txt");
    }

    @Test
    void printCellStates_1() {
        printCellStates("test_cell_states_1.txt");
    }

    @Test
    void printCellStates_2() {
        printCellStates("test_cell_states_2.txt");
    }

    @Test
    void printCellStates_3() {
        printCellStates("test_cell_states_3.txt");
    }

    void paintSurroundColors(String filePath) {
        gameBoard.readBoardFromFile(filePath);

        System.out.println("Surrounded for white:");
        gameBoard.paintSurroundColors(Color.WHITE.getOpponent(), true);
        System.out.println("Surrounded for black:");
        gameBoard.paintSurroundColors(Color.BLACK.getOpponent(), true);
    }

    void paintStonesToBeEaten(String filePath) {
        gameBoard.readBoardFromFile(filePath);

        System.out.println("Stones to be eaten for white:");
        PaintedData whitePaintedData = gameBoard.paintSurroundColors(Color.WHITE.getOpponent());
        gameBoard.paintStonesToBeEaten(Color.WHITE, whitePaintedData.playerBoundStones(), true);

        System.out.println("Stones to be eaten for black:");
        PaintedData blackPaintedData = gameBoard.paintSurroundColors(Color.BLACK.getOpponent());
        gameBoard.paintStonesToBeEaten(Color.BLACK, blackPaintedData.playerBoundStones(), true);
    }

    void printCellStates(String filePath) {
        gameBoard.readBoardFromFile(filePath);
        gameBoard.printCellStates();
    }
}