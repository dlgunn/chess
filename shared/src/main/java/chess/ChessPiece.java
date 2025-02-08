package chess;

import chess.pieces.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessGame.TeamColor color;
    private final ChessPiece.PieceType type;
    private boolean moved;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.color = pieceColor;
        this.type = type;
        moved = false;
    }

    public boolean getMovedStatus() {
        return moved;
    }

    public void setMovedStatus(boolean status) {
        moved = status;
    }



    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return color == that.color && type == that.type;
    }


    @Override
    public int hashCode() {
        return Objects.hash(color, type);
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);
        ArrayList<ChessMove> moves = null;
        switch (piece.getPieceType()) {
            case PieceType.KING:
                KingMovesCalculator kingCalc = new KingMovesCalculator();
                moves = (ArrayList<ChessMove>) kingCalc.pieceMoves(board, myPosition);
                break;
            case PieceType.ROOK:
                RookMovesCalculator rookCalc = new RookMovesCalculator();
                moves = (ArrayList<ChessMove>) rookCalc.pieceMoves(board,myPosition);
                break;
            case PieceType.BISHOP:
                BishopMovesCalculator bishopCalc = new BishopMovesCalculator();
                moves = (ArrayList<ChessMove>) bishopCalc.pieceMoves(board,myPosition);
                break;
            case PieceType.QUEEN:
                QueenMovesCalculator queenCalc = new QueenMovesCalculator();
                moves = (ArrayList<ChessMove>) queenCalc.pieceMoves(board,myPosition);
                break;
            case PieceType.KNIGHT:
                KnightMovesCalculator knightCalc = new KnightMovesCalculator();
                moves = (ArrayList<ChessMove>) knightCalc.pieceMoves(board,myPosition);
                break;
            case PieceType.PAWN:
                PawnMovesCalculator pawnCalc = new PawnMovesCalculator();
                moves = (ArrayList<ChessMove>) pawnCalc.pieceMoves(board,myPosition);
        }
        return moves;
    }
}
