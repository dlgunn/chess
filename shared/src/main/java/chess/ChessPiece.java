package chess;

import chess.pieces.BishopMovesCalculator;
import chess.pieces.KingMovesCalculator;
import chess.pieces.QueenMovesCalculator;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
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
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        // delegate this to pieceMovesCalculator
        // Creates an instance of rook moves calculator, then get back the answer.
        switch (type) {
            case PieceType.KING:
                KingMovesCalculator kingCalc = new KingMovesCalculator();
                return kingCalc.pieceMoves(board, myPosition);
            case PieceType.QUEEN:
                QueenMovesCalculator queenCalc = new QueenMovesCalculator();
                return queenCalc.pieceMoves(board, myPosition);
            case PieceType.BISHOP:
                BishopMovesCalculator bishopCalc = new BishopMovesCalculator();
                return bishopCalc.pieceMoves(board, myPosition);




        }
        return new ArrayList<>();
    }
}


