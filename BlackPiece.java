import java.awt.*;
import java.util.ArrayList;

public class BlackPiece extends Piece {

    public BlackPiece() {
        super(CheckersData.BLACK);
    }

    @Override
    public void draw(Graphics g, int x, int y) {
        g.setColor(Color.BLACK);
        g.fillOval(x, y, 50, 50);
    }

    @Override
    public ArrayList<CheckersMove> getLegalMoves(CheckersData board, int row, int col) {
        ArrayList<CheckersMove> moves = new ArrayList<>();

        // Black pieces move downward
        if (row < 7 && col < 7 && board.pieceAt(row + 1, col + 1) == CheckersData.EMPTY)
            moves.add(new CheckersMove(row, col, row + 1, col + 1));
        if (row < 7 && col > 0 && board.pieceAt(row + 1, col - 1) == CheckersData.EMPTY)
            moves.add(new CheckersMove(row, col, row + 1, col - 1));

        return moves;
    }

    @Override
    public ArrayList<CheckersMove> getLegalJumps(CheckersData board, int row, int col) {
        ArrayList<CheckersMove> jumps = new ArrayList<>();

        // Black pieces jump downward
        if (row < 6 && col < 6 && board.pieceAt(row + 1, col + 1) != CheckersData.EMPTY &&
            (board.pieceAt(row + 1, col + 1) == CheckersData.RED || board.pieceAt(row + 1, col + 1) == CheckersData.RED_KING) &&
            board.pieceAt(row + 2, col + 2) == CheckersData.EMPTY)
            jumps.add(new CheckersMove(row, col, row + 2, col + 2));

        if (row < 6 && col > 1 && board.pieceAt(row + 1, col - 1) != CheckersData.EMPTY &&
            (board.pieceAt(row + 1, col - 1) == CheckersData.RED || board.pieceAt(row + 1, col - 1) == CheckersData.RED_KING) &&
            board.pieceAt(row + 2, col - 2) == CheckersData.EMPTY)
            jumps.add(new CheckersMove(row, col, row + 2, col - 2));

        return jumps;
    }
}