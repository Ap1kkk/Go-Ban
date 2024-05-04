package ru.ap1kkk;

import org.fusesource.jansi.AnsiConsole;

public class Main {
    public static void main(String[] args) {
        AnsiConsole.systemInstall();
        // Создаем игровую доску размером 5x5
        GameBoard gameBoard = new GameBoard(5);

        gameBoard.readBoardFromFile("input.txt");
//        // Предположим, что на доске уже есть размещенные камни
//        // Здесь представлена простая ситуация для демонстрации
//        // В реальной игре доска может быть любой инициализированной позиции
//        Stone blackStone = new Stone(Color.BLACK);
//        Stone whiteStone = new Stone(Color.WHITE);
//
//        // Размещаем камни на доске
//        gameBoard.makeMove(1, 2, blackStone); // Черный камень
//        gameBoard.makeMove(2, 1, blackStone); // Черный камень
//        gameBoard.makeMove(2, 3, blackStone); // Черный камень
//        gameBoard.makeMove(3, 2, blackStone); // Черный камень
//        gameBoard.makeMove(2, 0, whiteStone); // Белый камень
//        gameBoard.makeMove(3, 1, whiteStone); // Белый камень
//        gameBoard.makeMove(4, 2, whiteStone); // Белый камень
//        gameBoard.makeMove(4, 3, whiteStone); // Белый камень

        // Задаем текущий цвет игрока
        Color playerColor = Color.BLACK; // Предположим, что текущий игрок - черные камни

        // Вычисляем оценку позиции
        int evaluation = EvaluationFunction.evaluatePosition(gameBoard, playerColor);

        // Выводим результат оценки
        System.out.println("Оценка позиции для текущего игрока: " + evaluation);

        Move bestMove = Minimax.findBestMove(gameBoard, playerColor);

//        gameBoard.printStones();
//        gameBoard.printCellStates();
        gameBoard.print();
        System.out.printf("x: %s, y: %s%n", bestMove.x(), bestMove.y());

        AnsiConsole.systemUninstall();
    }
}