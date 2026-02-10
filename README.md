# Chess Game

This project is a fully-featured Java-based Chess Game that I developed in my spare time. It includes multiple AI difficulty levels, a comprehensive leaderboard system, and an intuitive graphical interface. The game was created as a way to practice Java programming, game logic implementation, and GUI design with the help of GitHub Copilot.

## 🎮 Features

### Game Modes
- **Two Players Mode**: Play chess against another person on the same computer
- **Player vs Computer (Beginner)**: Practice against a basic AI that makes random moves with capture preference
- **Player vs Computer (Intermediate)**: Challenge yourself against a smarter AI with positional evaluation and tactical awareness
- **Player vs Computer (Advanced)**: Face the strongest AI with minimax algorithm and advanced strategic evaluation

### Chess Features
- **Full Chess Rules Implementation**: All standard chess rules including castling, en passant, and pawn promotion
- **Legal Move Validation**: Only legal moves are allowed, preventing moves that would put your own king in check
- **Check & Checkmate Detection**: Automatic detection of check, checkmate, and stalemate conditions
- **Visual Feedback**: Clear highlighting of selected pieces, possible moves, and check warnings
- **Move History Tracking**: Complete tracking of all moves made during the game
- **Color Selection**: When playing against AI, choose to play as White or Black

### Leaderboard System
- **Top 10 Rankings**: Compete for the highest scores against AI opponents
- **Score Calculation**: Sophisticated scoring based on:
  - **Base Score**: 1000 points for winning
  - **Difficulty Multiplier**: 1.5x (Beginner), 2.5x (Intermediate), 4.0x (Advanced)
  - **Efficiency Bonus**: Up to 500 bonus points for winning with fewer moves
- **Persistent Storage**: Leaderboard data saved locally in `chess_leaderboard.dat`
- **Detailed Statistics**: Track player names, scores, wins, move counts, and opponent types

### User Interface
- **Clean Design**: Modern Swing-based GUI with intuitive controls
- **Game Status Display**: Real-time status updates showing whose turn it is and game state
- **Interactive Board**: Click to select pieces and click destination squares to move
- **Menu Navigation**: Easy access to main menu, leaderboard, and game options
- **Resign & Draw Options**: Resign button available in all modes, mutual draw option in two-player mode

## 🎯 How to Play

### Starting the Game
1. Run `ChessGUIManager.java` as the main class
2. Choose your game mode from the main menu
3. Enter player name(s) as prompted
4. If playing against AI, select your piece color (White or Black)
5. Start playing!

### Making Moves
1. Click on a piece you want to move (must be your color)
2. Valid destination squares will be highlighted
3. Click on a highlighted square to make your move
4. The game will automatically validate your move and switch turns

### Game Controls
- **Resign**: Give up the current game
- **Declare Draw**: (Two-player mode only) Offer a draw to your opponent
- **Main Menu**: Return to the main menu (with confirmation if game is active)

### Winning & Scoring
- Win by checkmating your opponent's king
- Games end in a draw if stalemate occurs or by mutual agreement
- Score is only tracked when playing against AI opponents
- Higher difficulty levels and fewer moves result in higher scores

## 📊 Scoring System

### Score Formula
```
Total Score = (Base Score × Difficulty Multiplier) + Efficiency Bonus
```

### Breakdown
- **Base Score**: 1000 points (win only, 0 for loss)
- **Difficulty Multipliers**:
  - Beginner AI: 1.5x → 1,500 base points
  - Intermediate AI: 2.5x → 2,500 base points
  - Advanced AI: 4.0x → 4,000 base points
- **Efficiency Bonus**: `500 - (moves × 5)` (maximum 500 points)

### Example
Beat Intermediate AI in 30 moves:
- Base: 1000 × 2.5 = 2,500 points
- Efficiency: 500 - (30 × 5) = 350 points
- **Final Score: 2,850 points**

## 🤖 AI Implementation

### Beginner AI
- Random move selection with capture preference
- No strategic evaluation
- Good for learning chess basics

### Intermediate AI
- Evaluates material value (pawn=100, knight/bishop=300, rook=500, queen=900)
- Positional bonuses for center control and piece development
- Mobility scoring (values pieces with more legal moves)
- Check awareness and king safety evaluation
- Immediate checkmate detection and execution

### Advanced AI
- Minimax algorithm with alpha-beta pruning
- Multi-move look-ahead (depth-based search)
- Advanced position evaluation
- Strategic planning and tactical awareness
- Most challenging opponent

## 🛠️ Technical Details

### Architecture
- **MVC Pattern**: Separation of game logic, GUI, and data management
- **Object-Oriented Design**: Modular classes for pieces, board, moves, and game state
- **Event-Driven GUI**: Swing-based interface with mouse event handling

### Main Classes
- `ChessGUIManager`: Main GUI controller and entry point
- `ChessBoardPanel`: Visual board representation and user interaction
- `ChessGameEngine`: Core game logic and move coordination
- `ChessBoard`: Board state management and rule validation
- `ChessPiece`: Piece representation with type and color
- `ChessAI`, `ChessAIIntermediate`, `ChessAIAdvanced`: AI implementations
- `LeaderboardManager`: Persistent leaderboard storage and ranking

### Requirements
- Java 8 or higher
- Swing GUI library (included in standard JDK)
- No external dependencies required

## 📁 Project Structure
```
chess_game_project/
├── src/com/game/chess/
│   ├── gui/
│   │   ├── ChessGUIManager.java       (Main GUI & entry point)
│   │   └── ChessBoardPanel.java       (Board visualization)
│   ├── logic/
│   │   ├── ChessGameEngine.java       (Game coordinator)
│   │   ├── ChessBoard.java            (Board state & rules)
│   │   ├── ChessPiece.java            (Piece representation)
│   │   ├── ChessAI.java               (Beginner AI)
│   │   ├── ChessAIIntermediate.java   (Intermediate AI)
│   │   ├── ChessAIAdvanced.java       (Advanced AI)
│   │   ├── LeaderboardManager.java    (Score tracking)
│   │   └── LeaderboardEntry.java      (Score data model)
│   └── resources/                     (Image assets)
├── resources/images/chess/            (Chess piece images)
├── chess_leaderboard.dat              (Leaderboard data file)
├── AI_IMPROVEMENTS.md                 (AI development notes)
├── LEADERBOARD_README.md              (Detailed scoring info)
└── README.md                          (This file)
```

## 🚀 Running the Game

### From Eclipse IDE
1. Open the project in Eclipse
2. Navigate to `src/com/game/chess/gui/ChessGUIManager.java`
3. Right-click and select "Run As → Java Application"

### From Command Line
```bash
cd chess_game_project/src
javac com/game/chess/gui/ChessGUIManager.java
java com.game.chess.gui.ChessGUIManager
```

## 🎓 Learning Outcomes

Through this project, I practiced and improved my skills in:
- Object-oriented programming and design patterns
- Game logic implementation and state management
- AI algorithm development (minimax, alpha-beta pruning)
- Swing GUI development and event handling
- Data persistence and file I/O
- Code organization and modularity
- Bug fixing and testing

## 📝 Future Enhancements

Potential features to add:
- Online multiplayer support
- Move notation display (algebraic notation)
- Game replay and analysis
- Customizable themes and board colors
- Sound effects for moves and captures
- Timed games with chess clocks
- Opening book for AI
- Save and load game functionality

## 👨‍💻 Development

This project was developed with the assistance of GitHub Copilot, demonstrating how AI-powered coding tools can accelerate development while maintaining code quality and learning objectives.

---

**Enjoy playing chess! 🎮♟️**