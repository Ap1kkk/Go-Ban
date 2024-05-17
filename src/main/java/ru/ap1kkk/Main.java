package ru.ap1kkk;

import org.fusesource.jansi.AnsiConsole;

import java.util.Scanner;

public class Main {
    private static final int BOARD_SIZE = 5;
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        GameBoard gameBoard = new GameBoard(BOARD_SIZE);

//        gameBoard.readBoardFromFile("input.txt");
        PlayerType playerType = null;
        PlayerType opponentType = null;

        String gameType = "";
        System.out.println("Choose game type: PvP(0), PvE(1), EvE(2)");
        while (gameType.equals("")) {
            gameType = scanner.nextLine();
            if(gameType.equals("0")) {
                playerType = PlayerType.PLAYER;
                opponentType = PlayerType.PLAYER;
                break;
            } else if(gameType.equals("1")){
                playerType = PlayerType.PLAYER;
                opponentType = PlayerType.AI;
                break;
            } else if(gameType.equals("2")){
                playerType = PlayerType.AI;
                opponentType = PlayerType.AI;
                break;
            }

            System.out.println("Incorrect game type. Choose again");
        }

        Color playerColor = Color.WHITE;
        if(playerType.equals(PlayerType.PLAYER)) {
            System.out.println("Choose your color: WHITE, BLACK");
            playerColor = Color.valueOf(scanner.nextLine());
        }

        Color opponentColor = playerColor.getOpponent();
        while (!gameBoard.isGameOver(playerColor, opponentColor)) {
            if(playerColor.equals(Color.WHITE)) {
                move(gameBoard, playerColor, playerType);
                gameBoard.print();
                move(gameBoard, opponentColor, opponentType);
                gameBoard.print();
            } else {
                move(gameBoard, opponentColor, opponentType);
                gameBoard.print();
                move(gameBoard, playerColor, playerType);
                gameBoard.print();
            }
        }
    }

    public static void move(GameBoard gameBoard, Color playerColor, PlayerType playerType) {
        if(playerType.equals(PlayerType.AI)) {
            moveAI(gameBoard, playerColor);
            return;
        }

        System.out.println("Enter input: x,y");

        Move move = null;
        while (move == null) {
            String input = scanner.nextLine();
            move = validateInput(gameBoard, playerColor, input);
            if(move == null)
                System.out.println("Incorrect input. Try again");
        }
        gameBoard.makeMove(move, new Stone(playerColor));
    }

    private static void moveAI(GameBoard gameBoard, Color playerColor) {
        Move bestMove = Minimax.findBestMove(gameBoard, playerColor);
        gameBoard.makeMove(bestMove, new Stone(playerColor));
        System.out.printf("AI %s moved -> (%s,%s)", playerColor, bestMove.x(), bestMove.y());
    }

    private static Move validateInput(GameBoard gameBoard, Color playerColor, String input) {
        String[] splitInput = input.split(",");
        if(splitInput.length != 2) {
            return null;
        }
        try {
            int x = Integer.decode(splitInput[0]);
            int y = Integer.decode(splitInput[1]);
            if(!gameBoard.isValidMove(x, y, playerColor))
                throw new Exception();
            return new Move(x, y);
        } catch (Exception e) {
            return null;
        }
    }
}