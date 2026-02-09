package com.game.chess.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Advanced-level Chess AI using Minimax algorithm with alpha-beta pruning.
 * This AI thinks multiple moves ahead and considers opponent's best responses.
 */
public class ChessAIAdvanced {
	
	private final Random random = new Random();
	
	// Search depth - how many moves ahead the AI looks
	private static final int SEARCH_DEPTH = 3; // 3 ply = AI move, opponent response, AI counter
	
	/**
	 * Standard chess piece values (in centipawns, where 100 = 1 pawn)
	 */
	private static final int PAWN_VALUE = 100;
	private static final int KNIGHT_VALUE = 320;
	private static final int BISHOP_VALUE = 330;
	private static final int ROOK_VALUE = 500;
	private static final int QUEEN_VALUE = 900;
	private static final int KING_VALUE = 20000;

	/**
	 * Choose the best move using minimax algorithm with alpha-beta pruning.
	 */
	public ChessMove chooseMove(ChessBoard board, ChessColor color) {
		// Check for checkmate or stalemate first
		if (board.isCheckmate(color)) {
			System.out.println(color + " is in checkmate!");
			return null;
		}
		
		if (board.isStalemate(color)) {
			System.out.println(color + " is in stalemate (draw)!");
			return null;
		}
		
		List<ChessMove> allMoves = generateAllLegalMoves(board, color);
		
		if (allMoves.isEmpty()) {
			return null;
		}
		
		// Check for immediate checkmate moves
		for (ChessMove move : allMoves) {
			ChessBoard testBoard = copyBoard(board);
			testBoard.applyMove(move);
			if (testBoard.isCheckmate(color.opposite())) {
				System.out.println("Advanced AI found checkmate!");
				return move;
			}
		}
		
		// Use minimax to find the best move
		ChessMove bestMove = null;
		int bestScore = Integer.MIN_VALUE;
		List<ChessMove> equalMoves = new ArrayList<>();
		
		// Alpha-beta pruning parameters
		int alpha = Integer.MIN_VALUE;
		int beta = Integer.MAX_VALUE;
		
		for (ChessMove move : allMoves) {
			ChessBoard testBoard = copyBoard(board);
			testBoard.applyMove(move);
			
			// Minimize for opponent's response
			int score = minimax(testBoard, SEARCH_DEPTH - 1, alpha, beta, false, color);
			
			if (score > bestScore) {
				bestScore = score;
				bestMove = move;
				equalMoves.clear();
				equalMoves.add(move);
			} else if (score == bestScore) {
				equalMoves.add(move);
			}
			
			alpha = Math.max(alpha, score);
		}
		
		// Randomly choose among equally good moves to add variety
		return equalMoves.isEmpty() ? bestMove : equalMoves.get(random.nextInt(equalMoves.size()));
	}
	
	/**
	 * Minimax algorithm with alpha-beta pruning.
	 * 
	 * @param board Current board state
	 * @param depth Remaining search depth
	 * @param alpha Best value maximizer can guarantee
	 * @param beta Best value minimizer can guarantee
	 * @param maximizingPlayer True if current player is maximizing
	 * @param aiColor The AI's color (for evaluation)
	 * @return Best evaluation score
	 */
	private int minimax(ChessBoard board, int depth, int alpha, int beta, 
	                    boolean maximizingPlayer, ChessColor aiColor) {
		ChessColor currentColor = maximizingPlayer ? aiColor : aiColor.opposite();
		
		// Terminal conditions
		if (depth == 0) {
			return evaluatePosition(board, aiColor);
		}
		
		if (board.isCheckmate(currentColor)) {
			// Checkmate is very bad/good depending on who's checkmated
			return maximizingPlayer ? Integer.MIN_VALUE + 1000 : Integer.MAX_VALUE - 1000;
		}
		
		if (board.isStalemate(currentColor)) {
			return 0; // Stalemate is a draw
		}
		
		List<ChessMove> moves = generateAllLegalMoves(board, currentColor);
		
		if (moves.isEmpty()) {
			return evaluatePosition(board, aiColor);
		}
		
		if (maximizingPlayer) {
			int maxEval = Integer.MIN_VALUE;
			for (ChessMove move : moves) {
				ChessBoard testBoard = copyBoard(board);
				testBoard.applyMove(move);
				int eval = minimax(testBoard, depth - 1, alpha, beta, false, aiColor);
				maxEval = Math.max(maxEval, eval);
				alpha = Math.max(alpha, eval);
				if (beta <= alpha) {
					break; // Beta cutoff
				}
			}
			return maxEval;
		} else {
			int minEval = Integer.MAX_VALUE;
			for (ChessMove move : moves) {
				ChessBoard testBoard = copyBoard(board);
				testBoard.applyMove(move);
				int eval = minimax(testBoard, depth - 1, alpha, beta, true, aiColor);
				minEval = Math.min(minEval, eval);
				beta = Math.min(beta, eval);
				if (beta <= alpha) {
					break; // Alpha cutoff
				}
			}
			return minEval;
		}
	}
	
