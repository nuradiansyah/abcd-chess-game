package com.game.chess.resources;

import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;

import com.game.chess.logic.ChessColor;
import com.game.chess.logic.ChessPieceType;

public class ChessImageLoader {

	private static final int PIECE_SIZE = 64;

    public static ImageIcon loadPieceIcon(ChessPieceType type, ChessColor color) {
        String colorName = color == ChessColor.WHITE ? "white" : "black";
        String fileName = "/images/chess/" + colorName + "-" + type.name().toLowerCase() + ".png";

        URL resource = ChessImageLoader.class.getResource(fileName);
        if (resource == null) {
            System.err.println("Missing piece image: " + fileName);
            return null;
        }
        ImageIcon icon = new ImageIcon(resource);
        Image scaled = icon.getImage().getScaledInstance(PIECE_SIZE, PIECE_SIZE, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    public static ImageIcon loadBoardBackground() {
        String fileName = "/images/chess/board.png";
        URL resource = ChessImageLoader.class.getResource(fileName);
        if (resource == null) {
            return null;
        }
        return new ImageIcon(resource);
    }
}
