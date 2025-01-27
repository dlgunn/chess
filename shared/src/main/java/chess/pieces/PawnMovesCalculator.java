package chess.pieces;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator extends PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        ChessPiece piece = board.getPiece(position);
        ChessGame.TeamColor color = piece.getTeamColor();
        int rowOffset = 1;
        if (color == ChessGame.TeamColor.BLACK) {
            rowOffset = -1;
        }
        if (addPosition(rowOffset,0,board,position,moves)) {
            addPosition(rowOffset*2, 0, board,position,moves);
        }
        addPosition(rowOffset,1,board,position,moves);
        addPosition(rowOffset,-1,board,position,moves);
        return moves;
    }

}
