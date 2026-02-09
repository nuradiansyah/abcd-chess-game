package com.game.chess.logic;

public class ChessGameEngine {

	public enum AILevel {
        NONE,
        BEGINNER,
        INTERMEDIATE,
        ADVANCED
    }

	private final ChessBoard board;
    private ChessColor currentPlayer;
    private final AILevel aiLevel;
    private ChessColor aiColor; // The color the AI plays (null for two-player mode)
    private final ChessAI basicAI;
    private final ChessAIIntermediate intermediateAI;
    private final ChessAIAdvanced advancedAI;
    private int moveCount;
    private boolean gameEnded;

    public ChessGameEngine(AILevel aiLevel) {
        this.board = new ChessBoard();
        this.currentPlayer = ChessColor.WHITE;
        this.aiLevel = aiLevel;
        this.aiColor = (aiLevel != AILevel.NONE) ? ChessColor.BLACK : null; // Default: AI plays black
        this.basicAI = (aiLevel == AILevel.BEGINNER) ? new ChessAI() : null;
        this.intermediateAI = (aiLevel == AILevel.INTERMEDIATE) ? new ChessAIIntermediate() : null;
        this.advancedAI = (aiLevel == AILevel.ADVANCED) ? new ChessAIAdvanced() : null;
        this.moveCount = 0;
        this.gameEnded = false;
    }
    
    /**
     * Set which color the AI plays (and player plays opposite).
     * Must be called before the game starts.
     */
    public void setAIColor(ChessColor color) {
        this.aiColor = color;
    }
    
    public ChessColor getAIColor() {
        return aiColor;
    }
    
    // Deprecated constructor for backward compatibility
    @Deprecated
    public ChessGameEngine(boolean vsComputer) {
        this(vsComputer ? AILevel.BEGINNER : AILevel.NONE);
    }

    public ChessBoard getBoard() {
        return board;
    }

    public ChessColor getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean isVsComputer() {
        return aiLevel != AILevel.NONE;
    }

    public boolean makePlayerMove(ChessMove move) {
    	
    	// 1) quick sanity: there must be a piece at from-square, and it must be currentPlayer
        ChessPiece piece = board.getPiece(move.getFromRow(), move.getFromCol());
        if (piece == null || piece.getColor() != currentPlayer) {
            return false;
        }

        // 2) ask board/logic if this move is legal (you'll implement this next)
        if (!board.isLegalMove(move, currentPlayer)) {
            return false;
        }

        // 3) apply if legal
        board.applyMove(move);
        moveCount++;
        switchTurn();
        return true;
    }
    
    public void makeComputerMoveIfNeeded() {
        if (aiLevel != AILevel.NONE && aiColor != null && currentPlayer == aiColor) {
            ChessMove move = null;
            
            // Choose AI based on difficulty level
            if (aiLevel == AILevel.BEGINNER && basicAI != null) {
                move = basicAI.chooseMove(board, currentPlayer);
            } else if (aiLevel == AILevel.INTERMEDIATE && intermediateAI != null) {
                move = intermediateAI.chooseMove(board, currentPlayer);
            } else if (aiLevel == AILevel.ADVANCED && advancedAI != null) {
                move = advancedAI.chooseMove(board, currentPlayer);
            }
            
            if (move != null) {
                board.applyMove(move);
                moveCount++;
                switchTurn();
            } else {
                System.out.println("Computer has no legal move.");
                gameEnded = true;
            }
        }
    }
    
    public int getMoveCount() {
        return moveCount;
    }
    
    public AILevel getAILevel() {
        return aiLevel;
    }
    
    public boolean isGameEnded() {
        return gameEnded;
    }
    
    public void setGameEnded(boolean ended) {
        this.gameEnded = ended;
    }

    private void switchTurn() {
        currentPlayer = currentPlayer.opposite();
    }
}
