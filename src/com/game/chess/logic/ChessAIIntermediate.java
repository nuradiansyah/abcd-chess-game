package com.game.chess.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Intermediate-level Chess AI that evaluates moves based on material advantage.
 * Uses a simple but effective position evaluation based on piece values.
 */
public class ChessAIIntermediate {
	
	private final Random random = new Random();
	
	/**
	 * Standard chess piece values (in centipawns, where 100 = 1 pawn)
	 */
	private static final int PAWN_VALUE = 100;
	private static final int KNIGHT_VALUE = 320;
	private static final int BISHOP_VALUE = 330;
	private static final int ROOK_VALUE = 500;
	private static final int QUEEN_VALUE = 900;
	private static final int KING_VALUE = 20000; // King is invaluable

	/**
	 * Choose the best move for the AI based on position evaluation.
	 */
	public ChessMove chooseMove(ChessBoard board, ChessColor color) {
		List<ChessMove> allMoves = new ArrayList<>();
		List<ChessMove> captureMoves = new ArrayList<>();
		
		// Generate all legal moves
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
			return null; // Checkmate or stalemate
		}
		
		// Evaluate and choose best move
		if (!captureMoves.isEmpty()) {
			return chooseBestMove(board, captureMoves, color);
		} else {
			return chooseBestMove(board, allMoves, color);
		}
	}
	
	/**
	 * From a list of candidate moves, choose the one that leads to the best position.
	 * If multiple moves have the same evaluation, randomly pick one.
	 */
	private ChessMove chooseBestMove(ChessBoard board, List<ChessMove> moves, ChessColor color) {
		List<ChessMove> bestMoves = new ArrayList<>();
		int bestScore = Integer.MIN_VALUE;
		
		for (ChessMove move : moves) {
			// Simulate the move
			ChessBoard simulatedBoard = copyBoard(board);
			simulatedBoard.applyMove(move);
			
			// Evaluate the resulting position
			int score = evaluatePosition(simulatedBoard, color);
			
			if (score > bestScore) {
				bestScore = score;
				bestMoves.clear();
				bestMoves.add(move);
			} else if (score == bestScore) {
				bestMoves.add(move);
			}
		}
		
		// Randomly choose among equally good moves
		return bestMoves.get(random.nextInt(bestMoves.size()));
	}
	
	/**
	 * Evaluate the board position from the perspective of the given color.
	 * Positive score = good for the color, negative = bad for the color.
	 * 
	 * This uses material counting: the total value of all pieces.
	 */
	private int evaluatePosition(ChessBoard board, ChessColor color) {
		int score = 0;
		
		for (int row = 0; row < ChessBoard.SIZE; row++) {
			for (int col = 0; col < ChessBoard.SIZE; col++) {
				ChessPiece piece = board.getPiece(row, col);
				if (piece == null) {
					continue;
				}
				
				int pieceValue = getPieceValue(piece.getType());
				
				// Add to our score if it's our piece, subtract if opponent's
				if (piece.getColor() == color) {
					score += pieceValue;
				} else {
					score -= pieceValue;
				}
			}
		}
		
		return score;
	}
	
	/**
	 * Get the material value of a chess piece.
	 */
	private int getPieceValue(ChessPieceType type) {
		switch (type) {
			case PAWN:
				return PAWN_VALUE;
			case KNIGHT:
				return KNIGHT_VALUE;
			case BISHOP:
				return BISHOP_VALUE;
			case ROOK:
				return ROOK_VALUE;
			case QUEEN:
				return QUEEN_VALUE;
			case KING:
				return KING_VALUE;
			default:
				return 0;
		}
	}
	
	/**
	 * Create a deep copy of the chess board for move simulation.
	 */
	private ChessBoard copyBoard(ChessBoard original) {
		ChessBoard copy = new ChessBoard();
		
		// Clear the copy's initial position
		for (int row = 0; row < ChessBoard.SIZE; row++) {
			for (int col = 0; col < ChessBoard.SIZE; col++) {
				copy.setPiece(row, col, null);
			}
		}
		
		// Copy all pieces from original
		for (int row = 0; row < ChessBoard.SIZE; row++) {
			for (int col = 0; col < ChessBoard.SIZE; col++) {
				ChessPiece piece = original.getPiece(row, col);
				if (piece != null) {
					copy.setPiece(row, col, new ChessPiece(piece.getType(), piece.getColor()));
				}
			}
		}
		
		return copy;
	}
}
