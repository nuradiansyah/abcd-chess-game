# Chess Game - Leaderboard System

## Overview
The Chess Game now includes a comprehensive leaderboard system similar to the Memory Game, tracking player performance across different difficulty levels.

## Features

### 1. **Player Name Input**
- Before starting any game, players enter their name
- Names are tracked for leaderboard entries

### 2. **Three Game Modes**
- **Two Players**: Human vs Human (No leaderboard tracking)
- **Player vs Computer (Beginner)**: Against basic AI (1.5x score multiplier)
- **Player vs Computer (Intermediate)**: Against smart AI (2.5x score multiplier)

### 3. **Score Calculation System**

#### Base Score (Win Only)
- Winning a game: **1000 base points**
- Losing: **0 points**

#### Difficulty Multiplier
- vs Human: **1.0x** (No score recorded)
- vs Beginner AI: **1.5x** (1,500 points)
- vs Intermediate AI: **2.5x** (2,500 points)

#### Efficiency Bonus
- Formula: `500 - (moves × 5)`
- Maximum: **500 bonus points**
- Fewer moves = Higher bonus
- Examples:
  - Win in 20 moves: +400 bonus
  - Win in 50 moves: +250 bonus
  - Win in 100+ moves: 0 bonus

#### Total Score Formula
```
Total Score = (Base Score × Difficulty Multiplier) + Efficiency Bonus
```

### 4. **Example Calculations**

**Example 1: Beat Beginner AI in 25 moves**
- Base: 1000 × 1.5 = 1,500 points
- Efficiency: 500 - (25 × 5) = 375 points
- **Final Score: 1,875 points**

**Example 2: Beat Intermediate AI in 30 moves**
- Base: 1000 × 2.5 = 2,500 points
- Efficiency: 500 - (30 × 5) = 350 points
- **Final Score: 2,850 points**

**Example 3: Beat Intermediate AI in 15 moves**
- Base: 1000 × 2.5 = 2,500 points
- Efficiency: 500 - (15 × 5) = 425 points
- **Final Score: 2,925 points** ⭐ High score!

### 5. **Leaderboard Features**
- Displays **Top 10** scores only
- Shows: Rank, Player Name, Score, Wins, Moves, Opponent Type
- Persistent storage (saves to `chess_leaderboard.dat`)
- Can be cleared from the UI

### 6. **Game End Conditions**
- **Victory**: Capture opponent's king
- **Resign**: Player chooses to resign (counts as loss)
- **Draw**: Mutual agreement (no score recorded)

### 7. **Tips to Maximize Score**
1. Play against harder AI (Intermediate) for higher multiplier
2. Win efficiently with fewer moves
3. Plan your strategy carefully
4. Each move reduces your efficiency bonus by 5 points

## UI Components

### Main Menu
- Three game mode buttons
- 🏆 **View Leaderboard** button
- 📊 **How to Score** button (shows scoring explanation)

### During Game
- **Resign** button: Give up (counts as loss, 0 points)
- **Declare Draw** button: End game in draw (no score)
- **Main Menu** button: Return to menu

### Leaderboard Screen
- Shows all entries in ranked order
- **Back to Menu** button
- **Clear Leaderboard** button

## Technical Implementation

### Files Created
1. `LeaderboardEntry.java` - Data model for leaderboard entries
2. `LeaderboardManager.java` - Handles loading/saving/managing leaderboard
3. Updated `ChessGameEngine.java` - Added move counting and game state tracking
4. Updated `ChessGUIManager.java` - Added UI for leaderboard and scoring info
5. Updated `ChessBoard.java` - Added king capture detection
6. Updated `ChessBoardPanel.java` - Added game end detection

### Data Persistence
- Leaderboard stored in: `chess_leaderboard.dat`
- Uses Java serialization
- Automatically handles corrupted files
- Top 10 entries kept

## Differences from Memory Game Leaderboard

While modeled after the Memory Game's leaderboard, the Chess Game scoring has these differences:

| Aspect | Memory Game | Chess Game |
|--------|-------------|------------|
| Base Scoring | Pairs matched (100 per pair) | Win/Loss (1000 for win) |
| Bonus System | Time remaining per match | Efficiency (fewer moves) |
| Difficulty | Same for all games | AI difficulty multiplier |
| Game Modes | Always 2 players | 1 player vs AI or 2 players |
| Tracked Stats | Pairs found, time | Wins, moves, opponent type |

Both systems reward efficiency and skill, but Chess focuses on strategic victory against AI opponents.
