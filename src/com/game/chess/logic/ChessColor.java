package com.game.chess.logic;

public enum ChessColor {

	 WHITE, BLACK;

    public ChessColor opposite() {
        return this == WHITE ? BLACK : WHITE;
    }
}
