package chess.pieces;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class PieceMovesCalculator {
    //public PieceMovesCalculator() {}
    // maybe put a constructor?
    // inBounds method
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return new ArrayList<>();
    }

    public boolean inBounds(int position) {
        return position >= 0 && position <= 8;
    }

    public Collection<ChessMove> horizontalMoves(ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        Collection<ChessMove> returnList = new ArrayList<ChessMove>();
        for (int i = 1; i < 9; i++) {
            if (i != col) {
                returnList.add(new ChessMove(myPosition, new ChessPosition(row, i), null));
            }
        }
        return returnList;

    }
    public Collection<ChessMove> verticalMoves(ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        Collection<ChessMove> returnList = new ArrayList<ChessMove>();
        for (int i = 1; i < 9; i++) {
            if (i != row) {
                returnList.add(new ChessMove(myPosition, new ChessPosition(i, col), null));
            }
        }
        return returnList;
    }

    public Collection<ChessMove> diagonalMoves(ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        int tempRow = row;
        int tempCol = col;
        Collection<ChessMove> returnList = new ArrayList<ChessMove>();
        while (tempRow < 8 && tempCol < 8) {
            tempRow += 1;
            tempCol += 1;
            returnList.add(new ChessMove(myPosition, new ChessPosition(tempRow, tempCol), null));
        }
        tempRow = row;
        tempCol = col;
        while (tempRow > 1 && tempCol < 8) {
            tempRow -= 1;
            tempCol += 1;
            returnList.add(new ChessMove(myPosition, new ChessPosition(tempRow, tempCol), null));
        }
        tempRow = row;
        tempCol = col;
        while (tempRow > 1 && tempCol > 1) {
            tempRow -= 1;
            tempCol -= 1;
            returnList.add(new ChessMove(myPosition, new ChessPosition(tempRow, tempCol), null));
        }
        tempRow = row;
        tempCol = col;
        while (tempRow < 8 && tempCol > 1) {
            tempRow += 1;
            tempCol -= 1;
            returnList.add(new ChessMove(myPosition, new ChessPosition(tempRow, tempCol), null));

        }
        return returnList;
    }
}

