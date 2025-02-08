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
        board = new ChessBoard();
        board.resetBoard();
        turn = TeamColor.WHITE;
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
        if (piece==null) {
            return null;
        }
        TeamColor color = piece.getTeamColor();
        ChessBoard board;
        ArrayList<ChessMove> returnMoves = new ArrayList<>();
        ArrayList<ChessMove> moves = (ArrayList<ChessMove>) piece.pieceMoves(this.board,startPosition);
        for (ChessMove move : moves) {
            if (piece.getPieceType() == ChessPiece.PieceType.KING && Math.abs(move.getStartPosition().getColumn()-move.getEndPosition().getColumn())>1) {
                castle(move,returnMoves,color);
            } else {
                board = new ChessBoard(this.board);
                if (!isInSimulatedCheck(color,move,board)) {
                    returnMoves.add(move);
                }
            }

        }
        return returnMoves;
    }

    public void castle(ChessMove move, ArrayList<ChessMove> moves, TeamColor color) {
        int row = 1;
        int rookCol = 1;
        if (color == TeamColor.BLACK) {
            row = 8;
        }
        if (move.getEndPosition().getColumn() == 7) {
            rookCol = 8;
        }
        ChessPiece king = board.getPiece(move.getStartPosition());
        ChessPiece rook = board.getPiece(new ChessPosition(row, rookCol));


        if (this.isInCheck(color) || king.getMovedStatus() || rook.getMovedStatus()) {
            return;
        }

        ChessBoard board = new ChessBoard((this.board));
        int col = 6;
        if (move.getEndPosition().getColumn() != 7) {
            col = 4;
        }
        ChessPosition intermediatePosition = new ChessPosition(row, col);
        ChessMove intermediateMove = new ChessMove(move.getStartPosition(), intermediatePosition,null);
        if (!isInSimulatedCheck(color,intermediateMove,board) && !isInSimulatedCheck(color, move,new ChessBoard(this.board))) {
            moves.add(move);
        }

    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition startPosition = move.getStartPosition();
        ChessPiece piece = board.getPiece(startPosition);
        if (piece == null || piece.getTeamColor() != turn) {
            throw new InvalidMoveException();
        }
        ArrayList<ChessMove> moves = (ArrayList<ChessMove>) validMoves(startPosition);
        if (moves.contains(move)) {
            board.makeMove(move, board.getEnPassant());
            piece.setMovedStatus(true);
            changeTurn();
        } else {
            throw new InvalidMoveException();
        }
    }

    private void changeTurn() {
        if (turn == TeamColor.BLACK) {
            turn = TeamColor.WHITE;
        } else {
            turn = TeamColor.BLACK;
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = findPiece(teamColor, ChessPiece.PieceType.KING);
        ArrayList<ChessMove> moves;
        ChessPiece attackingPiece;
        for (int i = 1; i <= 8; ++i) {
            for (int j = 1; j <= 8; ++j) {
                ChessPosition position = new ChessPosition(i, j);
                attackingPiece = board.getPiece(position);
                if (attackingPiece != null && attackingPiece.getTeamColor() != teamColor) {
                    moves = (ArrayList<ChessMove>) attackingPiece.pieceMoves(board,position);
                    for (ChessMove move : moves) {
                        if (move.getEndPosition().equals(kingPosition)) {
                            return true;
                        }
                    }

                }
            }
        }
        return false;
    }

    public ChessPosition findPiece(ChessGame.TeamColor color, ChessPiece.PieceType pieceType) {
        ChessPiece piece = new ChessPiece(color,pieceType);
        for (int i = 1; i <= 8; ++i) {
            for (int j = 1; j <= 8; ++j) {
                ChessPosition position = new ChessPosition(i, j);
                if (piece.equals(board.getPiece(position))) {
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
        ChessBoard board;

        if (!isInCheck(teamColor)) {
            return false;
        }

        for (int i = 1; i <= 8; ++i) {
            for (int j = 1; j <= 8; ++j) {
                ChessPosition position = new ChessPosition(i, j);
                ChessPiece piece = this.board.getPiece(position);
                if (piece != null && piece.getTeamColor() == teamColor) {
                    for (ChessMove move : piece.pieceMoves(this.board,position)) {
                        board = new ChessBoard(this.board);
                        if (!isInSimulatedCheck(teamColor,move, board)) {
                            return false;
                        }
                    }
                }

            }
        }

        return true;

    }

    public boolean isInSimulatedCheck(TeamColor teamColor, ChessMove move, ChessBoard board) {
        board.makeMove(move, board.getEnPassant());
        ChessBoard tempBoard = this.board;
        this.board = board;
        boolean inCheck = this.isInCheck(teamColor);
        this.board = tempBoard;
        return inCheck;

    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        ChessPosition position;
        ChessPiece piece;
        if (isInCheck(teamColor)) {
            return false;
        }

        for (int i = 1; i < 9; ++i) {
            for (int j = 1; j < 9; ++j) {
                position = new ChessPosition(i,j);
                piece = board.getPiece(position);
                if (piece != null && piece.getTeamColor() == teamColor && !validMoves(position).isEmpty()) {
                    return false;
                }
            }
        }
        return true;
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
