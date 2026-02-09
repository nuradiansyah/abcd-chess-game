package com.game.chess.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChessAI {
	
	private final Random random = new Random();

    public ChessMove chooseMove(ChessBoard board, ChessColor color) {
    	List<ChessMove> allMoves = new ArrayList<>();
	 	List<ChessMove> captureMoves = new ArrayList<>();
         
    	for (int fromRow = 0; fromRow < ChessBoard.SIZE; fromRow++) {
            for (int fromCol = 0; fromCol < ChessBoard.SIZE; fromCol++) {
                ChessPiece piece = board.getPiece(fromRow, fromCol);
                if (piece == null || piece.getColor() != color) {
                    continue;
                }
                for (int toRow = 0; toRow < ChessBoard.SIZE; toRow++) {
                    for (int toCol = 0; toCol < ChessBoard.SIZE; toCol++) {
                        ChessMove move = new ChessMove(fromRow, fromCol, toRow, toCol);
                        if (!board.isLegalMove(move, color)) {
                            continue;
                        }

                        allMoves.add(move);

                        ChessPiece target = board.getPiece(toRow, toCol);
                        if (target != null && target.getColor() != color) {
                            captureMoves.add(move);
                        }
                    }
                }
            }
        }
    	
    	if (allMoves.isEmpty()) {
            return null; // no legal move
        }
    	// Prefer captures if available, otherwise any legal move
        List<ChessMove> pool = captureMoves.isEmpty() ? allMoves : captureMoves;
        return pool.get(random.nextInt(pool.size()));
    }
}
