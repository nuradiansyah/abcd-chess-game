package com.game.chess.gui;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Color;
import javax.swing.border.AbstractBorder;

public class RoundedBorder extends AbstractBorder {
    
    private static final long serialVersionUID = 1L;
    private Color color;
    private int thickness;
    private int radius;
    private Insets insets;
    
    public RoundedBorder(Color color, int thickness, int radius) {
        this.color = color;
        this.thickness = thickness;
        this.radius = radius;
        this.insets = new Insets(thickness + 5, thickness + 10, thickness + 5, thickness + 10);
    }
    
    public RoundedBorder(Color color, int thickness, int radius, Insets insets) {
        this.color = color;
        this.thickness = thickness;
        this.radius = radius;
        this.insets = insets;
    }
    
    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(color);
        
        for (int i = 0; i < thickness; i++) {
            g2d.drawRoundRect(x + i, y + i, width - 2 * i - 1, height - 2 * i - 1, radius, radius);
        }
        
        g2d.dispose();
    }
    
    @Override
    public Insets getBorderInsets(Component c) {
        return insets;
    }
    
    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.left = this.insets.left;
        insets.top = this.insets.top;
        insets.right = this.insets.right;
        insets.bottom = this.insets.bottom;
        return insets;
    }
}
