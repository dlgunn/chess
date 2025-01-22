package chess.pieces;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator extends PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        ChessPosition newPosition;
        ChessPiece newPiece;
        ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        if (row + 1 < 9 && col + 2 < 9) {
            newPosition = new ChessPosition(row + 1, col + 2);
            newPiece = board.getPiece(newPosition);
            if (newPiece == null) {
                moves.add(new ChessMove(myPosition, newPosition, null));
            } else if (newPiece.getTeamColor() != color){
                moves.add(new ChessMove(myPosition, newPosition, null));
            }
        }
        if (row + 2 < 9 && col + 1 < 9) {
            newPosition = new ChessPosition(row + 2, col + 1);
            newPiece = board.getPiece(newPosition);
            if (newPiece == null) {
                moves.add(new ChessMove(myPosition, newPosition, null));
            } else if (newPiece.getTeamColor() != color){
                moves.add(new ChessMove(myPosition, newPosition, null));
            }
        }
        if (row + 1 < 9 && col - 2 > 0) {
            newPosition = new ChessPosition(row + 1, col - 2);
            newPiece = board.getPiece(newPosition);
            if (newPiece == null) {
                moves.add(new ChessMove(myPosition, newPosition, null));
            } else if (newPiece.getTeamColor() != color){
                moves.add(new ChessMove(myPosition, newPosition, null));
            }
        }
        if (row + 2 < 9 && col - 1 > 0) {
            newPosition = new ChessPosition(row + 2, col - 1);
            newPiece = board.getPiece(newPosition);
            if (newPiece == null) {
                moves.add(new ChessMove(myPosition, newPosition, null));
            } else if (newPiece.getTeamColor() != color){
                moves.add(new ChessMove(myPosition, newPosition, null));
            }
        }
        if (row - 1 > 0 && col + 2 < 9) {
            newPosition = new ChessPosition(row - 1, col + 2);
            newPiece = board.getPiece(newPosition);
            if (newPiece == null) {
                moves.add(new ChessMove(myPosition, newPosition, null));
            } else if (newPiece.getTeamColor() != color){
                moves.add(new ChessMove(myPosition, newPosition, null));
            }
        }
        if (row - 2 > 0 && col + 1 < 9) {
            newPosition = new ChessPosition(row - 2, col + 1);
            newPiece = board.getPiece(newPosition);
            if (newPiece == null) {
                moves.add(new ChessMove(myPosition, newPosition, null));
            } else if (newPiece.getTeamColor() != color){
                moves.add(new ChessMove(myPosition, newPosition, null));
            }
        }
        if (row - 1 > 0 && col - 2 > 0) {
            newPosition = new ChessPosition(row - 1, col - 2);
            newPiece = board.getPiece(newPosition);
            if (newPiece == null) {
                moves.add(new ChessMove(myPosition, newPosition, null));
            } else if (newPiece.getTeamColor() != color){
                moves.add(new ChessMove(myPosition, newPosition, null));
            }
        }
        if (row - 2 > 0 && col - 1 > 0) {
            newPosition = new ChessPosition(row - 2, col - 1);
            newPiece = board.getPiece(newPosition);
            if (newPiece == null) {
                moves.add(new ChessMove(myPosition, newPosition, null));
            } else if (newPiece.getTeamColor() != color){
                moves.add(new ChessMove(myPosition, newPosition, null));
            }
        }
        return moves;
    }

}
