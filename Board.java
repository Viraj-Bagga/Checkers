import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Board extends JPanel implements ActionListener, MouseListener {

    private CheckersData board;
    private boolean gameInProgress;
    private int currentPlayer; // Current player (RED or BLACK)
    private int selectedRow = -1, selectedCol = -1; // Selected piece
    private CheckersMove[] legalMoves;
    private JButton newGameButton;
    private JButton resignButton;
    private JLabel message;

    public Board() {
        setBackground(new Color(240, 240, 240));
        addMouseListener(this);

        // Customize buttons
        newGameButton = new JButton("New Game");
        newGameButton.setFont(new Font("Arial", Font.BOLD, 14));
        newGameButton.setBackground(new Color(50, 150, 50));
        newGameButton.setForeground(Color.WHITE);
        newGameButton.setFocusPainted(false);
        newGameButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        newGameButton.addActionListener(this);

        resignButton = new JButton("Resign");
        resignButton.setFont(new Font("Arial", Font.BOLD, 14));
        resignButton.setBackground(new Color(200, 50, 50));
        resignButton.setForeground(Color.WHITE);
        resignButton.setFocusPainted(false);
        resignButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        resignButton.addActionListener(this);

        // Customize message label
        message = new JLabel("", JLabel.CENTER);
        message.setFont(new Font("Arial", Font.BOLD, 16));
        message.setForeground(new Color(30, 30, 30));
        message.setOpaque(true);
        message.setBackground(new Color(255, 255, 200));
        message.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Layout
        setLayout(new BorderLayout());
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(240, 240, 240));
        buttonPanel.add(newGameButton);
        buttonPanel.add(resignButton);
        add(buttonPanel, BorderLayout.NORTH);
        add(message, BorderLayout.SOUTH);

        board = new CheckersData();
        doNewGame();
    }

    public void actionPerformed(ActionEvent evt) {
        Object src = evt.getSource();
        if (src == newGameButton)
            doNewGame();
        else if (src == resignButton)
            doResign();
    }

    void doNewGame() {
        if (gameInProgress) {
            message.setText("Finish the current game first!");
            return;
        }
        board.setUpGame();
        currentPlayer = CheckersData.RED;
        legalMoves = board.getLegalMoves(currentPlayer);
        selectedRow = -1;
        selectedCol = -1;
        message.setText("Red: Make your move.");
        gameInProgress = true;
        newGameButton.setEnabled(false);
        resignButton.setEnabled(true);
        repaint();
    }

    void doResign() {
        if (!gameInProgress) {
            message.setText("There is no game in progress!");
            return;
        }
        if (currentPlayer == CheckersData.RED)
            gameOver("RED resigns. BLACK wins.");
        else
            gameOver("BLACK resigns. RED wins.");
    }

    void gameOver(String str) {
        message.setText(str);
        newGameButton.setEnabled(true);
        resignButton.setEnabled(false);
        gameInProgress = false;
    }

    void doClickSquare(int row, int col) {
        if (!gameInProgress || legalMoves == null) return;

        int piece = board.pieceAt(row, col);

        if (selectedRow == -1) {  // No piece selected yet
            // Check if the clicked piece belongs to the current player
            if (piece == CheckersData.EMPTY || (piece != currentPlayer && piece != currentPlayer + 2)) {
                message.setText("It's not your turn!");
                return;
            }

            // Ensure the selected piece has at least one legal move
            boolean validPiece = false;
            for (CheckersMove move : legalMoves) {
                if (move.fromRow == row && move.fromCol == col) {
                    validPiece = true;
                    break;
                }
            }

            if (!validPiece) {
                message.setText("Click a piece that has a valid move.");
                return;
            }

            // Select the piece
            selectedRow = row;
            selectedCol = col;
            message.setText((currentPlayer == CheckersData.RED ? "RED" : "BLACK") + ": Click where to move.");
            repaint();
        } else {  // A piece is already selected, so check if this is a valid move
            for (CheckersMove move : legalMoves) {
                if (move.fromRow == selectedRow && move.fromCol == selectedCol && move.toRow == row && move.toCol == col) {
                    doMakeMove(move);
                    return;
                }
            }

            // If clicked on a different valid piece, select it instead
            if (piece == currentPlayer || piece == currentPlayer + 2) {
                selectedRow = row;
                selectedCol = col;
                message.setText((currentPlayer == CheckersData.RED ? "RED" : "BLACK") + ": Click where to move.");
            } else {
                message.setText("Invalid move. Click a valid piece or destination.");
            }

            repaint();
        }
    }

    void doMakeMove(CheckersMove move) {
        board.makeMove(move);

        if (move.isJump()) {
            // Check if the same player has another jump
            legalMoves = board.getLegalJumpsFrom(currentPlayer, move.toRow, move.toCol);
            if (legalMoves != null && legalMoves.length > 0) {
                selectedRow = move.toRow;
                selectedCol = move.toCol;
                message.setText((currentPlayer == CheckersData.RED ? "RED" : "BLACK") + ": You must continue jumping.");
                repaint();
                return; // Stay on the same player's turn if another jump is possible
            }
        }

        // Switch turns properly
        currentPlayer = (currentPlayer == CheckersData.RED) ? CheckersData.BLACK : CheckersData.RED;
        legalMoves = board.getLegalMoves(currentPlayer); // Update legal moves for the new player

        if (legalMoves == null || legalMoves.length == 0) {
            // End game if the new player has no moves
            gameOver((currentPlayer == CheckersData.RED ? "BLACK" : "RED") + " has no moves. "
                    + (currentPlayer == CheckersData.RED ? "RED" : "BLACK") + " wins.");
            return;
        }

        // Reset selection for the new player's turn
        selectedRow = -1;
        selectedCol = -1;
        message.setText((currentPlayer == CheckersData.RED ? "RED" : "BLACK") + ": Make your move.");
        repaint();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int squareSize = Math.min(getWidth() / 8, (getHeight() - 100) / 8);
        int xOffset = (getWidth() - squareSize * 8) / 2;
        int yOffset = 50;

        // Draw the board and pieces
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                int x = xOffset + col * squareSize;
                int y = yOffset + row * squareSize;

                // Draw board squares
                if ((row + col) % 2 == 0)
                    g.setColor(Color.LIGHT_GRAY);
                else
                    g.setColor(Color.DARK_GRAY);
                g.fillRect(x, y, squareSize, squareSize);

                // Draw the piece
                int piece = board.pieceAt(row, col);
                if (piece != CheckersData.EMPTY) {
                    if (piece == CheckersData.RED || piece == CheckersData.RED_KING)
                        g.setColor(Color.RED);
                    else
                        g.setColor(Color.BLACK);

                    g.fillOval(x + 5, y + 5, squareSize - 10, squareSize - 10);
                }
            }
        }

        // Highlight only the current player's pieces with legal moves
        if (legalMoves != null) {
            for (CheckersMove move : legalMoves) {
                int fromRow = move.fromRow;
                int fromCol = move.fromCol;
                int piece = board.pieceAt(fromRow, fromCol);

                // Highlight only the current player's pieces
                if (piece == currentPlayer || piece == currentPlayer + 2) {
                    int x = xOffset + fromCol * squareSize;
                    int y = yOffset + fromRow * squareSize;

                    // Highlight the entire tile
                    g.setColor(Color.YELLOW);
                    g.fillRect(x, y, squareSize, squareSize);

                    // Redraw the piece on top of the highlighted tile
                    if (piece == CheckersData.RED || piece == CheckersData.RED_KING)
                        g.setColor(Color.RED);
                    else
                        g.setColor(Color.BLACK);
                    g.fillOval(x + 5, y + 5, squareSize - 10, squareSize - 10);
                }
            }
        }

        // Highlight the selected piece (if any)
        if (selectedRow >= 0 && selectedCol >= 0) {
            int x = xOffset + selectedCol * squareSize;
            int y = yOffset + selectedRow * squareSize;

            g.setColor(Color.ORANGE); // Different color for the selected piece
            g.fillRect(x, y, squareSize, squareSize);

            // Redraw the selected piece on top
            int piece = board.pieceAt(selectedRow, selectedCol);
            if (piece == CheckersData.RED || piece == CheckersData.RED_KING)
                g.setColor(Color.RED);
            else
                g.setColor(Color.BLACK);
            g.fillOval(x + 5, y + 5, squareSize - 10, squareSize - 10);
        }

        // Highlight legal moves for the selected piece
        if (legalMoves != null && selectedRow >= 0 && selectedCol >= 0) {
            for (CheckersMove move : legalMoves) {
                if (move.fromRow == selectedRow && move.fromCol == selectedCol) {
                    int highlightX = xOffset + move.toCol * squareSize;
                    int highlightY = yOffset + move.toRow * squareSize;

                    g.setColor(Color.GREEN); // Highlight color for legal moves
                    g.drawRect(highlightX, highlightY, squareSize, squareSize); // Draw border for legal moves
                }
            }
        }
    }

    public void mousePressed(MouseEvent evt) {
        if (!gameInProgress)
            message.setText("Click \"New Game\" to start a new game.");
        else {
            int availableHeight = getHeight() - 100; // Subtract space for buttons and message
            int squareSize = Math.min(getWidth() / 8, availableHeight / 8);
            int xOffset = (getWidth() - squareSize * 8) / 2;
            int yOffset = 50; // Space for buttons at the top

            int col = (evt.getX() - xOffset) / squareSize;
            int row = (evt.getY() - yOffset) / squareSize;
            if (col >= 0 && col < 8 && row >= 0 && row < 8)
                doClickSquare(row, col);
        }
    }

    public void mouseReleased(MouseEvent evt) {}
    public void mouseClicked(MouseEvent evt) {}
    public void mouseEntered(MouseEvent evt) {}
    public void mouseExited(MouseEvent evt) {}
}