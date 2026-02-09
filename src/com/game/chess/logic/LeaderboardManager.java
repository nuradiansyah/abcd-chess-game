package com.game.chess.logic;

import java.io.*;
import java.util.*;

public class LeaderboardManager {

    private String leaderboardFile;
    private static final String LEADERBOARD_FILE = "chess_leaderboard.dat";
    private static final int MAX_ENTRIES = 10;
    
    private List<LeaderboardEntry> entries;
    
    public LeaderboardManager() {
        this.leaderboardFile = LEADERBOARD_FILE;
        entries = new ArrayList<>();
        loadLeaderboard();
    }
    
    public LeaderboardManager(String filename) {
        this.leaderboardFile = filename;
        entries = new ArrayList<>();
        loadLeaderboard();
    }
    
    public void addEntry(LeaderboardEntry entry) {
        entries.add(entry);
        Collections.sort(entries);
        
        // Keep only top entries - create a new ArrayList to avoid SubList serialization issues
        if (entries.size() > MAX_ENTRIES) {
            entries = new ArrayList<>(entries.subList(0, MAX_ENTRIES));
        }
        
        saveLeaderboard();
    }
    
    public List<LeaderboardEntry> getTopEntries(int count) {
        return entries.subList(0, Math.min(count, entries.size()));
    }
    
    public List<LeaderboardEntry> getAllEntries() {
        return new ArrayList<>(entries);
    }
    
    @SuppressWarnings("unchecked")
    private void loadLeaderboard() {
        String fileToUse = (leaderboardFile != null) ? leaderboardFile : LEADERBOARD_FILE;
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(fileToUse))) {
            entries = (List<LeaderboardEntry>) ois.readObject();
        } catch (FileNotFoundException e) {
            // File doesn't exist yet, start with empty list
            entries = new ArrayList<>();
        } catch (java.io.InvalidClassException | java.io.NotSerializableException e) {
            // Corrupted or incompatible file
            System.err.println("Corrupted leaderboard file detected. Deleting and starting fresh...");
            File file = new File(fileToUse);
            if (file.exists()) {
                file.delete();
            }
            entries = new ArrayList<>();
        } catch (Exception e) {
            System.err.println("Error loading leaderboard: " + e.getMessage());
            // Try to delete corrupted file
            File file = new File(fileToUse);
            if (file.exists()) {
                file.delete();
                System.err.println("Deleted corrupted leaderboard file. Starting fresh...");
            }
            entries = new ArrayList<>();
        }
    }
    
    private void saveLeaderboard() {
        String fileToUse = (leaderboardFile != null) ? leaderboardFile : LEADERBOARD_FILE;
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(fileToUse))) {
            oos.writeObject(entries);
        } catch (IOException e) {
            System.err.println("Error saving leaderboard: " + e.getMessage());
        }
    }

    public void clearAll() {
        entries.clear();
        saveLeaderboard();
    }
    
    // Check if a player name already exists in the leaderboard
    public boolean playerNameExists(String playerName) {
        for (LeaderboardEntry entry : entries) {
            if (entry.getPlayerName().equalsIgnoreCase(playerName.trim())) {
                return true;
            }
        }
        return false;
    }
    
    // Check if a score would qualify for the leaderboard (top 10)
    public boolean wouldQualifyForLeaderboard(int score) {
        // If we have less than MAX_ENTRIES, all scores qualify
        if (entries.size() < MAX_ENTRIES) {
            return true;
        }
        
        // Check if the score is higher than the lowest score in the leaderboard
        int lowestScore = entries.get(entries.size() - 1).getScore();
        return score > lowestScore;
    }
    
    // Get the minimum score needed to enter the leaderboard
    public int getMinimumScoreToQualify() {
        if (entries.size() < MAX_ENTRIES) {
            return 0; // Any score qualifies if leaderboard isn't full
        }
        return entries.get(entries.size() - 1).getScore();
    }
}
