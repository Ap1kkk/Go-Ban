package ru.ap1kkk;

import lombok.Getter;

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

    // Конструктор
    public GameBoard(int size) {
        this.size = size;
        board = new Stone[size][size];
        cellStates = new CellState[size][size];
        whiteStones = new ArrayList<>();
        blackStones = new ArrayList<>();
        clearStates();
    }

    private void clearStates() {
        Arrays.stream(cellStates).forEach(row -> Arrays.fill(row, CellState.EMPTY));
    }

    private void updateStates() {
        clearStates();
        updateStonesAmount();
        paint(Color.WHITE);
        paint(Color.BLACK);
    }

    private void paint(Color playerColor) {
        if(playerColor.equals(Color.WHITE)) {
            if (whiteStones.size() < 2)
                return;
        }
        else {
            if (blackStones.size() < 2)
                return;
        }

        PaintedData whitePaintedData = paintSurroundColors(playerColor.getOpponent());
        boolean[][] toBeEaten = paintStonesToBeEaten(playerColor, whitePaintedData.playerBoundStones());
        for (int x = 0; x < getSize(); x++) {
            for (int y = 0; y < getSize(); y++) {
                if(whitePaintedData.painted()[x][y] == null)
                    cellStates[x][y] = CellState.fromColor(playerColor);
                if(toBeEaten[x][y])
                    cellStates[x][y] = CellState.eatenFromColor(playerColor);
            }
        }
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

    public GameBoard copyBoard() {
        GameBoard newBoard = new GameBoard(this.size);
        for (int i = 0; i < this.size; i++) {
            System.arraycopy(this.board[i], 0, newBoard.board[i], 0, this.size);
        }
        return newBoard;
    }

    public boolean isGameOver(Color currentPlayerColor, Color opponentColor) {
        // Проверяем, есть ли у текущего игрока доступные ходы
        boolean currentPlayerHasMoves = hasAvailableMoves(currentPlayerColor);

        // Проверяем, есть ли у противника доступные ходы
        boolean opponentHasMoves = hasAvailableMoves(opponentColor);

        // Если оба игрока не имеют доступных ходов, игра завершается
        return !currentPlayerHasMoves && !opponentHasMoves;
    }

    // Метод для проверки наличия доступных ходов у игрока указанного цвета
    private boolean hasAvailableMoves(Color playerColor) {
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                // Проверяем, есть ли доступный ход на данной клетке для указанного игрока
                if (isValidMove(x, y, playerColor)) {
                    return true; // Если ход найден, возвращаем true
                }
            }
        }
        return false; // Если ни одного доступного хода не найдено, возвращаем false
    }

    // Метод для проверки правильности хода для указанного игрока
    public boolean isValidMove(int x, int y, Color playerColor) {
        // Проверяем, что координаты хода находятся в пределах доски
        if (!isValidMove(x, y)) {
            return false;
        }

        // Проверяем, пуста ли клетка
        if (!isCellEmpty(x, y)) {
            return false;
        }

        // Если клетка окружена противником
        if(!cellStates[x][y].equals(CellState.EMPTY)
                && !cellStates[x][y].equals(CellState.fromColor(playerColor))) {
            return false;
        }

        return true; // Если все проверки пройдены успешно, ход допустим
    }

    public void makeMove(int x, int y, Stone stone) {
        if (isValidMove(x, y) && isCellEmpty(x, y) && stone != null) {
            board[x][y] = stone;
            updateStates();
        } else {
            System.out.println("Invalid move!"); // Обработка ошибки
        }
    }

    public void makeMove(Move move, Stone stone) {
        makeMove(move.x(), move.y(), stone);
    }

    // Метод для проверки правильности хода
    private boolean isValidMove(int x, int y) {
        return x >= 0 && x < size && y >= 0 && y < size;
    }

    // Метод для проверки, пуста ли клетка
    private boolean isCellEmpty(int x, int y) {
        return board[x][y] == null;
    }

    // Метод для получения камня по координатам
    public Stone getStone(int x, int y) {
        return board[x][y];
    }

    public PaintedData paintSurroundColors(Color opponentColor) {
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

        printSurroundColors(colors);

        return new PaintedData(colors, playerBoundStones);
    }

    private void paintSurroundColors(int x, int y, Color opponentColor, boolean[][] visited, SurroundColor[][] colors, List<Stone> playerBoundStones) {
        if (x < 0 || x >= getSize() || y < 0 || y >= getSize())
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
        paintSurroundColors( x, y - 1, opponentColor, visited, colors, playerBoundStones);
        paintSurroundColors( x, y + 1, opponentColor, visited, colors, playerBoundStones);
    }

    public boolean[][] paintStonesToBeEaten(Color playerColor, List<Stone> boundStones) {
        boolean[][] visited = new boolean[getSize()][getSize()];
        boolean[][] canBeEaten = new boolean[getSize()][getSize()];

        for (Stone stone: boundStones)
            paintStonesToBeEaten(stone.getX(), stone.getY(), playerColor, visited, canBeEaten);

        print(canBeEaten);
        return canBeEaten;
    }

    private boolean paintStonesToBeEaten(int x, int y, Color playerColor, boolean[][] visited, boolean[][] toBeEaten) {
        if (x < 0 || x >= getSize() || y < 0 || y >= getSize())
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
                System.out.printf("%s", fromBoolean(booleans[i][j]));
            }
            System.out.printf("%n");
        }
    }

    public void printStones() {
        System.out.println("Position of stones on the board:");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Stone stone = board[i][j];
                System.out.printf("%s", Stone.getView(stone));
            }
            System.out.printf("%n");
        }
    }

    public void printCellStates() {
        System.out.println("State of cells on the board:");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.printf("%s", cellStates[i][j].getView());
            }
            System.out.printf("%n");
        }
    }

    private void printSurroundColors(SurroundColor[][] colors) {
        System.out.println("Surround colors:");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.printf("%s", SurroundColor.getView(colors[i][j]));
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
                        // Для других символов можно добавить дополнительные условия
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
