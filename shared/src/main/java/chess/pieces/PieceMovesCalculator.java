package chess.pieces;

import chess.*;

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

    public Collection<ChessMove> horizontalMoves(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();
        ArrayList<ChessMove> returnList = new ArrayList<ChessMove>();
        for (int i = col + 1; i < 9; i++) {
            ChessPosition newPosition = new ChessPosition(row, i);
            ChessPiece pieceAtPosition = board.getPiece(newPosition);
            if (pieceAtPosition == null) {
                returnList.add(new ChessMove(myPosition, newPosition, null));
            } else if (pieceAtPosition.getTeamColor() != color) {
                returnList.add(new ChessMove(myPosition, newPosition, null));
                break;
            } else {
                break;
            }
        }
        for (int i = col - 1; i > 0; i--) {
            ChessPosition newPosition = new ChessPosition(row, i);
            ChessPiece pieceAtPosition = board.getPiece(newPosition);
            if (pieceAtPosition == null) {
                returnList.add(new ChessMove(myPosition, newPosition, null));
            } else if (pieceAtPosition.getTeamColor() != color) {
                returnList.add(new ChessMove(myPosition, newPosition, null));
                break;
            } else {
                break;
            }
        }
        return returnList;

    }
    public Collection<ChessMove> verticalMoves(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();
        ArrayList<ChessMove> returnList = new ArrayList<ChessMove>();
        for (int i = row + 1; i < 9; i++) {
            ChessPosition newPosition = new ChessPosition(i, col);
            ChessPiece pieceAtPosition = board.getPiece(newPosition);
            if (pieceAtPosition == null) {
                returnList.add(new ChessMove(myPosition, newPosition, null));
            } else if (pieceAtPosition.getTeamColor() != color) {
                returnList.add(new ChessMove(myPosition, newPosition, null));
                break;
            } else {
                break;
            }
        }

        for (int i = row - 1; i > 0; i--) {
            ChessPosition newPosition = new ChessPosition(i, col);
            ChessPiece pieceAtPosition = board.getPiece(newPosition);
            if (pieceAtPosition == null) {
                returnList.add(new ChessMove(myPosition, newPosition, null));
            } else if (pieceAtPosition.getTeamColor() != color) {
                returnList.add(new ChessMove(myPosition, newPosition, null));
                break;
            } else {
                break;
            }
        }
        return returnList;
    }

    public Collection<ChessMove> diagonalMoves(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        int tempRow = row;
        int tempCol = col;
        ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();
        ArrayList<ChessMove> returnList = new ArrayList<ChessMove>();
        while (tempRow < 8 && tempCol < 8) {
            tempRow += 1;
            tempCol += 1;
            ChessPosition newPosition = new ChessPosition(tempRow, tempCol);
            ChessPiece pieceAtPosition = board.getPiece(newPosition);
            if (pieceAtPosition == null) {
                returnList.add(new ChessMove(myPosition, newPosition, null));
            } else if (pieceAtPosition.getTeamColor() != color) {
                returnList.add(new ChessMove(myPosition, newPosition, null));
                break;
            } else {
                break;
            }

        }
        tempRow = row;
        tempCol = col;
        while (tempRow > 1 && tempCol < 8) {
            tempRow -= 1;
            tempCol += 1;
            ChessPosition newPosition2 = new ChessPosition(tempRow, tempCol);
            ChessPiece pieceAtPosition2 = board.getPiece(newPosition2);
            if (pieceAtPosition2 == null) {
                returnList.add(new ChessMove(myPosition, newPosition2, null));
            } else if (pieceAtPosition2.getTeamColor() != color) {
                returnList.add(new ChessMove(myPosition, newPosition2, null));
                break;
            } else {
                break;
            }
        }
        tempRow = row;
        tempCol = col;
        while (tempRow > 1 && tempCol > 1) {
            tempRow -= 1;
            tempCol -= 1;
            ChessPosition newPosition3 = new ChessPosition(tempRow, tempCol);
            ChessPiece pieceAtPosition3 = board.getPiece(newPosition3);
            if (pieceAtPosition3 == null) {
                returnList.add(new ChessMove(myPosition, newPosition3, null));
            } else if (pieceAtPosition3.getTeamColor() != color) {
                returnList.add(new ChessMove(myPosition, newPosition3, null));
                break;
            } else {
                break;
            }
        }
        tempRow = row;
        tempCol = col;
        while (tempRow < 8 && tempCol > 1) {
            tempRow += 1;
            tempCol -= 1;
            ChessPosition newPosition4 = new ChessPosition(tempRow, tempCol);
            ChessPiece pieceAtPosition4 = board.getPiece(newPosition4);
            if (pieceAtPosition4 == null) {
                returnList.add(new ChessMove(myPosition, newPosition4, null));
            } else if (pieceAtPosition4.getTeamColor() != color) {
                returnList.add(new ChessMove(myPosition, newPosition4, null));
                break;
            } else {
                break;
            }
        }
        return returnList;
    }
}

