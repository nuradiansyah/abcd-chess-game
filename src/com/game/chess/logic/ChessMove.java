package com.game.chess.logic;

public class ChessMove {

	private final int fromRow;
    private final int fromCol;
    private final int toRow;
    private final int toCol;
    private final boolean isCastling;

    public ChessMove(int fromRow, int fromCol, int toRow, int toCol) {
        this(fromRow, fromCol, toRow, toCol, false);
    }
    
    public ChessMove(int fromRow, int fromCol, int toRow, int toCol, boolean isCastling) {
        this.fromRow = fromRow;
        this.fromCol = fromCol;
        this.toRow = toRow;
        this.toCol = toCol;
        this.isCastling = isCastling;
    }

    public int getFromRow() { return fromRow; }
    public int getFromCol() { return fromCol; }
    public int getToRow()   { return toRow; }
    public int getToCol()   { return toCol; }
    public boolean isCastling() { return isCastling; }
}
