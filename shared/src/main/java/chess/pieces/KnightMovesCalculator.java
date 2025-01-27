package chess.pieces;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator extends PieceMovesCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        addPosition(1, 2,board,position,moves);
        addPosition(1, -2,board,position,moves);
        addPosition(2, 1,board,position,moves);
        addPosition(2, -1,board,position,moves);
        addPosition(-1, 2,board,position,moves);
        addPosition(-1, -2,board,position,moves);
        addPosition(-2, 1,board,position,moves);
        addPosition(-2, -1,board,position,moves);
        return moves;
    }


}
