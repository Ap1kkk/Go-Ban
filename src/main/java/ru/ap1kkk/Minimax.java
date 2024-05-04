package ru.ap1kkk;

import java.util.ArrayList;
import java.util.List;

public class Minimax {

    private static final int MAX_DEPTH = 3; // Максимальная глубина просмотра

    public static Move findBestMove(GameBoard board, Color playerColor) {
        int bestScore = Integer.MIN_VALUE;
        Move bestMove = null;

        List<Move> possibleMoves = generateMoves(board, playerColor);

        for (Move move : possibleMoves) {
            GameBoard newBoard = board.copyBoard();
            newBoard.makeMove(move, new Stone(playerColor));
            int score = minimax(newBoard, 0, true, playerColor);
            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }

        return bestMove;
    }

    private static int minimax(GameBoard board, int depth, boolean maximizingPlayer, Color playerColor) {
        System.out.printf("Minimax for: %s%n", playerColor);

        if (depth == MAX_DEPTH || board.isGameOver(playerColor, playerColor.getOpponent())) {
            return EvaluationFunction.evaluatePosition(board, playerColor);
        }

        if (!maximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;
            List<Move> possibleMoves = generateMoves(board, playerColor);
            for (Move move : possibleMoves) {
                GameBoard newBoard = board.copyBoard();
                newBoard.makeMove(move, new Stone(playerColor));
                int eval = minimax(newBoard, depth + 1, false, playerColor);
                maxEval = Math.max(maxEval, eval);
            }

            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            Color opponentColor = playerColor.getOpponent();
            List<Move> possibleMoves = generateMoves(board, opponentColor);
            for (Move move : possibleMoves) {
                GameBoard newBoard = board.copyBoard();
                newBoard.makeMove(move, new Stone(opponentColor));
                int eval = minimax(newBoard, depth + 1, true, playerColor);
                minEval = Math.min(minEval, eval);
            }
            return minEval;
        }
    }

    private static List<Move> generateMoves(GameBoard board, Color playerColor) {
        List<Move> possibleMoves = new ArrayList<>();

        for (int x = 0; x < board.getSize(); x++) {
            for (int y = 0; y < board.getSize(); y++) {
                if (board.isValidMove(x, y, playerColor)) {
                    possibleMoves.add(new Move(x, y));
                }
            }
        }

        return possibleMoves;
    }
}
