package ru.ap1kkk;

import ru.ap1kkk.models.enums.CellState;
import ru.ap1kkk.models.enums.Color;
import ru.ap1kkk.records.EvaluationData;

public class EvaluationFunction {
    private static final int EAT_OPPONENT_WEIGHT = 60;
    private static final int TERRITORY_WEIGHT = 20;
    private static final int STONE_COUNT_WEIGHT = 10;
    private static final int LIBERTY_COUNT_WEIGHT = 5;


    public static int evaluatePosition(GameBoard board, Color playerColor) {
        EvaluationData playerData = countData(board, playerColor);
        EvaluationData opponentData = countData(board, playerColor.getOpponent());
        int playerScore =
                playerData.toBeEaten() * EAT_OPPONENT_WEIGHT
                + playerData.surrounded() * TERRITORY_WEIGHT
                + playerData.liberties() * LIBERTY_COUNT_WEIGHT
                + playerData.stones() * STONE_COUNT_WEIGHT;

        int opponentScore =
                opponentData.toBeEaten() * EAT_OPPONENT_WEIGHT
                + opponentData.surrounded() * TERRITORY_WEIGHT
                + opponentData.liberties() * LIBERTY_COUNT_WEIGHT
                + opponentData.stones() * STONE_COUNT_WEIGHT;

        return playerScore - opponentScore;
    }

    public static EvaluationData countData(GameBoard board, Color color) {
        int stones = board.getStonesAmount(color);
        int liberties = calculateLiberties(board, color);
        int toBeEaten = 0;
        int surroundedCells = 0;

        for (int x = 0; x < board.getSize(); x++) {
            for (int y = 0; y < board.getSize(); y++) {
                if(!board.getCellStates()[x][y].equals(CellState.EMPTY)){
                    if(board.getCellStates()[x][y].equals(CellState.fromColor(color))) {
                        surroundedCells++;
                    }
                    if(board.getCellStates()[x][y].equals(CellState.eatenFromColor(color.getOpponent()))) {
                        toBeEaten++;
                    }
                }
            }
        }

        return new EvaluationData(
                surroundedCells,
                liberties,
                toBeEaten,
                stones
        );
    }

    private static int calculateLiberties(GameBoard board, Color color) {
        int liberties = 0;
        boolean[][] visited = new boolean[board.getSize()][board.getSize()];

        for (int x = 0; x < board.getSize(); x++) {
            for (int y = 0; y < board.getSize(); y++) {
                if (!visited[x][y] && board.getStone(x, y) != null && board.getStone(x, y).getColor() == color) {
                    int groupLiberties = calculateGroupLiberties(board, color, x, y, visited);
                    liberties += groupLiberties;
                }
            }
        }

        return liberties;
    }

    private static int calculateGroupLiberties(GameBoard board, Color color, int x, int y, boolean[][] visited) {
        if (board.isOutOfBounds(x, y)) {
            return 0;
        }

        if (visited[x][y] || (board.getStone(x, y) != null && board.getStone(x, y).getColor() != color)) {
            return 0;
        }

        visited[x][y] = true;

        int liberties = 0;
        if (board.getStone(x, y) == null) {
            liberties++;
        } else {
            liberties += calculateGroupLiberties(board, color, x - 1, y, visited);
            liberties += calculateGroupLiberties(board, color, x + 1, y, visited);
            liberties += calculateGroupLiberties(board, color, x, y - 1, visited);
            liberties += calculateGroupLiberties(board, color, x, y + 1, visited);
        }

        return liberties;
    }
}

