package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import model.GameData;
import server.ServerFacade;
import ui.websocket.WebSocketFacade;

import java.util.ArrayList;
import java.util.Arrays;

public class GameplayClient extends Client {
    private GameData gameData;
    private ChessGame.TeamColor color;
    private String url;
    private String authToken;
    private ServerFacade facade;

    public GameplayClient(GameData gameData, ChessGame.TeamColor color, String url, String authToken, ServerFacade facade) {
        this.gameData = gameData;
        this.color = color;
        this.url = url;
        this.authToken = authToken;
        this.facade = facade;
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

    private String resign() throws Exception {
        WebSocketFacade ws = new WebSocketFacade(url, null);
        ws.resign(authToken, gameData.gameID());
        return "";
    }

    private String move(Repl repl, String[] params) throws Exception {
        if (params.length != 4) {
            return "Wrong number of parameters";
        }

        int row1;
        int col1;
        int row2;
        int col2;
        try {
            row1 = Integer.parseInt(params[0]);
            col1 = Integer.parseInt(params[1]);
            row2 = Integer.parseInt(params[2]);
            col2 = Integer.parseInt(params[3]);
        } catch (NumberFormatException e) {
            return "Must input integers";
        }
        ChessMove move = new ChessMove(new ChessPosition(row1, col1), new ChessPosition(row2, col2), null);
        WebSocketFacade ws = new WebSocketFacade(url, null);
        ws.makeMove(move, authToken, gameData.gameID());


        return "";
    }

    private String leave(Repl repl) throws Exception {
        WebSocketFacade ws = new WebSocketFacade(url, null);
        ws.leave(authToken, gameData.gameID());
        repl.setClient(new PostLoginClient(facade,url));
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

    public void setGameData(GameData gameData) {
        this.gameData = gameData;
    }
}
