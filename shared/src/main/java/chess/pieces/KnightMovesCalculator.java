package chess.pieces;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator extends PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        if (row + 1 < 9 && col + 2 < 9) {
            moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col + 2), null));
        }
        if (row + 2 < 9 && col + 1 < 9) {
            moves.add(new ChessMove(myPosition, new ChessPosition(row + 2, col + 1), null));
        }
        if (row + 1 < 9 && col - 2 > 0) {
            moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col -2), null));
        }
        if (row + 2 < 9 && col - 1 > 0) {
            moves.add(new ChessMove(myPosition, new ChessPosition(row + 2, col - 1), null));
        }
        if (row - 1 > 0 && col + 2 > 0) {
            moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col + 2), null));
        }
        if (row - 2 > 0 && col + 1 > 0) {
            moves.add(new ChessMove(myPosition, new ChessPosition(row - 2, col + 1), null));
        }
        if (row - 1 > 0 && col - 2 > 0) {
            moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col -2), null));
        }
        if (row - 2 > 0 && col - 1 > 0) {
            moves.add(new ChessMove(myPosition, new ChessPosition(row - 2, col - 1), null));
        }
        return moves;
    }

}
