package ru.ap1kkk;

import lombok.Getter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.logging.Logger;

public class GameBoard {
    @Getter
    private final int size;
    private final Stone[][] board;
    @Getter
    private final CellState[][] cellStates;

    // Конструктор
    public GameBoard(int size) {
        this.size = size;
        board = new Stone[size][size];
        cellStates = new CellState[size][size];
        clearStates();
    }

    private void clearStates() {
        Arrays.stream(cellStates).forEach(row -> Arrays.fill(row, CellState.EMPTY));
    }

    private void updateStates() {
        clearStates();
        updateSurroundedCells(this, Color.BLACK);
        updateSurroundedCells(this, Color.WHITE);
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

        //TEMP
        if(x == 1 && y == 3 && playerColor == Color.WHITE) {
            System.out.print("");
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

    private void updateSurroundedCells(GameBoard board, Color color) {
        boolean[][] visited = new boolean[board.getSize()][board.getSize()];
        boolean[][] surrounded = new boolean[board.getSize()][board.getSize()];
        Integer count = 0;

        // Обходим каждую клетку доски
        for (int x = 1; x < board.getSize() - 1; x++) {
            for (int y = 1; y < board.getSize() - 1; y++) {
                isSurrounded(board, color, x, y, visited, surrounded, count);
            }
        }

        for (int x = 0; x < board.getSize(); x++) {
            for (int y = 0; y < board.getSize(); y++) {
                if(surrounded[x][y])
                    cellStates[x][y] = CellState.fromColor(color);
            }
        }
    }

    private static boolean isSurrounded(GameBoard board, Color color, int x, int y, boolean[][] visited, boolean[][] surrounded, Integer count) {
        if (x < 0 || x >= board.getSize() || y < 0 || y >= board.getSize()) {
            return false;
        }

        if(x == 0 || x == board.getSize() || y == 0 || y == board.getSize()) {
            if(board.getStone(x, y) != null && board.getStone(x, y).getColor() == color) {
                return true;
            }
            return false;
        }

        if(board.getStone(x, y) != null) {
            if(board.getStone(x, y).getColor() == color)
                return true;
            return false;
        }

        // Если клетка уже была посещена или принадлежит противоположному цвету, возвращаем 0
        if (visited[x][y])
            return true;

        // Отмечаем клетку как посещенную
        visited[x][y] = true;

        boolean isSurrounded = true;
        isSurrounded &= isSurrounded(board, color, x - 1, y, visited, surrounded, count);
        isSurrounded &= isSurrounded(board, color, x + 1, y, visited, surrounded, count);
        isSurrounded &= isSurrounded(board, color, x, y - 1, visited, surrounded, count);
        isSurrounded &= isSurrounded(board, color, x, y + 1, visited, surrounded, count);
        surrounded[x][y] = isSurrounded;

        return isSurrounded;
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

    public void print() {
        System.out.println("Game board (positions|states):");
        System.out.print(" ");
        for (int i = 0; i < size; i++) {
            System.out.print(i);
        }
        System.out.printf("%n");
        for (int i = 0; i < size; i++) {
            System.out.print(i);
            for (int j = 0; j < size; j++) {
                Stone stone = board[i][j];
                System.out.printf("%s", Stone.getView(stone));
            }
            System.out.print("\t\t");
            for (int j = 0; j < size; j++) {
                System.out.printf("%s", cellStates[i][j].getView());
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
                            board[row][col] = new Stone(Color.BLACK);
                        } else if (cell == 'W') {
                            board[row][col] = new Stone(Color.WHITE);
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
