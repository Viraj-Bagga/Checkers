import java.awt.*;
import java.util.ArrayList;

public class KingPiece extends Piece {

    public KingPiece(int player) {
        super(player);
    }

    @Override
    public void draw(Graphics g, int x, int y) {
        g.setColor(player == CheckersData.RED ? Color.RED : Color.BLACK);
        g.fillOval(x, y, 50, 50);
        g.setColor(Color.WHITE);
        g.drawString("K", x + 20, y + 30);
    }

    @Override
    public ArrayList<CheckersMove> getLegalMoves(CheckersData board, int row, int col) {
        ArrayList<CheckersMove> moves = new ArrayList<>();

        // Kings can move in all four directions
        if (row > 0 && col < 7 && board.pieceAt(row - 1, col + 1) == CheckersData.EMPTY)
            moves.add(new CheckersMove(row, col, row - 1, col + 1));
        if (row > 0 && col > 0 && board.pieceAt(row - 1, col - 1) == CheckersData.EMPTY)
            moves.add(new CheckersMove(row, col, row - 1, col - 1));
        if (row < 7 && col < 7 && board.pieceAt(row + 1, col + 1) == CheckersData.EMPTY)
            moves.add(new CheckersMove(row, col, row + 1, col + 1));
        if (row < 7 && col > 0 && board.pieceAt(row + 1, col - 1) == CheckersData.EMPTY)
            moves.add(new CheckersMove(row, col, row + 1, col - 1));

        return moves;
    }

    @Override
    public ArrayList<CheckersMove> getLegalJumps(CheckersData board, int row, int col) {
        ArrayList<CheckersMove> jumps = new ArrayList<>();

        // Kings can jump in all four directions
        if (row > 1 && col < 6 && board.pieceAt(row - 1, col + 1) != CheckersData.EMPTY &&
            (board.pieceAt(row - 1, col + 1) % 2 != player % 2) && // Opponent's piece
            board.pieceAt(row - 2, col + 2) == CheckersData.EMPTY)
            jumps.add(new CheckersMove(row, col, row - 2, col + 2));

        if (row > 1 && col > 1 && board.pieceAt(row - 1, col - 1) != CheckersData.EMPTY &&
            (board.pieceAt(row - 1, col - 1) % 2 != player % 2) && // Opponent's piece
            board.pieceAt(row - 2, col - 2) == CheckersData.EMPTY)
            jumps.add(new CheckersMove(row, col, row - 2, col - 2));

        if (row < 6 && col < 6 && board.pieceAt(row + 1, col + 1) != CheckersData.EMPTY &&
            (board.pieceAt(row + 1, col + 1) % 2 != player % 2) && // Opponent's piece
            board.pieceAt(row + 2, col + 2) == CheckersData.EMPTY)
            jumps.add(new CheckersMove(row, col, row + 2, col + 2));

        if (row < 6 && col > 1 && board.pieceAt(row + 1, col - 1) != CheckersData.EMPTY &&
            (board.pieceAt(row + 1, col - 1) % 2 != player % 2) && // Opponent's piece
            board.pieceAt(row + 2, col - 2) == CheckersData.EMPTY)
            jumps.add(new CheckersMove(row, col, row + 2, col - 2));

        return jumps;
    }
}