package ru.ap1kkk;

import lombok.Getter;
import ru.ap1kkk.models.enums.CellState;
import ru.ap1kkk.models.enums.Color;
import ru.ap1kkk.models.enums.SurroundColor;
import ru.ap1kkk.models.Stone;
import ru.ap1kkk.records.Move;
import ru.ap1kkk.records.PaintedData;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameBoard {
    @Getter
    private final int size;
    private final Stone[][] board;
    @Getter
    private final CellState[][] cellStates;
    @Getter
    private final List<Stone> whiteStones;
    @Getter
    private final List<Stone> blackStones;
    @Getter
    private Move whiteLastMove;
    @Getter
    private Move blackLastMove;


    public GameBoard(int size) {
        this.size = size;
        board = new Stone[size][size];
        cellStates = new CellState[size][size];
        whiteStones = new ArrayList<>();
        blackStones = new ArrayList<>();
        updateStates();
    }

    private void updateStates() {
        clearStates();
        updateStonesAmount();

        List<Stone> whiteToEaten = paint(Color.WHITE);
        List<Stone> blackToEaten = paint(Color.BLACK);

        clearEatenCells(whiteToEaten);
        clearEatenCells(blackToEaten);
    }

    private void clearStates() {
        Arrays.stream(cellStates).forEach(row -> Arrays.fill(row, CellState.EMPTY));
    }

    private void updateStonesAmount() {
        whiteStones.clear();
        blackStones.clear();
        for (int x = 0; x < getSize(); x++) {
            for (int y = 0; y < getSize(); y++) {
                Stone stone = board[x][y];
                if(stone != null) {
                    if(stone.getColor().equals(Color.WHITE))
                        whiteStones.add(stone);
                    else
                        blackStones.add(stone);
                }
            }
        }
    }

    private List<Stone> paint(Color playerColor) {
        List<Stone> stonesToEat = new ArrayList<>();
        if(playerColor.equals(Color.WHITE)) {
            if (whiteStones.size() < 2)
                return stonesToEat;
        }
        else {
            if (blackStones.size() < 2)
                return stonesToEat;
        }

        PaintedData whitePaintedData = paintSurroundColors(playerColor.getOpponent());
        boolean[][] toBeEaten = paintStonesToBeEaten(playerColor, whitePaintedData.playerBoundStones());
        for (int x = 0; x < getSize(); x++) {
            for (int y = 0; y < getSize(); y++) {
                if(whitePaintedData.painted()[x][y] == null)
                    cellStates[x][y] = CellState.fromColor(playerColor);
                if(toBeEaten[x][y]) {
                    cellStates[x][y] = CellState.eatenFromColor(playerColor);
                    Stone stone = board[x][y];
                    stonesToEat.add(stone);
                }
            }
        }

        return stonesToEat;
    }

    private void clearEatenCells(List<Stone> stonesToEat) {
        for(Stone stone: stonesToEat) {
            board[stone.getX()][stone.getY()] = stone;
        }
    }

    public int getStonesAmount(Color playerColor) {
        if(playerColor.equals(Color.WHITE))
            return whiteStones.size();
        return blackStones.size();
    }

    public GameBoard copyBoard() {
        GameBoard newBoard = new GameBoard(this.size);
        for (int i = 0; i < this.size; i++) {
            System.arraycopy(this.board[i], 0, newBoard.board[i], 0, this.size);
        }
        return newBoard;
    }

    public boolean isGameOver(Color currentPlayerColor, Color opponentColor) {
        boolean currentPlayerHasMoves = hasAvailableMoves(currentPlayerColor);

        boolean opponentHasMoves = hasAvailableMoves(opponentColor);

        return !currentPlayerHasMoves && !opponentHasMoves;
    }

    private boolean hasAvailableMoves(Color playerColor) {
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (isValidMove(x, y, playerColor)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isValidMove(int x, int y, Color playerColor) {
        if (!isValidMove(x, y)) {
            return false;
        }
        if (!isCellEmpty(x, y)) {
            return false;
        }

        if(!cellStates[x][y].equals(CellState.EMPTY)
                && CellState.isLockedByOpponent(playerColor, cellStates[x][y])) {
            return false;
        }
        return true;
    }

    // Метод для проверки правильности хода
    private boolean isValidMove(int x, int y) {
        return x >= 0 && x < size && y >= 0 && y < size;
    }

    private void makeMove(int x, int y, Stone stone) {
        if (isValidMove(x, y) && isCellEmpty(x, y) && stone != null) {
            stone.setX(x);
            stone.setY(y);
            board[x][y] = stone;
            updateStates();
        } else {
            System.out.println("Invalid move!"); // Обработка ошибки
        }
    }

    public void makeMove(Move move, Stone stone) {
        stone.setX(move.x());
        stone.setY(move.y());
        makeMove(move.x(), move.y(), stone);

        if(stone.getColor().equals(Color.WHITE))
            whiteLastMove = move;
        else
            blackLastMove = move;
    }

    private boolean isCellEmpty(int x, int y) {
        return board[x][y] == null;
    }

    public boolean isOutOfBounds(int x, int y) {
        return x < 0 || x >= getSize() || y < 0 || y >= getSize();
    }

    public Stone getStone(int x, int y) {
        return board[x][y];
    }
    public PaintedData paintSurroundColors(Color opponentColor) {
        return paintSurroundColors(opponentColor, false);
    }

    public PaintedData paintSurroundColors(Color opponentColor, boolean debug) {
        boolean[][] visited = new boolean[getSize()][getSize()];
        SurroundColor[][] colors = new SurroundColor[getSize()][getSize()];

        List<Stone> playerBoundStones = new ArrayList<>();
        List<Stone> stones;

        if(opponentColor.equals(Color.WHITE))
            stones = whiteStones;
        else
            stones = blackStones;

        for (Stone stone: stones)
            paintSurroundColors(stone.getX(), stone.getY(), opponentColor, visited, colors, playerBoundStones);

        if(debug)
            printSurroundColors(colors);

        return new PaintedData(colors, playerBoundStones);
    }

    private void paintSurroundColors(int x, int y, Color opponentColor, boolean[][] visited, SurroundColor[][] colors, List<Stone> playerBoundStones) {
        if (isOutOfBounds(x, y))
            return;

        if(visited[x][y])
            return;
        visited[x][y] = true;

        Stone stone = getStone(x, y);
        if(stone != null) {
            if(!stone.getColor().equals(opponentColor)) {
                colors[x][y] = SurroundColor.PLAYER;
                playerBoundStones.add(stone);
                return;
            }
            colors[x][y] = SurroundColor.ENEMY;
        } else {
            colors[x][y] = SurroundColor.IMPOSSIBLE_TO_SURROUND;
        }

        paintSurroundColors(x - 1, y, opponentColor, visited, colors, playerBoundStones);
        paintSurroundColors(x + 1, y, opponentColor, visited, colors, playerBoundStones);
        paintSurroundColors(x, y - 1, opponentColor, visited, colors, playerBoundStones);
        paintSurroundColors(x, y + 1, opponentColor, visited, colors, playerBoundStones);
    }

    public boolean[][] paintStonesToBeEaten(Color playerColor, List<Stone> boundStones) {
        return paintStonesToBeEaten(playerColor, boundStones, false);
    }
    public boolean[][] paintStonesToBeEaten(Color playerColor, List<Stone> boundStones, boolean debug) {
        boolean[][] visited = new boolean[getSize()][getSize()];
        boolean[][] canBeEaten = new boolean[getSize()][getSize()];

        for (Stone stone: boundStones)
            paintStonesToBeEaten(stone.getX(), stone.getY(), playerColor, visited, canBeEaten);

        if(debug)
            print(canBeEaten);

        return canBeEaten;
    }

    private boolean paintStonesToBeEaten(int x, int y, Color playerColor, boolean[][] visited, boolean[][] toBeEaten) {
        if (isOutOfBounds(x, y))
            return true;

        Stone stone = getStone(x, y);
        if(stone == null)
            return false;

        if(visited[x][y])
            return true;

        visited[x][y] = true;

        if(!stone.getColor().equals(playerColor))
            return true;

        boolean canBeEaten = true;
        canBeEaten &= paintStonesToBeEaten(x - 1, y, playerColor, visited, toBeEaten);
        canBeEaten &= paintStonesToBeEaten(x, y - 1, playerColor, visited, toBeEaten);
        canBeEaten &= paintStonesToBeEaten(x + 1, y, playerColor, visited, toBeEaten);
        canBeEaten &= paintStonesToBeEaten(x, y + 1, playerColor, visited, toBeEaten);

        toBeEaten[x][y] = canBeEaten;

        return canBeEaten;
    }

    private String fromBoolean(boolean bool) {
        if(bool)
            return "T";
        return "-";
    }

    public void print(boolean[][] booleans) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.printf("%s ", fromBoolean(booleans[i][j]));
            }
            System.out.printf("%n");
        }
    }

    public void printStones() {
        System.out.println("Position of stones on the board:");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Stone stone = board[i][j];
                System.out.printf("%s ", Stone.getView(stone));
            }
            System.out.printf("%n");
        }
    }

    public void printCellStates() {
        System.out.println("State of cells on the board:");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.printf("%s ", cellStates[i][j].getView());
            }
            System.out.printf("%n");
        }
    }

    private void printSurroundColors(SurroundColor[][] colors) {
        System.out.println("Surround colors:");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.printf("%s ", SurroundColor.getView(colors[i][j]));
            }
            System.out.printf("%n");
        }
    }

    public void print() {
        System.out.println("Game board (positions|states):");
        System.out.print(" ");
        for (int i = 0; i < size; i++) {
            System.out.print(i+ " ");
        }
        System.out.printf("%n");
        for (int i = 0; i < size; i++) {
            System.out.print(i);
            for (int j = 0; j < size; j++) {
                Stone stone = board[i][j];
                System.out.printf("%s ", Stone.getView(stone));
            }
            System.out.print("\t\t");
            for (int j = 0; j < size; j++) {
                System.out.printf("%s ", cellStates[i][j].getView());
            }
            System.out.printf("%n");
        }
    }

    public void readBoardFromFile(String filePath) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath)) {
            if (inputStream != null) {
                InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                BufferedReader reader = new BufferedReader(streamReader);
                String line;
                int row = 0;
                while ((line = reader.readLine()) != null && row < size) {
                    for (int col = 0; col < Math.min(line.length(), size); col++) {
                        char cell = line.charAt(col);
                        if (cell == 'B') {
                            Stone stone = new Stone(Color.BLACK, row, col);
                            board[row][col] = stone;
                            blackStones.add(stone);
                        } else if (cell == 'W') {
                            Stone stone = new Stone(Color.WHITE, row, col);
                            board[row][col] = stone;
                            whiteStones.add(stone);
                        }
                    }
                    row++;
                }
                updateStates();
            } else {
                System.out.println("File not found: " + filePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
