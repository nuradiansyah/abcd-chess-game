package com.game.chess.gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.game.chess.logic.ChessBoard;
import com.game.chess.logic.ChessColor;
import com.game.chess.logic.ChessGameEngine;
import com.game.chess.logic.ChessMove;
import com.game.chess.logic.ChessPiece;
import com.game.chess.resources.ChessImageLoader;

public class ChessBoardPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = -1252813890138463622L;
	
	private final ChessGameEngine engine;
	private final ChessGUIManager guiManager;
    private final JButton[][] squares = new JButton[ChessBoard.SIZE][ChessBoard.SIZE];

    private int selectedRow = -1;
    private int selectedCol = -1;

    public ChessBoardPanel(ChessGameEngine engine, ChessGUIManager guiManager) {
        this.engine = engine;
        this.guiManager = guiManager;
        setLayout(new GridLayout(ChessBoard.SIZE, ChessBoard.SIZE));
        initSquares();
        refreshBoard();
    }

    private void initSquares() {
        for (int row = 0; row < ChessBoard.SIZE; row++) {
            for (int col = 0; col < ChessBoard.SIZE; col++) {
                JButton btn = new JButton();
                btn.setHorizontalAlignment(SwingConstants.CENTER);
                btn.setOpaque(true);
                btn.setBorderPainted(false);

                Color light = new Color(240, 217, 181);
                Color dark = new Color(181, 136, 99);
                btn.setBackground(((row + col) % 2 == 0) ? light : dark);

                btn.setActionCommand(row + "," + col);
                btn.addActionListener(this);
                squares[row][col] = btn;
                add(btn);
            }
        }
    }

    public void refreshBoard() {
        ChessBoard board = engine.getBoard();
        for (int row = 0; row < ChessBoard.SIZE; row++) {
            for (int col = 0; col < ChessBoard.SIZE; col++) {
                ChessPiece piece = board.getPiece(row, col);
                JButton btn = squares[row][col];
                if (piece != null) {
                    btn.setIcon(ChessImageLoader.loadPieceIcon(piece.getType(), piece.getColor()));
                } else {
                    btn.setIcon(null);
                }
            }
        }
        revalidate();
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    	
    	// If vs computer and it's the AI's turn, ignore all clicks
        if (engine.isVsComputer() && engine.getCurrentPlayer() == engine.getAIColor()) {
            return;
        }
        
        String[] parts = e.getActionCommand().split(",");
        int row = Integer.parseInt(parts[0]);
        int col = Integer.parseInt(parts[1]);

        if (selectedRow == -1) {
            // first click: select a piece
            ChessPiece piece = engine.getBoard().getPiece(row, col);
            if (piece != null && piece.getColor() == engine.getCurrentPlayer()) {
                selectedRow = row;
                selectedCol = col;
                // TODO: highlight possible moves
            }
        } else {
            // second click: attempt move
            // Check if this is a castling move (king moves 2 squares horizontally)
            ChessPiece selectedPiece = engine.getBoard().getPiece(selectedRow, selectedCol);
            boolean isCastling = false;
            if (selectedPiece != null && selectedPiece.getType() == com.game.chess.logic.ChessPieceType.KING) {
                int dc = Math.abs(col - selectedCol);
                if (selectedRow == row && dc == 2) {
                    isCastling = true;
                }
            }
            
            ChessMove move = new ChessMove(selectedRow, selectedCol, row, col, isCastling);
            boolean ok = engine.makePlayerMove(move);
            selectedRow = -1;
            selectedCol = -1;
            if (ok) {
                refreshBoard();
                guiManager.updateStatusLabel(); // after player move
                
                // Check if opponent's king was captured (player won)
                ChessColor opponentColor = engine.getAIColor() != null ? engine.getAIColor() : engine.getCurrentPlayer();
                if (engine.getBoard().isKingCaptured(opponentColor)) {
                    guiManager.handleGameEnd(true);
                    return;
                }
                
                engine.makeComputerMoveIfNeeded();
                refreshBoard();
                guiManager.updateStatusLabel(); // after computer move, if any
                
                // Check if player's king was captured (player lost)
                ChessColor playerColor = engine.getAIColor() != null ? engine.getAIColor().opposite() : engine.getCurrentPlayer().opposite();
                if (engine.getBoard().isKingCaptured(playerColor)) {
                    guiManager.handleGameEnd(false);
                    return;
                }
            } else {
                // Illegal move attempted
                guiManager.showIllegalMoveMessage();
            }
        }
    }
}
