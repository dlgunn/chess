package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private final ChessPiece[][] board = new ChessPiece[8][8];
    public ChessBoard() {

    }

    public ChessBoard(ChessBoard board) {
        ChessPiece piece;
        for (int i = 1; i <= 8; ++i) {
            for (int j = 1; j <= 8; ++j) {
                ChessPosition position = new ChessPosition(i, j);
                piece = board.getPiece(position);
                this.addPiece(position, piece);
            }
        }
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        int row = position.getRow();
        int col = position.getColumn();
        this.board[row-1][col-1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return this.board[position.getRow()-1][position.getColumn()-1];
    }


    public void removePiece(ChessPosition position) {
        this.board[position.getRow()-1][position.getColumn()-1] = null;
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        setPawns(ChessGame.TeamColor.BLACK);
        setPawns(ChessGame.TeamColor.WHITE);
        setOtherPieces(ChessGame.TeamColor.BLACK);
        setOtherPieces(ChessGame.TeamColor.WHITE);

    }

    public void makeMove(ChessMove move) {
        ChessPiece piece = this.getPiece(move.getStartPosition());
        removePiece(move.getStartPosition());
        ChessPiece.PieceType promotionPieceType = move.getPromotionPiece();
        if (promotionPieceType != null) {
            addPiece(move.getEndPosition(), new ChessPiece(piece.getTeamColor(),promotionPieceType));
        } else {
            addPiece(move.getEndPosition(),piece);
        }

    }



    private void setPawns(ChessGame.TeamColor color) {
        int row = 2;
        if (color == ChessGame.TeamColor.BLACK) {
            row = 7;
        }
        for (int i = 1; i <= 8; ++i) {
            addPiece(new ChessPosition(row, i),new ChessPiece(color, ChessPiece.PieceType.PAWN));
        }
    }
    private void setOtherPieces(ChessGame.TeamColor color) {
        int row = 1;
        if (color == ChessGame.TeamColor.BLACK) {
            row = 8;
        }
        addPiece(new ChessPosition(row, 1), new ChessPiece(color, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(row, 8), new ChessPiece(color, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(row, 2), new ChessPiece(color, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(row, 7), new ChessPiece(color, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(row, 3), new ChessPiece(color, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(row, 6), new ChessPiece(color, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(row, 4), new ChessPiece(color, ChessPiece.PieceType.QUEEN));
        addPiece(new ChessPosition(row, 5), new ChessPiece(color, ChessPiece.PieceType.KING));





    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }
}
