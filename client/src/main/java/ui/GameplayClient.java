package ui;

import java.util.Arrays;

public class GameplayClient extends Client {
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
