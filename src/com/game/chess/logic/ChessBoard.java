package com.game.chess.logic;

public class ChessBoard {

	public static final int SIZE = 8;

    private final ChessPiece[][] board = new ChessPiece[SIZE][SIZE];

    public ChessBoard() {
        setupInitialPosition();
    }

    private void setupInitialPosition() {
        // Pawns
        for (int c = 0; c < SIZE; c++) {
            board[1][c] = new ChessPiece(ChessPieceType.PAWN, ChessColor.WHITE);
            board[6][c] = new ChessPiece(ChessPieceType.PAWN, ChessColor.BLACK);
        }
        // Rooks
        board[0][0] = new ChessPiece(ChessPieceType.ROOK, ChessColor.WHITE);
        board[0][7] = new ChessPiece(ChessPieceType.ROOK, ChessColor.WHITE);
        board[7][0] = new ChessPiece(ChessPieceType.ROOK, ChessColor.BLACK);
        board[7][7] = new ChessPiece(ChessPieceType.ROOK, ChessColor.BLACK);
        // Knights
        board[0][1] = new ChessPiece(ChessPieceType.KNIGHT, ChessColor.WHITE);
        board[0][6] = new ChessPiece(ChessPieceType.KNIGHT, ChessColor.WHITE);
        board[7][1] = new ChessPiece(ChessPieceType.KNIGHT, ChessColor.BLACK);
        board[7][6] = new ChessPiece(ChessPieceType.KNIGHT, ChessColor.BLACK);
        // Bishops
        board[0][2] = new ChessPiece(ChessPieceType.BISHOP, ChessColor.WHITE);
        board[0][5] = new ChessPiece(ChessPieceType.BISHOP, ChessColor.WHITE);
        board[7][2] = new ChessPiece(ChessPieceType.BISHOP, ChessColor.BLACK);
        board[7][5] = new ChessPiece(ChessPieceType.BISHOP, ChessColor.BLACK);
        // Queens
        board[0][3] = new ChessPiece(ChessPieceType.QUEEN, ChessColor.WHITE);
        board[7][3] = new ChessPiece(ChessPieceType.QUEEN, ChessColor.BLACK);
        // Kings
        board[0][4] = new ChessPiece(ChessPieceType.KING, ChessColor.WHITE);
        board[7][4] = new ChessPiece(ChessPieceType.KING, ChessColor.BLACK);
    }

    public ChessPiece getPiece(int row, int col) {
        return board[row][col];
    }

    public void setPiece(int row, int col, ChessPiece piece) {
        board[row][col] = piece;
    }

    public void applyMove(ChessMove move) {
        ChessPiece piece = board[move.getFromRow()][move.getFromCol()];
        board[move.getFromRow()][move.getFromCol()] = null;
        board[move.getToRow()][move.getToCol()] = piece;
    }
    
