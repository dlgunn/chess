package chess.pieces;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator extends PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition startPosition) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        this.addPosition(1, 0, board, startPosition,moves);
        this.addPosition(-1, 0, board, startPosition,moves);
        this.addPosition(0, 1, board, startPosition,moves);
        this.addPosition(0, -1, board, startPosition,moves);
        this.addPosition(1, 1, board, startPosition,moves);
        this.addPosition(1, -1, board, startPosition,moves);
        this.addPosition(-1, 1, board, startPosition,moves);
        this.addPosition(-1, -1, board, startPosition,moves);
        return moves;
    }
}
