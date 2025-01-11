package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator extends PieceMovesCalculator {
    public KingMovesCalculator() {
        super();
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        ChessPosition endPosition;
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        int right = row + 1;
        int left = row - 1;
        int up = col + 1;
        int down = col - 1;

        if (inBounds(right)) {
            endPosition = new ChessPosition(right, col);
            moves.add(new ChessMove(myPosition,endPosition, null));
        }
        if (inBounds(left)) {
            moves.add(new ChessMove(myPosition,new ChessPosition(left, col), null));
        }
        if (inBounds(up)) {
            moves.add(new ChessMove(myPosition, new ChessPosition(row, up), null));
        }
        if (inBounds(down)) {
            moves.add(new ChessMove(myPosition, new ChessPosition(row, down), null));
        }
        if (inBounds(up) && inBounds(right)) {
            moves.add(new ChessMove(myPosition, new ChessPosition(right, up), null));
        }
        if (inBounds(down) && inBounds(right)) {
            moves.add(new ChessMove(myPosition, new ChessPosition(right, down), null));
        }
        if (inBounds(up) && inBounds(left)) {
            moves.add(new ChessMove(myPosition, new ChessPosition(left, up), null));
        }
        if (inBounds(down) && inBounds(left)) {
            moves.add(new ChessMove(myPosition, new ChessPosition(left, down), null));

        }

        return moves;
    }

}
