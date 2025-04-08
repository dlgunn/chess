package ui;

import chess.ChessGame;
import model.GameData;

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

    private String highlight(String[] params) {
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
