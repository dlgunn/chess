package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import model.GameData;

import java.util.ArrayList;
import java.util.Arrays;

public class GameplayClient extends Client {
    private GameData gameData;
    private ChessGame.TeamColor color;

    public GameplayClient(GameData gameData, ChessGame.TeamColor color) {
        this.gameData = gameData;
        this.color = color;
    }

    @Override
    public String eval(String input, Repl repl) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "redraw" -> redraw();
                case "leave" -> leave(repl);
                case "move" -> move(repl, params);
                case "resign" -> resign();
                case "highlight" -> highlight(params);
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    private String highlight(String[] params) throws Exception {
        if (params.length != 2) {
            throw new Exception("Wrong number of arguments");
        }
        int row;
        int col;
        try {
            row = Integer.parseInt(params[0]);
            col = Integer.parseInt(params[1]);
        } catch (NumberFormatException e) {
            return "Not a valid position";
        }
        if (row > 8 || row < 1 || col > 8 || col < 1) {
            return "Not a valid position";
        }
        ArrayList<ChessMove> validMoves = (ArrayList<ChessMove>) gameData.game().validMoves(new ChessPosition(row, col));
        printBoard(gameData.game().getBoard(), color, validMoves);

        return "";
    }

    private String resign() {
        return "";
    }

    private String move(Repl repl, String[] params) {
        return "";
    }

    private String leave(Repl repl) {
        return "";
    }

    private String redraw() {
        printBoard(gameData.game().getBoard(), color);
        return "";
    }

    public String help() {

        return """
                    Possible Commands (case insensitive)
                    - redraw
                    - leave
                    - move <start row> <start col> <end row> <end col>
                    - resign
                    - highlight <row> <col>
                    - help
                    """;
    }
}
