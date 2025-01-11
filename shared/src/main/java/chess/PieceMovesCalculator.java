package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PieceMovesCalculator {
    //public PieceMovesCalculator() {}
    // maybe put a constructor?
    // inBounds method
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return new ArrayList<>();
    }

    public boolean inBounds(int position) {
        return position >= 0 && position <= 8;
    }
}

