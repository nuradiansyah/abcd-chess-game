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
		// Check for checkmate or stalemate first
		if (board.isCheckmate(color)) {
			System.out.println(color + " is in checkmate!");
			return null; // Game over - checkmate
		}
		
		if (board.isStalemate(color)) {
			System.out.println(color + " is in stalemate (draw)!");
			return null; // Game over - stalemate (draw)
		}
		
		List<ChessMove> allMoves = new ArrayList<>();
		List<ChessMove> checkmateMoves = new ArrayList<>();
		
		// Generate all legal moves and identify checkmate moves
		for (int fromRow = 0; fromRow < ChessBoard.SIZE; fromRow++) {
			for (int fromCol = 0; fromCol < ChessBoard.SIZE; fromCol++) {
				ChessPiece piece = board.getPiece(fromRow, fromCol);
				if (piece == null || piece.getColor() != color) {
					continue;
				}
				
				// Check for castling moves if this is a king
				if (piece.getType() == ChessPieceType.KING) {
					// Try kingside castling
					ChessMove kingsideCastle = new ChessMove(fromRow, fromCol, fromRow, fromCol + 2, true);
					if (board.isLegalMove(kingsideCastle, color)) {
						allMoves.add(kingsideCastle);
					}
					// Try queenside castling
					ChessMove queensideCastle = new ChessMove(fromRow, fromCol, fromRow, fromCol - 2, true);
					if (board.isLegalMove(queensideCastle, color)) {
						allMoves.add(queensideCastle);
					}
				}
				
				for (int toRow = 0; toRow < ChessBoard.SIZE; toRow++) {
					for (int toCol = 0; toCol < ChessBoard.SIZE; toCol++) {
						ChessMove move = new ChessMove(fromRow, fromCol, toRow, toCol);
						if (!board.isLegalMove(move, color)) {
							continue;
						}
						
						allMoves.add(move);
						
						// Check if this move leads to checkmate
						ChessBoard testBoard = copyBoard(board);
						testBoard.applyMove(move);
						if (testBoard.isCheckmate(color.opposite())) {
							checkmateMoves.add(move);
						}
					}
				}
			}
		}
		
		if (allMoves.isEmpty()) {
			// This shouldn't happen as we checked above, but safety check
			return null;
		}
		
		// Always play checkmate if available!
		if (!checkmateMoves.isEmpty()) {
			System.out.println("AI found checkmate move!");
			return checkmateMoves.get(random.nextInt(checkmateMoves.size()));
		}
		
		// Evaluate and choose best move from all legal moves
		return chooseBestMove(board, allMoves, color);
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
	 * This uses material counting plus positional bonuses.
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
				int positionalBonus = getPositionalBonus(piece, row, col);
				
				// Add to our score if it's our piece, subtract if opponent's
				if (piece.getColor() == color) {
					score += pieceValue + positionalBonus;
				} else {
					score -= pieceValue + positionalBonus;
				}
			}
		}
		
		// Bonus for having the opponent in check
		if (board.isInCheck(color.opposite())) {
			score += 50;
		}
		
		// Penalty for being in check ourselves
		if (board.isInCheck(color)) {
			score -= 50;
		}
		
		// Bonus for mobility (number of legal moves available)
		int ourMobility = countLegalMoves(board, color);
		int theirMobility = countLegalMoves(board, color.opposite());
		score += (ourMobility - theirMobility) * 2;
		
		return score;
	}
	
	/**
	 * Get positional bonuses for piece placement.
	 * Central squares are more valuable, and pieces should be developed.
	 */
	private int getPositionalBonus(ChessPiece piece, int row, int col) {
		int bonus = 0;
		
		// Center control bonus (squares d4, d5, e4, e5 are most valuable)
		double centerDistance = Math.abs(3.5 - row) + Math.abs(3.5 - col);
		
		switch (piece.getType()) {
			case PAWN:
				// Pawns are better when advanced
				bonus = (piece.getColor() == ChessColor.WHITE) ? row * 5 : (7 - row) * 5;
				// Bonus for center pawns
				if (col >= 2 && col <= 5) bonus += 10;
				break;
				
			case KNIGHT:
				// Knights are best in the center
				bonus = (int) (20 - centerDistance * 5);
				break;
				
			case BISHOP:
				// Bishops like center and diagonals
				bonus = (int) (15 - centerDistance * 3);
				break;
				
			case ROOK:
				// Rooks prefer open files (simplified: just give small center bonus)
				bonus = (int) (5 - centerDistance * 1);
				break;
				
			case QUEEN:
				// Queen developed too early can be vulnerable, slight center preference
				bonus = (int) (10 - centerDistance * 2);
				break;
				
			case KING:
				// King should stay safe (near corners/back rank in opening/middlegame)
				// This is simplified - in endgame king should be centralized
				boolean backRank = (piece.getColor() == ChessColor.WHITE && row <= 1) || 
				                   (piece.getColor() == ChessColor.BLACK && row >= 6);
				if (backRank) bonus += 20;
				break;
		}
		
		return bonus;
	}
	
	/**
	 * Count the number of legal moves available for a given color.
	 * More moves = better mobility and control.
	 */
	private int countLegalMoves(ChessBoard board, ChessColor color) {
		int count = 0;
		
		for (int fromRow = 0; fromRow < ChessBoard.SIZE; fromRow++) {
			for (int fromCol = 0; fromCol < ChessBoard.SIZE; fromCol++) {
				ChessPiece piece = board.getPiece(fromRow, fromCol);
				if (piece == null || piece.getColor() != color) {
					continue;
				}
				
				// Check for castling moves if this is a king
				if (piece.getType() == ChessPieceType.KING) {
					// Try kingside castling
					ChessMove kingsideCastle = new ChessMove(fromRow, fromCol, fromRow, fromCol + 2, true);
					if (board.isLegalMove(kingsideCastle, color)) {
						count++;
					}
					// Try queenside castling
					ChessMove queensideCastle = new ChessMove(fromRow, fromCol, fromRow, fromCol - 2, true);
					if (board.isLegalMove(queensideCastle, color)) {
						count++;
					}
				}
				
				for (int toRow = 0; toRow < ChessBoard.SIZE; toRow++) {
					for (int toCol = 0; toCol < ChessBoard.SIZE; toCol++) {
						ChessMove move = new ChessMove(fromRow, fromCol, toRow, toCol);
						if (board.isLegalMove(move, color)) {
							count++;
						}
					}
				}
			}
		}
		
		return count;
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
