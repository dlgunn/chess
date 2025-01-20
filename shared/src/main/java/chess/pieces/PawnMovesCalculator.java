package chess.pieces;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator extends PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        ChessPiece myChessPiece = board.getPiece(myPosition);
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        if (myChessPiece.getTeamColor() == ChessGame.TeamColor.BLACK) {
            if (row - 1 >= 0) {
                moves.add(new ChessMove(myPosition,new ChessPosition(row -1, col),null));
            }
            if (row == 7) {
                moves.add(new ChessMove(myPosition, new ChessPosition(row -2, col), null));
            }
        } else if (myChessPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            if (row + 1 < 9) {
                moves.add(new ChessMove(myPosition,new ChessPosition(row + 1, col),null));
            }
            if (row == 2) {
                moves.add(new ChessMove(myPosition,new ChessPosition(row + 2, col),null));
            }

        }
        return moves;
    }

}