	/**
	 * Generate all legal moves for a given color.
	 */
	private List<ChessMove> generateAllLegalMoves(ChessBoard board, ChessColor color) {
		List<ChessMove> moves = new ArrayList<>();
		
		for (int fromRow = 0; fromRow < ChessBoard.SIZE; fromRow++) {
			for (int fromCol = 0; fromCol < ChessBoard.SIZE; fromCol++) {
				ChessPiece piece = board.getPiece(fromRow, fromCol);
				if (piece == null || piece.getColor() != color) {
					continue;
				}
				
				for (int toRow = 0; toRow < ChessBoard.SIZE; toRow++) {
					for (int toCol = 0; toCol < ChessBoard.SIZE; toCol++) {
						ChessMove move = new ChessMove(fromRow, fromCol, toRow, toCol);
						if (board.isLegalMove(move, color)) {
							moves.add(move);
						}
					}
				}
			}
		}
		
		return moves;
	}
	
	/**
	 * Evaluate the board position with material and positional considerations.
	 */
	private int evaluatePosition(ChessBoard board, ChessColor color) {
		int score = 0;
		
		// Material and positional evaluation
		for (int row = 0; row < ChessBoard.SIZE; row++) {
			for (int col = 0; col < ChessBoard.SIZE; col++) {
				ChessPiece piece = board.getPiece(row, col);
				if (piece == null) {
					continue;
				}
				
				int pieceValue = getPieceValue(piece.getType());
				int positionalBonus = getPositionalBonus(piece, row, col);
				
				if (piece.getColor() == color) {
					score += pieceValue + positionalBonus;
				} else {
					score -= pieceValue + positionalBonus;
				}
			}
		}
		
		// Check bonuses
		if (board.isInCheck(color.opposite())) {
			score += 50;
		}
		if (board.isInCheck(color)) {
			score -= 50;
		}
		
		// Mobility (number of legal moves)
		int ourMobility = countLegalMoves(board, color);
		int theirMobility = countLegalMoves(board, color.opposite());
		score += (ourMobility - theirMobility) * 3;
		
		// King safety: penalize exposed king
		score += evaluateKingSafety(board, color);
		score -= evaluateKingSafety(board, color.opposite());
		
		return score;
	}
	
	/**
	 * Evaluate king safety based on pawn shield and open lines.
	 */
	private int evaluateKingSafety(ChessBoard board, ChessColor color) {
		int safety = 0;
		
		// Find king position
		int kingRow = -1, kingCol = -1;
		for (int row = 0; row < ChessBoard.SIZE; row++) {
			for (int col = 0; col < ChessBoard.SIZE; col++) {
				ChessPiece piece = board.getPiece(row, col);
				if (piece != null && piece.getType() == ChessPieceType.KING && piece.getColor() == color) {
					kingRow = row;
					kingCol = col;
					break;
				}
			}
			if (kingRow != -1) break;
		}
		
		if (kingRow == -1) return 0;
		
		// Check for pawn shield (pawns in front of king)
		int direction = (color == ChessColor.WHITE) ? 1 : -1;
		int shieldRow = kingRow + direction;
		
		if (shieldRow >= 0 && shieldRow < ChessBoard.SIZE) {
			for (int c = Math.max(0, kingCol - 1); c <= Math.min(7, kingCol + 1); c++) {
				ChessPiece piece = board.getPiece(shieldRow, c);
				if (piece != null && piece.getType() == ChessPieceType.PAWN && piece.getColor() == color) {
					safety += 10;
				}
			}
		}
		
		return safety;
	}
	
	/**
	 * Get positional bonuses for piece placement.
	 */
	private int getPositionalBonus(ChessPiece piece, int row, int col) {
		int bonus = 0;
		double centerDistance = Math.abs(3.5 - row) + Math.abs(3.5 - col);
		
		switch (piece.getType()) {
			case PAWN:
				bonus = (piece.getColor() == ChessColor.WHITE) ? row * 5 : (7 - row) * 5;
				if (col >= 2 && col <= 5) bonus += 10;
				break;
			case KNIGHT:
				bonus = (int) (20 - centerDistance * 5);
				break;
			case BISHOP:
				bonus = (int) (15 - centerDistance * 3);
				break;
			case ROOK:
				bonus = (int) (5 - centerDistance * 1);
				break;
			case QUEEN:
				bonus = (int) (10 - centerDistance * 2);
				break;
			case KING:
				boolean backRank = (piece.getColor() == ChessColor.WHITE && row <= 1) || 
				                   (piece.getColor() == ChessColor.BLACK && row >= 6);
				if (backRank) bonus += 20;
				break;
		}
		
		return bonus;
	}
	
	/**
	 * Count legal moves for mobility evaluation.
	 */
	private int countLegalMoves(ChessBoard board, ChessColor color) {
		int count = 0;
		
		for (int fromRow = 0; fromRow < ChessBoard.SIZE; fromRow++) {
			for (int fromCol = 0; fromCol < ChessBoard.SIZE; fromCol++) {
				ChessPiece piece = board.getPiece(fromRow, fromCol);
				if (piece == null || piece.getColor() != color) {
					continue;
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
	 * Create a deep copy of the chess board.
	 */
	private ChessBoard copyBoard(ChessBoard original) {
		ChessBoard copy = new ChessBoard();
		
		for (int row = 0; row < ChessBoard.SIZE; row++) {
			for (int col = 0; col < ChessBoard.SIZE; col++) {
				copy.setPiece(row, col, null);
			}
		}
		
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
