package chess.pieces;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMovesCalculator extends PieceMovesCalculator {
    public QueenMovesCalculator() {
        super();
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = this.horizontalMoves(board, myPosition);
        moves.addAll(this.verticalMoves(board, myPosition));
        moves.addAll(this.diagonalMoves(board, myPosition));
        return moves;
    }
}
