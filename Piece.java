import java.awt.*;
import java.util.ArrayList;

public abstract class Piece {
    protected int player;

    public Piece(int player) {
        this.player = player;
    }

    public int getPlayer() {
        return player;
    }

    public abstract void draw(Graphics g, int x, int y);

    public abstract ArrayList<CheckersMove> getLegalMoves(CheckersData board, int row, int col);

    public abstract ArrayList<CheckersMove> getLegalJumps(CheckersData board, int row, int col);
}