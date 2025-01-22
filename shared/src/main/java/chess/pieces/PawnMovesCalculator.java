package chess.pieces;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator extends PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        ChessPiece myChessPiece = board.getPiece(myPosition);
        ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();
        ChessPosition newPosition;
        ChessPiece occupyingPiece;
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        if (myChessPiece.getTeamColor() == ChessGame.TeamColor.BLACK) {
            if (row == 2) {
                newPosition = new ChessPosition(1, col);
                moves.addAll(this.addPromotion(myPosition, newPosition));
                if (col < 8) {
                    newPosition = new ChessPosition(1, col + 1);
                    occupyingPiece = board.getPiece(newPosition);
                    if (occupyingPiece != null) {
                        moves.addAll(this.addPromotion(myPosition, newPosition));
                    }
                }
                if (col > 1) {
                    newPosition = new ChessPosition(1, col - 1);
                    occupyingPiece = board.getPiece(newPosition);
                    if (occupyingPiece != null) {
                        moves.addAll(this.addPromotion(myPosition, newPosition));
                    }
                }
            } else {
                newPosition = new ChessPosition(row - 1 , col);
                occupyingPiece = board.getPiece(newPosition);
                if (occupyingPiece == null) {
                    moves.add(new ChessMove(myPosition, newPosition, null));
                    if (row == 7) {
                        newPosition = new ChessPosition(row - 2, col);
                        occupyingPiece = board.getPiece(newPosition);
                        if (occupyingPiece == null) {
                            moves.add(new ChessMove(myPosition, newPosition, null));
                        }
                    }
                }
                if (col < 8) {
                    newPosition = new ChessPosition(row - 1, col + 1);
                    occupyingPiece = board.getPiece(newPosition);
                    if (occupyingPiece != null) {
                        if (occupyingPiece.getTeamColor() != color) {
                            moves.add(new ChessMove(myPosition, newPosition, null));
                        }
                    }
                }
                if (col > 1) {
                    newPosition = new ChessPosition(row -1, col - 1);
                    occupyingPiece = board.getPiece(newPosition);
                    if (occupyingPiece != null) {
                        if (occupyingPiece.getTeamColor() != color) {
                            moves.add(new ChessMove(myPosition, newPosition, null));
                        }
                    }
                }
            }
        } else if (myChessPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            if (row == 7) {
                newPosition = new ChessPosition(8, col);
                moves.addAll(this.addPromotion(myPosition, newPosition));
                if (col < 8) {
                    newPosition = new ChessPosition(8, col + 1);
                    occupyingPiece = board.getPiece(newPosition);
                    if (occupyingPiece != null) {
                        moves.addAll(this.addPromotion(myPosition, newPosition));
                    }
                }
                if (col > 1) {
                    newPosition = new ChessPosition(8, col - 1);
                    occupyingPiece = board.getPiece(newPosition);
                    if (occupyingPiece != null) {
                        moves.addAll(this.addPromotion(myPosition, newPosition));
                    }
                }
            } else {
                newPosition = new ChessPosition(row + 1 , col);
                occupyingPiece = board.getPiece(newPosition);
                if (occupyingPiece == null) {
                    moves.add(new ChessMove(myPosition, newPosition, null));
                    if (row == 2) {
                        newPosition = new ChessPosition(row + 2, col);
                        occupyingPiece = board.getPiece(newPosition);
                        if (occupyingPiece == null) {
                            moves.add(new ChessMove(myPosition, newPosition, null));
                        }
                    }
                }
                if (col < 8) {
                    newPosition = new ChessPosition(row + 1, col + 1);
                    occupyingPiece = board.getPiece(newPosition);
                    if (occupyingPiece != null) {
                        if (occupyingPiece.getTeamColor() != color) {
                            moves.add(new ChessMove(myPosition, newPosition, null));
                        }
                    }
                }
                if (col > 1) {
                    newPosition = new ChessPosition(row + 1, col - 1);
                    occupyingPiece = board.getPiece(newPosition);
                    if (occupyingPiece != null) {
                        if (occupyingPiece.getTeamColor() != color) {
                            moves.add(new ChessMove(myPosition, newPosition, null));
                        }
                    }
                }
            }
        }
        return moves;
    }

    public ArrayList<ChessMove> addPromotion(ChessPosition startPosition, ChessPosition endPosition) {
        ArrayList<ChessMove> promotionMoves = new ArrayList<ChessMove>();
        promotionMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.BISHOP));
        promotionMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.QUEEN));
        promotionMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.ROOK));
        promotionMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.KNIGHT));
        return promotionMoves;
    }

}