    public boolean isKingCaptured(ChessColor color) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                ChessPiece piece = board[row][col];
                if (piece != null && piece.getType() == ChessPieceType.KING && piece.getColor() == color) {
                    return false; // King still exists
                }
            }
        }
        return true; // King not found, was captured
    }
    
    /**
     * Check if the king of the given color is currently under attack (in check).
     */
    public boolean isInCheck(ChessColor kingColor) {
        // Find the king's position
        int kingRow = -1, kingCol = -1;
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                ChessPiece piece = board[row][col];
                if (piece != null && piece.getType() == ChessPieceType.KING && piece.getColor() == kingColor) {
                    kingRow = row;
                    kingCol = col;
                    break;
                }
            }
            if (kingRow != -1) break;
        }
        
        if (kingRow == -1) {
            // King not found (shouldn't happen in normal game)
            return false;
        }
        
        // Check if any opponent piece can attack the king's position
        ChessColor opponentColor = kingColor.opposite();
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                ChessPiece piece = board[row][col];
                if (piece != null && piece.getColor() == opponentColor) {
                    ChessMove attackMove = new ChessMove(row, col, kingRow, kingCol);
                    // Check if this piece can legally move to the king's position
                    // We use a simplified check that doesn't verify if the move leaves the attacker's king in check
                    if (isLegalMoveIgnoringCheck(attackMove, opponentColor)) {
                        return true;
                    }
                }
            }
        }
        
        return false;
    }
    
    /**
     * Check if the given color is in checkmate (in check with no legal moves).
     */
    public boolean isCheckmate(ChessColor color) {
        if (!isInCheck(color)) {
            return false; // Not in check, so can't be checkmate
        }
        return !hasAnyLegalMove(color);
    }
    
    /**
     * Check if the given color is in stalemate (not in check but no legal moves).
     */
    public boolean isStalemate(ChessColor color) {
        if (isInCheck(color)) {
            return false; // In check, so can't be stalemate
        }
        return !hasAnyLegalMove(color);
    }
    
    /**
     * Check if the given color has any legal move available.
     */
    public boolean hasAnyLegalMove(ChessColor color) {
        for (int fromRow = 0; fromRow < SIZE; fromRow++) {
            for (int fromCol = 0; fromCol < SIZE; fromCol++) {
                ChessPiece piece = board[fromRow][fromCol];
                if (piece == null || piece.getColor() != color) {
                    continue;
                }
                
                for (int toRow = 0; toRow < SIZE; toRow++) {
                    for (int toCol = 0; toCol < SIZE; toCol++) {
                        ChessMove move = new ChessMove(fromRow, fromCol, toRow, toCol);
                        if (isLegalMove(move, color)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    
    /**
     * Check if a move is legal according to piece movement rules only,
     * without checking if it leaves the player's own king in check.
     * Used internally for check detection.
     */
    private boolean isLegalMoveIgnoringCheck(ChessMove move, ChessColor player) {
        int fromRow = move.getFromRow();
        int fromCol = move.getFromCol();
        int toRow = move.getToRow();
        int toCol = move.getToCol();

        if (!isInsideBoard(fromRow, fromCol) || !isInsideBoard(toRow, toCol)) {
            return false;
        }

        ChessPiece piece = board[fromRow][fromCol];
        if (piece == null || piece.getColor() != player) {
            return false;
        }

        ChessPiece target = board[toRow][toCol];
        if (target != null && target.getColor() == player) {
            return false;
        }

        // dispatch to piece-specific rules
        switch (piece.getType()) {
            case PAWN:
                return isLegalPawnMove(fromRow, fromCol, toRow, toCol, piece.getColor(), target);
            case KNIGHT:
                return isLegalKnightMove(fromRow, fromCol, toRow, toCol);
            case BISHOP:
                return isLegalBishopMove(fromRow, fromCol, toRow, toCol);
            case ROOK:
                return isLegalRookMove(fromRow, fromCol, toRow, toCol);
            case QUEEN:
                return isLegalQueenMove(fromRow, fromCol, toRow, toCol);
            case KING:
                return isLegalKingMove(fromRow, fromCol, toRow, toCol);
            default:
                return false;
        }
    }

    public boolean isInsideBoard(int row, int col) {
        return row >= 0 && row < SIZE && col >= 0 && col < SIZE;
    }

    public boolean isLegalMove(ChessMove move, ChessColor player) {
        int fromRow = move.getFromRow();
        int fromCol = move.getFromCol();
        int toRow = move.getToRow();
        int toCol = move.getToCol();

        if (!isInsideBoard(fromRow, fromCol) || !isInsideBoard(toRow, toCol)) {
            return false;
        }

        ChessPiece piece = board[fromRow][fromCol];
        if (piece == null || piece.getColor() != player) {
            return false;
        }

        ChessPiece target = board[toRow][toCol];
        // can't capture your own piece
        if (target != null && target.getColor() == player) {
            return false;
        }

        // Check piece-specific movement rules first
        boolean validMove = false;
        switch (piece.getType()) {
            case PAWN:
                validMove = isLegalPawnMove(fromRow, fromCol, toRow, toCol, piece.getColor(), target);
                break;
            case KNIGHT:
                validMove = isLegalKnightMove(fromRow, fromCol, toRow, toCol);
                break;
            case BISHOP:
                validMove = isLegalBishopMove(fromRow, fromCol, toRow, toCol);
                break;
            case ROOK:
                validMove = isLegalRookMove(fromRow, fromCol, toRow, toCol);
                break;
            case QUEEN:
                validMove = isLegalQueenMove(fromRow, fromCol, toRow, toCol);
                break;
            case KING:
                validMove = isLegalKingMove(fromRow, fromCol, toRow, toCol);
                break;
            default:
                return false;
        }
        
        if (!validMove) {
            return false;
        }
        
        // Critical: Check if this move would leave our king in check
        // This is a fundamental rule of chess - you cannot make a move that puts/leaves your king in check
        ChessPiece capturedPiece = board[toRow][toCol];
        board[fromRow][fromCol] = null;
        board[toRow][toCol] = piece;
        
        boolean leavesKingInCheck = isInCheck(player);
        
        // Undo the move
        board[fromRow][fromCol] = piece;
        board[toRow][toCol] = capturedPiece;
        
        return !leavesKingInCheck;
    }
    
    private boolean isPathClear(int fromRow, int fromCol, int toRow, int toCol) {
        int dRow = Integer.compare(toRow, fromRow);
        int dCol = Integer.compare(toCol, fromCol);

        int r = fromRow + dRow;
        int c = fromCol + dCol;
        while (r != toRow || c != toCol) {
            if (board[r][c] != null) {
                return false;
            }
            r += dRow;
            c += dCol;
        }
        return true;
    }

    private boolean isLegalKnightMove(int fromRow, int fromCol, int toRow, int toCol) {
        int dr = Math.abs(toRow - fromRow);
        int dc = Math.abs(toCol - fromCol);
        return (dr == 2 && dc == 1) || (dr == 1 && dc == 2);
    }
    
    private boolean isLegalBishopMove(int fromRow, int fromCol, int toRow, int toCol) {
        int dr = Math.abs(toRow - fromRow);
        int dc = Math.abs(toCol - fromCol);
        if (dr != dc) return false;
        return isPathClear(fromRow, fromCol, toRow, toCol);
    }

    private boolean isLegalRookMove(int fromRow, int fromCol, int toRow, int toCol) {
        if (fromRow != toRow && fromCol != toCol) return false;
        return isPathClear(fromRow, fromCol, toRow, toCol);
    }
    
    private boolean isLegalQueenMove(int fromRow, int fromCol, int toRow, int toCol) {
        int dr = Math.abs(toRow - fromRow);
        int dc = Math.abs(toCol - fromCol);
        if (dr == dc || fromRow == toRow || fromCol == toCol) {
            return isPathClear(fromRow, fromCol, toRow, toCol);
        }
        return false;
    }

    private boolean isLegalKingMove(int fromRow, int fromCol, int toRow, int toCol) {
        int dr = Math.abs(toRow - fromRow);
        int dc = Math.abs(toCol - fromCol);
        return dr <= 1 && dc <= 1;
    }
    
    private boolean isLegalPawnMove(int fromRow, int fromCol, int toRow, int toCol,
            ChessColor color, ChessPiece target) {
			int dir = (color == ChessColor.WHITE) ? 1 : -1;
			int startRow = (color == ChessColor.WHITE) ? 1 : 6;
			
			int dr = toRow - fromRow;
			int dc = Math.abs(toCol - fromCol);
			
			// forward move
			if (dc == 0) {
				// must be empty
				if (target != null) return false;
				// one step forward
				if (dr == dir) return true;
				// two steps from starting rank
				if (fromRow == startRow && dr == 2 * dir) {
					// intermediate square must be empty
					int midRow = fromRow + dir;
					if (board[midRow][fromCol] != null) return false;
					return true;
				}
				return false;
			}
		
			// capture
			if (dc == 1 && dr == dir) {
				return target != null; // must capture something
			}
		
			return false;
		}
	
	}
