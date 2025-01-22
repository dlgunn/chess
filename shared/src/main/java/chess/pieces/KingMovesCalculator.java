package chess.pieces;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator extends PieceMovesCalculator {
    public KingMovesCalculator() {
        super();
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        ChessPosition endPosition;
        ChessPiece newPiece;
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        int right = row + 1;
        int left = row - 1;
        int up = col + 1;
        int down = col - 1;
        ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();

        if (inBounds(right)) {
            endPosition = new ChessPosition(right, col);
            newPiece = board.getPiece(endPosition);
            if (newPiece == null) {
                moves.add(new ChessMove(myPosition, endPosition, null));
            } else if (board.getPiece(endPosition).getTeamColor() != color) {
                moves.add(new ChessMove(myPosition,endPosition, null));
            }
        }
        if (inBounds(left)) {
            endPosition = new ChessPosition(left, col);
            newPiece = board.getPiece(endPosition);
            if (newPiece == null) {
                moves.add(new ChessMove(myPosition,endPosition, null));
            } else if (board.getPiece(endPosition).getTeamColor() != color) {
                moves.add(new ChessMove(myPosition,endPosition, null));
            }
        }
        if (inBounds(up)) {
            endPosition = new ChessPosition(row, up);
            newPiece = board.getPiece(endPosition);
            if (newPiece == null) {
                moves.add(new ChessMove(myPosition,endPosition, null));
            } else if (board.getPiece(endPosition).getTeamColor() != color) {
                moves.add(new ChessMove(myPosition,endPosition, null));
            }
        }
        if (inBounds(down)) {
            endPosition = new ChessPosition(row, down);
            newPiece = board.getPiece(endPosition);
            if (newPiece == null) {
                moves.add(new ChessMove(myPosition,endPosition, null));
            } else if (board.getPiece(endPosition).getTeamColor() != color) {
                moves.add(new ChessMove(myPosition,endPosition, null));
            }
        }
        if (inBounds(up) && inBounds(right)) {
            endPosition = new ChessPosition(right, up);
            newPiece = board.getPiece(endPosition);
            if (newPiece == null) {
                moves.add(new ChessMove(myPosition,endPosition, null));
            } else if (board.getPiece(endPosition).getTeamColor() != color) {
                moves.add(new ChessMove(myPosition,endPosition, null));
            }
        }
        if (inBounds(down) && inBounds(right)) {
            endPosition = new ChessPosition(right, down);
            newPiece = board.getPiece(endPosition);
            if (newPiece == null) {
                moves.add(new ChessMove(myPosition,endPosition, null));
            } else if (board.getPiece(endPosition).getTeamColor() != color) {
                moves.add(new ChessMove(myPosition,endPosition, null));
            }
        }
        if (inBounds(up) && inBounds(left)) {
            endPosition = new ChessPosition(left, up);
            newPiece = board.getPiece(endPosition);
            if (newPiece == null) {
                moves.add(new ChessMove(myPosition,endPosition, null));
            } else if (board.getPiece(endPosition).getTeamColor() != color) {
                moves.add(new ChessMove(myPosition,endPosition, null));
            }
        }
        if (inBounds(down) && inBounds(left)) {
            endPosition = new ChessPosition(left, down);
            newPiece = board.getPiece(endPosition);
            if (newPiece == null) {
                moves.add(new ChessMove(myPosition,endPosition, null));
            } else if (board.getPiece(endPosition).getTeamColor() != color) {
                moves.add(new ChessMove(myPosition,endPosition, null));
            }
        }

        return moves;
    }

}
