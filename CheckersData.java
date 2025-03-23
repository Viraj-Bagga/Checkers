import java.util.ArrayList;
import java.util.Arrays;

public class CheckersData {

    static final int EMPTY = 0;
    static final int RED = 1;
    static final int RED_KING = 2;
    static final int BLACK = 3;
    static final int BLACK_KING = 4;

    int[][] board;

    CheckersData() {
        board = new int[8][8];
        setUpGame();
    }

    void setUpGame() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (row % 2 == col % 2) {
                    if (row < 3)
                        board[row][col] = BLACK;
                    else if (row > 4)
                        board[row][col] = RED;
                    else
                        board[row][col] = EMPTY;
                } else {
                    board[row][col] = EMPTY;
                }
            }
        }
    }

    int pieceAt(int row, int col) {
        return board[row][col];
    }

    void makeMove(CheckersMove move) {
        if (move == null || !isLegalMove(move)) {
            throw new IllegalArgumentException("Invalid move");
        }
    
        // Move the piece
        board[move.toRow][move.toCol] = board[move.fromRow][move.fromCol];
        board[move.fromRow][move.fromCol] = EMPTY;
    
        // If it's a jump, remove the captured piece
        if (move.isJump()) {
            int jumpRow = (move.fromRow + move.toRow) / 2;
            int jumpCol = (move.fromCol + move.toCol) / 2;
            board[jumpRow][jumpCol] = EMPTY;
        }
    
        // Promote to king if the piece reaches the last row
        if (move.toRow == 0 && board[move.toRow][move.toCol] == RED)
            board[move.toRow][move.toCol] = RED_KING;
        if (move.toRow == 7 && board[move.toRow][move.toCol] == BLACK)
            board[move.toRow][move.toCol] = BLACK_KING;
    }
    
    boolean isLegalMove(CheckersMove move) {
        CheckersMove[] legalMoves = getLegalMoves(board[move.fromRow][move.fromCol]);
        if (legalMoves == null) return false;
    
        for (CheckersMove legalMove : legalMoves) {
            if (legalMove.fromRow == move.fromRow && legalMove.fromCol == move.fromCol &&
                legalMove.toRow == move.toRow && legalMove.toCol == move.toCol) {
                return true;
            }
        }
        return false;
    }

    CheckersMove[] getLegalMoves(int player) {
        ArrayList<CheckersMove> moves = new ArrayList<>();
    
        // Check for all legal moves (both regular moves and jumps)
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (board[row][col] == player || board[row][col] == player + 2) { // Include kings
                    // Add regular moves
                    ArrayList<CheckersMove> regularMoves = getLegalMovesFrom(row, col);
                    if (regularMoves != null) {
                        moves.addAll(regularMoves);
                    }
    
                    // Add jumps
                    CheckersMove[] jumps = getLegalJumpsFrom(player, row, col);
                    if (jumps != null) {
                        moves.addAll(Arrays.asList(jumps));
                    }
                }
            }
        }
    
        if (moves.isEmpty())
            return null;
    
        return moves.toArray(new CheckersMove[0]);
    }

    ArrayList<CheckersMove> getLegalMovesFrom(int row, int col) {
        ArrayList<CheckersMove> moves = new ArrayList<>();

        int piece = board[row][col];

        // Red pieces (and kings) can move upward
        if (piece == RED || piece == RED_KING) {
            if (row > 0 && col < 7 && board[row - 1][col + 1] == EMPTY)
                moves.add(new CheckersMove(row, col, row - 1, col + 1));
            if (row > 0 && col > 0 && board[row - 1][col - 1] == EMPTY)
                moves.add(new CheckersMove(row, col, row - 1, col - 1));
        }

        // Black pieces (and kings) can move downward
        if (piece == BLACK || piece == BLACK_KING) {
            if (row < 7 && col < 7 && board[row + 1][col + 1] == EMPTY)
                moves.add(new CheckersMove(row, col, row + 1, col + 1));
            if (row < 7 && col > 0 && board[row + 1][col - 1] == EMPTY)
                moves.add(new CheckersMove(row, col, row + 1, col - 1));
        }

        return moves;
    }

    CheckersMove[] getLegalJumpsFrom(int player, int row, int col) {
        ArrayList<CheckersMove> jumps = new ArrayList<>();

        int piece = board[row][col];

        // Red pieces (and kings) can jump upward
        if (piece == RED || piece == RED_KING) {
            if (row > 1 && col < 6 && board[row - 1][col + 1] != EMPTY &&
                (board[row - 1][col + 1] == BLACK || board[row - 1][col + 1] == BLACK_KING) &&
                board[row - 2][col + 2] == EMPTY)
                jumps.add(new CheckersMove(row, col, row - 2, col + 2));

            if (row > 1 && col > 1 && board[row - 1][col - 1] != EMPTY &&
                (board[row - 1][col - 1] == BLACK || board[row - 1][col - 1] == BLACK_KING) &&
                board[row - 2][col - 2] == EMPTY)
                jumps.add(new CheckersMove(row, col, row - 2, col - 2));
        }

        // Black pieces (and kings) can jump downward
        if (piece == BLACK || piece == BLACK_KING) {
            if (row < 6 && col < 6 && board[row + 1][col + 1] != EMPTY &&
                (board[row + 1][col + 1] == RED || board[row + 1][col + 1] == RED_KING) &&
                board[row + 2][col + 2] == EMPTY)
                jumps.add(new CheckersMove(row, col, row + 2, col + 2));

            if (row < 6 && col > 1 && board[row + 1][col - 1] != EMPTY &&
                (board[row + 1][col - 1] == RED || board[row + 1][col - 1] == RED_KING) &&
                board[row + 2][col - 2] == EMPTY)
                jumps.add(new CheckersMove(row, col, row + 2, col - 2));
        }

        if (jumps.isEmpty())
            return null;

        return jumps.toArray(new CheckersMove[0]);
    }
}