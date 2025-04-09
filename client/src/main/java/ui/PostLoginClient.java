package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import model.GameData;
import server.ServerFacade;
import ui.websocket.WebSocketFacade;

import java.util.Arrays;

import static ui.EscapeSequences.*;

public class PostLoginClient extends Client {
    private final ServerFacade server;
    private String url;

    public PostLoginClient(ServerFacade server, String url) {
        this.server = server;
        this.url = url;
    }


    @Override
    public String eval(String input, Repl repl) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "logout" -> logout(repl, params);
                case "create" -> createGame(params);
                case "join" -> joinGame(repl, params);
                case "observe" -> observeGame(params);
                case "list" -> listGames(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    private String logout(Repl repl, String[] params) throws Exception {
        if (params.length == 0) {
            server.logout();
            repl.setClient(new PreLoginClient(server));
            return "You have been logged out";
        }
        throw new Exception("Wrong number of arguments");
    }

    private String joinGame(Repl repl, String[] params) throws Exception {
        if (params.length == 2) {
            ChessGame.TeamColor color;
            try {
                color = ChessGame.TeamColor.valueOf(params[1].toUpperCase());
            } catch (Exception ex) {
                throw new Exception("Not a color option");
            }

            GameData gameData = server.joinGame(Integer.parseInt(params[0]), color);
//            printBoard(gameData.game().getBoard(), color);
            WebSocketFacade ws =  new WebSocketFacade(url);
            ws.join(server.getAuthToken(), gameData.gameID(), color);
            repl.setClient(new GameplayClient(gameData, color, url, server.getAuthToken(), server));
            return "";
        }
        throw new Exception("Wrong number of arguments");
    }

    public void clear() throws Exception {
        server.clear();
    }

    private String observeGame(String[] params) throws Exception {
        if (params.length == 1) {
//            GameData gameData = server.observeGame(Integer.parseInt(params[0]));
//            printBoard(gameData.game().getBoard(), ChessGame.TeamColor.WHITE);
            WebSocketFacade ws = new WebSocketFacade(url);
            try {
                ws.join(server.getAuthToken(), Integer.parseInt(params[0]), ChessGame.TeamColor.WHITE );
            } catch (Exception e) {
                throw new Exception("Not a number");
            }
        } else {
            throw new Exception("Wrong number of parameters");
        }
        return "";
    }





    private String createGame(String[] params) throws Exception {
        if (params.length == 1) {

            server.createGame(params[0]);
            return String.format("You have created a game named %s", params[0]);
        }

        throw new Exception("Wrong number of arguments");
    }

    private String listGames(String[] params) throws Exception {
        if (params.length == 0) {
            GameData[] games = server.listGames();
            var result = new StringBuilder();
            int i = 1;
            for (var game : games) {
                String playerInfo = getPlayerInfo(game);
                result.append(i).append(" Game Name: ").append(game.gameName()).append(". ").append(playerInfo).append('\n');
                ++i;
            }
            return result.toString();
        }
        throw new Exception("Wrong number of arguments");
    }

    private static String getPlayerInfo(GameData game) {
        String playerInfo = " ";
        playerInfo += "White: ";
        if (game.whiteUsername() == null) {
            playerInfo += "not joined.";
        } else {
            playerInfo += game.whiteUsername() + ".";
        }
        playerInfo += " Black: ";
        if (game.blackUsername() == null) {
            playerInfo += "not joined.";
        } else {
            playerInfo += game.blackUsername() + ".";
        }
        return playerInfo;
    }

    public String help() {
        return """
                    Possible Commands (case insensitive)
                    - create <GAME NAME>
                    - list
                    - join <ID> [WHITE][BLACK]
                    - observe <ID>
                    - logout
                    - quit
                    - help
                    """;
    }
}
