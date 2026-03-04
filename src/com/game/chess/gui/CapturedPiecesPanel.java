package com.game.chess.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.game.chess.logic.ChessColor;
import com.game.chess.logic.ChessGameEngine;
import com.game.chess.logic.ChessPiece;
import com.game.chess.resources.ChessImageLoader;

public class CapturedPiecesPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private final ChessGameEngine engine;
	private final JPanel whiteCapturedPanel;
	private final JPanel blackCapturedPanel;
	
	public CapturedPiecesPanel(ChessGameEngine engine) {
		this.engine = engine;
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(new Color(245, 245, 250));
		setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(new Color(100, 100, 120), 2),
			BorderFactory.createEmptyBorder(15, 15, 15, 15)
		));
		
		// Title with fancy styling
		JLabel titleLabel = new JLabel("Captured Pieces");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
		titleLabel.setForeground(new Color(50, 50, 80));
		titleLabel.setAlignmentX(CENTER_ALIGNMENT);
		add(titleLabel);
		add(Box.createRigidArea(new Dimension(0, 15)));
		
		// White pieces captured (by Black) - wrapped in panel to prevent truncation
		JPanel whiteLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		whiteLabelPanel.setBackground(new Color(245, 245, 250));
		whiteLabelPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
		JLabel whiteLabel = new JLabel("⬜ White Pieces:");
		whiteLabel.setFont(new Font("Arial", Font.BOLD, 14));
		whiteLabel.setForeground(new Color(60, 60, 60));
		whiteLabelPanel.add(whiteLabel);
		add(whiteLabelPanel);
		add(Box.createRigidArea(new Dimension(0, 5)));
		
		// Larger box: 4 pieces per row at 30px + spacing = ~140px width, 4 rows = ~160px height
		whiteCapturedPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 3));
		whiteCapturedPanel.setBackground(new Color(255, 255, 255));
		whiteCapturedPanel.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(new Color(200, 200, 200), 2),
			BorderFactory.createEmptyBorder(5, 5, 5, 5)
		));
		whiteCapturedPanel.setPreferredSize(new Dimension(180, 180));
		whiteCapturedPanel.setMinimumSize(new Dimension(180, 180));
		whiteCapturedPanel.setMaximumSize(new Dimension(180, 180));
		add(whiteCapturedPanel);
		
		add(Box.createRigidArea(new Dimension(0, 20)));
		
		// Black pieces captured (by White) - wrapped in panel to prevent truncation
		JPanel blackLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		blackLabelPanel.setBackground(new Color(245, 245, 250));
		blackLabelPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
		JLabel blackLabel = new JLabel("⬛ Black Pieces:");
		blackLabel.setFont(new Font("Arial", Font.BOLD, 14));
		blackLabel.setForeground(new Color(60, 60, 60));
		blackLabelPanel.add(blackLabel);
		add(blackLabelPanel);
		add(Box.createRigidArea(new Dimension(0, 5)));
		
		blackCapturedPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 3));
		blackCapturedPanel.setBackground(new Color(255, 255, 255));
		blackCapturedPanel.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(new Color(200, 200, 200), 2),
			BorderFactory.createEmptyBorder(5, 5, 5, 5)
		));
		blackCapturedPanel.setPreferredSize(new Dimension(180, 180));
		blackCapturedPanel.setMinimumSize(new Dimension(180, 180));
		blackCapturedPanel.setMaximumSize(new Dimension(180, 180));
		add(blackCapturedPanel);
		
		// Set preferred size for the whole panel
		setPreferredSize(new Dimension(220, 500));
	}
	
	/**
	 * Refresh the display of captured pieces from the current game state.
	 */
	public void refreshCapturedPieces() {
		whiteCapturedPanel.removeAll();
		blackCapturedPanel.removeAll();
		
		List<ChessPiece> capturedWhite = engine.getBoard().getCapturedWhitePieces();
		List<ChessPiece> capturedBlack = engine.getBoard().getCapturedBlackPieces();
		
		// Display captured white pieces
		for (ChessPiece piece : capturedWhite) {
			JLabel pieceLabel = new JLabel(
				ChessImageLoader.loadPieceIcon(piece.getType(), ChessColor.WHITE, 30)
			);
			whiteCapturedPanel.add(pieceLabel);
		}
		
		// Display captured black pieces
		for (ChessPiece piece : capturedBlack) {
			JLabel pieceLabel = new JLabel(
				ChessImageLoader.loadPieceIcon(piece.getType(), ChessColor.BLACK, 30)
			);
			blackCapturedPanel.add(pieceLabel);
		}
		
		whiteCapturedPanel.revalidate();
		whiteCapturedPanel.repaint();
		blackCapturedPanel.revalidate();
		blackCapturedPanel.repaint();
	}
}
