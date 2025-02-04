package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessGame.TeamColor turn;
    private ChessBoard board;


    public ChessGame() {

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.turn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        turn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        ArrayList<ChessMove> moves = (ArrayList<ChessMove>) piece.pieceMoves(board,startPosition);


        return moves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = findPiece(teamColor, ChessPiece.PieceType.KING);
        ArrayList<ChessMove> moves = null;
        ChessPiece attackingPiece = null;
        for (int i = 0; i <= 8; ++i) {
            for (int j = 0; j <= 8; ++j) {
                ChessPosition position = new ChessPosition(i, j);
                attackingPiece = board.getPiece(position);
                if (attackingPiece != null && attackingPiece.getTeamColor() != teamColor) {
                    moves = (ArrayList<ChessMove>) attackingPiece.pieceMoves(board,position);
                    for (ChessMove move : moves) {
                        if (move.getEndPosition() == kingPosition) {
                            return true;
                        }
                    }

                }
            }
        }
        return false;
    }

//    public boolean checkSlidingPieces(ChessPosition kingPosition, ChessGame.TeamColor color) {
//        return isPieceScary(color, kingPosition,1, 0) || isPieceScary(color,kingPosition,0,1) || isPieceScary(color,kingPosition,-1,0) || isPieceScary(color,kingPosition,0,-1)
//                || isPieceScary(color,kingPosition,1,1) || isPieceScary(color,kingPosition,1,-1) || isPieceScary(color,kingPosition,-1,1) || isPieceScary(color,kingPosition,-1,-1);
//    }

//    public boolean isPieceScary(ChessGame.TeamColor color, ChessPosition kingPosition, int rowOffset, int colOffset) {
//        ChessPiece.PieceType type = ChessPiece.PieceType.ROOK;
//        if (rowOffset != 0 && colOffset != 0) {
//            type = ChessPiece.PieceType.BISHOP;
//        }
//        ChessPiece scaryPiece = extendCheck(kingPosition,rowOffset,colOffset);
//        if (scaryPiece.getPieceType() == ChessPiece.PieceType.PAWN) {
//            return scaryPawn(color, kingPosition, rowOffset, colOffset);
//        }
//        return scaryPiece.getTeamColor() != color && (scaryPiece.getPieceType() == ChessPiece.PieceType.QUEEN || scaryPiece.getPieceType() == type);
//    }

//    public boolean scaryPawn(TeamColor color, ChessPosition kingPosition, int rowOffset, int colOffset) {
//        boolean scary = false;
//        int up = 1;
//        if (color == TeamColor.BLACK) {
//            up = -1;
//        }
//        return rowOffset == up && colOffset != 0;
//    }

//    public ChessPiece extendCheck(ChessPosition kingPosition, int rowOffset, int colOffset) {
//        int row = kingPosition.getRow();
//        int col = kingPosition.getColumn();
//        int originalRowOffset = rowOffset;
//        int originalColOffset = colOffset;
//        int distance = 0;
//        ChessPiece occupyingPiece = null;
//        do {
//            ChessPosition newPosition = new ChessPosition(row + rowOffset, col + colOffset);
//            if (!newPosition.inBounds()) {
//                return null;
//            }
//            occupyingPiece = board.getPiece(newPosition);
//            rowOffset += originalRowOffset;
//            colOffset += originalColOffset;
//            distance++;
//        } while (occupyingPiece == null);
//        if (occupyingPiece.getPieceType() == ChessPiece.PieceType.PAWN && distance > 1) {
//            return null;
//        }
//        return occupyingPiece;
//    }

    public ChessPosition findPiece(ChessGame.TeamColor color, ChessPiece.PieceType pieceType) {
        ChessPiece piece = new ChessPiece(color,pieceType);
        for (int i = 0; i <= 8; ++i) {
            for (int j = 0; j <= 8; ++j) {
                ChessPosition position = new ChessPosition(i, j);
                if (board.getPiece(position) == piece) {
                    return position;
                }
            }
        }
        return null;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        ChessPosition kingPosition = findPiece(teamColor, ChessPiece.PieceType.KING);
        if (!isInCheck(teamColor)) {
            return false;
        } else {
            ArrayList<ChessMove> moves = (ArrayList<ChessMove>) board.getPiece(kingPosition).pieceMoves(board,kingPosition);
            return false;
        }
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }
}
