package chess.pieces;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator extends PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition startPosition) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        ChessGame.TeamColor color = board.getPiece(startPosition).getTeamColor();

        this.addPosition(1, 0, board, startPosition,moves);
        this.addPosition(-1, 0, board, startPosition,moves);
        this.addPosition(0, 1, board, startPosition,moves);
        this.addPosition(0, -1, board, startPosition,moves);
        this.addPosition(1, 1, board, startPosition,moves);
        this.addPosition(1, -1, board, startPosition,moves);
        this.addPosition(-1, 1, board, startPosition,moves);
        this.addPosition(-1, -1, board, startPosition,moves);
        castle(1, board,startPosition,color,moves);
        castle(-1,board,startPosition,color,moves);

        return moves;
    }

    public void castle(int direction, ChessBoard board, ChessPosition startPosition, ChessGame.TeamColor color, Collection<ChessMove> moves) {
        int rookColumn = 1;
        if (direction == 1) {
            rookColumn = 8;
        }
        int castleRow = 1;
        if (color == ChessGame.TeamColor.BLACK) {
            castleRow = 8;

        }
        if (!isClear(direction, board, castleRow)) {
            return;
        } else if (direction == -1 && !isClear(3*direction, board, castleRow)) {
            return;
        }

        if (startPosition.getRow() == castleRow && startPosition.getColumn() == 5) {
            ChessPiece potentialRook = board.getPiece(new ChessPosition(castleRow,rookColumn));
            ChessPosition sidePosition = new ChessPosition(startPosition.getRow(),startPosition.getColumn()+direction);
            if (board.getPiece(sidePosition)==null && potentialRook != null && potentialRook.getPieceType() == ChessPiece.PieceType.ROOK
                    && potentialRook.getTeamColor() == color) {
                this.addPosition(0, 2*direction, board, startPosition,moves);
            }
        }
    }

    public boolean isClear(int offSet, ChessBoard board, int row) {
        ChessPosition testPosition = new ChessPosition(row, 5 + offSet);
        return board.getPiece(testPosition)  == null;
    }
}
