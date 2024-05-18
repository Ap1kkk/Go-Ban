package ru.ap1kkk;

import lombok.Getter;
import ru.ap1kkk.models.enums.Color;
import ru.ap1kkk.models.Stone;
import ru.ap1kkk.records.Move;

import java.util.ArrayList;
import java.util.List;

public class Minimax {

    private static final int MAX_DEPTH = 3;

    @Getter
    private static int bestScore;

    public static Move findBestMove(GameBoard board, Color playerColor) {
        int bestScore = Integer.MIN_VALUE;
        Move bestMove = null;
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;

        List<Move> possibleMoves = generateMoves(board, playerColor);

        for (Move move : possibleMoves) {
            GameBoard newBoard = board.copyBoard();
            newBoard.makeMove(move, new Stone(playerColor));
            int score = minimax(newBoard, 0, true, playerColor, alpha, beta);
            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }

        Minimax.bestScore = bestScore;
        return bestMove;
    }

    private static int minimax(GameBoard board, int depth, boolean maximizingPlayer, Color playerColor, int alpha, int beta) {
        if (depth == MAX_DEPTH || board.isGameOver(playerColor, playerColor.getOpponent())) {
            return EvaluationFunction.evaluatePosition(board, playerColor);
        }

        if (!maximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;
            List<Move> possibleMoves = generateMoves(board, playerColor);
            for (Move move : possibleMoves) {
                GameBoard newBoard = board.copyBoard();
                newBoard.makeMove(move, new Stone(playerColor));
                int eval = minimax(newBoard, depth + 1, false, playerColor,  alpha, beta);
                maxEval = Math.max(maxEval, eval);
                beta = Math.min(beta, maxEval);
                if (beta <= alpha) {
                    break; // Beta cut-off
                }
            }

            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            Color opponentColor = playerColor.getOpponent();
            List<Move> possibleMoves = generateMoves(board, opponentColor);
            for (Move move : possibleMoves) {
                GameBoard newBoard = board.copyBoard();
                newBoard.makeMove(move, new Stone(opponentColor));
                int eval = minimax(newBoard, depth + 1, true, playerColor,  alpha, beta);
                minEval = Math.min(minEval, eval);
                alpha = Math.max(alpha, minEval);
                if (beta <= alpha) {
                    break; // Alpha cut-off
                }
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
