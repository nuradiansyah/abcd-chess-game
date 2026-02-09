package com.game.chess.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.game.chess.logic.ChessColor;
import com.game.chess.logic.ChessGameEngine;
import com.game.chess.logic.LeaderboardEntry;
import com.game.chess.logic.LeaderboardManager;

public class ChessGUIManager extends JFrame {

	private static final long serialVersionUID = 1L;

    private ChessGameEngine engine;
    private ChessBoardPanel boardPanel;
    private JLabel statusLabel;
    private LeaderboardManager leaderboardManager;
    private String playerName;
    private ChessGameEngine.AILevel selectedAILevel;

    public ChessGUIManager() {
        setTitle("Chess Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 800); // Default size, will be adjusted per screen
        setLocationRelativeTo(null);
        
        leaderboardManager = new LeaderboardManager();

        showModeSelectionScreen();
    }

    private void showModeSelectionScreen() {
        getContentPane().removeAll();
        setSize(650, 480); // Increased size to accommodate emoji buttons with text
        setLocationRelativeTo(null); // Re-center after resize

        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JLabel title = new JLabel("Choose Game Mode");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(title, BorderLayout.NORTH);

        JPanel buttons = new JPanel(new GridLayout(3, 1, 10, 10));
        buttons.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        JButton twoPlayersBtn = new JButton("Two Players");
        JButton vsComputerBeginnerBtn = new JButton("Player vs Computer (Beginner)");
        JButton vsComputerIntermediateBtn = new JButton("Player vs Computer (Intermediate)");
        
        // Set font for better visibility
        Font buttonFont = new Font("Arial", Font.BOLD, 16);
        twoPlayersBtn.setFont(buttonFont);
        vsComputerBeginnerBtn.setFont(buttonFont);
        vsComputerIntermediateBtn.setFont(buttonFont);

        twoPlayersBtn.addActionListener((ActionEvent e) -> showPlayerNameInput(ChessGameEngine.AILevel.NONE));
        vsComputerBeginnerBtn.addActionListener((ActionEvent e) -> showPlayerNameInput(ChessGameEngine.AILevel.BEGINNER));
        vsComputerIntermediateBtn.addActionListener((ActionEvent e) -> showPlayerNameInput(ChessGameEngine.AILevel.INTERMEDIATE));

        buttons.add(twoPlayersBtn);
        buttons.add(vsComputerBeginnerBtn);
        buttons.add(vsComputerIntermediateBtn);
        panel.add(buttons, BorderLayout.CENTER);
        
        // Bottom panel with leaderboard and scoring info buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        // Create custom buttons with separate labels for emoji and text
        JButton leaderboardBtn = createIconTextButton("🏆", "View Leaderboard");
        JButton scoringInfoBtn = createIconTextButton("📊", "How to Score");
        
        leaderboardBtn.addActionListener(e -> showLeaderboard());
        scoringInfoBtn.addActionListener(e -> showScoringInfoDialog());
        
        bottomPanel.add(leaderboardBtn);
        bottomPanel.add(scoringInfoBtn);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        add(panel);
        revalidate();
        repaint();
    }

    private void showPlayerNameInput(ChessGameEngine.AILevel aiLevel) {
        selectedAILevel = aiLevel;
        getContentPane().removeAll();
        
        JPanel namePanel = new JPanel();
        namePanel.setLayout(new BorderLayout(10, 10));
        namePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Check if this is two-player mode
        boolean isTwoPlayerMode = (aiLevel == ChessGameEngine.AILevel.NONE);
        
        if (isTwoPlayerMode) {
            setSize(450, 300); // Slightly larger for two name inputs
            setLocationRelativeTo(null);
            
            JLabel titleLabel = new JLabel("Enter Player Names");
            titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
            titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
            namePanel.add(titleLabel, BorderLayout.NORTH);

            JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));

            JLabel whiteLabel = new JLabel("White Player:");
            whiteLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            JTextField whiteField = new JTextField("White Player");
            whiteField.setFont(new Font("Arial", Font.PLAIN, 16));

            JLabel blackLabel = new JLabel("Black Player:");
            blackLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            JTextField blackField = new JTextField("Black Player");
            blackField.setFont(new Font("Arial", Font.PLAIN, 16));

            inputPanel.add(whiteLabel);
            inputPanel.add(whiteField);
            inputPanel.add(blackLabel);
            inputPanel.add(blackField);

            JButton startButton = new JButton("Start Game");
            startButton.setFont(new Font("Arial", Font.BOLD, 18));
            startButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            startButton.addActionListener(e -> {
                String whiteName = whiteField.getText().trim();
                String blackName = blackField.getText().trim();
                if (whiteName.isEmpty()) {
                    whiteName = "White Player";
                }
                if (blackName.isEmpty()) {
                    blackName = "Black Player";
                }
                playerName = whiteName + " vs " + blackName;
                startGame(selectedAILevel);
            });

            getRootPane().setDefaultButton(startButton);
            blackField.addActionListener(e -> startButton.doClick());

            inputPanel.add(new JLabel());
            inputPanel.add(startButton);

            namePanel.add(inputPanel, BorderLayout.CENTER);
        } else {
            setSize(450, 250); // Compact size for single name input
            setLocationRelativeTo(null);
            
            JLabel titleLabel = new JLabel("Enter Your Name");
            titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
            titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
            namePanel.add(titleLabel, BorderLayout.NORTH);

            JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));

            JLabel nameLabel = new JLabel("Player Name:");
            nameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            JTextField nameField = new JTextField("Player");
            nameField.setFont(new Font("Arial", Font.PLAIN, 16));

            inputPanel.add(nameLabel);
            inputPanel.add(nameField);

            JButton startButton = new JButton("Start Game");
            startButton.setFont(new Font("Arial", Font.BOLD, 18));
            startButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            startButton.addActionListener(e -> {
                String name = nameField.getText().trim();
                if (name.isEmpty()) {
                    name = "Player";
                }
                playerName = name;
                startGame(selectedAILevel);
            });

            getRootPane().setDefaultButton(startButton);
            nameField.addActionListener(e -> startButton.doClick());

            inputPanel.add(new JLabel());
            inputPanel.add(startButton);

            namePanel.add(inputPanel, BorderLayout.CENTER);
        }
        
        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e -> showModeSelectionScreen());
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backPanel.add(backBtn);
        namePanel.add(backPanel, BorderLayout.SOUTH);

        add(namePanel);
        revalidate();
        repaint();
    }

    private void startGame(ChessGameEngine.AILevel aiLevel) {
        // Create engine first
        engine = new ChessGameEngine(aiLevel);
        
        // Create all components before modifying the window
        ChessBoardPanel newBoardPanel = new ChessBoardPanel(engine, this);
        
        JLabel newStatusLabel = new JLabel("White to move");
        newStatusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton resignBtn = new JButton("Resign");
        JButton drawBtn = new JButton("Declare Draw");
        JButton backBtn = new JButton("Main Menu");
        
        resignBtn.addActionListener(e -> handleResign());
        drawBtn.addActionListener(e -> handleDraw());
        backBtn.addActionListener(e -> showModeSelectionScreen());
        
        topPanel.add(resignBtn);
        topPanel.add(drawBtn);
        topPanel.add(backBtn);
        
        // Now update the window all at once
        getContentPane().removeAll();
        setLayout(new BorderLayout());
        
        add(newBoardPanel, BorderLayout.CENTER);
        add(newStatusLabel, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);
        
        // Assign to instance variables
        boardPanel = newBoardPanel;
        statusLabel = newStatusLabel;
        
        // Resize window for chess board
        setSize(800, 800);
        setLocationRelativeTo(null); // Re-center after resize
        
        updateStatusLabel(); // set initial "White to move"

        revalidate();
        repaint();
    }

    public void updateStatusLabel() {
    	 if (engine == null || statusLabel == null) {
             return;
         }
         ChessColor current = engine.getCurrentPlayer();
         String text = (current == ChessColor.WHITE) ? "White to move" : "Black to move";
         statusLabel.setText(text);
	}
	
	private void handleResign() {
	    if (engine == null || engine.isGameEnded()) {
	        return;
	    }
	    
	    int choice = JOptionPane.showConfirmDialog(
	        this,
	        "Are you sure you want to resign?",
	        "Resign Game",
	        JOptionPane.YES_NO_OPTION
	    );
	    
	    if (choice == JOptionPane.YES_OPTION) {
	        engine.setGameEnded(true);
	        handleGameEnd(false); // Player lost
	    }
	}
	
	private void handleDraw() {
	    if (engine == null || engine.isGameEnded()) {
	        return;
	    }
	    
	    int choice = JOptionPane.showConfirmDialog(
	        this,
	        "Declare this game as a draw?",
	        "Draw Game",
	        JOptionPane.YES_NO_OPTION
	    );
	    
	    if (choice == JOptionPane.YES_OPTION) {
	        engine.setGameEnded(true);
	        JOptionPane.showMessageDialog(this, "Game ended in a draw!");
	        showModeSelectionScreen();
	    }
	}
	
	public void handleGameEnd(boolean playerWon) {
	    if (engine.isGameEnded()) {
	        return;
	    }
	    
	    engine.setGameEnded(true);
	    
	    // Calculate score
	    int score = calculateScore(playerWon);
	    
	    String opponentType;
	    if (engine.getAILevel() == ChessGameEngine.AILevel.NONE) {
	        opponentType = "Human";
	    } else if (engine.getAILevel() == ChessGameEngine.AILevel.BEGINNER) {
	        opponentType = "Beginner AI";
	    } else {
	        opponentType = "Intermediate AI";
	    }
	    
	    String message = playerWon ? 
	        String.format("Congratulations %s! You won!\n\nYour Score: %d points\nMoves: %d\nOpponent: %s", 
	            playerName, score, engine.getMoveCount(), opponentType) :
	        String.format("Game Over! You lost.\n\nYour Score: %d points\nMoves: %d\nOpponent: %s", 
	            playerName, score, engine.getMoveCount(), opponentType);
	    
	    JOptionPane.showMessageDialog(this, message);
	    
	    // Add to leaderboard if playing against AI
	    if (engine.getAILevel() != ChessGameEngine.AILevel.NONE) {
	        if (leaderboardManager.wouldQualifyForLeaderboard(score)) {
	            LeaderboardEntry entry = new LeaderboardEntry(
	                playerName, 
	                score, 
	                playerWon ? 1 : 0, 
	                1, 
	                engine.getMoveCount(), 
	                opponentType
	            );
	            leaderboardManager.addEntry(entry);
	            
	            JOptionPane.showMessageDialog(
	                this,
	                "🏆 You made it to the leaderboard!",
	                "Leaderboard",
	                JOptionPane.INFORMATION_MESSAGE
	            );
	        }
	    }
	    
	    showModeSelectionScreen();
	}
	
	private int calculateScore(boolean won) {
	    if (!won) {
	        return 0; // No points for losing
	    }
	    
	    int baseScore = 1000; // Base points for winning
	    
	    // Difficulty multiplier
	    double difficultyMultiplier = 1.0;
	    if (engine.getAILevel() == ChessGameEngine.AILevel.BEGINNER) {
	        difficultyMultiplier = 1.5;
	    } else if (engine.getAILevel() == ChessGameEngine.AILevel.INTERMEDIATE) {
	        difficultyMultiplier = 2.5;
	    }
	    
	    // Efficiency bonus (fewer moves = better)
	    int moveBonus = Math.max(0, 500 - (engine.getMoveCount() * 5));
	    
	    int totalScore = (int) (baseScore * difficultyMultiplier) + moveBonus;
	    
	    return totalScore;
	}
	
	private void showLeaderboard() {
	    getContentPane().removeAll();
	    setSize(700, 500); // Appropriate size for leaderboard
	    setLocationRelativeTo(null); // Re-center after resize
	    
	    JPanel leaderboardPanel = new JPanel(new BorderLayout(10, 10));
	    leaderboardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	    
	    JLabel titleLabel = new JLabel("🏆 Leaderboard - Top 10 Players");
	    titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
	    titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
	    leaderboardPanel.add(titleLabel, BorderLayout.NORTH);
	    
	    JTextArea leaderboardText = new JTextArea();
	    leaderboardText.setEditable(false);
	    leaderboardText.setFont(new Font("Monospaced", Font.PLAIN, 14));
	    
	    StringBuilder sb = new StringBuilder();
	    sb.append(String.format("%-4s %-20s %-8s %-8s %-8s %-18s%n", 
	        "Rank", "Player", "Score", "Wins", "Moves", "Opponent"));
	    sb.append("=".repeat(80)).append("\n");
	    
	    var entries = leaderboardManager.getAllEntries();
	    if (entries.isEmpty()) {
	        sb.append("\nNo entries yet. Be the first to make the leaderboard!\n");
	    } else {
	        int rank = 1;
	        for (LeaderboardEntry entry : entries) {
	            sb.append(String.format("%-4d %-20s %-8d %-8d %-8d %-18s%n",
	                rank++,
	                entry.getPlayerName().length() > 20 ? 
	                    entry.getPlayerName().substring(0, 17) + "..." : entry.getPlayerName(),
	                entry.getScore(),
	                entry.getGamesWon(),
	                entry.getMovesCount(),
	                entry.getOpponentType()
	            ));
	        }
	    }
	    
	    leaderboardText.setText(sb.toString());
	    JScrollPane scrollPane = new JScrollPane(leaderboardText);
	    leaderboardPanel.add(scrollPane, BorderLayout.CENTER);
	    
	    JPanel buttonPanel = new JPanel(new FlowLayout());
	    JButton backBtn = new JButton("Back to Menu");
	    JButton clearBtn = new JButton("Clear Leaderboard");
	    
	    backBtn.addActionListener(e -> showModeSelectionScreen());
	    clearBtn.addActionListener(e -> {
	        int choice = JOptionPane.showConfirmDialog(
	            this,
	            "Are you sure you want to clear the leaderboard?",
	            "Clear Leaderboard",
	            JOptionPane.YES_NO_OPTION
	        );
	        if (choice == JOptionPane.YES_OPTION) {
	            leaderboardManager.clearAll();
	            showLeaderboard(); // Refresh
	        }
	    });
	    
	    buttonPanel.add(backBtn);
	    buttonPanel.add(clearBtn);
	    leaderboardPanel.add(buttonPanel, BorderLayout.SOUTH);
	    
	    add(leaderboardPanel);
	    revalidate();
	    repaint();
	}
	
	private void showScoringInfoDialog() {
	    String message =
	        "📊 HOW SCORING WORKS 📊\n\n" +
	        "Your final score is calculated based on:\n\n" +
	        "🎯 BASE SCORE (Win Only):\n" +
	        "   • Winning a game = 1000 base points\n" +
	        "   • Losing = 0 points\n\n" +
	        "🎮 DIFFICULTY MULTIPLIER:\n" +
	        "   • vs Human: 1.0x (No score recorded)\n" +
	        "   • vs Beginner AI: 1.5x (1,500 points)\n" +
	        "   • vs Intermediate AI: 2.5x (2,500 points)\n\n" +
	        "⚡ EFFICIENCY BONUS:\n" +
	        "   • Fewer moves = Higher bonus!\n" +
	        "   • Maximum 500 bonus points\n" +
	        "   • Formula: 500 - (moves × 5)\n" +
	        "   • Win in 20 moves = +400 bonus\n" +
	        "   • Win in 50 moves = +250 bonus\n\n" +
	        "💡 EXAMPLE:\n" +
	        "   • Beat Intermediate AI in 30 moves\n" +
	        "   • Base: 1000 × 2.5 = 2,500 points\n" +
	        "   • Efficiency: 500 - (30 × 5) = 350 points\n" +
	        "   • Final Score = 2,850 points\n\n" +
	        "🏆 TIPS TO MAXIMIZE YOUR SCORE:\n" +
	        "   • Play against harder AI for more points\n" +
	        "   • Win efficiently with fewer moves\n" +
	        "   • Plan your strategy carefully\n\n" +
	        "Only the TOP 10 scores make it to the leaderboard!";
	    
	    JOptionPane.showMessageDialog(
	        this,
	        message,
	        "How to Score",
	        JOptionPane.INFORMATION_MESSAGE
	    );
	}

	/**
	 * Creates a custom button with an emoji icon and text displayed side by side.
	 * This approach ensures both the emoji and text are visible on macOS.
	 */
	private JButton createIconTextButton(String emoji, String text) {
	    JButton button = new JButton();
	    button.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
	    
	    // Create emoji label with larger font
	    JLabel emojiLabel = new JLabel(emoji);
	    emojiLabel.setFont(new Font("Arial", Font.PLAIN, 18));
	    
	    // Create text label with normal font
	    JLabel textLabel = new JLabel(text);
	    textLabel.setFont(new Font("Arial", Font.PLAIN, 13));
	    
	    // Add both labels to the button
	    button.add(emojiLabel);
	    button.add(textLabel);
	    
	    // Set button size to accommodate both labels
	    button.setPreferredSize(new java.awt.Dimension(190, 40));
	    button.setCursor(new Cursor(Cursor.HAND_CURSOR));
	    
	    return button;
	}

	public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> new ChessGUIManager().setVisible(true));
    }
}
