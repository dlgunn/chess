package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import static ui.EscapeSequences.*;
import static ui.EscapeSequences.BLACK_PAWN;

public abstract class Client {
    public abstract String eval(String input, Repl repl);

    public void printBoard(ChessBoard board, ChessGame.TeamColor color) {
        int inc = 1;
        int start = 0;
        if (color == ChessGame.TeamColor.BLACK) {
            inc = -1;
            start = 9;
        }
        for (int i = 8; i > 0; i--) {
            for (int j = 1; j < 9; j++) {
                ChessPiece piece = board.getPiece(new ChessPosition(start + inc * i, start + inc * j));
                printSquareArt(piece, start + inc * i, start + inc * j);
            }
            System.out.print(RESET_BG_COLOR + "\n");
        }
    }

    private void printSquareArt(ChessPiece piece, int row, int col) {
        if ((row + col) % 2 == 0) {
            System.out.print(SET_BG_COLOR_BLUE);
            System.out.print(SET_TEXT_COLOR_BLACK);
        } else {
            System.out.print(SET_BG_COLOR_WHITE);
            System.out.print(SET_TEXT_COLOR_BLACK);
        }

        if (piece == null) {
            System.out.print(EMPTY);
        } else {
            System.out.print(getIcon(piece));
        }
    }

    private String getIcon(ChessPiece piece) {
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            return switch (piece.getPieceType()) {
                case KING -> WHITE_KING;
                case QUEEN -> WHITE_QUEEN;
                case BISHOP -> WHITE_BISHOP;
                case KNIGHT -> WHITE_KNIGHT;
                case ROOK -> WHITE_ROOK;
                case PAWN -> WHITE_PAWN;
            };
        } else {
            return switch (piece.getPieceType()) {
                case KING -> BLACK_KING;
                case QUEEN -> BLACK_QUEEN;
                case BISHOP -> BLACK_BISHOP;
                case KNIGHT -> BLACK_KNIGHT;
                case ROOK -> BLACK_ROOK;
                case PAWN -> BLACK_PAWN;
            };
        }
    }
}
