package ui;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import server.ServerFacade;

import java.util.Arrays;

public class PostLoginClient extends Client {
    private ServerFacade server;

    public PostLoginClient(ServerFacade server) {
        this.server = server;
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
                case "join" -> joinGame(params);
//                case "login" -> signIn(params);
//                case "rescue" -> rescuePet(params);
                case "list" -> listGames(params);
//                case "signout" -> signOut();
//                case "adopt" -> adoptPet(params);
//                case "adoptall" -> adoptAllPets();
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
        throw new Exception();
    }

    private String joinGame(String[] params) throws Exception {
        if (params.length == 2) {
            server.joinGame(params[0], params[1]);
            return "Where is my game?";
        }
        throw new Exception();
    }

    private String createGame(String[] params) throws Exception {
        if (params.length == 1) {

            server.createGame(params[0]);
            return String.format("You have created a game named %s", params[0]);
        }

        throw new Exception();
    }

    private String listGames(String[] params) throws Exception {
        if (params.length == 0) {
            GameData[] games = server.listGames();
            var result = new StringBuilder();
            var gson = new Gson();
            int i = 1;
            for (var game : games) {
                result.append(i).append(" ").append(game.gameName()).append('\n');
                ++i;
            }
            return result.toString();
        }
        throw new Exception();
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
