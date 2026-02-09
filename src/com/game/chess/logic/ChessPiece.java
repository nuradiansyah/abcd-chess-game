package com.game.chess.logic;

public class ChessPiece {

	 private final ChessPieceType type;
	    private final ChessColor color;

	    public ChessPiece(ChessPieceType type, ChessColor color) {
	        this.type = type;
	        this.color = color;
	    }

	    public ChessPieceType getType() {
	        return type;
	    }

	    public ChessColor getColor() {
	        return color;
	    }

	    @Override
	    public String toString() {
	        return color + " " + type;
	    }
}
