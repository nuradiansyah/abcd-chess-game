package com.game.chess.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JButton;

public class RoundedButton extends JButton {
    
    private static final long serialVersionUID = 1L;
    private int cornerRadius;
    
    public RoundedButton(String text, int cornerRadius) {
        super(text);
        this.cornerRadius = cornerRadius;
        setOpaque(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Paint background with rounded corners
        if (getModel().isPressed()) {
            g2d.setColor(getBackground().darker());
        } else if (getModel().isRollover()) {
            g2d.setColor(getBackground().brighter());
        } else {
            g2d.setColor(getBackground());
        }
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
        
        g2d.dispose();
        super.paintComponent(g);
    }
    
    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Get border color from RoundedBorder if present
        if (getBorder() instanceof RoundedBorder) {
            // Border will be painted by RoundedBorder class
            super.paintBorder(g);
        }
        
        g2d.dispose();
    }
    
    @Override
    public boolean contains(int x, int y) {
        // Make click area match rounded shape
        Shape shape = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
        return shape.contains(x, y);
    }
}
