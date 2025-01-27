package chess.pieces;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class PieceMovesCalculator {

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        return null;
    }

    public boolean addPosition(int rowOffset, int colOffset, ChessBoard board, ChessPosition startPosition, Collection<ChessMove> moves) {
        ChessPiece piece = board.getPiece(startPosition);
        ChessGame.TeamColor color = piece.getTeamColor();
        int row = startPosition.getRow();
        int col = startPosition.getColumn();
        if (!this.inBounds(row, col, rowOffset, colOffset)) {
            return false;
        }
        ChessPosition newPosition = new ChessPosition(row + rowOffset, col + colOffset);
        ChessPiece occupyingPiece = board.getPiece(newPosition);
        if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
            return addPawnMove(moves,row,rowOffset,colOffset,startPosition, newPosition, occupyingPiece);
        }
        if (occupyingPiece == null) {
            moves.add(new ChessMove(startPosition, newPosition, null));
            return true;
        } else if (occupyingPiece.getTeamColor() != color){
            moves.add(new ChessMove(startPosition, newPosition, null));
            return false;
        }
        return false;
    }

    public boolean addPawnMove(Collection<ChessMove> moves, int row, int rowOffset, int colOffset,
                               ChessPosition startPosition, ChessPosition newPosition, ChessPiece occupyingPiece) {
        int doubleStep = 2;
        ChessGame.TeamColor color = ChessGame.TeamColor.WHITE;
        if (rowOffset==-1) {
            doubleStep = 7;
            color = ChessGame.TeamColor.BLACK;
        }
        if (colOffset == 0) {
            if (occupyingPiece == null) {
                addPromotion(rowOffset,moves,startPosition,newPosition);
                return row == doubleStep;
            } else {
                return false;
            }
        } else {
            if (occupyingPiece != null && color != occupyingPiece.getTeamColor()) {
                addPromotion(rowOffset, moves, startPosition, newPosition);
            }
            return false;
        }
    }

    public boolean inBounds(int row, int col, int rowOffset, int colOffset) {
        return row + rowOffset <= 8 && row + rowOffset >= 1 && col + colOffset <= 8 && col + colOffset >=1;
    }

    public Collection<ChessMove> horizontalMoves(ChessBoard board, ChessPosition startPosition) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        extendMoves(1, 0, board,startPosition,moves);
        extendMoves(0,1,board,startPosition,moves);
        extendMoves(0,-1,board,startPosition,moves);
        extendMoves(-1,0,board,startPosition,moves);
        return moves;
    }
    public Collection<ChessMove> diagonalMoves(ChessBoard board, ChessPosition startPosition) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        extendMoves(1, 1, board,startPosition,moves);
        extendMoves(1, -1, board,startPosition,moves);
        extendMoves(-1, 1, board,startPosition,moves);
        extendMoves(-1, -1, board,startPosition,moves);
        return moves;
    }
    public void extendMoves(int rowOffset, int colOffset, ChessBoard board, ChessPosition startPosition, Collection<ChessMove> moves) {
        int initialRowOffset = rowOffset;
        int initialColOffset = colOffset;
        while (addPosition(rowOffset,colOffset,board,startPosition,moves)) {
            rowOffset += initialRowOffset;
            colOffset += initialColOffset;
        }
    }
    public void addPromotion(int rowOffset, Collection<ChessMove> moves, ChessPosition startPosition, ChessPosition endPosition) {
        int promotionRow = 8;
        if (rowOffset == -1) {
            promotionRow = 1;
        }
        if (endPosition.getRow() == promotionRow) {
            addPromotionHelper(moves, startPosition,endPosition);
        } else {
            moves.add(new ChessMove(startPosition,endPosition,null));
        }


    }

    public void addPromotionHelper(Collection<ChessMove> moves, ChessPosition startPosition, ChessPosition endPosition) {
        moves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.QUEEN));
        moves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.ROOK));
        moves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.BISHOP));
        moves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.KNIGHT));

    }


}
