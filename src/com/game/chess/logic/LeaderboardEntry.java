package com.game.chess.logic;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LeaderboardEntry implements Serializable, Comparable<LeaderboardEntry> {
	
	private static final long serialVersionUID = 1L;
    
    private String playerName;
    private int score;
    private int gamesWon;
    private int gamesPlayed;
    private int movesCount;
    private String opponentType; // "Beginner AI", "Intermediate AI", or "Human"
    private LocalDateTime dateTime;
    
    public LeaderboardEntry(String playerName, int score, int gamesWon, 
            int gamesPlayed, int movesCount, String opponentType) {
        this.playerName = playerName;
        this.score = score;
        this.gamesWon = gamesWon;
        this.gamesPlayed = gamesPlayed;
        this.movesCount = movesCount;
        this.opponentType = opponentType;
        this.dateTime = LocalDateTime.now();
    }
    
    @Override
    public int compareTo(LeaderboardEntry other) {
        return Integer.compare(other.score, this.score); // Higher score first
    }
    
    public String getFormattedDateTime() {
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
    
    public double getWinRate() {
        return gamesPlayed > 0 ? (double) gamesWon / gamesPlayed * 100 : 0;
    }
    
    // Getters
    public String getPlayerName() { return playerName; }
    public int getScore() { return score; }
    public int getGamesWon() { return gamesWon; }
    public int getGamesPlayed() { return gamesPlayed; }
    public int getMovesCount() { return movesCount; }
    public String getOpponentType() { return opponentType; }
    public LocalDateTime getDateTime() { return dateTime; }
}
